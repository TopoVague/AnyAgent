package agentPanel13m_JAR;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import igeo.ISurface;
import igeo.IVec;


import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class XmlReconstructureHandlerReader {

	//private static final double PI = 0;
	public int isAlive_JAR;
	public int totalAgents_JAR;
	public double panelAgentLength_JAR;
	public boolean uniformExtrusion_JAR;
	public double extrusion_JAR;
	public double panelCollisionClearance_JAR;
	public String genAngle_JAR;
	public double probRight_JAR;
	public double probLeft_JAR;
	public double probStraight_JAR;
	public Double panelOffsetZ_JAR;
	public Double shadeVal_JAR;
	public Double w1x_JAR;
	public Double w1y_JAR;
	public Double w1r_JAR;
	public Double w2x_JAR;
	public Double w2y_JAR;
	public Double w2r_JAR;
	public String LocationAndType_JAR;
	public static ArrayList<xmlAgent> returnedList = new ArrayList<xmlAgent>();

	//Might already be there
	private String xml_filePath;
	private ISurface surf;
	
	public XmlReconstructureHandlerReader(String xml_filePath, ISurface surf){
		this.xml_filePath= xml_filePath;
		this.surf= surf;
	}
	
	public XmlReconstructureHandlerReader(String xml_filePath){
		this.xml_filePath= xml_filePath;

	}
	
	public String[] reconstructWindows(){
		
		String windowsParams[] = new String[2];
		
		try {
	  	String filepath = xml_filePath;
		File fXmlFile = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);	
		doc.getDocumentElement().normalize();	
		NodeList nodeW1 = doc.getElementsByTagName("Window_1");
		NodeList nodeW2 = doc.getElementsByTagName("Window_2");
		
		Attr w1attr = (Attr) nodeW1.item(0).getAttributes().getNamedItem("WLocation");
		String w1attrStr = w1attr.getValue();
		windowsParams[0] = w1attrStr;
		Attr w2attr = (Attr) nodeW2.item(0).getAttributes().getNamedItem("WLocation");
		String w2attrStr = w2attr.getValue();
		windowsParams[1] = w2attrStr;

	    } catch (Exception e) {
		e.printStackTrace();
	    }
		return windowsParams;
	}
	
	 public String[] reInitiateVariables(){
		 String initialVar[] = new String[11] ; 
		 
		 try { 
			 
			
		  	String filepath = xml_filePath;
			File fXmlFile = new File(filepath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);	
			doc.getDocumentElement().normalize();	
			NodeList nList = doc.getElementsByTagName("Agent");
			for (int temp = 1; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;	

				setTotalAgents(eElement.getElementsByTagName("totalAgents").item(0).getTextContent() );
				setPanelAgentLength(eElement.getElementsByTagName("panelAgentLength").item(0).getTextContent() );
				setUniformExtrusion(eElement.getElementsByTagName("uniformExtrusion").item(0).getTextContent() );
				setExtrusion(eElement.getElementsByTagName("extrusion").item(0).getTextContent() );
				setPanelCollisionClearance(eElement.getElementsByTagName("panelCollisionClearance").item(0).getTextContent() );
				setGenAngle(eElement.getElementsByTagName("temGenAngle").item(0).getTextContent() );
				setProbRight(eElement.getElementsByTagName("probRight").item(0).getTextContent() );
				setProbLeft(eElement.getElementsByTagName("probLeft").item(0).getTextContent() );
				setProbStraight(eElement.getElementsByTagName("probStraight").item(0).getTextContent() );
				setPanelOffsetZ(eElement.getElementsByTagName("panelOffsetZ").item(0).getTextContent() );
				setShadeVal(eElement.getElementsByTagName("shadeVal").item(0).getTextContent() );
				


			//total agents
			initialVar[0]= Double.toString(getTotalAgents());
			//System.out.println ("total agents are" + initialVar[0]);
			//extrusion
			initialVar[1]=Double.toString(getPanelAgentLength());
			//offsetZ
			initialVar[2]=Double.toString(getPanelOffsetZ());
			//uniform extrusion
			initialVar[3]=Boolean.toString(getUniformExtrusion());
			//uniform extrusion
			initialVar[4]=Double.toString(getExtrusion());
			//extrusion weight
			initialVar[5]=Double.toString(getShadeVal());
			//extrusion weight
			initialVar[6]=Double.toString(getPanelCollisionClearance());	
			//angle

			double genAngle = ((double) Math.PI / Integer.parseInt((getGenAngle().replaceAll("[^0-9]", ""))));
			initialVar[7]=Double.toString(genAngle);
			initialVar[8]=Double.toString(getProbRight());
			initialVar[9]=Double.toString(getProbLeft());
			initialVar[10]=Double.toString(getProbStraight());
			}
		    } catch (Exception e) {
			  e.printStackTrace();
			}
		 
		
		 return initialVar;
		 
		 
	 }
	
	
	
	public ArrayList <xmlAgent> reconstructGeo(){
		//public void reconstructGeo(){
		  try {
			  
			  	String filepath = xml_filePath;
				File fXmlFile = new File(filepath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);	
				doc.getDocumentElement().normalize();		
				NodeList nList = doc.getElementsByTagName("Agent");
				//System.out.println("nList: "+ nList);
				
				for (int temp = 1; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
					Element eElement = (Element) nNode;	
					//System.out.println("eElement: "+ eElement);
					
					setLocationTypeID(eElement.getAttribute("Location") );
	
					setIsAlive(eElement.getElementsByTagName("isAlive").item(0).getTextContent() );
					setTotalAgents(eElement.getElementsByTagName("totalAgents").item(0).getTextContent() );
					setPanelAgentLength(eElement.getElementsByTagName("panelAgentLength").item(0).getTextContent() );
					setUniformExtrusion(eElement.getElementsByTagName("uniformExtrusion").item(0).getTextContent() );
					setExtrusion(eElement.getElementsByTagName("extrusion").item(0).getTextContent() );
					setPanelCollisionClearance(eElement.getElementsByTagName("panelCollisionClearance").item(0).getTextContent() );
					setGenAngle(eElement.getElementsByTagName("temGenAngle").item(0).getTextContent() );
					setProbRight(eElement.getElementsByTagName("probRight").item(0).getTextContent() );
					setProbLeft(eElement.getElementsByTagName("probLeft").item(0).getTextContent() );
					setProbStraight(eElement.getElementsByTagName("probStraight").item(0).getTextContent() );
					setPanelOffsetZ(eElement.getElementsByTagName("panelOffsetZ").item(0).getTextContent() );
					setShadeVal(eElement.getElementsByTagName("shadeVal").item(0).getTextContent() );
					
					
					String locationTypeID = getLocationTypeID();
					String array[] = locationTypeID.split(",");
					double angle = (double) Math.PI / Integer.parseInt(getGenAngle().replaceAll("[^0-9]", "")); 
					//String id= array[4];
					double extrusion = getExtrusion();
					double extrusionVal = getShadeVal();
					double offsetZ = getPanelOffsetZ();
					double agentLength = getPanelAgentLength();
					
					int life = getIsAlive();
					
					
					IVec initPt = surf.pt(Double.parseDouble(array[0]),Double.parseDouble(array[1]));			
					IVec finPt = surf.pt(Double.parseDouble(array[2]),Double.parseDouble(array[3]));
					
					IVec pt1 = new IVec (initPt.x(), initPt.y(), initPt.z());
					IVec dir = new IVec (finPt.x(), finPt.y(), finPt.z());
					IVec pt2 = pt1.dup().add(dir.dup().len(agentLength));
					
					//IVec point1 = initPt;
					dir = pt2.dif(pt1);

					//System.out.println("id is: " + array[4]); 
					//System.out.println("agent Length is : " + agentLength ); 
					
					
//					if (id == "0"){
//						 dir = finPt.dif(initPt);
//						 finPt =  dir.dup();
//					}else if (id == "1"){
//						 dir = finPt.dif(initPt);
//						finPt =  dir.dup().rot(IG.zaxis, angle);
//						
//					}else if(id == "2"){
//						 dir = finPt.dif(initPt);
//						 finPt =  dir.dup().rot(IG.zaxis, -angle);
//					}
//				
					xmlAgent panel= new xmlAgent(initPt, finPt, surf,life, array[4],extrusion, extrusionVal, offsetZ, agentLength, angle);
					returnedList.add(panel);

				}
				
				//do window agent stuff
				
				
			    } catch (Exception e) {
				e.printStackTrace();
			    }
		  
			return returnedList;
	}
	
	
	
	public xmlAgent getAgent(int index) {
		return (xmlAgent)returnedList.get(index);
		//return null;
	}

	
	public static void p(String s){
	//	System.out.println(s);
	}

	int getIsAlive()
	{
		return isAlive_JAR;
	}
	
	void setIsAlive(String isAlive )
	{
		isAlive_JAR = Integer.parseInt(isAlive);
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
		probRight_JAR = Double.parseDouble(probRight);
	}

	double getProbRight()
	{
		return probRight_JAR;
	}
	
	void setProbLeft(String probLeft )
	{
		probLeft_JAR = Double.parseDouble(probLeft);
	}

	double getProbLeft()
	{
		return probLeft_JAR;
	}
	
	void setProbStraight(String probStraight )
	{
		probStraight_JAR = Double.parseDouble(probStraight);
	}

	double getProbStraight()
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
	
	void setLocationTypeID(String LocationAndType )
	{
		LocationAndType_JAR = LocationAndType;
	}

	String getLocationTypeID()
	{
		return LocationAndType_JAR;
	}
	
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	 //   double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static double parseGenAngle(String genString){
		StringTokenizer st= new StringTokenizer(genString, "/");
		String parses[]= new String[2];	
		
		parses[0]= st.nextToken();
		//p("parses: "+ parses[0]);
		parses[1]= st.nextToken();
		//p("parses: "+ parses[1]);
		
		for (int x= 0; x< parses.length; x++){
			if (!isNumeric(parses[x])){
				parses[x]= ""+Math.PI;
			}
		}
		
		double nom = Double.parseDouble(parses[0]);
		double denmo= Double.parseDouble(parses[1]);
		//p("parsed gen angle: "+ nom/denmo);
		//p("gen angle: "+ Math.PI/8);
		return nom/denmo;
	}
	
	public static void testXml(String xml_filePath, int temp){
		  try {	  
		  	String filepath = xml_filePath;
		  	//System.out.println("nside xml reconstruction, filepath= " +filepath);
			File fXmlFile = new File(filepath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);	
			doc.getDocumentElement().normalize();		
			NodeList nList = doc.getElementsByTagName("Agent");
			//System.out.println("nList: "+ nList);
			
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;	
//			//System.out.println("eElement: "+ eElement);
		for (int temp1 = 0; temp1 < nList.getLength(); temp1++) {
			p("location ID: "+ eElement.getAttribute("Location"));
			p("panel Agent Length: "+ eElement.getElementsByTagName("panelAgentLength").item(0).getTextContent());
			p("gen angle: "+ parseGenAngle(eElement.getElementsByTagName("temGenAngle").item(0).getTextContent()));
	 		//setLocationTypeID(eElement.getAttribute("Location") );
		   // locationTypeID = getLocationTypeID();
			String array[] = eElement.getAttribute("Location").split(",");
			p("location x on surf: "+ array[0]);
			p("loccation y on surf: "+ array[1]);
			p("agent type  "+ array[2]);
		}

			
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
	public static void main(String[] args){
		String xml_filePath= "C:\\Users\\Evangelos\\Desktop\\151021_agentPanel12_jar\\configFile\\agents2.xml";
		
		testXml(xml_filePath,0);
	}


}

