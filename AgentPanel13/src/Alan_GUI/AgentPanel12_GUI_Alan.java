package Alan_GUI;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Alan_GUI.SystemVariablesClass.guiVariableTypes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.filechooser.*;



public class AgentPanel12_GUI_Alan {
	private static JFrame mainFrame;
	
	private static JPanel largeSystemPanel;
	private static JPanel largeXmlPanel;
	private static JPanel largeGraphPanel;
	
	private static JPanel xmlVariablePanel;
	private static JPanel messagePanel;
	private static JPanel geometryPanel;
	private static JPanel plotPanel;
	private static JTextArea jtaMessages;
	
	public static void main(String[] args){
		/*
		 * The general idea:
		 * The gui is split into three slots, from left to right: 0,1,2. 
		 * -	The left side one (0) contains the configurations for the system exe
		 * -	The middle one, (1) contains two divided panels, top for the xml configuration, bottom for the text messages
		 * -	The right one, (2), contains top a panel for the jar generation, and the bottom a panel for the plots.
		 * 
		 */
		mainFrame= new JFrame("AnyAgentFrame");		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(new GridBagLayout());		
				
		initializeSystemPanel();
		initializeXmlPanel();
		initializeGraphPanel();
		
		Constants.addGBCComponent(mainFrame, largeSystemPanel, 0,0,1,1);		
		Constants.addGBCComponent(mainFrame, largeXmlPanel, 1,0,1,1);
		Constants.addGBCComponent(mainFrame, largeGraphPanel, 2,0,1,1);
		
		mainFrame.pack();
		mainFrame.setSize(new Dimension(1280,720));
		mainFrame.setMinimumSize(mainFrame.getSize() );
		mainFrame.setVisible(true);
		
		for (int x =0; x< 100; x++){
			addMessage("Testing " + x);
		}
	}		
	
	public static JPanel initializeSystemPanel(){
		largeSystemPanel= new JPanel();	
		//systemPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		largeSystemPanel.setLayout(new BorderLayout());
		Constants.addSetBorderLayoutTitle(largeSystemPanel, "System Variables", true);
		
		//JPanel systemPanelCanvas= new JPanel();

		JPanel jpScrollPanePanel= new JPanel();		
		jpScrollPanePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JScrollPane jspSystemScrollPane= new JScrollPane(jpScrollPanePanel);
		jspSystemScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jspSystemScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		largeSystemPanel.add(jspSystemScrollPane, BorderLayout.CENTER);
		jpScrollPanePanel.setLayout(new GridBagLayout());
		SystemVariablesClass svc= new SystemVariablesClass();
			
		for (int n =0 ; n < svc.variables.size(); n++){
			NameToVariableClass nvc = svc.variables.get(n);
			guiVariableTypes gvType= nvc.gvt;	
			Constants.addGBCComponent(jpScrollPanePanel, svc.getPanel(nvc.name, gvType), 0,n,0.25,1);
			//System.out.println(n+ " Name: " + nvc.name + ", type: "+ gvType.toString());
		}
		
		JButton runButton= new JButton("Run");
		Constants.addGBCComponent(jpScrollPanePanel, runButton, 0, svc.variables.size() ,0.25,1);
		
		return largeSystemPanel;
	}
	
	public static JPanel initializeXmlPanel(){
		//setting up outside panel
		largeXmlPanel= new JPanel();
		largeXmlPanel.setBackground(new Color(200,200,200));			
		largeXmlPanel.setLayout(new GridBagLayout());
		largeXmlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		xmlVariablePanel= new JPanel();
		Constants.addSetBorderLayoutTitle(xmlVariablePanel, "Xml Variables", true);
		
		messagePanel= new JPanel();
								
		jtaMessages = new JTextArea();
		jtaMessages.setWrapStyleWord(true);
		jtaMessages.setLineWrap(true);
		jtaMessages.setBorder(new EmptyBorder(10, 10, 10, 10));
		JScrollPane jspMessages= new JScrollPane(jtaMessages);				
		jspMessages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jspMessages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		Constants.addSetBorderLayoutTitle(messagePanel, "Messages", true);
		messagePanel.add(jspMessages, BorderLayout.CENTER);
		
		Constants.addGBCComponent(largeXmlPanel, xmlVariablePanel, 0,0,1,1);
		Constants.addGBCComponent(largeXmlPanel, messagePanel, 0,1,1,1);
		return largeXmlPanel;
	}
	
	public static JPanel initializeGraphPanel(){
		largeGraphPanel= new JPanel();
		largeGraphPanel.setLayout(new GridBagLayout());
		//graphPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		geometryPanel= new JPanel();	
		Constants.addSetBorderLayoutTitle(geometryPanel, "Geometry Pane", true);
		//geometryPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		plotPanel= new JPanel();
		Constants.addSetBorderLayoutTitle(plotPanel, "Plot Pane", true);
		//plotPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		Constants.addGBCComponent(largeGraphPanel, geometryPanel, 0,0,1,1);
		Constants.addGBCComponent(largeGraphPanel, plotPanel, 0,1,1,1);
		return largeGraphPanel;
	}
	
	public static void addMessage(String message){
		if (jtaMessages!= null){
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//System.out.println( sdf.format(cal.getTime()) );
			jtaMessages.append("["+sdf.format(cal.getTime()) +"]  "+ message+"\n");
			jtaMessages.setCaretPosition(jtaMessages.getDocument().getLength());
			mainFrame.repaint();
		}else{
			System.out.println("JTA Messages is null");
		}
	}	
}