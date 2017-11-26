package alan_GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
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
}
