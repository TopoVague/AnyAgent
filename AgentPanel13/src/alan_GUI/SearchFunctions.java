package alan_GUI;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import alan_GUI.SystemVariablesClass.guiVariableTypes;
public class SearchFunctions {
	public static Random randall= new Random();

	public static HashMap<String, Double> runSimulatedAnnealing(HashMap<String, NameToVariableClass> systemVariables, HashMap<String, NameToVariableClass> geoGenerationVariables) throws IOException{
		AgentPanel12_GUI_Alan.addMessage("Running simulated annealing");
		return null;
	}
	
	public static HashMap<String, Double> runHillClimbing(HashMap<String, NameToVariableClass> systemVariables, HashMap<String, NameToVariableClass> geoGenerationVariables) throws IOException, ClassNotFoundException{
		AgentPanel12_GUI_Alan.addMessage("Running hill climbing");
		return null;
	}
	
	public static HashMap<String, Double> runMonteCarlo (HashMap<String, NameToVariableClass> systemVariables, HashMap<String, NameToVariableClass> geoGenerationVariables) throws IOException {
		AgentPanel12_GUI_Alan.addMessage("Running monte carlo search");
		
		//Retrieve all doubles, sliders, and positive integers, then randomize all of them until the budget runs out
		HashMap<String, Double> history= new HashMap<String,Double>();		
		int iterationBudget= Integer.parseInt(systemVariables.get("Iteration Budget").valueString);
		ArrayList<NameToVariableClass> variablesToModify= new ArrayList<NameToVariableClass>();
		
		for (String key: geoGenerationVariables.keySet()){
			boolean somethingAdded= false;
			if (geoGenerationVariables.get(key).gvt== guiVariableTypes.DOUBLE){
				variablesToModify.add(geoGenerationVariables.get(key));
				somethingAdded= true;
			}
			if (geoGenerationVariables.get(key).gvt== guiVariableTypes.ZERO_TO_ONE_SLIDER){
				variablesToModify.add(geoGenerationVariables.get(key));
				somethingAdded= true;
			}

			if (geoGenerationVariables.get(key).gvt== guiVariableTypes.POSITIVE_INT){
				variablesToModify.add(geoGenerationVariables.get(key));
				somethingAdded= true;
			}		
			
			if (somethingAdded){
				AgentPanel12_GUI_Alan.addMessage("... "+ key);
			}					
		}
		
		//Randomize variables here
		
		double maxValue= DummyPythonEXEValueRetriever.getHeuristicValue(systemVariables, geoGenerationVariables); //get current max value
		HashMap<String, NameToVariableClass> bestConfigurationSet= (HashMap<String, NameToVariableClass>) geoGenerationVariables.clone();
		
		for (int n = 0; n < iterationBudget; n++){
			AgentPanel12_GUI_Alan.addMessage("Running " + (n+1) + " / "+ iterationBudget);
			for (String key: geoGenerationVariables.keySet()){
				boolean somethingAdded= false;
				if (geoGenerationVariables.get(key).gvt== guiVariableTypes.DOUBLE){				
					geoGenerationVariables.get(key).valueString= String.valueOf(-Double.MAX_VALUE + (Double.MAX_VALUE- Double.MIN_VALUE)  * randall.nextDouble());
				}
				if (geoGenerationVariables.get(key).gvt== guiVariableTypes.ZERO_TO_ONE_SLIDER){
					geoGenerationVariables.get(key).valueString= String.valueOf(randall.nextDouble());
				}
	
				if (geoGenerationVariables.get(key).gvt== guiVariableTypes.POSITIVE_INT){
					geoGenerationVariables.get(key).valueString= String.valueOf(-Integer.MAX_VALUE + (Integer.MAX_VALUE- Integer.MIN_VALUE)  * randall.nextInt());
				}		
			}
			
			//Get value after the randomization
			String searchKey= Serializer.serialize(geoGenerationVariables).toString(); //using the state of the geo hashset to retrive a pseudo-unique identifier
			double candidateVal= 0;
			if (!history.containsKey(searchKey)){
				candidateVal= DummyPythonEXEValueRetriever.getHeuristicValue(systemVariables, geoGenerationVariables);	
				history.put(searchKey, candidateVal); //the hash is the key, and the calculated value stored as its value	
			}else{
				candidateVal= history.get(searchKey);
			}			
			AgentPanel12_GUI_Alan.addMessage("\tCandidate value: " + candidateVal);
			
			if (candidateVal > maxValue){ //if the candidate is larger, replace it
				maxValue= candidateVal;
				bestConfigurationSet= (HashMap<String, NameToVariableClass>) geoGenerationVariables.clone();
				AgentPanel12_GUI_Alan.addMessage("\tNew max value: " + maxValue);
			}
		}
		
		AgentPanel12_GUI_Alan.addMessage("Final max value: " + maxValue);
		AgentPanel12_GUI_Alan.addMessage("Final configution: ");
		printVariableSet(bestConfigurationSet);
		return history;
	}

	public static void printVariableSet(HashMap<String, NameToVariableClass> toPrintVariableSet){
		for (String key: toPrintVariableSet.keySet()){
			AgentPanel12_GUI_Alan.addMessage("\t" + key + " : " + toPrintVariableSet.get(key).valueString);
		}	
		
	}
	
	public static String getHash(HashMap<String, NameToVariableClass> geoGenerationVariables){ 
		//System.out.println(geoGenerationVariables.values().toString());
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
