package alan_GUI;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class CustomFileChooser extends JPanel implements ActionListener {
	//Thieved: http://www.rgagnon.com/javadetails/java-0370.html
	
	JButton go;
	JFileChooser chooser;
	String choosertitle;
	
	int JFileChooserSelectionType;
	
//	public CustomFileChooser() {
//		go = new JButton("Do it");
//		go.addActionListener(this);
//		add(go);
//	}
	
	public CustomFileChooser(int JFileChooserSelectionType){
		this.JFileChooserSelectionType= JFileChooserSelectionType;
		go = new JButton("Do it");
		go.addActionListener(this);
		add(go);
	}
	
	public void actionPerformed(ActionEvent e) {
		int result;

		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle(choosertitle);
		//chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setFileSelectionMode(JFileChooserSelectionType);
		
		//
		// disable the "All files" option.
		//
		chooser.setAcceptAllFileFilterUsed(false);
		//
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
		} else {
			System.out.println("No Selection ");
		}
	}

//	public Dimension getPreferredSize() {
//		return new Dimension(200, 200);
//	}

	public static void main(String s[]) {
		JFrame frame = new JFrame("");
		CustomFileChooser panel = new CustomFileChooser(JFileChooser.DIRECTORIES_ONLY);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(panel, "Center");
		frame.setSize(panel.getPreferredSize());
		frame.setVisible(true);
	}
}