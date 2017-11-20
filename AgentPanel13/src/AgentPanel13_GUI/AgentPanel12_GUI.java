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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.filechooser.*;


@SuppressWarnings("unused")
public class AgentPanel12_GUI {

	
	ProcessBuilder openJAR; 
	Process process;
	JFrame mainFrame;
	JFileChooser chooser;
    JTextField totalAgents_JTF, extrusionType_JTF, probRight_JTF, probLeft_JTF, probStraight_JTF, panelAgentLength_JTF,
    		panelCollisionClearance_JTF, extrusion_JTF, shadeVal_JTF, genAngle_JTF, panelOffsetZ_JTF, xml_filePath_JTF, threeDM_filePath_JTF, jar_filePath_JTF,
    		w1x_JTF,w1y_JTF, w1r_JTF,w2x_JTF,w2y_JTF, w2r_JTF;
    
    JButton saveSnapShot_JB, recordGeneration_JB, saveXML_JB, runGenerativePanel_JB, killProcess_JB, Choose_XML_JB, Choose_3dm_JB, Choose_jar_JB;
    
    JSlider panelAgentLength_JS,panelCollisionClearance_JS, extrusion_JS, shadeVal_JS, genAngle_JS, panelOffsetZ_JS;
    
    JLabel totalAgents_JL, extrusionType_JL, probRight_JL, probLeft_JL, probStraight_JL,
    		panelAgentLength_JL, panelCollisionClearance_JL, extrusion_JL, shadeVal_JL,
    		genAngle_JL, panelOffsetZ_JL, unitScale_JL, wireFrame_JL, generatedPoints_JL, analysis_JL,
    		w1x_JL,w1y_JL, w1r_JL,w2x_JL,w2y_JL, w2r_JL,windowParameters_JTF;
    
    JRadioButton unitScaleMeter_JRB, unitScaleCentiMeter_JRB, unitScaleMiliMeter_JRB,shadedWireFrame_JRB,
    		nonShadedWireFrame_JRB, showGeneratedPoints_JRB,hideGeneratedPoints_JRB, showAnalysis, hideAnalysis;
	
    ButtonGroup unitScale_BG, wireFrame_BG, generatedPoints_BG, analysis_BG; 
	
    JTextArea processOutput;
	
    JScrollPane scroll;
    
    String xml_filePath, jar_filePath;
    
