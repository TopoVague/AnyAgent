package alan_GUI;

import java.util.HashMap;
import java.util.Random;

public class DummyPythonEXEValueRetriever {
	public static double getHeuristicValue(HashMap<String, NameToVariableClass> systemVariableSet, HashMap<String, NameToVariableClass> geoGenerationVariableSet){
		Random randall= new Random();
		//randall.setSeed(geoGenerationVariableSet.hashCode());
		//AgentPanel12_GUI_Alan.addMessage("\tHash value of variable set: " + geoGenerationVariableSet.hashCode());
		return randall.nextDouble();
	}
}
