package alan_GUI;

import java.io.Serializable;

import javax.swing.JTextField;

import alan_GUI.SystemVariablesClass.guiVariableTypes;

public class NameToVariableClass implements Serializable {
	guiVariableTypes gvt;
	transient JTextField jtf_assoc;
	String valueString;
	
	public NameToVariableClass(guiVariableTypes gvt){
		//this.name= name;
		this.gvt= gvt;
	}	
	
	public void setAssocJtf(JTextField jtf){//so you can change what it looks like
		this.jtf_assoc= jtf;
	}
}
