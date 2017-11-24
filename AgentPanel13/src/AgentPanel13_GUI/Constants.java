package AgentPanel13_GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;

import javax.swing.JTextField;

public class Constants {
	public static void addSetBorderLayoutTitle(Container parent, String title, Boolean capitalized){
		//Set the parent as border layout, then adds a JTextField to the North with the title capitalized and the text area non editable
		parent.setLayout(new BorderLayout());
		String titleString;
		titleString= title;
		if (capitalized){
			titleString= titleString.toUpperCase();
		}
		JTextField jtfTitle= new JTextField(titleString);
		jtfTitle.setEditable(false);
		parent.add(jtfTitle, BorderLayout.NORTH);
	}
	
	public static void addGBCComponent(Container parent, Container child, int x, int y, double weightX, double weightY){
		//GridBagLayout gbl= new GridBagLayout();
		//parent.setLayout(gbl);
		GridBagConstraints gbc= new GridBagConstraints();	
		gbc.gridy= y;
		gbc.gridx= x;		
		gbc.weightx= weightX;
		gbc.weighty= weightY;
		gbc.fill= GridBagConstraints.BOTH;		
		parent.add(child, gbc);
	}
}
