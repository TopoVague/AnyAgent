package alan_GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

public class SystemVariablesClass {
	public enum guiVariableTypes{FILE, DIRECTORY, DOUBLE, INT, SLIDER, BOOLEAN, STRING};
	ArrayList<NameToVariableClass> variables;
	HashMap<String, String> variableStrings;	//The variables used to run the system
	
	public SystemVariablesClass(){		
		variableStrings= new HashMap<String, String>();
		variables= new ArrayList<NameToVariableClass>();
		variables.add(new NameToVariableClass("Rhino EXE Path", guiVariableTypes.FILE));
		variables.add(new NameToVariableClass("XML dir", guiVariableTypes.DIRECTORY));
		variables.add(new NameToVariableClass("Date Text File", guiVariableTypes.FILE));
		variables.add(new NameToVariableClass("Archive dir", guiVariableTypes.DIRECTORY));
		variables.add(new NameToVariableClass("GH Path", guiVariableTypes.FILE));
		variables.add(new NameToVariableClass("Export Directory", guiVariableTypes.DIRECTORY));
		variables.add(new NameToVariableClass("Hill Climbing Step Size", guiVariableTypes.DOUBLE));
		variables.add(new NameToVariableClass("Simluated Annealing Budget", guiVariableTypes.INT));
	}
	
	public JPanel getPanel(String name, guiVariableTypes gvt){
		JPanel jp= new JPanel();
		//jp.setBorder(BorderFactory.createEtchedBorder());
		//jp.setBorder(BorderFactory.createLineBorder(Color.black));
		jp.setBorder(new EmptyBorder(10,10,10,10));
		//jp.setBackground(Color.red);
		Constants.addSetBorderLayoutTitle(jp, name, false);
		JTextField jtfFileName= new JTextField();
		jtfFileName.setForeground(Color.gray);
		jtfFileName.setEditable(false);
		jp.add(jtfFileName, BorderLayout.CENTER);
		
		//Addings stuff into the bottom of the panel
		if (gvt== guiVariableTypes.DIRECTORY || gvt==guiVariableTypes.FILE){
			JButton jb= getFileChooserButton(name, gvt, jtfFileName);
			jtfFileName.setText("No Selection");
			jp.add(jb,BorderLayout.SOUTH);
		}		
		
		//Integer Component set here
		if (gvt == guiVariableTypes.INT || gvt==guiVariableTypes.DOUBLE) {
			JTextField jtf = new JTextField();
			jtf.setInputVerifier(new InputVerifier() {
				
				@Override
				public boolean verify(JComponent input) {
					// TODO Auto-generated method stub
					JTextField associatedJTf= (JTextField) input;
					String text = associatedJTf.getText();
					boolean isCorrespondingValue= false;
					if (gvt== guiVariableTypes.INT){
						isCorrespondingValue= isInteger(text);
					}else{
						isCorrespondingValue= isDouble(text);
					}
					
					if (isCorrespondingValue){
						variableStrings.put(name, text);
						AgentPanel12_GUI_Alan.addMessage("["+name + "] set to "+ text);
						return true;
					}else{
						variableStrings.remove(name);
						AgentPanel12_GUI_Alan.addMessage("["+name + "]'s value '"+text+ "' is not a "+gvt.toString());
						return false;
					}
				}
			});
			
			jp.add(jtf, BorderLayout.CENTER);						
		}
		return jp;
	}
	
	public JButton getFileChooserButton(String keyName, guiVariableTypes gvt, JTextField jtf){		
		JButton jb= new JButton();
		
		if (gvt.equals(guiVariableTypes.DIRECTORY)){
			jb.setText("Choose a directory");
		}else{
			jb.setText("Choose a file");
		}
		
		//jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setContentAreaFilled(false);
		
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				
				if (gvt.equals(guiVariableTypes.DIRECTORY)){
					chooser.setDialogTitle("Choose a directory");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				}else{
					chooser.setDialogTitle("Choose a file");
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				}
				
				//
				// disable the "All files" option.
				//
				chooser.setAcceptAllFileFilterUsed(false);
				//
				if (chooser.showOpenDialog(jb) == JFileChooser.APPROVE_OPTION) {
					System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
					System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
					jtf.setText(chooser.getSelectedFile().getName().toString());
					AgentPanel12_GUI_Alan.addMessage("["+keyName+"] set to "+chooser.getSelectedFile().toString());
					variableStrings.put(keyName, chooser.getSelectedFile().toString());
					jtf.setForeground(new Color(10,200,10));
				} else {
					System.out.println("No Selection ");
					jtf.setText("No Selection ");
					//AgentPanel12_GUI_Alan.addMessage("["+keyName+"] key removed");					
					variableStrings.remove(keyName); //removes if present
					jtf.setForeground(Color.gray);
				}
			}
		});

		return jb;
	}
	
	public boolean canRun(){
		boolean answer= true;
		AgentPanel12_GUI_Alan.addLine();
		AgentPanel12_GUI_Alan.addMessage("Starting run...");
		AgentPanel12_GUI_Alan.addLine();
		for (int n = 0; n < variables.size(); n++){
			String variableName= variables.get(n).name;
			if (!variableStrings.containsKey(variableName)){
				answer= false;
				AgentPanel12_GUI_Alan.addMessage("not set: ["+variableName+"]");
				//break;
			}
		}
		//AgentPanel12_GUI_Alan.addLine();
		return answer;
	}
	
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static boolean isDouble( String str ){
	    try{
	        Double.parseDouble( str );
	        return true;
	    }
	    catch( Exception e ){
	        return false;
	    }
	}
}
