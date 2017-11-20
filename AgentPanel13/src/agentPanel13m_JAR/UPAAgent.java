package agentPanel13m_JAR;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.TransformException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import igeo.*;


@SuppressWarnings("unused")
public class UPAAgent {
    
    private String xmlFile;//the xml file location
    private String daysimFile;//the daysim file location
    private String udiFile;//the sensor file
    private double daysimLoc[] = new double[4];//the coordinate location of the daysim sensor
    private ISurface surf;//
    private int daysimSen[];//holds the daysim sensor info from the file
    private int totAgentSenHit[][];//holds all the hits for all the agents
    private double userPref[];//holds the userPref data
    private double agentWeight[] = null;
    
    
    NodeList nodes;//contains all the agents from xml
	private BufferedReader bufferedReader;
    
    //Constructor
    public UPAAgent(String xml, String userPref, ISurface _surf){
        
        xmlFile = xml;
        //daysimFile = daysim;//add pack in to make this actually work
        surf = _surf;//make the surface
        udiFile = userPref;
        //this.surf.del();
        
        //define daysimFile explicit for now
        
        
    }
    /////////////////////////////////////////////////////////////////////////////
    //Public Functions
    /////////////////////////////////////////////////////////////////////////////
    public void mapLoop(String daysimFiles[]) throws ParserConfigurationException, SAXException, IOException, TransformException, TransformerFactoryConfigurationError, TransformerException{
        int fileCount = 1;
        readUserPreference();
        
        for(int i=0; i< daysimFiles.length; i++){
            fileCount++;
            if(fileCount == 5){
                String daysimFilesTotal[] = {daysimFiles[i-3],daysimFiles[i-2],daysimFiles[i-1],daysimFiles[i]};
                daysimFile = daysimFiles[i];
                String daysim[];
                daysim = daysimFile.split("/");
                String daysimNum = daysim[daysim.length-1];
                daysimNum = daysimNum.replace(".ill","");
                
                //combine the daysim files together
                daysimSen = combineDaysimFiles(daysimFilesTotal);
                
                String daysimLocStr[] = daysimNum.split("_");
                for(int j=0; j<daysimLocStr.length; j++){
                    daysimLoc[j] = Double.parseDouble(daysimLocStr[j]);//gets the x, y and z coordinates of the daysim file
                }
                mapAgents();
                fileCount = 0;
            }
        }

        writeXML();        //write the xml
        
    }
    public void mapAgents() throws ParserConfigurationException, SAXException, IOException{
        
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setIgnoringComments(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new File(xmlFile));
        
        nodes = doc.getElementsByTagName("Agent");//get the agent nodes from the xml
        if(agentWeight == null){
            agentWeight = new double[nodes.getLength()];
            for(int i=0; i<agentWeight.length; i++){
                agentWeight[i] = 1;
            }
        }
        
        //getDaysimList();//read the daysim file and  
        //compare the attributes of the agent node to the daysim file
        for(int i=0; i<nodes.getLength(); i++){//loop through all the nodes
            Node node = nodes.item(i);
            if(node.hasAttributes()){//make sure the node actually has attributes
                Attr attr = (Attr) node.getAttributes().getNamedItem("Location");//get the attribute Location from the agent in the xml
                if(attr != null){//make sure its not null
                    String attribute = attr.getValue();//get the value of the attribute
                    //System.out.println("Attribute: " + attribute); //only needed for testing
                    String attrArrStr[] = attribute.split(",");//split the attributes based on commas
                    double attrArr[] = new double[3];//holds the locations of the agents
                    for(int j=0; j<attrArr.length; j++){
                        attrArr[j] = Double.parseDouble(attrArrStr[j]);//parse the string version of the locations into doubles
                    }
                    IVec attrPt = new IVec(attrArr[0], attrArr[1], 0);//create a IVec for the agent locations from the xml
                    IVec daysimPt = new IVec(daysimLoc[0]/10, daysimLoc[2]/5, 0);//create the IVec for the daysim location
                    
                    //IPoint p = new IPoint(daysimLoc[0],0,daysimLoc[2] ).clr( 0, 0, 255);
                    //IVec daysimSurfPt = surf.pt(daysimPt);

                    double dist = attrPt.dist(daysimPt);//get the distance from the two
                    //System.out.println("Agent x: " + attrPt.x + " Agent y: " + attrPt.y);
                    //System.out.println("Daysim x: " + daysimPt.x + " Daysim y: " + daysimPt.y);
                    //System.out.println("Distance between two is: " + dist);
                    if(dist < .1){
                        //System.out.println("Here");
                        //totAgentSenHit[i] = checkSensors();
                        //get the weight for the agents
                        agentWeight[i] = solveWeight();
                        
                    }
                    
                    
                }
            }
        }//end of the for loop
        
        
    }//end of map agents

