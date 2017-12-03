package alan_GUI;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import alan_GUI.SystemVariablesClass.guiVariableTypes;
public class SearchFunctions {
	public static HashMap<String, Double> runHillClimbing(HashMap<String, NameToVariableClass> systemVariables, HashMap<String, NameToVariableClass> geoGenerationVariables) throws IOException, ClassNotFoundException{
		
		/*
		 * Basically, start at one point, change based on the step size, then go again if you found a value larger than your current one. Every new iteration you remove on from your budget, until you run out
		 */
		AgentPanel12_GUI_Alan.addMessage("Running hillclimbing");
		AgentPanel12_GUI_Alan.addMessageReportLine();
		HashMap<String, Double> history= new HashMap<String,Double>();
		
		int iterationBudget= Integer.parseInt(systemVariables.get("Iteration Budget").valueString);
		ArrayList<String> variablesToStep= new ArrayList<String>();
		
		double hillClimbingStepSize= Double.parseDouble(systemVariables.get("Hill Climbing Step Size").valueString);
		
		//If the step size is an integer, you can also iterate the integer types
		boolean alsoCheckIntegers= false;		
		if ((hillClimbingStepSize == Math.floor(hillClimbingStepSize)) && !Double.isInfinite(hillClimbingStepSize)) {		   
			alsoCheckIntegers= true;
		}
		
		for (String key: geoGenerationVariables.keySet()){
			if (geoGenerationVariables.get(key).gvt== guiVariableTypes.DOUBLE){
				variablesToStep.add(key);
			}
			if (geoGenerationVariables.get(key).gvt== guiVariableTypes.ZERO_TO_ONE_SLIDER){
				variablesToStep.add(key);
			}
			if (alsoCheckIntegers){
				if (geoGenerationVariables.get(key).gvt== guiVariableTypes.POSITIVE_INT){
					variablesToStep.add(key);
				}
			}
			
		}
		AgentPanel12_GUI_Alan.addMessage("Hill climbing considering integers: "+ alsoCheckIntegers );
		AgentPanel12_GUI_Alan.addMessage("Hillclimbing is stepping on these variables: " );
		for (String key: variablesToStep){
			AgentPanel12_GUI_Alan.addMessage("... "+ key);
		}
		
		
		//For each variable being iterated, create an added and a subtracted version corresponding to their limits
		double largestHValue= DummyPythonEXEValueRetriever.getHeuristicValue(systemVariables, geoGenerationVariables);
		byte[] bestSerializedConfiguration= Serializer.serialize(geoGenerationVariables);
		
		history.put(getHash(geoGenerationVariables), largestHValue);
		AgentPanel12_GUI_Alan.addMessage("Initial value for (" + getHash(geoGenerationVariables) + "): " + largestHValue);
		
		boolean takeAnotherStep= true;
		for (int n = 0; n < iterationBudget; n++){
			AgentPanel12_GUI_Alan.addMessage("STEP " + (n+1) + " / " + iterationBudget);
			AgentPanel12_GUI_Alan.addMessageReportLine();
			if (takeAnotherStep){
				takeAnotherStep= false;
				for (String key: variablesToStep ) { //Iterate through each variable
					//Iterate up and down for each and add those values to history
					
					AgentPanel12_GUI_Alan.addMessage("Changing steps for : " + key);
					for (int j = -1; j < 2; j+=2){ //iterating positive and negative getting -1 and 1
						double initialValueForTheVariable= Double.parseDouble(geoGenerationVariables.get(key).valueString);
						System.out.println(j);
						System.out.println(initialValueForTheVariable);
						System.out.println(String.valueOf(initialValueForTheVariable+ (j*Math.abs(hillClimbingStepSize))));
						geoGenerationVariables.get(key).valueString= String.valueOf(initialValueForTheVariable+ (j*hillClimbingStepSize)); //set that stepped value as part of the formula
						
						double retrievedValue= 0;
						byte[] serializedConfiguration= null;
						String geoConfigurationKey= getHash(geoGenerationVariables);
						if (!history.containsKey(geoConfigurationKey)){ //if it doesn't exist calculate it
							AgentPanel12_GUI_Alan.addMessage("\tCalculating value for key: " + geoConfigurationKey);
							retrievedValue= DummyPythonEXEValueRetriever.getHeuristicValue(systemVariables, geoGenerationVariables);
							serializedConfiguration= Serializer.serialize(geoGenerationVariables);
							history.put(String.valueOf(geoGenerationVariables.hashCode()), retrievedValue);
						}else{
							AgentPanel12_GUI_Alan.addMessage("\tRetrieved value for key: " + geoConfigurationKey);
							retrievedValue= history.get(geoConfigurationKey); //if it does exist retrieve it from history
							serializedConfiguration= Serializer.serialize(geoGenerationVariables);
						}						
						
						AgentPanel12_GUI_Alan.addMessage("\tStep value: " + retrievedValue);
						
						if (retrievedValue > largestHValue){
							AgentPanel12_GUI_Alan.addMessage("Larger heuristic value than " + largestHValue+ " retrieved: " + retrievedValue + " replaciing...");
							largestHValue= retrievedValue;
							bestSerializedConfiguration= serializedConfiguration;
							takeAnotherStep= true;
						}
						
						//Here you need to return the changed value back to its original state so you don't iterate too far...
						geoGenerationVariables.get(key).valueString= String.valueOf(initialValueForTheVariable);
					}
					//If you are here you can adapt the best one?					
				}
				
				if (!takeAnotherStep){
					AgentPanel12_GUI_Alan.addMessage("No larger value than " + largestHValue + " was found, ending climb");							
					break;
				}else{
					//Set the new configuration to the best one
					geoGenerationVariables= (HashMap<String, NameToVariableClass>) Serializer.deserialize(bestSerializedConfiguration);
				}
				//Once you're here, you are done with one iteration!
			}
		}
		
		HashMap<String, NameToVariableClass> deserializedBestConfigurations= (HashMap<String, NameToVariableClass>) Serializer.deserialize(bestSerializedConfiguration);
		AgentPanel12_GUI_Alan.addMessage("Printing best configuration found: ");
		for (String key: deserializedBestConfigurations.keySet()){
			AgentPanel12_GUI_Alan.addMessage("\t" + key + " : " + deserializedBestConfigurations.get(key).valueString);
		}	
		
		return history;
	}	
	
	public static String getHash(HashMap<String, NameToVariableClass> geoGenerationVariables){ 
		System.out.println(geoGenerationVariables.values().toString());
		String hash= null;
		try {
			hash= java.security.MessageDigest.getInstance("MD5").digest(Serializer.serialize(geoGenerationVariables)).toString();
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hash;
	}
}
