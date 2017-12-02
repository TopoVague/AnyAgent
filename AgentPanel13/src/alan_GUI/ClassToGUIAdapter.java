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
					System.out.println("\t"+fields[i].getName());
					variableSet.put(fields[i].getName(), new NameToVariableClass(guiVariableTypes.POSITIVE_INT));
					
					try {
						variableSet.get(fields[i].getName()).valueString= String.valueOf(fields[i].get(c));
						//variableSet.get(fields[i].getName()).setAssocGuiValue(variableSet.get(fields[i].getName()).valueString); //calling this here is useless since the the textFields aren't loaded yet
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (fields[i].getType().equals(Double.class)){
				if (fields[i].getName().split("_")[0]=="DBL"){
					variableSet.put(fields[i].getName(), new NameToVariableClass(guiVariableTypes.DOUBLE));
				}
			}
			if (fields[i].getType().equals(String.class)){
				if (fields[i].getName().split("_")[0]=="FILE"){
					variableSet.put(fields[i].getName(), new NameToVariableClass(guiVariableTypes.FILE));
				}
			}
		}				
		return variableSet;		
	}
}
