package AgentPanel13_GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class systemVariablesClass {
	public enum guiVariableTypes{FILEPATH, FLOAT, SLIDER, BOOLEAN, STRING};
	ArrayList<nameToVariableClass> variables;
		
	public systemVariablesClass(){		
		variables= new ArrayList<nameToVariableClass>();
		variables.add(new nameToVariableClass("Rhino Path", guiVariableTypes.FILEPATH));
		variables.add(new nameToVariableClass("XML dir", guiVariableTypes.FILEPATH));
		variables.add(new nameToVariableClass("Archive dir", guiVariableTypes.FILEPATH));
		variables.add(new nameToVariableClass("GH Path", guiVariableTypes.FILEPATH));
		variables.add(new nameToVariableClass("Export Directory", guiVariableTypes.FILEPATH));
		variables.add(new nameToVariableClass("Hill Climbing Step Size", guiVariableTypes.FLOAT));
		variables.add(new nameToVariableClass("Simluated Annealing Budget", guiVariableTypes.FLOAT));
	}
	
	public JPanel getPanel(String name, guiVariableTypes gvt){
		JPanel jp= new JPanel();
		//jp.setBorder(BorderFactory.createEtchedBorder());
		jp.setBorder(BorderFactory.createLineBorder(Color.black));
		//jp.setBackground(Color.red);
		Constants.addSetBorderLayoutTitle(jp, name, false);
		
		if (gvt== guiVariableTypes.FILEPATH){
			JButton jb= new JButton("Choose File");
			JTextField jtfFileName= new JTextField();
			jtfFileName.setEditable(false);
			jp.add(jtfFileName, BorderLayout.CENTER);
			jp.add(jb, BorderLayout.SOUTH);
			
			jb.addActionListener(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFileChooser jfc= new JFileChooser();
					int returnVal = jfc.showOpenDialog(jp);		
				}
			});
		}		
		return jp;
	}
}
