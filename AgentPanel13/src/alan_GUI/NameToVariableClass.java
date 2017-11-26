package alan_GUI;

import javax.swing.JTextField;

import alan_GUI.SystemVariablesClass.guiVariableTypes;

public class NameToVariableClass {
	guiVariableTypes gvt;
	JTextField jtf_assoc;
	String valueString;
	
	public NameToVariableClass(guiVariableTypes gvt){
		//this.name= name;
		this.gvt= gvt;
	}	
	
	public void initializeJtf(JTextField jtf){//so you can change what it looks like
		this.jtf_assoc= jtf;
	}
}
