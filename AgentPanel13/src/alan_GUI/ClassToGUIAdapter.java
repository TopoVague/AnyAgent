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
		 * You know, we can also set here unique field name openers like dir_somename for this type of parsing as well!
		 */
		
		for (int i = 0; i< fields.length; i++){
			if (fields[i].getType().equals(int.class)){
				variableSet.put(fields[i].getName(), new NameToVariableClass(guiVariableTypes.POSITIVE_INT));
			}
			if (fields[i].getType().equals(Double.class)){
				variableSet.put(fields[i].getName(), new NameToVariableClass(guiVariableTypes.DOUBLE));
			}
			if (fields[i].getType().equals(String.class)){
				variableSet.put(fields[i].getName(), new NameToVariableClass(guiVariableTypes.FILE));
			}
		}				
		return variableSet;		
	}
}
