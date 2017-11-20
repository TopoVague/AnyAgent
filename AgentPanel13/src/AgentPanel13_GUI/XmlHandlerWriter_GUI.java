package AgentPanel13_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


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


@SuppressWarnings("unused")
public class XmlHandlerWriter_GUI {
	
	public XmlHandlerWriter_GUI( JTextField totalAgents_JTF,JTextField extrusionType_JTF,JTextField probRight_JTF,JTextField probLeft_JTF,
			JTextField probStraight_JTF,JTextField panelAgentLength_JTF,JTextField panelCollisionClearance_JTF,JTextField extrusion_JTF,
			JTextField shadeVal_JTF,JTextField genAngle_JTF,JTextField panelOffsetZ_JTF,
			JTextField w1x_JTF, JTextField w1y_JTF,JTextField w1r_JTF,JTextField w2x_JTF, JTextField w2y_JTF, JTextField w2r_JTF,  String xml_filePath )
	{
		
		try{
		String filepath = xml_filePath;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		 
		Node parameters = doc.getFirstChild();
		
		Node options = doc.getElementsByTagName("options").item(0);
		
		NodeList list = options.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			
           Node node = list.item(i);

		   // get the salary element, and update the value
		   if ("totalAgents".equals(node.getNodeName())) {
			node.setTextContent(totalAgents_JTF.getText());
		   }

		   // get the salary element, and update the value
		   if ("panelAgentLength".equals(node.getNodeName())) {
			node.setTextContent(panelAgentLength_JTF.getText());
		   }
		   
		   // get the salary element, and update the value
		   if ("uniformExtrusion".equals(node.getNodeName())) {
			node.setTextContent(extrusionType_JTF.getText());
		   }
		   
		   
		   // get the salary element, and update the value
		   if ("extrusion".equals(node.getNodeName())) {
			node.setTextContent(extrusion_JTF.getText());
		   }
		   
		   
		   // get the salary element, and update the value
		   if ("panelCollisionClearance".equals(node.getNodeName())) {
			node.setTextContent(panelCollisionClearance_JTF.getText());
		   }
		   
		   // get the salary element, and update the value
		   if ("genAngle".equals(node.getNodeName())) {
			node.setTextContent(genAngle_JTF.getText());
		   }
		   
		   // get the salary element, and update the value
		   if ("probRight".equals(node.getNodeName())) {
			node.setTextContent(probRight_JTF.getText());
		   }
		
		   // get the salary element, and update the value
		   if ("probLeft".equals(node.getNodeName())) {
			node.setTextContent(probLeft_JTF.getText());
		   }
		
		   // get the salary element, and update the value
		   if ("probStraight".equals(node.getNodeName())) {
			node.setTextContent(probStraight_JTF.getText());
		   }
		   
		// get the salary element, and update the value
		   if ("shadeVal".equals(node.getNodeName())) {
			node.setTextContent(shadeVal_JTF.getText());
		   }
		
		// get the salary element, and update the value
		   if ("panelOffsetZ".equals(node.getNodeName())) {
			node.setTextContent(panelOffsetZ_JTF.getText());
		   }
		   
		   if ("w1x".equals(node.getNodeName())) {
				node.setTextContent(w1x_JTF.getText());
			   }
		   
		   if ("w1y".equals(node.getNodeName())) {
				node.setTextContent(w1y_JTF.getText());
			   }
		   
		   if ("w1r".equals(node.getNodeName())) {
				node.setTextContent(w1r_JTF.getText());
			   }
		   
		   if ("w2x".equals(node.getNodeName())) {
				node.setTextContent(w2x_JTF.getText());
			   }
		   
		   if ("w2y".equals(node.getNodeName())) {
				node.setTextContent(w2y_JTF.getText());
			   }
		   
		   if ("w2r".equals(node.getNodeName())) {
				node.setTextContent(w2r_JTF.getText());
			   }
		
		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);

		
		
		
		
		
		
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
	   } catch (IOException ioe) {
		   ioe.printStackTrace();
	   } catch (SAXException sae) {
				sae.printStackTrace();
	   }
		
		
		
	}

}
