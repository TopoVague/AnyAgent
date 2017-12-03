package alan_GUI;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

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

import com.sun.xml.internal.ws.resources.AddressingMessages;

import alan_GUI.SystemVariablesClass.guiVariableTypes;

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

	private static JPanel xmlVariableContainer;
	private static JPanel messagePanel;
	private static JPanel geometryPanel;
	private static JPanel plotPanel;
	private static JTextArea jtaMessages;
	private static SystemVariablesClass svc;
	
	public static HashMap<String, NameToVariableClass> geoGenerationVariableSet;
	
	public static void main(String[] args) {
		/*
		 * The general idea: The gui is split into three slots, from left to
		 * right: 0,1,2. - The left side one (0) contains the configurations for
		 * the system exe - The middle one, (1) contains two divided panels, top
		 * for the xml configuration, bottom for the text messages - The right
		 * one, (2), contains top a panel for the jar generation, and the bottom
		 * a panel for the plots.
		 * 
		 */
		mainFrame = new JFrame("Any Agent Frame");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(new GridBagLayout());

		initializeXmlPanel();
		initializeSystemPanel();
		initializeGraphPanel();

		Constants.addGBCComponent(mainFrame, largeSystemPanel, 0, 0, 1, 1);
		Constants.addGBCComponent(mainFrame, largeXmlPanel, 1, 0, 1, 1);
		Constants.addGBCComponent(mainFrame, largeGraphPanel, 2, 0, 1, 1);

		mainFrame.pack();
		mainFrame.setSize(new Dimension(1280, 720));
		mainFrame.setMinimumSize(mainFrame.getSize());
		mainFrame.setLocationRelativeTo(null); // Center the gui
		mainFrame.setVisible(true);

		// for (int x =0; x< 100; x++){
		// addMessage("Testing " + x);
		// }
		svc.loadDefault();
		
	}

	public static JPanel initializeSystemPanel() {
		largeSystemPanel = new JPanel();
		// systemPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		//largeSystemPanel.setLayout(new BorderLayout());
		Constants.addSetBorderLayoutTitle(largeSystemPanel, "System Variables", true);

		// JPanel systemPanelCanvas= new JPanel();

		JPanel jpScrollPanePanel = new JPanel();
		jpScrollPanePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JScrollPane jspSystemScrollPane = new JScrollPane(jpScrollPanePanel);
		jspSystemScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jspSystemScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		largeSystemPanel.add(jspSystemScrollPane, BorderLayout.CENTER);
		jpScrollPanePanel.setLayout(new GridBagLayout());
		svc = new SystemVariablesClass();

		int componentIndex = 0; // Keeping track of an index adding components
								// down this panel

		JButton loadDefault = new JButton("Load Default");
		loadDefault.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				svc.loadDefault();
			}
		});
		// loadDefault.setBorder(new EmptyBorder(10,10,10,10));

		Constants.addGBCComponent(jpScrollPanePanel, loadDefault, 0, componentIndex, 0.25, 1,
				GridBagConstraints.HORIZONTAL);
		componentIndex++;
		
		for (int n = 0; n < svc.variablesOrder.size(); n++) {
			// NameToVariableClass nvc = svc.variables.get(n);
			NameToVariableClass nvc = svc.variableSet.get(svc.variablesOrder.get(n));
			guiVariableTypes gvType = nvc.gvt;
			Constants.addGBCComponent(jpScrollPanePanel, VariableJPanelCreator.getPanel(svc.variablesOrder.get(n), gvType, svc.variableSet), 0, componentIndex, 0, 1,
					GridBagConstraints.HORIZONTAL);
			// System.out.println(n+ " Name: " + nvc.name + ", type: "+
			// gvType.toString());
			componentIndex++;
		}

		JPanel searchPanel= new JPanel();	
		Constants.addSetBorderLayoutTitle(searchPanel, "Search Options", false);
		String[] searchChoices= {"Hillclimbing", "Simulated Annealing", "Monte Carlo"}; //hillclinbg sliders only, hill climb doubles
		JComboBox<String> jcbSearchChoice= new JComboBox<String>(searchChoices);
		searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		searchPanel.add(jcbSearchChoice, BorderLayout.SOUTH);
		componentIndex++;
		
		Constants.addGBCComponent(jpScrollPanePanel, searchPanel, 0, componentIndex, 0.25, 1,
				GridBagConstraints.HORIZONTAL);

		JButton saveButton = new JButton("Save as default");
		// saveButton.setBorder(new EmptyBorder(10,10,10,10));
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				svc.saveDefault();
			}
		});
		componentIndex++;
		
		Constants.addGBCComponent(jpScrollPanePanel, saveButton, 0, componentIndex, 0.25, 1,
				GridBagConstraints.HORIZONTAL);
		componentIndex++;
		JButton runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (svc.canRun()) {
					addMessage("Variables checked out. Starting run...");
					String pyStr= svc.getPythonDictStr();
					
					if (geoGenerationVariableSet!= null){
						//double heuristicValue= DummyPythonEXEValueRetriever.getHeuristicValue(svc.variableSet, geoGenerationVariableSet);
						//addMessage("Heuristic value received: "+ heuristicValue);
						
						if (jcbSearchChoice.getSelectedItem().equals("Hillclimbing")){
							SearchFunctions.runHillClimbing(svc.variableSet, geoGenerationVariableSet);
						}
						
					}
				}
				addMessageReportLine();
				addMessage("Run ended");
			}
		});
		// runButton.setEnabled(false);
		Constants.addGBCComponent(jpScrollPanePanel, runButton, 0, componentIndex, 0.25, 1,
				GridBagConstraints.HORIZONTAL);
		componentIndex++;
		
		return largeSystemPanel;
	}

	public static JPanel initializeXmlPanel() {
		// setting up outside panel===============================================
		largeXmlPanel = new JPanel();
		largeXmlPanel.setBackground(new Color(200, 200, 200));
		largeXmlPanel.setLayout(new GridBagLayout());
		largeXmlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		xmlVariableContainer = new JPanel(); //the top xml variables panel
		Constants.addSetBorderLayoutTitle(xmlVariableContainer, "Geometry Generation Variables", true);
		JPanel variablePanel= new JPanel();
		JScrollPane jspVariables= new JScrollPane(variablePanel);
		jspVariables.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jspVariables.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		variablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		variablePanel.setLayout(new GridBagLayout());
		
		int componentIndex= 0;
		geoGenerationVariableSet= ClassToGUIAdapter.generateSetFromClass(new GeometryGenerationTestClass().getClass());	
		System.out.println("Number of variables within the class: "+ geoGenerationVariableSet.size());
		for (String key: geoGenerationVariableSet.keySet()){
			NameToVariableClass nvc= geoGenerationVariableSet.get(key);
			Constants.addGBCComponent(variablePanel, VariableJPanelCreator.getPanel(key, nvc.gvt, geoGenerationVariableSet), 0, componentIndex, 1, 1,
				GridBagConstraints.HORIZONTAL);
			System.out.println("Adding variables : " + key);
			nvc.setAssocGuiValue(nvc.valueString);
			componentIndex++;
		}		
		xmlVariableContainer.add(jspVariables, BorderLayout.CENTER);
		
		//The msaage panel on the bottom===============================================
		messagePanel = new JPanel(); //the bottom message panel
		jtaMessages = new JTextArea();
		jtaMessages.setWrapStyleWord(true);
		jtaMessages.setLineWrap(true);
		jtaMessages.setBorder(new EmptyBorder(10, 10, 10, 10));
		jtaMessages.setEditable(false);
		JScrollPane jspMessages = new JScrollPane(jtaMessages);
		jspMessages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jspMessages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		Constants.addSetBorderLayoutTitle(messagePanel, "Messages", true);
		messagePanel.add(jspMessages, BorderLayout.CENTER);

		Constants.addGBCComponent(largeXmlPanel, xmlVariableContainer, 0, 0, 1, 1);
		Constants.addGBCComponent(largeXmlPanel, messagePanel, 0, 1, 1, 1);
		return largeXmlPanel;
	}

	public static JPanel initializeGraphPanel() {
		largeGraphPanel = new JPanel();
		largeGraphPanel.setLayout(new GridBagLayout());
		// graphPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		geometryPanel = new JPanel();
		Constants.addSetBorderLayoutTitle(geometryPanel, "Geometry Pane", true);
		// geometryPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		plotPanel = new JPanel();
		Constants.addSetBorderLayoutTitle(plotPanel, "Plot Pane", true);
		// plotPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		Constants.addGBCComponent(largeGraphPanel, geometryPanel, 0, 0, 1, 1);
		Constants.addGBCComponent(largeGraphPanel, plotPanel, 0, 1, 1, 1);
		return largeGraphPanel;
	}

	public static void addMessage(String message) {
		if (jtaMessages != null) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			// System.out.println( sdf.format(cal.getTime()) );
			jtaMessages.append("[" + sdf.format(cal.getTime()) + "]  " + message + "\n");
			jtaMessages.setCaretPosition(jtaMessages.getDocument().getLength());
			mainFrame.repaint();
		} else {
			System.out.println("Can't add messages to yet. JTA Messages is null");
		}
	}

	public static void addMessageReportLine() {
		addMessage("------------");
	}
}