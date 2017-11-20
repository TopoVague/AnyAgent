package agentPanel13m_JAR;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


public class XmlHandlerReader_JAR {
	
	public int totalAgents_JAR;
	public double panelAgentLength_JAR;
	public boolean uniformExtrusion_JAR;
	public double extrusion_JAR;
	public double panelCollisionClearance_JAR;
	public String genAngle_JAR;
	public int probRight_JAR;
	public int probLeft_JAR;
	public int probStraight_JAR;
	public Double panelOffsetZ_JAR;
	public Double shadeVal_JAR;
	public Double w1x_JAR;
	public Double w1y_JAR;
	public Double w1r_JAR;
	public Double w2x_JAR;
	public Double w2y_JAR;
	public Double w2r_JAR;
	
	
	public XmlHandlerReader_JAR(String xml_filePath)
	{
		  try {

			  String filepath = xml_filePath;
				File fXmlFile = new File(filepath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);	
				doc.getDocumentElement().normalize();		
				NodeList nList = doc.getElementsByTagName("options");
		
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
					Element eElement = (Element) nNode;	
					
					setTotalAgents(eElement.getElementsByTagName("totalAgents").item(0).getTextContent() );
					setPanelAgentLength(eElement.getElementsByTagName("panelAgentLength").item(0).getTextContent() );
					setUniformExtrusion(eElement.getElementsByTagName("uniformExtrusion").item(0).getTextContent() );
					setExtrusion(eElement.getElementsByTagName("extrusion").item(0).getTextContent() );
					setPanelCollisionClearance(eElement.getElementsByTagName("panelCollisionClearance").item(0).getTextContent() );
					setGenAngle(eElement.getElementsByTagName("genAngle").item(0).getTextContent() );
					setProbRight(eElement.getElementsByTagName("probRight").item(0).getTextContent() );
					setProbLeft(eElement.getElementsByTagName("probLeft").item(0).getTextContent() );
					setProbStraight(eElement.getElementsByTagName("probStraight").item(0).getTextContent() );
					setPanelOffsetZ(eElement.getElementsByTagName("panelOffsetZ").item(0).getTextContent() );
					setShadeVal(eElement.getElementsByTagName("shadeVal").item(0).getTextContent() );
					setW1x(eElement.getElementsByTagName("w1x").item(0).getTextContent() );
					setW1y(eElement.getElementsByTagName("w1y").item(0).getTextContent() );
					setW1r(eElement.getElementsByTagName("w1r").item(0).getTextContent() );
					setW2x(eElement.getElementsByTagName("w2x").item(0).getTextContent() );
					setW2y(eElement.getElementsByTagName("w2y").item(0).getTextContent() );
					setW2r(eElement.getElementsByTagName("w2r").item(0).getTextContent() );
					
				}
			    } catch (Exception e) {
				e.printStackTrace();
			    }
	}

	void setTotalAgents(String totalAgents )
	{
		totalAgents_JAR = Integer.parseInt(totalAgents);
	}

	int getTotalAgents()
	{
		return totalAgents_JAR;
	}
	
	void setPanelAgentLength(String panelAgentLength )
	{
		panelAgentLength_JAR = Double.parseDouble(panelAgentLength);
	}

	double getPanelAgentLength()
	{
		return panelAgentLength_JAR;
	}
	
	void setUniformExtrusion(String uniformExtrusion )
	{
		uniformExtrusion_JAR = Boolean.parseBoolean(uniformExtrusion);
	}

	boolean getUniformExtrusion()
	{
		return uniformExtrusion_JAR;
	}
	
	void setExtrusion(String extrusion )
	{
		extrusion_JAR = Double.parseDouble(extrusion);
	}

	double getExtrusion()
	{
		return extrusion_JAR;
	}
	
	void setPanelCollisionClearance(String panelCollisionClearance )
	{
		panelCollisionClearance_JAR = Double.parseDouble(panelCollisionClearance);
	}

	double getPanelCollisionClearance()
	{
		return panelCollisionClearance_JAR;
	}
	
	void setGenAngle(String genAngle )
	{
		genAngle_JAR = genAngle;
	}

	String getGenAngle()
	{
		return genAngle_JAR;
	}
	
	void setProbRight(String probRight )
	{
		probRight_JAR = Integer.parseInt(probRight);
	}

	int getProbRight()
	{
		return probRight_JAR;
	}
	
	void setProbLeft(String probLeft )
	{
		probLeft_JAR = Integer.parseInt(probLeft);
	}

	int getProbLeft()
	{
		return probLeft_JAR;
	}
	
	void setProbStraight(String probStraight )
	{
		probStraight_JAR = Integer.parseInt(probStraight);
	}

	int getProbStraight()
	{
		return probStraight_JAR;
	}
	
	void setPanelOffsetZ(String panelOffsetZ )
	{
		panelOffsetZ_JAR = Double.parseDouble(panelOffsetZ);
	}

	Double getPanelOffsetZ()
	{
		return panelOffsetZ_JAR;
	}
	
	void setShadeVal(String shadeVal )
	{
		shadeVal_JAR = Double.parseDouble(shadeVal);
	}

	Double getShadeVal()
	{
		return shadeVal_JAR;
	}
	
	void setW1x(String w1x )
	{
		w1x_JAR = Double.parseDouble(w1x);
	}

	Double getW1x()
	{
		//System.out.println(w1x_JAR);
		return w1x_JAR;
	}
	
	void setW1y(String w1y )
	{
		w1y_JAR = Double.parseDouble(w1y);
	}

	Double getW1y()
	{
		return w1y_JAR;
	}
	
	void setW1r(String w1r )
	{
		w1r_JAR = Double.parseDouble(w1r);
	}

	Double getW1r()
	{
		return w1r_JAR;
	}
	
	void setW2x(String w2x )
	{
		w2x_JAR = Double.parseDouble(w2x);
	}

	Double getW2x()
	{
		return w2x_JAR;
	}
	
	void setW2y(String w2y )
	{
		w2y_JAR = Double.parseDouble(w2y);
	}

	Double getW2y()
	{
		return w2y_JAR;
	}
	
	void setW2r(String w2r )
	{
		w2r_JAR = Double.parseDouble(w2r);
	}

	Double getW2r()
	{
		return w2r_JAR;
	}
	
	
}
