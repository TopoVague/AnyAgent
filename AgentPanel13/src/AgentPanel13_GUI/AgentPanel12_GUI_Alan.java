package AgentPanel13_GUI;
import java.io.File;
import java.io.IOException;

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

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.filechooser.*;



public class AgentPanel12_GUI_Alan {
	static JPanel systemPanel;
	static JPanel xmlPanel;
	static JPanel graphPanel;
	static JPanel xmlVariablePanel;
	static JPanel messagePanel;
	static JPanel geometryPanel;
	static JPanel plotPanel;
	
	public static void main(String[] args){
		/*
		 * The general idea:
		 * The gui is split into three slots, from left to right: 0,1,2. 
		 * -	The left side one (0) contains the configurations for the system exe
		 * -	The middle one, (1) contains two divided panels, top for the xml configuration, bottom for the text messages
		 * -	The right one, (2), contains top a panel for the jar generation, and the bottom a panel for the plots.
		 * 
		 */
		JFrame mainFrame= new JFrame("AnyAgentFrame");		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(new GridBagLayout());		
				
		initializeSystemPanel();
		initializeXmlPanel();
		initializeGraphPanel();
		
		addGBCComponent(mainFrame, systemPanel, 0,0,1,1);		
		addGBCComponent(mainFrame, xmlPanel, 1,0,1,1);
		addGBCComponent(mainFrame, graphPanel, 2,0,1,1);
		
		mainFrame.pack();
		mainFrame.setSize(new Dimension(800,600));
		mainFrame.setVisible(true);
	}		
	
	public static JPanel initializeSystemPanel(){
		systemPanel= new JPanel();	
		return systemPanel;
	}
	
	public static JPanel initializeXmlPanel(){
		xmlPanel= new JPanel();
		xmlPanel.setBackground(new Color(200,200,200));			
		xmlPanel.setLayout(new GridBagLayout());
		
		xmlVariablePanel= new JPanel();
		messagePanel= new JPanel();
		xmlVariablePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		messagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		addGBCComponent(xmlPanel, xmlVariablePanel, 0,0,1,1);
		addGBCComponent(xmlPanel, messagePanel, 0,1,1,1);
		return xmlPanel;
	}
	
	public static JPanel initializeGraphPanel(){
		graphPanel= new JPanel();
		graphPanel.setLayout(new GridBagLayout());
		
		geometryPanel= new JPanel();		
		plotPanel= new JPanel();
		geometryPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		plotPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		addGBCComponent(graphPanel, geometryPanel, 0,0,1,1);
		addGBCComponent(graphPanel, plotPanel, 0,1,1,1);
		return graphPanel;
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