	public AgentPanel12_GUI(String someZero ,String geoPath )
	{
		
		//Create main JFrame
		mainFrame = new JFrame("AgentPanel1GUI");
		
		//Create buttons
		saveXML_JB = new JButton("Save");
		runGenerativePanel_JB = new JButton("Run");
		killProcess_JB = new JButton("Kill");
		Choose_XML_JB = new JButton( "Choose XML" );
		Choose_3dm_JB = new JButton( "Choose 3DM" );
		Choose_jar_JB = new JButton( "Choose JAR" );
		
		//Create radio buttons & groups
		unitScale_BG = new ButtonGroup();
		wireFrame_BG = new ButtonGroup();
		generatedPoints_BG = new ButtonGroup();
		analysis_BG = new ButtonGroup();
		
		unitScaleMeter_JRB = new JRadioButton("Meters");
		unitScaleCentiMeter_JRB = new JRadioButton("CentiMeters");
		unitScaleMiliMeter_JRB = new JRadioButton("MiliMeters");
		shadedWireFrame_JRB = new JRadioButton("Shaded");
		nonShadedWireFrame_JRB = new JRadioButton("Non-Shaded");
		showGeneratedPoints_JRB = new JRadioButton("Show");
		hideGeneratedPoints_JRB = new JRadioButton("Hide");
		showAnalysis = new JRadioButton("Show");
		hideAnalysis = new JRadioButton("Hide");
		
		//Create textfields
		totalAgents_JTF = new JTextField("");
		extrusionType_JTF = new JTextField( "" );
		probRight_JTF = new JTextField( "" );
		probLeft_JTF = new JTextField( "" );
		probStraight_JTF = new JTextField( "" );
		panelAgentLength_JTF = new JTextField( "" );
		panelCollisionClearance_JTF = new JTextField( "" );
		extrusion_JTF = new JTextField( "" );
		shadeVal_JTF = new JTextField( "" );
		genAngle_JTF = new JTextField( "" );
		panelOffsetZ_JTF = new JTextField( "" );
		xml_filePath_JTF = new JTextField();
		threeDM_filePath_JTF = new JTextField();
		jar_filePath_JTF = new JTextField();
		w1x_JTF = new JTextField();
		w1y_JTF = new JTextField();
		w1r_JTF = new JTextField();
		w2x_JTF = new JTextField();
		w2y_JTF = new JTextField();
		w2r_JTF = new JTextField();
		
		//Create text area
		processOutput = new JTextArea();
			
		//Create labels
		totalAgents_JL = new JLabel( "Set Max Agents:" );
		extrusionType_JL = new JLabel( "Set Extrusion Type:" );
		probRight_JL = new JLabel( "Probability Right Agent:" );
		probLeft_JL = new JLabel( "Probability Left Agent:" );
		probStraight_JL = new JLabel( "Probability Straight Agent:" );
		panelAgentLength_JL = new JLabel( "Agent Length: " );
		panelCollisionClearance_JL = new JLabel( "Clearance:" );
		extrusion_JL = new JLabel( "Extrusion:" );
		shadeVal_JL = new JLabel( "Extrusion Uniformity:" );
		genAngle_JL = new JLabel( "Generative Angle:") ;
		panelOffsetZ_JL = new JLabel( "Aperture:" );
		unitScale_JL = new JLabel( "Unit Scale" );
		wireFrame_JL = new JLabel( "Wire Frame" ); 
		generatedPoints_JL = new JLabel( "Generated Points" );
		analysis_JL = new JLabel( "Analysis" );
		w1x_JL = new JLabel( "w1x:" );
		w1y_JL = new JLabel( "w1y:" );
		w1r_JL = new JLabel( "w1r:" );
		w2x_JL = new JLabel( "w2x:" );
		w2y_JL = new JLabel( "w2y:" );
		w2r_JL = new JLabel( "w2r:" );
		windowParameters_JTF = new JLabel( "Window Parameters" );
		
		//Create Sliders
		panelAgentLength_JS = new JSlider(JSlider.HORIZONTAL,0, 100, 50); 
		panelCollisionClearance_JS= new JSlider(JSlider.HORIZONTAL,0, 1000, 500  ); 
	 	extrusion_JS = new JSlider(JSlider.HORIZONTAL,0, 1000, 50);
	 	shadeVal_JS = new JSlider(JSlider.HORIZONTAL,0, 100, 50); 
	 	genAngle_JS = new JSlider(JSlider.HORIZONTAL,1, 10, 5); 
	 	panelOffsetZ_JS = new JSlider(JSlider.HORIZONTAL,0, 100, 50); 
		
	 	//Set bounds buttons
	 	runGenerativePanel_JB.setBounds(1000, 25, 100, 25);
	 	saveXML_JB.setBounds(1110, 25, 100, 25);
	 	killProcess_JB.setBounds(1000, 60, 100, 25);
	 	Choose_XML_JB.setBounds(300, 25, 125, 25);
	 	Choose_3dm_JB.setBounds(300, 60, 125, 25);
	 	Choose_jar_JB.setBounds(300, 95, 125, 25);
	 	
	 	//Set bounds radio buttons
	 	unitScaleMeter_JRB.setBounds(300, 175, 125, 25);
		unitScaleCentiMeter_JRB.setBounds(300, 200, 125, 25);
		unitScaleMiliMeter_JRB.setBounds(300, 225, 125, 25);
		shadedWireFrame_JRB.setBounds(450, 175, 125, 25);
		nonShadedWireFrame_JRB.setBounds(450, 200, 125, 25);
		showGeneratedPoints_JRB.setBounds(600, 175, 125, 25);
		hideGeneratedPoints_JRB.setBounds(600, 200, 125, 25);
		showAnalysis.setBounds(750, 175, 125, 25);
		hideAnalysis.setBounds(750, 200, 125, 25);
		
		
		
	 	
		//Set bounds for textfields
		totalAgents_JTF.setBounds(200, 25, 75, 25);
		extrusionType_JTF.setBounds(200, 50, 75, 25);
		probRight_JTF.setBounds(200, 75, 75, 25);
		probLeft_JTF.setBounds(200, 100, 75, 25); 
		probStraight_JTF.setBounds(200, 125, 75, 25); 
		panelAgentLength_JTF.setBounds(200, 175, 75,25 );
		panelCollisionClearance_JTF.setBounds(200, 250, 75, 25);
		extrusion_JTF.setBounds(200, 325, 75, 25);
		shadeVal_JTF.setBounds(200, 400, 75, 25);
		genAngle_JTF.setBounds(200, 475, 75, 25); 
		panelOffsetZ_JTF.setBounds(200, 550, 75, 25);
		xml_filePath_JTF.setBounds(435, 25, 500, 25);
		threeDM_filePath_JTF.setBounds(435, 60, 500, 25);
		jar_filePath_JTF.setBounds(435, 95, 500, 25);
		w1x_JTF.setBounds(330, 275, 50, 25);
		w1y_JTF.setBounds(430, 275, 50, 25);
		w1r_JTF.setBounds(530, 275, 50, 25);
		w2x_JTF.setBounds(630, 275, 50, 25);
		w2y_JTF.setBounds(730, 275, 50, 25);
		w2r_JTF.setBounds(830, 275, 50, 25);
		
		//Set bounds for Text Area
		processOutput.setBounds(500,300, 500, 300);
		//500,200, 400,400
		//500,300, 500, 300
		
		//Set bounds for labels
		totalAgents_JL.setBounds( 10, 25, 350, 25);
		extrusionType_JL.setBounds( 10, 50, 350, 25);
		probRight_JL.setBounds( 10, 75, 350, 25);
		probLeft_JL.setBounds( 10, 100, 350, 25);
		probStraight_JL.setBounds( 10, 125, 350, 25);
		panelAgentLength_JL.setBounds( 10, 175, 350, 25 );
		panelCollisionClearance_JL.setBounds( 10, 250, 350, 25 );
		extrusion_JL.setBounds( 10, 325, 350, 25 );
		shadeVal_JL.setBounds( 10, 400, 350, 25 );
		genAngle_JL.setBounds( 10, 475, 350, 25 );
		panelOffsetZ_JL.setBounds( 10, 550, 350, 25 );
		unitScale_JL.setBounds(300, 150, 350, 25);
		wireFrame_JL.setBounds(450, 150, 125, 25);
		generatedPoints_JL.setBounds(600, 150, 125, 25);
		analysis_JL.setBounds(750, 150, 150, 25);
		w1x_JL.setBounds(300, 275, 150, 25);
		w1y_JL.setBounds(400, 275, 150, 25);
		w1r_JL.setBounds(500, 275, 150, 25);
		w2x_JL.setBounds(600, 275, 150, 25);
		w2y_JL.setBounds(700, 275, 150, 25);
		w2r_JL.setBounds(800, 275, 150, 25);
		
		//Set bounds for sliders
		panelAgentLength_JS.setBounds(10, 200, 250, 50);
		panelCollisionClearance_JS.setBounds(10, 275, 250, 50);
	 	extrusion_JS.setBounds(10, 350, 250, 50);
	 	shadeVal_JS.setBounds(10, 425, 250, 50);
	 	genAngle_JS.setBounds(10, 500, 250, 50);
	 	panelOffsetZ_JS.setBounds(10, 575, 250, 50);
		
		
		//Set slider qualities
	 	/*
		panelAgentLength_JS.setMajorTickSpacing(50);
		panelAgentLength_JS.setMinorTickSpacing(10);
		panelAgentLength_JS.setPaintTicks(true);
		panelAgentLength_JS.setPaintLabels(true);
		
		panelCollisionClearance_JS.setMajorTickSpacing(500);
		panelCollisionClearance_JS.setMinorTickSpacing(100);
		panelCollisionClearance_JS.setPaintTicks(true);
		panelCollisionClearance_JS.setPaintLabels(true);
		
		extrusion_JS.setMajorTickSpacing(100);
		extrusion_JS.setMinorTickSpacing(10);
		extrusion_JS.setPaintTicks(true);
		extrusion_JS.setPaintLabels(true);
		
		shadeVal_JS.setMajorTickSpacing(100);
		shadeVal_JS.setMinorTickSpacing(10);
		shadeVal_JS.setPaintTicks(true);
		shadeVal_JS.setPaintLabels(true);
		
		genAngle_JS.setMajorTickSpacing(157);
		genAngle_JS.setMinorTickSpacing(73);
		genAngle_JS.setPaintTicks(true);
		genAngle_JS.setPaintLabels(true);
		
		panelOffsetZ_JS.setMajorTickSpacing(10);
		panelOffsetZ_JS.setMinorTickSpacing(1);
		panelOffsetZ_JS.setPaintTicks(true);
		panelOffsetZ_JS.setPaintLabels(true);
		*/
		
		//Create scroll for text area
		scroll = new JScrollPane(processOutput);
		
		scroll.setBounds(300,350, 500, 300);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Set text area qualities
		processOutput.setLineWrap(true);
		
		//Add buttons
		mainFrame.add(runGenerativePanel_JB);
		mainFrame.add(saveXML_JB);
		mainFrame.add(killProcess_JB);
		mainFrame.add(Choose_XML_JB);
		mainFrame.add(Choose_3dm_JB);
		mainFrame.add(Choose_jar_JB);
		//Add radio buttons
		unitScale_BG.add(unitScaleMeter_JRB);
		unitScale_BG.add(unitScaleCentiMeter_JRB);
		unitScale_BG.add(unitScaleMiliMeter_JRB);
		wireFrame_BG.add(shadedWireFrame_JRB);
		wireFrame_BG.add(nonShadedWireFrame_JRB);
		generatedPoints_BG.add(showGeneratedPoints_JRB);
		generatedPoints_BG.add(hideGeneratedPoints_JRB);
		analysis_BG.add(showAnalysis);
		analysis_BG.add(hideAnalysis);
		
		
		mainFrame.add(unitScaleMeter_JRB);
		mainFrame.add(unitScaleCentiMeter_JRB);
		mainFrame.add(unitScaleMiliMeter_JRB);
		mainFrame.add(shadedWireFrame_JRB);
		mainFrame.add(nonShadedWireFrame_JRB);
		mainFrame.add(showGeneratedPoints_JRB);
		mainFrame.add(hideGeneratedPoints_JRB);
		mainFrame.add(showAnalysis);
		mainFrame.add(hideAnalysis);
		mainFrame.add(wireFrame_JL);
		mainFrame.add(generatedPoints_JL);
		mainFrame.add(analysis_JL);
		
		
		
		//Add textfields
		mainFrame.add(totalAgents_JTF);
		mainFrame.add(extrusionType_JTF);
		mainFrame.add(probRight_JTF);
		mainFrame.add(probLeft_JTF);
		mainFrame.add(probStraight_JTF);
		mainFrame.add(panelAgentLength_JTF);
		mainFrame.add(panelCollisionClearance_JTF);
		mainFrame.add(extrusion_JTF);
		mainFrame.add(shadeVal_JTF);
		mainFrame.add(genAngle_JTF);
		mainFrame.add(panelOffsetZ_JTF);
		mainFrame.add(threeDM_filePath_JTF);
		mainFrame.add(xml_filePath_JTF);
		mainFrame.add(jar_filePath_JTF);
		mainFrame.add(w1x_JTF);
		mainFrame.add(w1y_JTF);
		mainFrame.add(w1r_JTF);
		mainFrame.add(w2x_JTF);
		mainFrame.add(w2y_JTF);
		mainFrame.add(w2r_JTF);
		
		//Add text areas
		//mainFrame.add(processOutput);
		mainFrame.add(scroll);
		
		
		//Add labels
		mainFrame.add(totalAgents_JL);
		mainFrame.add(extrusionType_JL);
		mainFrame.add(probRight_JL);
		mainFrame.add(probLeft_JL);
		mainFrame.add(probStraight_JL);
		mainFrame.add(panelAgentLength_JL);
		mainFrame.add(panelCollisionClearance_JL);
		mainFrame.add(extrusion_JL);
		mainFrame.add(shadeVal_JL);
		mainFrame.add(genAngle_JL);
		mainFrame.add(panelOffsetZ_JL);
		mainFrame.add(unitScale_JL);
		mainFrame.add(w1x_JL);
		mainFrame.add(w1y_JL);
		mainFrame.add(w1r_JL);
		mainFrame.add(w2x_JL);
		mainFrame.add(w2y_JL);
		mainFrame.add(w2r_JL);
		
		//Button Listener
		runGenerativePanel_JB_Action( runGenerativePanel_JB, processOutput, someZero , geoPath );
		saveXML_JB_Action( saveXML_JB, totalAgents_JTF, extrusionType_JTF, probRight_JTF, probLeft_JTF, probStraight_JTF,
				panelAgentLength_JTF, panelCollisionClearance_JTF, extrusion_JTF, shadeVal_JTF, genAngle_JTF, panelOffsetZ_JTF,
				w1x_JTF,w1y_JTF, w1r_JTF,w2x_JTF,w2y_JTF, w2r_JTF);
		killProcess_JB_Action( killProcess_JB );
		Choose_XML_JB_Action(Choose_XML_JB, xml_filePath_JTF, totalAgents_JTF, extrusionType_JTF, probRight_JTF, probLeft_JTF,
				probStraight_JTF, panelAgentLength_JTF, panelCollisionClearance_JTF, extrusion_JTF, shadeVal_JTF, genAngle_JTF,
				panelOffsetZ_JTF, w1x_JTF,w1y_JTF, w1r_JTF,w2x_JTF,w2y_JTF, w2r_JTF );
	 	
	 	Choose_3dm_JB_Action(Choose_3dm_JB, threeDM_filePath_JTF);
	 	Choose_jar_JB_Action( Choose_jar_JB, jar_filePath_JTF );
		
		
		//Slider Listener
		panelAgentLength_JS_Action(panelAgentLength_JS ,panelAgentLength_JTF);
		panelCollisionClearance_JS_Action( panelCollisionClearance_JS, panelCollisionClearance_JTF );
		extrusion_JS_Action(extrusion_JS,extrusion_JTF);
		shadeVal_JS_Action(shadeVal_JS,shadeVal_JTF);
		genAngle_JS_Action( genAngle_JS,genAngle_JTF  );
		panelOffsetZ_JS_Action( panelOffsetZ_JS, panelOffsetZ_JTF  );
		
		//Add sliders
		mainFrame.add(panelAgentLength_JS);
		mainFrame.add(panelCollisionClearance_JS);
		mainFrame.add(extrusion_JS);
		mainFrame.add(shadeVal_JS);
		mainFrame.add(genAngle_JS);
		mainFrame.add(panelOffsetZ_JS);
		
		
	
		mainFrame.setSize(1280,720);
		mainFrame.setLayout(null);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void Choose_jar_JB_Action( final JButton Choose_jar_JB, final JTextField jar_filePath_JTF )
	{
		Choose_jar_JB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	chooser = new JFileChooser();
            	int returnVal = chooser.showOpenDialog(null);
            	 if (returnVal == JFileChooser.APPROVE_OPTION) {
                     File file = chooser.getSelectedFile();
                     //This is where a real application would open the file.
                     jar_filePath_JTF.setText(file.getPath());
                     jar_filePath = file.getPath();
                    
                     
                 } else {
                	 jar_filePath_JTF.setText("Open command cancelled by user.");
                 }
                 
  
            }
        });
	}
	
	public void Choose_XML_JB_Action( final JButton Choose_XML_JB, final  JTextField xml_filePath_JTF, final JTextField totalAgents_JTF,final JTextField extrusionType_JTF,
	      final JTextField probRight_JTF,final JTextField probLeft_JTF,final JTextField probStraight_JTF,final JTextField panelAgentLength_JTF,final JTextField panelCollisionClearance_JTF,
	      final JTextField extrusion_JTF,final JTextField shadeVal_JTF,final JTextField genAngle_JTF,final JTextField panelOffsetZ_JTF,final JTextField w1x_JTF, final JTextField w1y_JTF,
	      final JTextField w1r_JTF,final JTextField w2x_JTF,final JTextField w2y_JTF, final JTextField w2r_JTF )
	{
		Choose_XML_JB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	chooser = new JFileChooser();
            	int returnVal = chooser.showOpenDialog(null);
            	 if (returnVal == JFileChooser.APPROVE_OPTION) {
                     File file = chooser.getSelectedFile();
                     //This is where a real application would open the file.
                     xml_filePath_JTF.setText(file.getPath());
                     xml_filePath = file.getPath();
                     XmlHandlerReader_GUI reader = new XmlHandlerReader_GUI( file.getPath() );
                     
                     totalAgents_JTF.setText( Integer.toString(reader.getTotalAgents()) );
                     extrusionType_JTF.setText( Boolean.toString( reader.getUniformExtrusion()) );
                     probRight_JTF.setText(Integer.toString(reader.getProbRight()));
                     probLeft_JTF.setText(Integer.toString(reader.getProbLeft()));
                     probStraight_JTF.setText(Integer.toString(reader.getProbStraight()));
                     panelAgentLength_JTF.setText(Double.toString(reader.getPanelAgentLength()));
                     panelCollisionClearance_JTF.setText(Double.toString(reader.getPanelCollisionClearance()));
                     extrusion_JTF.setText(Double.toString(reader.getExtrusion()));
                     shadeVal_JTF.setText(Double.toString(reader.getShadeVal()));
                     genAngle_JTF.setText(reader.getGenAngle());
                     panelOffsetZ_JTF.setText(Double.toString(reader.getPanelOffsetZ()));
                     w1x_JTF.setText(Double.toString(reader.getW1x()));
                     w1y_JTF.setText(Double.toString(reader.getW1y()));
                     w1r_JTF.setText(Double.toString(reader.getW1r()));
                     w2x_JTF.setText(Double.toString(reader.getW2x()));
                     w2y_JTF.setText(Double.toString(reader.getW2y()));
                     w2r_JTF.setText(Double.toString(reader.getW2r()));
                     
                     
                     
                 } else {
                	 xml_filePath_JTF.setText("Open command cancelled by user.");
                 }
                 
  
            }
        });
	}
	

	public void Choose_3dm_JB_Action( JButton Choose_3dm_JB, JTextField threeDM_filePath_JTF )
	{
		Choose_XML_JB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            

            }
        });
	}
	
	
	//, String jarPath, String geoPath, String inpu
	public void killProcess_JB_Action( JButton killProcess_JB )
	{
		killProcess_JB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	process.destroy();

            }
        });
	}
	
	
	public void saveXML_JB_Action( final JButton saveXML_JB, final JTextField totalAgents_JTF,final JTextField extrusionType_JTF,final JTextField probRight_JTF,final JTextField probLeft_JTF,
	      final JTextField probStraight_JTF,final JTextField panelAgentLength_JTF,final JTextField panelCollisionClearance_JTF,final JTextField extrusion_JTF,final JTextField shadeVal_JTF,
	      final JTextField genAngle_JTF,final JTextField panelOffsetZ_JTF,final JTextField w1x_JTF,final JTextField w1y_JTF,
	      final JTextField w1r_JTF,final JTextField w2x_JTF,final JTextField w2y_JTF,final JTextField w2r_JTF   )
	{
		saveXML_JB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            		XmlHandlerWriter_GUI handler = new XmlHandlerWriter_GUI(  totalAgents_JTF, extrusionType_JTF, probRight_JTF, probLeft_JTF, probStraight_JTF,
            				panelAgentLength_JTF, panelCollisionClearance_JTF, extrusion_JTF, shadeVal_JTF, genAngle_JTF, panelOffsetZ_JTF,
            				w1x_JTF,w1y_JTF, w1r_JTF,w2x_JTF,w2y_JTF, w2r_JTF, xml_filePath  );
                   

            }
        });
	}
	
	
	public void runGenerativePanel_JB_Action( final JButton runGenerativePanel_JB, final JTextArea processOutput,final String someZero ,final String geoPath)
	{
		runGenerativePanel_JB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	try {
            		// (jarPath + geoPath + input)
					openJAR = new ProcessBuilder("java", "-jar",  jar_filePath, someZero ,geoPath, xml_filePath );
					openJAR.redirectErrorStream(true);
					process = openJAR.start();
					
					InputStream error = process.getErrorStream();
					//InputStreamReader iserror = new InputStreamReader(error);
					BufferedReader output = new BufferedReader( new InputStreamReader(error));
					String str = null;
					while( (str = output.readLine()) != null )
					{
						processOutput.append(str);
					}
					
					
            	} catch (IOException e1) {
					// TODO Auto-generated catch block
					processOutput.append("\n"+e1.toString());
					e1.printStackTrace();
				}
                   

            }
        });
	}

	
	public void panelAgentLength_JS_Action( final JSlider panelAgentLength_JS, final JTextField panelAgentLength_JTF  )
	{
		panelAgentLength_JS.addChangeListener(new javax.swing.event.ChangeListener(){
		      public void stateChanged(javax.swing.event.ChangeEvent ce){
		    	  
		    	  double value = (double)panelAgentLength_JS.getValue()/100;
		    	  panelAgentLength_JTF.setText( Double.toString(value) );
		      }
		    });
	}
	
	public void panelCollisionClearance_JS_Action( final JSlider panelCollisionClearance_JS,final JTextField panelCollisionClearance_JTF  )
	{
		panelCollisionClearance_JS.addChangeListener(new javax.swing.event.ChangeListener(){
		      public void stateChanged(javax.swing.event.ChangeEvent ce){
		    	  
		    	  double value = (double)panelCollisionClearance_JS.getValue()/1000;
		    	  panelCollisionClearance_JTF.setText( Double.toString(value) );
		      }
		    });
	}
	
	public void extrusion_JS_Action( final JSlider extrusion_JS, final JTextField extrusion_JTF  )
	{
		extrusion_JS.addChangeListener(new javax.swing.event.ChangeListener(){
		      public void stateChanged(javax.swing.event.ChangeEvent ce){
		    	  
		    	  double value = (double)extrusion_JS.getValue()/1000;
		    	  extrusion_JTF.setText( Double.toString(value) );
		    	 
		      }
		    });
	}
	
	public void shadeVal_JS_Action( final JSlider shadeVal_JS,final JTextField shadeVal_JTF  )
	{
		shadeVal_JS.addChangeListener(new javax.swing.event.ChangeListener(){
		      public void stateChanged(javax.swing.event.ChangeEvent ce){
		    	  
		    	  double value = (double)shadeVal_JS.getValue()/1000;
		    	  shadeVal_JTF.setText( Double.toString(value) );
		      }
		    });
	}

	public void genAngle_JS_Action(final JSlider genAngle_JS,final JTextField genAngle_JTF  )
	{
		genAngle_JS.addChangeListener(new javax.swing.event.ChangeListener(){
		      public void stateChanged(javax.swing.event.ChangeEvent ce){
		    	  
		    	  genAngle_JTF.setText( "\u03c0/"+Integer.toString(genAngle_JS.getValue()) );
		      }
		    });
	}

	public void panelOffsetZ_JS_Action( final JSlider panelOffsetZ_JS, final JTextField panelOffsetZ_JTF  )
	{
		panelOffsetZ_JS.addChangeListener(new javax.swing.event.ChangeListener(){
		      public void stateChanged(javax.swing.event.ChangeEvent ce){
		    	  
		    	  Double value = (double)panelOffsetZ_JS.getValue()/100;
		    	  panelOffsetZ_JTF.setText( Double.toString(value) );
		      }
		    });
	}

	
	public static void main( String[] args )
	{
		//String jarPath = "C:\\Users\\sg1el\\Dropbox\\003_Agent_panel_system_dev\\java_igeo\\150927_environmentalAgent_package\\150927_gettingItToRun\\00_resources\\source_code\\TempJAR\\agentPanel11_JAR.jar";
		String someZero = "0";
		String geoPath = "D:\\Dropbox\\003_Agent_panel_system_dev\\java_igeo\\150927_environmentalAgent_package\\150927_gettingItToRun\\00_resources\\source_code\\AgentPanel11_Kevin_Project\\src\\data";
	
		AgentPanel12_GUI mainJframe = new AgentPanel12_GUI( someZero , geoPath );
		 //jarPath,geoPath, input 
	}

}