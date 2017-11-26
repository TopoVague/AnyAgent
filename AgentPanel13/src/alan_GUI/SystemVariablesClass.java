package alan_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
	public enum guiVariableTypes {
		FILE, DIRECTORY, DOUBLE, POSITIVE_INT, SLIDER, BOOLEAN, STRING
	};

	ArrayList<String> variablesOrder;
	HashMap<String, NameToVariableClass> variableSet; // The variables used to
														// run the system

	private String savedDefaultVariableFileName = "systemVariables.ser";

	public SystemVariablesClass() {
		variablesOrder = new ArrayList<String>(Arrays.asList("Rhino EXE Path", "XML dir", "Date Text File",
				"Archive dir", "GH Path", "Export Directory", "Hill Climbing Step Size", "Simluated Annealing Budget"));
		//variableSet= loadDefault();
		AgentPanel12_GUI_Alan.addMessage("Initializing default variables");
		variableSet = new HashMap<String, NameToVariableClass>();
		variableSet.put("Rhino EXE Path", new NameToVariableClass(guiVariableTypes.FILE));
		variableSet.put("XML dir", new NameToVariableClass(guiVariableTypes.DIRECTORY));
		variableSet.put("Date Text File", new NameToVariableClass(guiVariableTypes.FILE));
		variableSet.put("Archive dir", new NameToVariableClass(guiVariableTypes.DIRECTORY));
		variableSet.put("GH Path", new NameToVariableClass(guiVariableTypes.FILE));
		variableSet.put("Export Directory", new NameToVariableClass(guiVariableTypes.DIRECTORY));
		variableSet.put("Hill Climbing Step Size", new NameToVariableClass(guiVariableTypes.DOUBLE));
		variableSet.put("Simluated Annealing Budget", new NameToVariableClass(guiVariableTypes.POSITIVE_INT));
		
	}

	public HashMap<String, NameToVariableClass> loadDefault() {
		// Look for the save file in the current directory. Attempt to
		// deserialize it
		AgentPanel12_GUI_Alan.addMessageReportLine();
		AgentPanel12_GUI_Alan.addMessage("Attempting to load default system variables from "+ savedDefaultVariableFileName);
		
		HashMap<String, NameToVariableClass> loadedVariableSet = null;
		File SerializedSystemVariables = new File(savedDefaultVariableFileName);
		if (SerializedSystemVariables.exists()) {
			try {
				FileInputStream fis = new FileInputStream(savedDefaultVariableFileName);
				ObjectInputStream ois = new ObjectInputStream(fis);
				loadedVariableSet = (HashMap) ois.readObject();
				ois.close();
				fis.close();
				AgentPanel12_GUI_Alan.addMessage("System variables loaded successfully");
			} catch (IOException ioe) {
				AgentPanel12_GUI_Alan.addMessage("IO Exception: " + ioe.toString());
				ioe.printStackTrace();
			} catch (ClassNotFoundException c) {
				System.out.println("Class not found");
				AgentPanel12_GUI_Alan.addMessage("Class not found exception: " + c.toString());
				c.printStackTrace();
			}
		} else {
			AgentPanel12_GUI_Alan.addMessage("Default system variable file does not exist");
		}
		
		if (variableSet!= null){ //That means this thing is already loaded
			for (String key : loadedVariableSet.keySet()){
				if (variableSet.containsKey(key) && variableSet.get(key).jtf_assoc!= null && loadedVariableSet.get(key).valueString!= null){
					
					if (variableSet.get(key).gvt== guiVariableTypes.FILE || variableSet.get(key).gvt== guiVariableTypes.DIRECTORY){					
						variableSet.get(key).jtf_assoc.setText(loadedVariableSet.get(key).valueString);						
						setFileString(key,new File(loadedVariableSet.get(key).valueString));
					}
					if (variableSet.get(key).gvt== guiVariableTypes.DOUBLE || variableSet.get(key).gvt== guiVariableTypes.POSITIVE_INT){
						JTextField jtfAssoc= variableSet.get(key).jtf_assoc;
						jtfAssoc.setText(loadedVariableSet.get(key).valueString);	
						variableSet.get(key).valueString= loadedVariableSet.get(key).valueString;
						AgentPanel12_GUI_Alan.addMessage("[" + key + "] set to "+ loadedVariableSet.get(key).valueString);
					}
				}
			}
		}
		AgentPanel12_GUI_Alan.addMessageReportLine();
		return loadedVariableSet;
	}

	public void saveDefault() {
		// Serialize the variable set into the current directory
		if (variableSet != null) {
			AgentPanel12_GUI_Alan.addMessage("Saving system variable set...");
			try {
				FileOutputStream fos = new FileOutputStream(savedDefaultVariableFileName);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(variableSet);
				oos.close();
				fos.close();
				// System.out.printf("Serialized HashMap data is saved in
				// hashmap.ser");
				AgentPanel12_GUI_Alan.addMessage("Saved default system variables to: " + savedDefaultVariableFileName);
			} catch (IOException ioe) {
				AgentPanel12_GUI_Alan.addMessage("Error saving system variables...");
				AgentPanel12_GUI_Alan.addMessage(ioe.toString());
				ioe.printStackTrace();
			}
		} else {
			AgentPanel12_GUI_Alan.addMessage("System variable set is null");
		}
	}

	public JPanel getPanel(String name, guiVariableTypes gvt) {
		JPanel jp = new JPanel();
		// jp.setBorder(BorderFactory.createEtchedBorder());
		// jp.setBorder(BorderFactory.createLineBorder(Color.black));
		jp.setBorder(new EmptyBorder(10, 10, 10, 10));
		// jp.setBackground(Color.red);
		Constants.addSetBorderLayoutTitle(jp, name, false);
		JTextField jtfFileName = new JTextField();

		jtfFileName.setForeground(Color.gray);
		jtfFileName.setEditable(false);
		variableSet.get(name).setAssocJtf(jtfFileName);
		jp.add(jtfFileName, BorderLayout.CENTER);

		// Addings stuff into the bottom of the panel
		if (gvt == guiVariableTypes.DIRECTORY || gvt == guiVariableTypes.FILE) {
			JButton jb = getFileChooserButton(name, gvt, jtfFileName);
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
						variableSet.get(name).valueString = text;
						AgentPanel12_GUI_Alan.addMessage("[" + name + "] set to " + text);
						return true;
					} else {
						variableSet.remove(name);
						AgentPanel12_GUI_Alan
								.addMessage("[" + name + "]'s value '" + text + "' is not a " + gvt.toString());
						return false;
					}
				}
			});
			variableSet.get(name).setAssocJtf(jtf); //!! these are using a different type of text field, so the associated field needs to be swapped
			jp.add(jtf, BorderLayout.CENTER);
		}
		return jp;
	}

	public JButton getFileChooserButton(String keyName, guiVariableTypes gvt, JTextField jtf) {
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
					setFileString(keyName, chooser.getSelectedFile());
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

	public void setFileString(String keyName, File file){
		if (file!= null && variableSet.containsKey(keyName)){
			JTextField jtf= variableSet.get(keyName).jtf_assoc;
			jtf.setText(file.getName());
			variableSet.get(keyName).valueString = file.toString();
			variableSet.get(keyName).jtf_assoc.setForeground(new Color(10, 200, 10));
			AgentPanel12_GUI_Alan.addMessage("[" + keyName + "] set to " +file.toString());
		}
	}
	
	public boolean canRun() {
		boolean answer = true;
		AgentPanel12_GUI_Alan.addMessageReportLine();
		AgentPanel12_GUI_Alan.addMessage("Starting run...");
		AgentPanel12_GUI_Alan.addMessageReportLine();
		for (int n = 0; n < variablesOrder.size(); n++) {
			String variableName = variablesOrder.get(n);
			if (!variableSet.containsKey(variableName) || variableSet.get(variableName).valueString == null) {
				answer = false;
				AgentPanel12_GUI_Alan.addMessage("not set: [" + variableName + "]");
				// break;
			}
		}
		// AgentPanel12_GUI_Alan.addLine();
		return answer;
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
