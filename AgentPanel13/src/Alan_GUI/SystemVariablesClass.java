package Alan_GUI;
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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SystemVariablesClass {
	public enum guiVariableTypes{FILEPATH, FLOAT, SLIDER, BOOLEAN, STRING};
	ArrayList<NameToVariableClass> variables;
	HashMap<String, String> variableStrings;	//The variables used to run the system
	
	public SystemVariablesClass(){		
		variableStrings= new HashMap<String, String>();
		variables= new ArrayList<NameToVariableClass>();
		variables.add(new NameToVariableClass("Rhino Path", guiVariableTypes.FILEPATH));
		variables.add(new NameToVariableClass("XML dir", guiVariableTypes.FILEPATH));
		variables.add(new NameToVariableClass("Date Text File", guiVariableTypes.FILEPATH));
		variables.add(new NameToVariableClass("Archive dir", guiVariableTypes.FILEPATH));
		variables.add(new NameToVariableClass("GH Path", guiVariableTypes.FILEPATH));
		variables.add(new NameToVariableClass("Export Directory", guiVariableTypes.FILEPATH));
		variables.add(new NameToVariableClass("Hill Climbing Step Size", guiVariableTypes.FLOAT));
		variables.add(new NameToVariableClass("Simluated Annealing Budget", guiVariableTypes.FLOAT));
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
		if (gvt== guiVariableTypes.FILEPATH){
			JButton jb= getFileChooserButton(name, jtfFileName);
			jtfFileName.setText("No Selection");
			jp.add(jb,BorderLayout.SOUTH);
		}		
		return jp;
	}
	
	public JButton getFileChooserButton(String keyName, JTextField jtf){
		JButton jb= new JButton("Choose File");
		//jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setContentAreaFilled(false);
		
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select a file or directory");
				//chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				
				//
				// disable the "All files" option.
				//
				chooser.setAcceptAllFileFilterUsed(false);
				//
				if (chooser.showOpenDialog(jb) == JFileChooser.APPROVE_OPTION) {
					System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
					System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
					jtf.setText(chooser.getSelectedFile().getName().toString());
					AgentPanel12_GUI_Alan.addMessage("Set ["+keyName+"] to "+chooser.getSelectedFile().toString());
					variableStrings.put(keyName, chooser.getSelectedFile().toString());
				} else {
					System.out.println("No Selection ");
					jtf.setText("No Selection ");
					AgentPanel12_GUI_Alan.addMessage("Removed ["+keyName+"] key");
					variableStrings.remove(keyName);
				}
			}
		});

		return jb;
	}
}
