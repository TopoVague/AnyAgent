package alan_GUI;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import alan_GUI.SystemVariablesClass.guiVariableTypes;

public class DummyPythonEXEValueRetriever {
	public static double getHeuristicValue(HashMap<String, NameToVariableClass> systemVariableSet,
			HashMap<String, NameToVariableClass> geoGenerationVariableSet) throws InterruptedException {

		//AgentPanel12_GUI_Alan.addMessage("Waiting for answer...");
		// TimeUnit.SECONDS.sleep(10);
		
		HvalFinderWorker hfw= new HvalFinderWorker(systemVariableSet, geoGenerationVariableSet);
		Thread t= new Thread(hfw);
		t.start();
		t.join();
		return hfw.getHVal();
	}
}