    public void readUserPreference(){//reads the user preference
        
        userPref = new double[2623];
        String fileName = udiFile;
        
        String line = null;
        
        try{
            
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int index = 0;
            
            while((line = bufferedReader.readLine()) != null){
                
                userPref[index] = Double.parseDouble(line);
                index++;
                
            }
            
            bufferedReader.close();
            
        }
        catch(FileNotFoundException ex){
            System.out.println("Unabale to open file " + fileName);
        }
        catch(IOException ex){
            System.out.println("Error reading file " + fileName);
        }
        
    }
    
    public void readSensorFile(){//read the sensor file
        
        
        
    }
    
    //////////////////////////////////////////////////////////////////////////////
    //Private Functions
    //////////////////////////////////////////////////////////////////////////////
    private int[] combineDaysimFiles(String daysimGroup[]) throws IOException{//needs to be expanded for multiple days
        List<String> combinedDaysim = new ArrayList<String>();
        for(int i=0; i<daysimGroup.length; i++){
            String daysimStr[] = null;
            try{
                FileReader fileReader = new FileReader(daysimGroup[i]);
                bufferedReader = new BufferedReader(fileReader);
                for(int j=0; j<12; j++){
                    bufferedReader.readLine();
                }
                //for(int j=0; j<10; j++){
                    daysimStr = bufferedReader.readLine().substring(10).trim().split(" ");
                //}
            }
            catch(FileNotFoundException ex){
                
            }
            for(int j=0; j<daysimStr.length; j++){
                combinedDaysim.add(daysimStr[j]);
            }
        }//main for loop finish
        
        Object[] combinedString = combinedDaysim.toArray();
        double daysimDouble[] = new double[combinedString.length];
        int daysimInt[] = new int[combinedString.length];
        for(int i=0; i< daysimDouble.length; i++){
            
            daysimInt[i] = Integer.parseInt(combinedString[i].toString());
            //System.out.println("Sensor point value: " + daysimDouble[i]);
            
        }
        //System.out.println("Total size of the thing: " + daysimDouble.length);
        return daysimInt;
        
    }
    private double solveWeight(){
        double weight = 1;
        for(int i=0; i<userPref.length; i++){
            
            if(userPref[i] < 1 && daysimSen[i] > 0){
                
                weight = 1 - userPref[i];
                
            }
            
        }
        return weight;
        
    }

    private void writeXML() throws ParserConfigurationException, SAXException, IOException, TransformException, TransformerFactoryConfigurationError, TransformerException{//fix this
        
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setIgnoringComments(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new File(xmlFile));
        
        nodes = doc.getElementsByTagName("Agent");
        
        for(int i=0; i<nodes.getLength(); i++){
            
            nodes.item(i).appendChild(createWeightElement(doc, agentWeight[i]));
            
        }
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");//make it pretty
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}index-amount", "10");
        DOMSource source = new DOMSource(doc);
        
        StreamResult file = new StreamResult(new File(xmlFile));
        
        System.out.println(nodeToString(doc));
        transformer.transform(source, file);
        
    }
            
    private Element createWeightElement(Document doc, Double weight){
            
            Element el = doc.createElement("Weight");
            el.appendChild(doc.createTextNode(weight.toString()));
            
            return el;
        
    }
    
    private String nodeToString(Node node) throws TransformException, TransformerFactoryConfigurationError, TransformerException{
        
        StringWriter buf = new StringWriter();
        Transformer xform = TransformerFactory.newInstance().newTransformer();
        xform.transform(new DOMSource(node), new StreamResult(buf));
        return buf.toString();
        
    }
    
    
}
