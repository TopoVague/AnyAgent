package alan_GUI;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.JSlider;
import javax.swing.JTextField;

import alan_GUI.SystemVariablesClass.guiVariableTypes;

public class NameToVariableClass implements Serializable {
	guiVariableTypes gvt;
	private transient JTextField jtf_assoc;
	private transient JSlider js_assoc; //associated slider
	String valueString;
	
	public NameToVariableClass(guiVariableTypes gvt){
		//this.name= name;
		this.gvt= gvt;
	}	
	
	public void setAssocJtf(JTextField jtf){//so you can change what it looks like
		this.jtf_assoc= jtf;
	}
	
	public void setAssocJS(JSlider js){//so you can change what it looks like
		this.js_assoc= js;
	}
	
	public void setAssocGuiValue(String valueString){
		if (jtf_assoc!= null){
			jtf_assoc.setText(valueString);
			jtf_assoc.setForeground(new Color(10, 200, 10));
		}
		
		if (js_assoc!= null){ //I guess it's possible that some one fucks up here			
			try{
				//System.out.println("slider set for value of "+valueString);
				//System.out.println("Max value of slider right now is " + js_assoc.getMaximum());
				int value= (int) (Double.parseDouble(valueString) * 100);
				//System.out.println("Slider value: " + value);
				js_assoc.setValue(value);
				//js_assoc.repaint();
			}catch (Exception e) {
				// TODO: handle exception
				AgentPanel12_GUI_Alan.addMessage(e.toString());
			}
		}else{
			System.out.println("value string set for "+ valueString + " has no slider");
		}
		
	}
}
