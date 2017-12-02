package alan_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import alan_GUI.SystemVariablesClass.guiVariableTypes;
import jdk.nashorn.internal.scripts.JS;

public class VariableJPanelCreator {
	//Return a JPanel that contains the GUI component relating to the given variables
	public static JPanel getPanel(String variableName, guiVariableTypes gvt,  HashMap<String, NameToVariableClass> variableSet) {
		JPanel jp = new JPanel();
		// jp.setBorder(BorderFactory.createEtchedBorder());
		// jp.setBorder(BorderFactory.createLineBorder(Color.black));
		jp.setBorder(new EmptyBorder(10, 10, 10, 10));
		// jp.setBackground(Color.red);
		Constants.addSetBorderLayoutTitle(jp, variableName, false);
		JTextField jtfFileName = new JTextField();

		jtfFileName.setForeground(Color.gray);
		jtfFileName.setEditable(false);
		variableSet.get(variableName).setAssocJtf(jtfFileName);
		jp.add(jtfFileName, BorderLayout.CENTER);

		// Addings stuff into the bottom of the panel
		if (gvt == guiVariableTypes.DIRECTORY || gvt == guiVariableTypes.FILE) {
			JButton jb = getFileChooserButton(variableName, gvt, jtfFileName, variableSet);
			jtfFileName.setText("No Selection");
			jp.add(jb, BorderLayout.SOUTH);
		}

		// Integer Component set here
		if (gvt == guiVariableTypes.POSITIVE_INT || gvt == guiVariableTypes.DOUBLE) {
			JTextField jtf = new JTextField();
			jtf.setInputVerifier(new InputVerifier() {
				@Override
				public boolean verify(JComponent input) {
					// TODO Auto-generated method stub
					JTextField associatedJTf = (JTextField) input;
					String text = associatedJTf.getText();
					boolean isCorrespondingValue = false;
					if (gvt == guiVariableTypes.POSITIVE_INT) {
						isCorrespondingValue = isPostivieInteger(text);
					} else {
						isCorrespondingValue = isDouble(text);
					}

					if (isCorrespondingValue) {
						// variableStrings.put(name, text);
						variableSet.get(variableName).valueString = text;
						AgentPanel12_GUI_Alan.addMessage("[" + variableName + "] set to " + text);
						return true;
					} else {
						variableSet.remove(variableName);
						AgentPanel12_GUI_Alan
								.addMessage("[" + variableName + "]'s value '" + text + "' is not a " + gvt.toString());
						return false;
					}
				}
			});
			variableSet.get(variableName).setAssocJtf(jtf); //!! these are using a different type of text field, so the associated field needs to be swapped
			jp.add(jtf, BorderLayout.CENTER);
		}
		
		
		if (gvt== guiVariableTypes.ZERO_TO_ONE_SLIDER){
			JTextField jtf= new JTextField();
			jtf.setEditable(false);
			
			JSlider jsVal= new JSlider(0,100); //Since there are apparently no decmial types
			jsVal.setPaintTicks(true);
			jsVal.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					String StrVal= String.valueOf(jsVal.getValue()/100.0);
					jtf.setText(StrVal);
					variableSet.get(variableName).valueString = StrVal;
				}
			});
			jtf.setText(String.valueOf(jsVal.getValue()/100)); //setDefaultText
			variableSet.get(variableName).valueString = jtf.getText(); //setting default value
			variableSet.get(variableName).setAssocJtf(jtf);
			variableSet.get(variableName).setAssocJS(jsVal);
			jp.add(jtf, BorderLayout.CENTER);
			jp.add(jsVal, BorderLayout.SOUTH);			
		}
		
		return jp;
	}
	
	public static JButton getFileChooserButton(String keyName, guiVariableTypes gvt, JTextField jtf, HashMap<String, NameToVariableClass> variableSet) {
		JButton jb = new JButton();

		if (gvt.equals(guiVariableTypes.DIRECTORY)) {
			jb.setText("Choose a directory");
		} else {
			jb.setText("Choose a file");
		}

		// jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.setContentAreaFilled(false);

		jb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));

				if (gvt.equals(guiVariableTypes.DIRECTORY)) {
					chooser.setDialogTitle("Choose a directory");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				} else {
					chooser.setDialogTitle("Choose a file");
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				}

				chooser.setAcceptAllFileFilterUsed(false);// disable the "All files" option.

				if (chooser.showOpenDialog(jb) == JFileChooser.APPROVE_OPTION) {
					//System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
					//System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
					variableSet.get(keyName).valueString = chooser.getSelectedFile().toString();
					setFileString(keyName, chooser.getSelectedFile(), variableSet);
				} else {
					System.out.println("No Selection ");
					jtf.setText("No Selection ");
					// AgentPanel12_GUI_Alan.addMessage("["+keyName+"] key
					// removed");
					// variableSet.remove(keyName); //removes if present
					variableSet.get(keyName).valueString = null;
					jtf.setForeground(Color.gray);
				}
			}
		});

		return jb;
	}
	
	public static void setFileString(String keyName, File file, HashMap<String, NameToVariableClass> variableSet){
		if (file!= null && variableSet.containsKey(keyName)){
			variableSet.get(keyName).setAssocGuiValue(file.getName());
			variableSet.get(keyName).valueString = file.toString();
			AgentPanel12_GUI_Alan.addMessage("[" + keyName + "] set to " +file.toString());
		}
	}
	
	public static void addSetBorderLayoutTitle(Container parent, String title, Boolean capitalized){
		//Set the parent as border layout, then adds a JTextField to the North with the title capitalized and the text area non editable
		parent.setLayout(new BorderLayout());
		String titleString;
		titleString= title;
		if (capitalized){
			titleString= titleString.toUpperCase();
		}
		JTextField jtfTitle= new JTextField(titleString);
		Font font= new Font(jtfTitle.getText(), Font.BOLD,12);		
		jtfTitle.setFont(font);
		jtfTitle.setEditable(false);
		parent.add(jtfTitle, BorderLayout.NORTH);
	}
	
	public static Container addGBCComponent(Container parent, Container child, int x, int y, double weightX, double weightY){
		//GridBagLayout gbl= new GridBagLayout();
		//parent.setLayout(gbl);
		GridBagConstraints gbc= new GridBagConstraints();	
		gbc.gridy= y;
		gbc.gridx= x;		
		gbc.weightx= weightX;
		gbc.weighty= weightY;
		gbc.fill= GridBagConstraints.BOTH;		
		parent.add(child, gbc);
		return child;
	}
	
	public static Container addGBCComponent(Container parent, Container child, int x, int y, double weightX, double weightY, int fill){
		//GridBagLayout gbl= new GridBagLayout();
		//parent.setLayout(gbl);
		GridBagConstraints gbc= new GridBagConstraints();	
		gbc.gridy= y;
		gbc.gridx= x;		
		gbc.weightx= weightX;
		gbc.weighty= weightY;
		//gbc.fill= GridBagConstraints.BOTH;
		gbc.fill= fill;
		parent.add(child, gbc);
		return child;
	}
	
	public static Container addGBCComponent(Container parent, Container child, int x, int y, double weightX, double weightY, int fill, int anchor){
		//GridBagLayout gbl= new GridBagLayout();
		//parent.setLayout(gbl);
		GridBagConstraints gbc= new GridBagConstraints();	
		gbc.gridy= y;
		gbc.gridx= x;		
		gbc.weightx= weightX;
		gbc.weighty= weightY;
		//gbc.fill= GridBagConstraints.BOTH;
		gbc.fill= fill;
		gbc.anchor= GridBagConstraints.NORTH;
		parent.add(child, gbc);
		return child;
	}
	
	public static boolean isPostivieInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		// if (str.charAt(0) == '-') {
		// if (length == 1) {
		// return false;
		// }
		// i = 1;
		// }
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
