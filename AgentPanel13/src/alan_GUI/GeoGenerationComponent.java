package alan_GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JPanel;

public interface GeoGenerationComponent {
	/**
	 * Let's assume that the way things are done is Jar --> python exe <-- which returns a set of values which is a dictionary of values, and then the exe call is closed immediately after. In that case, it is possible to do the hill-climbing and other stuff in this jar itself. 
	 * 
	 * It'll be annoying to freaking work on a project with old specs, so I guess I'm going to try to set up that system for the new project. Also with the interfaces that allows for all the future types of variable changes. The values that are passed will be held as a dictionary of strings to strings?
	 * 
	 * How should the information be transferred between programs? How about, Dict<String, String>!
	 * Assuming that the second variable can only ever be a float or a string, in either case it can be interpreted as such when the time is needed?
	 * 
	 * But would the GUI variables be portrayed in the xml itself?
	 * 
	 * @return
	 */
	//public JPanel getPanel(); //basically the panel is generated off of reflected field names
	//public HashMap<String, NameToVariableClass> getVariables();  //Don't need this since the variables that want to be exposed has its own syntax
	//public ArrayList<String> getVariableOrder(); //Get the order of the variables that you want to display in the GUI //NO LONGER needs this since the order is also retrieved by reflection
	public String[] GetVariableDisplayOrder();
	public String SaveConfiguration(String filepath); //returns the path if it's true, or null if false
	public String Run(LinkedHashMap<String, NameToVariableClass> guiVariables); //return the file path to the geometry if it works		
}
