package alan_GUI;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HvalFinderWorker implements Runnable{
	//https://stackoverflow.com/questions/9148899/returning-value-from-thread
	public boolean calculationComplete= false;
	private volatile double hVal;	
	private HashMap<String, NameToVariableClass>  systemVariableSet;
	private HashMap<String, NameToVariableClass>  geoGenerationVariableSet;
	
	public HvalFinderWorker(HashMap<String, NameToVariableClass>  systemVariableSet, HashMap<String, NameToVariableClass>  geoGenerationVariableSet) {
		//Clone it, so you don't care if it gets changed later
		this.systemVariableSet= (HashMap<String, NameToVariableClass>) systemVariableSet.clone();
		this.geoGenerationVariableSet= (HashMap<String, NameToVariableClass>) geoGenerationVariableSet.clone();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		if (systemVariableSet!= null && geoGenerationVariableSet!= null){
			try {
				AgentPanel12_GUI_Alan.addMessage("Worker searching for value");
				AgentPanel12_GUI_Alan.repaint();
				TimeUnit.SECONDS.sleep(3);
				Random randall = new Random();
				randall.setSeed(hash(SearchFunctions.getHash(geoGenerationVariableSet)));				
				hVal= randall.nextDouble();
				calculationComplete= true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	public double getHVal(){
		return hVal;
	}
	
	public static long hash(String string) {
		long h = 1125899906842597L; // prime
		int len = string.length();

		for (int i = 0; i < len; i++) {
			h = 31 * h + string.charAt(i);
		}
		return h;
	}	
}