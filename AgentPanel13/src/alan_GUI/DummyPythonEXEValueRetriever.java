package alan_GUI;

import java.util.HashMap;
import java.util.Random;

public class DummyPythonEXEValueRetriever {
	public static double getHeuristicValue(HashMap<String, NameToVariableClass> systemVariableSet, HashMap<String, NameToVariableClass> geoGenerationVariableSet){
		Random randall= new Random();
		//randall.setSeed(hash(SearchFunctions.getHash(geoGenerationVariableSet)));
		return randall.nextDouble();
	}
	
	public static long hash(String string) {
		  long h = 1125899906842597L; // prime
		  int len = string.length();

		  for (int i = 0; i < len; i++) {
		    h = 31*h + string.charAt(i);
		  }
		  return h;
		}
}
