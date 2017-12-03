package alan_GUI;

import java.lang.reflect.Field;
import java.util.HashMap;

import alan_GUI.SystemVariablesClass.guiVariableTypes;


public class ClassToGUIAdapter {	
	public static HashMap<String, NameToVariableClass> generateSetFromClass(Class c){
		HashMap<String, NameToVariableClass> variableSet= new HashMap<String, NameToVariableClass>();
		Field[] fields = c.getFields();
		
		/**
		 * Basically, for each variable within the given class, it generates a corresponding GUI interpretation
		 * Float and int for a corresponding float and int text fields
		 * String for file fields
		 * If you don't want to show some of them, use the other function where you can pass an array list of string names you want to ommit
		 * 
		 * As an example, maybe PINT_ opener is checked for all variables that we want to show into the gui, and stuff like
		 * 
		 * PINT_
		 * FILE_
		 * DBL_
		 * 
		 * You know, we can also set here unique field name openers like dir_somename for this type of parsing as well!		 
		 */
		
		for (int i = 0; i< fields.length; i++){
			if (fields[i].getType().equals(int.class)){
				//System.out.println(fields[i].getName());
				//System.out.println(fields[i].getName().trim().split("_")[0]);
				if (fields[i].getName().trim().split("_")[0].equals("PINT")){ //apparently == doesn't work
					setVariable(c, fields[i], guiVariableTypes.POSITIVE_INT, variableSet);
				}
			}
			if (fields[i].getType().equals(double.class)){
				if (fields[i].getName().split("_")[0].equals("DBL")){
					setVariable(c, fields[i], guiVariableTypes.DOUBLE, variableSet);
				}
				if (fields[i].getName().split("_")[0].equals("SLD")){
					setVariable(c, fields[i], guiVariableTypes.ZERO_TO_ONE_SLIDER, variableSet);
				}
			}
			if (fields[i].getType().equals(String.class)){
				if (fields[i].getName().split("_")[0].equals("FILE")){
					setVariable(c, fields[i], guiVariableTypes.FILE, variableSet);
				}
				if (fields[i].getName().split("_")[0].equals("DIR")){
					setVariable(c, fields[i], guiVariableTypes.DIRECTORY, variableSet);
				}
			}
		}				
		return variableSet;		
	}
	
	private static void setVariable(Class c, Field f, guiVariableTypes gvt, HashMap<String, NameToVariableClass> variableSet){
		System.out.println("\t"+f.getName());
		variableSet.put(f.getName(), new NameToVariableClass(gvt));
		
		try {
			variableSet.get(f.getName()).valueString= String.valueOf(f.get(c));
			//variableSet.get(fields[i].getName()).setAssocGuiValue(variableSet.get(fields[i].getName()).valueString); //calling this here is useless since the the textFields/GUI components aren't loaded yet
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
