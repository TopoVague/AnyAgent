package agentPanel13m_JAR;

import processing.core.PApplet;

import java.util.ArrayList;

import igeo.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("unused")
public class AgentPanel13m_JAR extends PApplet {
	private AgentPanel13m_JAR myself= this;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String runIndex ;
	
	public static boolean recreatedBool = false;
	public static boolean hillClimbComplete = false ; 

	public static int cnt = 0;// counter for the agent ids had to be placed here
	public static int leftCnt = 0;
	public static int rightCnt = 0;
	public static int straightCnt = 0;
	public static int currAgent = 0;
	public static int failCounter = 0;
	public static int tripleFail = 0;
	public IVec[] failHolder = new IVec[3];
	public boolean failFlag;
	public boolean lifeFlag;
	public boolean changeExtrusion ; 

	public static int reCnt = 0;// counter for the regenerated agent ids had to
								// be placed here
	public static int reLeftCnt = 0;
	public static int reRightCnt = 0;
	public static int reStraightCnt = 0;
	public static int reGenAgent = 0;
	// vars for screenshotting and movie making
	public int scrnCnt = 0;

	static int runDuration = 1000;// How long the IGEO server runs

	// file path with inputParameters
	public static String xml_filePath;
	static XmlHandlerReader_JAR handler;
	static XmlReconstructureHandlerReader reHandler;
	// filename for output geometry
	public static String fileName = "";

	// location to retrieve input geometry for growing the panels
	public static String geoFilePath;
	// location to save the generated Geometry
	public static String exportedGeoFileLoc;
	// location to save the xml with the agents information, save geometry and
	// agents.xml in the same folder
	public static String exportXmlFilePath;
	// location to save a screenshot from each run
	public static String screenshotFileLocation;

	// variables for the PANEL Agent 1) totalAgents 2) panelAgentLength 3)
	// panelOffsetZ 4)panelCollisionClearance 5)uniformExtrusion 6)extrusion 7)
	// shadeVal 8 )genAngle 9) Type (left,right,straight)
	public static int totalAgents;
	public static double panelAgentLength;// length in u-v space, less than	// 1.0
	public static double panelCollisionClearance; // less than length
	public static double genAngle; // PI/2, PI/4 , PI/4 etc

	public static boolean uniformExtrusion;
	public static double extrusion;
	public static double shadeVal;
	public static double panelOffsetZ;
	
	// probabilty for each agent Type at each timestep
	static int probRight;
	static int probLeft;
	static int probStraight;

	public static boolean stuck = false;
	public static int CrashCounter =0; 

	// parameters for WINDOW Agents-Position
	public static double w1x;
	public static double w1y;
	public static double w1r;
	public static double w2x;
	public static double w2y;
	public static double w2r;
	public static double tempWindowFix = 0.5 ;
	// extra variable for window block agent
	// number of windows
	double windowWidth = 1.4;
	double windowDepth = 1;
	double windowHeight = 2;
	public static int delay	=0;

	// parameters for regenerating Geometry
	int regenProb;
	public static int deathCount = 0;

	// variable for starting regenerating panels
	public static int deathGeoCall = 0;
	int deathCountLimit = 10;
	public static int blockCounter = 0;
	// declare lineAgent
	public static ArrayList<LineAgent> lineAgentList = new ArrayList<LineAgent>();
	public static ArrayList<LineAgent> deletedAgents = new ArrayList<LineAgent>();
	public static ArrayList<xmlAgent> theReturnedList = new ArrayList<xmlAgent>();
	private static Writer w;
	public static IVec temp;
	

	public static void initiateVariables(String xml_filePath) {
		handler = new XmlHandlerReader_JAR(xml_filePath);
		// variables for the panels

		totalAgents = handler.getTotalAgents();
		panelAgentLength = handler.getPanelAgentLength();
		panelOffsetZ = handler.getPanelOffsetZ();

		uniformExtrusion = handler.getUniformExtrusion();
		extrusion = handler.getExtrusion();
		shadeVal = handler.getShadeVal();
		panelCollisionClearance = handler.getPanelCollisionClearance();
		genAngle = (double) PI / Integer.parseInt((handler.getGenAngle().replaceAll("[^0-9]", "")));

		probRight = handler.getProbRight();
		probLeft = handler.getProbLeft();
		probStraight = handler.getProbStraight();

		w1x = handler.getW1x();
		w1y = handler.getW1y();
		w1r = handler.getW1r();
		w2x = handler.getW2x();
		w2y = handler.getW2y();
		w2r = handler.getW2r();

	}
	
	public static void updateVariables(String xml_filePath){
		
		
		reHandler = new XmlReconstructureHandlerReader( xml_filePath);
		
		String[] upDatedWindowParams = new String[2];
		String[] updatedVariables = new String[11];
		
		
		//retrieve all the panel variables from the agents xml and initialize them 
		updatedVariables = reHandler.reInitiateVariables();
		//println("updated total max agent are : " + updatedVariables[0]);
		totalAgents = (int) Double.parseDouble (updatedVariables[0]);
	
		panelAgentLength =Double.parseDouble(updatedVariables[1]);
		//println("updated lenght is : " + updatedVariables[1]);
		
		panelOffsetZ = Double.parseDouble(updatedVariables[2]);
		//println("updated panel offzet is : " + updatedVariables[2]);
		
		uniformExtrusion = Boolean.parseBoolean(updatedVariables[3]);
		//println("updated panel extrusion is : " + updatedVariables[3]);
		extrusion = Double.parseDouble(updatedVariables[4]);
		//println("updated panel extrusion is : " + updatedVariables[4]);
		
		shadeVal = Double.parseDouble(updatedVariables[5]);
		//println("updated panel extrusion is : " + updatedVariables[5]);
		
		panelCollisionClearance = Double.parseDouble(updatedVariables[6]);
		//println("updated clearance is : " + updatedVariables[6]);
		
		genAngle = Double.parseDouble(updatedVariables[7]);
		//println("updated angle is  : " + updatedVariables[7]);
		
		probRight = (int) Double.parseDouble(updatedVariables[8]);
		//println("updated probRight is  : " + updatedVariables[8]);
		probLeft =(int) Double.parseDouble(updatedVariables[9]);
		//println("updated probLeft is  : " + updatedVariables[9]);
		probStraight =(int) Double.parseDouble(updatedVariables[10]);
		//println("updated probStraight is  : " + updatedVariables[10]);
		
		//retrieve all the window variables from the agents xml and
		upDatedWindowParams = reHandler.reconstructWindows();

		//println ("window 1 position is : (" + upDatedWindowParams[0]+")");
		//println ("window 2 position is : (" + upDatedWindowParams[1] +")");
		String[] paramsWin1 = upDatedWindowParams[0].split(",");
		w1x = Double.parseDouble (paramsWin1[0]);
		w1y = Double.parseDouble (paramsWin1[1]);
		w1r = Double.parseDouble (paramsWin1[2]);
		String[] paramsWin2 = upDatedWindowParams[1].split(",");
		w2x = Double.parseDouble (paramsWin2[0]);
		w2y = Double.parseDouble (paramsWin2[1]);
		w2r = Double.parseDouble (paramsWin2[2]);	
	}
	

	public void setup() {

		IG.perspective();
		IG.bg(255, 255, 255);
		size(1920, 1080, IG.GL);
		IG.duration(runDuration);
		IG.open(geoFilePath);
		IG.focus();
		ISurface surf = IG.surface(0).clr(48, 144, 255, 50);
		IPoint[] suppPoints = IG.layer("support_points").points();
		IPoint[] boundaryPoints = IG.layer("boundary").points();
		IVec[] supportVec = new IVec[10];
		IVec[] dirVec = new IVec[10];
		/// read point for window positioning	
		//initial position change based on 
		double initX ; 
		double initDirX ; 
		double initDirY ;
		if ((w1x < 0.5)&&(w1y < 0.5)&& (w2y < 0.5)){
			initX = (w1x+w2x)/2 ;
			initDirX = initX;
			initDirY = 1 ;					
		}else if ((w1y < 0.25)&& (w2y > 0.25)){
			initX = (w1x+w2x)/2 ; 
			initDirX = initX+ w2x;
			initDirY = w2y ;
		}else if ((w1y > 0.25)&& (w2y < 0.25)){
			initX = (w1x)/2 ; 
			initDirX = 1 + w1x ;
			initDirY = w1y ;
		}else if ((w1y > 0.25)&& (w2y > 0.25)&&(w1x < 0.25)){
			initX = (w1x+w2x)/2 ; 
			initDirX = 1 + w2x ;
			initDirY = 1 ;
		}else{
			initX = 0 ; 
			initDirX = 1;
			initDirY = 1 ;		
		}		
		IVec initialAgentPos = new IVec(initX, 0, 0);
		IVec initialAgentDir = new IVec(initDirX, initDirY, 0);		
		// map points to surface
		IVec surfWin1 = surf.pt(w1x, w1y);
		IVec surfWin2 = surf.pt(w2x, w2y);
		IVec surfWin3 = surf.pt(w2x + tempWindowFix, w2y);

		if (runIndex.equals("0")) {// first agent on left side
			 println ("running from input parameters");
			//Wwindow agent created with parameters
			new WindowBlockAgent(surfWin1, w1r, 2 * w1r, 2 * w1r, surf);
			new WindowBlockAgent(surfWin2, w2r, 2 * w2r, 2 * w1r, surf);
			//new WindowBlockAgent(surfWin3, w2r, 2 * w2r, 2 * w2r, surf);
			//draw the first agent
			new LineAgent(initialAgentPos, initialAgentDir, surf, 1, "root1");
							
		}else if (runIndex.equals("1")){
			    println ("hill climber updates window positions");
				reHandler = new XmlReconstructureHandlerReader( xml_filePath,surf);
				String[] upDatedWindowParams = new String[2];
				upDatedWindowParams = reHandler.reconstructWindows();			
				String[] paramsWin1 = upDatedWindowParams[0].split(",");
				w1x = Double.parseDouble (paramsWin1[0]);
				w1y = Double.parseDouble (paramsWin1[1]);
				w1r = Double.parseDouble (paramsWin1[2]);
		
				String[] paramsWin2 = upDatedWindowParams[1].split(",");
				w2x = Double.parseDouble (paramsWin2[0]);
				w2y = Double.parseDouble (paramsWin2[1]);
				w2r = Double.parseDouble (paramsWin2[2]);
				surfWin1 = surf.pt(w1x, w1y);
				surfWin2 = surf.pt(w2x, w2y);
				surfWin3 = surf.pt(w2x , w2y);
				// read the values from the input params
				theReturnedList = reHandler.reconstructGeo();			
				panelAgentLength = reHandler.panelAgentLength_JAR;
				panelOffsetZ = reHandler.getPanelOffsetZ();
				uniformExtrusion = reHandler.getUniformExtrusion();
				extrusion = reHandler.getExtrusion();
				shadeVal = reHandler.getShadeVal();
				panelCollisionClearance =  reHandler.getPanelCollisionClearance();
				genAngle = (double) PI / Integer.parseInt(( reHandler.getGenAngle().replaceAll("[^0-9]", "")));
				probRight = (int)reHandler.getProbRight();
				probLeft =  (int) reHandler.getProbLeft();
				probStraight = (int) reHandler.getProbStraight();

				new WindowBlockAgent(surfWin1, w1r, 2 * w1r, 2 * w1r, surf);
				new WindowBlockAgent(surfWin2, w2r, 2 * w2r, 2 * w1r, surf);
				new WindowBlockAgent(surfWin3, w2r, 2 * w2r, 2 * w2r, surf);			
				new LineAgent(initialAgentPos, initialAgentDir, surf, 1, "root1");
							 
				
		}else if (runIndex.equals("2")){
		        println ("Runs from adapted Agents.xml");
				reHandler = new XmlReconstructureHandlerReader(xml_filePath, surf);
				theReturnedList = reHandler.reconstructGeo();	    
				recreatedBool = true;
				xmlAgent agent1 = theReturnedList.get(0);
				new LineAgent(agent1.xmlPt1, agent1.xmlPt2, agent1.surf, agent1.lifeValue, agent1.agentID);
				createGeometryFromXml(surf);
		}else {
				println ("ERROR: your last argument 'runIndex' should be between 0-2");
			
		}
		//delete the base surface
		//surf.del();

	}
		
	

	

	
	// reconstruct geometry from agents.xml
	public void createGeometryFromXml(ISurface _surf) {
		
	 ISurface s1 = _surf ;
		
     for (int i=0; i<theReturnedList.size();i++){
    	
	    xmlAgent reGenAgent = reHandler.getAgent(i);
	   // //println("agent ID is : " + two.agentID);	
		double newOffsetDepth2 = reGenAgent.extrusion * reGenAgent.extrusionVal;
		
		
		IVec mid = reGenAgent.xmlPt1.mid(reGenAgent.xmlPt2);		
		IPoint midPt = new IPoint (mid.x(),mid.y(),mid.z());				
//		IVec surfPt1 = new IVec (mid.x, mid.y,mid.z);
//		IVec surfPt1d = s1.pt(mid.x,mid.y-reGenAgent.extrusion, reGenAgent.offsetZ);
		
		//working version
		IVec surfPt1 =  reGenAgent.xmlPt1 ;
		IVec surfPt1d = new IVec(surfPt1.x(), surfPt1.y()-reGenAgent.extrusion,surfPt1.z()-reGenAgent.offsetZ);		
		IPoint xmlPt1 = new IPoint(surfPt1.x(), surfPt1.y(), surfPt1.z()).clr(0,255,0);	
		IPoint xmlPt1d = new IPoint(surfPt1d.x(), surfPt1d.y(), surfPt1d.z()).clr(0,0,255);	
		IVec surfPt2 = reGenAgent.xmlPt2 ; 
		IVec surfPt2d = new IVec (reGenAgent.xmlPt2.x(), reGenAgent.xmlPt2.y()-reGenAgent.extrusion, reGenAgent.xmlPt2.z()- reGenAgent.offsetZ);

						
		if (reGenAgent.agentID.contains("0")) {// typNum = 0 =(straight)
			
			IVec xmlMidPt = reGenAgent.xmlPt1.mid(reGenAgent.xmlPt2);	
			//IPoint midPoint = new IPoint(xmlMidPt .x(), xmlMidPt .y(), xmlMidPt .z()).clr(0,255,0);	
			IVec xmlMidPt3D = new IVec (xmlMidPt.x(), xmlMidPt.y()-newOffsetDepth2, xmlMidPt.z() - reGenAgent.offsetZ);
			IVec surfPt3 = new IVec (xmlMidPt .x(), xmlMidPt.y(), xmlMidPt .z());
			IVec surfPt3d = new IVec(xmlMidPt3D.x(), xmlMidPt3D.y() ,xmlMidPt3D.z());
			//IPoint nextPoint = new IPoint(surfPt3.x(), surfPt3.y(), surfPt3.z()).clr(255,0,0);	
			//IPoint xmlMidPt3d = new IPoint(surfPt3d.x(), surfPt3d.y(), surfPt3d.z()).clr(255,0,0); 
			
			IVec[][] cpts = new IVec[3][2];
			cpts[0][0] = surfPt2;
			cpts[0][1] = surfPt2d;
			cpts[1][0] = surfPt3;
			cpts[1][1] = surfPt3d;
			cpts[2][0] = surfPt1;
			cpts[2][1] = surfPt1d;
			new ISurface(cpts, 2, 1).clr(255, 255, 0);
		}
		if (reGenAgent.agentID.contains("1")) // typeNum = 1 =(left)
		{
			IVec dir =  reGenAgent.xmlPt2.mid(reGenAgent.xmlPt1);	
			IVec nextDir = dir.dup().rot(IG.zaxis, genAngle);
			IVec xmlMidPt = nextDir;			
			//IPoint midPoint = new IPoint(xmlMidPt .x(), xmlMidPt.y(), xmlMidPt.z()).clr(0,255,0);	
			//xmlMidPt.rot(IG.zaxis, two.xmlGenAngle);	
	
			IVec xmlMidPt3D = new IVec (xmlMidPt.x(), xmlMidPt.y()-newOffsetDepth2, xmlMidPt.z() - reGenAgent.offsetZ);
			//IPoint midPoint1 = new IPoint(xmlMidPt .x(), xmlMidPt .y(), xmlMidPt .z()).clr(255,0,0);			
			//IVec mid2 = xmlMidPt.cp(nextDir.dup().len(panelAgentLength  / 2));
			//IPoint midPoint1 = new IPoint(mid2 .x(), mid2 .y(), mid2 .z()).clr(255,0,0);
			
			IVec surfPt3 = new IVec (xmlMidPt .x(), xmlMidPt.y(), xmlMidPt .z());
			IVec surfPt3d = new IVec(xmlMidPt3D.x(), xmlMidPt3D.y() ,xmlMidPt3D.z());
			//IVec surfPt3 = new IVec (mid2.x(), mid2.y(), mid2.z());
			//IVec surfPt3d = new IVec (mid2.x, mid2.y -newOffsetDepth2, mid2.z - two.offsetZ);		
			IVec[][] cpts = new IVec[3][2];
			//Points for the surface
			cpts[0][0] = surfPt2;
			cpts[0][1] = surfPt2d;
			cpts[1][0] = surfPt3;
			cpts[1][1] = surfPt3d;
			cpts[2][0] = surfPt1;
			cpts[2][1] = surfPt1d;
			new ISurface(cpts, 2, 1).clr(0, 255, 0);

		}
		
		if (reGenAgent.agentID.contains("2")) // typNum = 2 (right)
			{			
			IVec dir=  reGenAgent.xmlPt1.mid(reGenAgent.xmlPt2);	
			IVec nextDir = dir.dup().rot(IG.zaxis, -genAngle);
			IVec xmlMidPt = nextDir;	
			IPoint midPoint1 = new IPoint(xmlMidPt .x(), xmlMidPt .y(), xmlMidPt .z()).clr(255,255,0);	
			IVec xmlMidPt3D = new IVec (xmlMidPt.x(), xmlMidPt.y()-newOffsetDepth2, xmlMidPt.z() - reGenAgent.offsetZ);

			IVec surfPt3 = new IVec (xmlMidPt .x(), xmlMidPt.y(), xmlMidPt .z());
			IVec surfPt3d = new IVec(xmlMidPt3D.x(), xmlMidPt3D.y() ,xmlMidPt3D.z());

			IVec[][] cpts = new IVec[3][2];
			//Points for the surface
			cpts[0][0] = surfPt2;
			cpts[0][1] = surfPt2d;
			cpts[1][0] = surfPt3;
			cpts[1][1] = surfPt3d;
			cpts[2][0] = surfPt1;
			cpts[2][1] = surfPt1d;
			new ISurface(cpts, 2, 1).clr(0, 0, 255);
			
			}
     	}
	} /// end createGeometryFromXml


	////////////////// WindowBlockAgent Class////////////////////////////////
	static class WindowBlockAgent extends IAgent {
		IVec posW;
		double wWidth;
		double wHeight;
		double wDepth;

		boolean isInBlockedArea = false;
		ISurface surf;
		IBox bBox;

		WindowBlockAgent(IVec p, double _width, double _height, double _depth, ISurface s) {
			posW = p;
			wWidth = _width;
			wHeight = _height;
			wDepth = _depth;
			surf = s;

		}



		public void interact(ArrayList<IDynamics> agents) {
			// not need as of right now
		}

		public void update() {

			if (time == 0) {
				bBox = new IBox(posW, wWidth, wHeight, wDepth).clr(0, 1, 1, 0.5);
				// move to center the window agent
				bBox.mv(new IVec(-wWidth / 2, -wHeight / 2, -wDepth / 2));

			}
			if (true)
				bBox.del();

		}
	}

	////////////////// WindowBlockAgent Class End//////////////////////////

	////////////////// Line Agent///////////////////////////////
	public class LineAgent extends IAgent {
		double lineAgentLength = getLength();
		double clearance = panelCollisionClearance; 
		boolean isMaxedAgent = false;
		boolean isColliding = false;
		boolean isInBlockedArea = false;
		int typeNum; // 0= straight, 1=left, 2=right
		private IVec pt1, pt2; // start point, end point of agent
		ISurface surf; // surface to grow the line agent
		WindowBlockAgent w;
		int lifeValue;// number between 0 and 1
		String agentID;
		double agentAngle = genAngle;
		double offsetDepth1 = extrusion;
		double offsetDepth2;
		double offsetZ1 = panelOffsetZ;
		double offsetDepthWeight;
					
		
		public LineAgent(IVec pt, IVec dir, ISurface s, int life, String id) {
			pt1 = pt;
			pt2 = pt.dup().add(dir.dup().len(lineAgentLength));
			dir.dup().rot(IG.zaxis, genAngle);
			surf = s;
			lifeValue = life;
			agentID = id;
			w = null;
			if (id.contains("straight")) {
				typeNum = 0;
			} else if (id.contains("left")) {
				typeNum = 1;
			} else if (id.contains("right")) {
				typeNum = 2;
			}
			lineAgentList.add(this);
			currAgent++;
		}
		
		
		public LineAgent(IVec pt, IVec dir, ISurface s, int life, String id, double _extrusion) {
			pt1 = pt;
			pt2 = pt.dup().add(dir.dup().len(lineAgentLength));
			//dir.dup().rot(IG.zaxis, genAngle);
			surf = s;
			lifeValue = life;
			agentID = id;
			w = null;
			if (id.contains("straight")) {
				typeNum = 0;
			} else if (id.contains("left")) {
				typeNum = 1;
			} else if (id.contains("right")) {
				typeNum = 2;
			}
			if (life == 2){
				extrusion=0.001;
			}
			lineAgentList.add(this);
			currAgent++;
		}
		


		public void interact(ArrayList<IDynamics> agents) {
				if (time == 0) { // only in the first time
					
					for (int i = 0; i < agents.size() && !isColliding; i++) {
						if (agents.get(i) instanceof WindowBlockAgent) {
							WindowBlockAgent opening = (WindowBlockAgent) agents.get(i);
							IVec surfPt1 = surf.pt(pt1.x, pt1.y);
							IVec surfPt2 = surf.pt(pt2.x, pt2.y);
							IVec openingPt = surf.pt(opening.posW.x(),opening.posW.y() );
							IPoint p1 = new IPoint(surfPt1.x(), surfPt1.y(), surfPt1.z()).clr(255, 0, 255);
							IPoint p2 = new IPoint(surfPt2.x(), surfPt2.y(), surfPt2.z()).clr( 0, 255,0);
							p1.hide();
							p2.hide();
							double dist1 = opening.posW.dist(p2);
							
							//this.offsetDepth2 = 1 - dist1 ; 

						
					 if (dist1 < opening.wWidth / 2 || dist1 < opening.wHeight / 2 || dist1 < opening.wDepth / 2) {
								isInBlockedArea = true;
								lifeValue = 0;
								println ("agent " + this.agentID +"  was inside the window");// deletes agents inside the window
								if (lifeFlag == true){
									CrashCounter++;
									//println("found one from triple");
									println ("Current crashCount is " + CrashCounter);																						
									lifeFlag = false;					
								}
								
//case override here with 3extrusion 
								if (CrashCounter>2){
										this.offsetDepth1 = 0.001;
										isInBlockedArea = false;
										lifeValue = 1;
										println("case Override");
//									}else{
//									  println("case change extrusion");
//									  this.offsetDepth1 =dist1*2;
								}
									

//												change behavior here
//												change extrusion based on distance from window
//									}else if(pt1.x() < w1x && pt1.y() < w1y ){
//										
//										surfPt2 = surf.pt(-pt2.x, pt2.y);																		
//										probLeft = probLeft++;
//					
//									}else if (pt1.x() < w1x && pt1.y() > w1y && pt1.y() > w2y  ){
//										println("case2");
//										
//										probRight = probLeft ++ ;	
//										probStraight = probStraight ++ ;	
//										probLeft = probRight;	
//
//										
//									}else if (pt1.x() > w1x && pt1.x() < w2x && pt1.y() < w1y && pt1.y() < w2y  ){
//										println("case3");
//										probStraight = probStraight +10 ;	
//										probLeft = probLeft + 20 ;	
//										probRight = probRight -- ;	
//										
//									}else if (pt1.x() > w2x && pt1.y() > w2y ){
//										println("case4");
//										surfPt2 = surf.pt(pt2.x, pt2.y);
//										probLeft = probLeft ++ ;	
//										probStraight = probStraight +10 ;
//										probRight = probRight +10 ;
//									}else{
//										println("caseOveride");
//										this.offsetDepth1 =0.001;
//									}
//									if (CrashCounter>1){
//										println("caseOveride");
//										extrusion =0.001;
//									}
						
								
							}
						if( this.offsetDepth1 != 0.001 ){
							//println ("got in");
							//this.offsetDepth1 = dist1; 
							//this.offsetDepth1 = dist1 * 0.03; 
							//isInBlockedArea = false;
							//lifeValue = 1;
						}	
						
							
						
						} else if (agents.get(i) instanceof LineAgent) {
							LineAgent lineAgent = (LineAgent) agents.get(i);
						
							// checking clearance of end point
							if (lineAgent != this) {
								if (lineAgent.pt2.dist(pt2) < clearance) {
									//println("agent "+ this.agentID+ " didnt have enough clearance");
									isColliding = true;
									lifeValue = 0;
								}
								if (!lineAgent.isColliding) {
									IVec intxn = IVec.intersectSegment(pt1, pt2, lineAgent.pt1, lineAgent.pt2);
									if (intxn != null) {
										if (!intxn.eq(pt1)) {
											isColliding = true;
											lifeValue = 0;
										}
									}
								}
							}
						}
					}
				}

				if (agents.size() >= totalAgents) {
					isMaxedAgent = true;
				}				
			
		}
		
		

		public void update() {
			if (pt2.x < 0.0 || pt2.x > 1.0 || pt2.y < 0.0 || pt2.y > 1.0) {
					// //println("Agent with id" + agentID + " was outside bounding box");
				    isColliding = true;
					lifeValue = 0;
			}
			
			if (isColliding || lifeValue == 0 || isInBlockedArea) {

				if( failHolder[0] == failHolder[1] && failHolder[1] == failHolder[2]  )
				{
					//println( "A Triple!");
					lifeFlag = true;
					failCounter = 0;
					failFlag = true;	
				}
				
				
				if( failFlag == false ){
					deletedAgents.add(this);	
					this.del();
					deathCount++;
					currAgent--;
					IVec surfPt1 = surf.pt(pt1.x, pt1.y);
					IVec surfPt2 = surf.pt(pt2.x, pt2.y);
					IPoint p1 = new IPoint(surfPt1.x(), surfPt1.y(), surfPt1.z()).clr(0, 255, 0);		
				//	IPoint p2 = new IPoint(surfPt2.x(), surfPt2.y(), surfPt2.z()).clr(255, 0, 0);		
					//println("Deleted agent at : "+ pt1);
				}
					
					if( failCounter > 2)
					{
						failHolder[0] = failHolder[1];
						failHolder[1] = failHolder[2];
						failCounter = 2;
								
					}
					failHolder[failCounter] = pt1;
					failCounter++;
					failFlag = false;
					
			} else if (time == 0) { // if not colliding needs more rules
				//check if we are above max agents
				
				if ((currAgent >= totalAgents) && (runIndex.equals("0")) ){	
					//println("Death count is now: " + deathCount);
					println("current number of agents is bigger than the max agents allowed");
					exitAndSave();
				
				} else if( currAgent < totalAgents ) {
					
					probStraight = probStraight + IG.rand(10, -10);
					createGeometry(probStraight, probLeft, probRight);
				
					
				}
		
				
			}

			if (IG.time() == runDuration - 20 ){		
				println("Ran out of time");
				exitAndSave();
				}
				
				if ((recreatedBool == true)){
					delay++;
					if (delay == 20){
						println("Got a glimpse of the regen geometry");
						exitAndSave();
					}
				}	
		}
		
		

		public void createGeometry(int _probStraight, int _probLeft, int _probRight) {

			// uniform / non uniform extrusion
			probStraight = _probStraight;
			probLeft = _probLeft;
			probRight = _probRight;
			if (uniformExtrusion == true) {
				offsetDepth2 = offsetDepth1;
				// //println("extrusion is uniform");
			} else {
				offsetDepth2 = offsetDepth1 * shadeVal;
				// //println("extrusion is non-uniform");
			}
	
			IVec mid = pt1.mid(pt2);
			IVec surfPt1 = surf.pt(mid.x, mid.y);
			IVec surfPt1d = surf.pt(mid.x, mid.y - offsetZ1 / 10, offsetDepth1);

			// endpoint of current agent
			IVec surfPt2 = surf.pt(pt2.x, pt2.y);
			IVec surfPt2d = surf.pt(pt2.x, pt2.y - offsetZ1 / 10, offsetDepth2);

			IVec dir = pt2.dif(pt1);

			if (IRandom.percent(probLeft)) { // bend usually 40
				cnt++;
				leftCnt++;
				// rotated endpoint of agent
				IVec nextDir = dir.dup().rot(IG.zaxis, genAngle);
				// midpoint of next agent
				IVec mid2 = pt2.cp(nextDir.dup().len(lineAgentLength / 2));
				// map point coordinates onto the surface
				IVec surfPt3 = surf.pt(mid2.x, mid2.y);
		
				IVec surfPt3d = surf.pt(mid2.x, mid2.y - offsetZ1, offsetDepth1);
				// draw surface
				IVec[][] cpts = new IVec[3][2];
				cpts[0][0] = surfPt1;
				cpts[0][1] = surfPt1d;
				cpts[1][0] = surfPt2;
				cpts[1][1] = surfPt2d;
				cpts[2][0] = surfPt3;
				cpts[2][1] = surfPt3d;
				String newID = "left_" + cnt;
				new ISurface(cpts, 2, 1).clr(0, 0, 255);
			
				new LineAgent(pt2, dir.dup().rot(IG.zaxis, genAngle), surf, 1, newID);
				// //println("Total left agents is: "+leftCnt);
			}
			if (IRandom.percent(probRight)) { // bend the other way usually 40
				cnt++;
				rightCnt++;

				IVec nextDir = dir.dup().rot(IG.zaxis, -genAngle);
				// midpoint of next agent
				IVec mid2 = pt2.cp(nextDir.dup().len(lineAgentLength / 2));
				IVec surfPt3 = surf.pt(mid2.x, mid2.y);
				IVec surfPt3d = surf.pt(mid2.x, mid2.y - offsetZ1, offsetDepth1);
				IVec[][] cpts = new IVec[3][2];
				cpts[0][0] = surfPt1;
				cpts[0][1] = surfPt1d;
				cpts[1][0] = surfPt2;
				cpts[1][1] = surfPt2d;
				cpts[2][0] = surfPt3;
				cpts[2][1] = surfPt3d;
				String newID = "right_" + cnt;
				new ISurface(cpts, 2, 1).clr(0, 255, 0);
				new LineAgent(pt2, dir.dup().rot(IG.zaxis, -genAngle), surf, 1, newID, offsetDepth1);
				// //println("Total right agents is: "+ rightCnt);
			}
			if (IRandom.percent(probStraight)) {
				cnt++;
				straightCnt++;

				IVec nextDir = dir;
				// midpoint of the next agent
				IVec mid2 = pt2.cp(nextDir.dup().len(lineAgentLength / 2));
				IVec surfPt3 = surf.pt(mid2.x, mid2.y);
				IVec surfPt3d = surf.pt(mid2.x, mid2.y - offsetZ1, offsetDepth1);
				IVec[][] cpts = new IVec[3][2];
				cpts[0][0] = surfPt1;
				cpts[0][1] = surfPt1d;
				cpts[1][0] = surfPt2;
				cpts[1][1] = surfPt2d;
				cpts[2][0] = surfPt3;
				cpts[2][1] = surfPt3d;

				String newID = "straight_" + cnt;
				new ISurface(cpts, 2, 1).clr(255, 255, 0);
				new LineAgent(pt2, dir.dup(), surf, 1, newID);
				// //println("Total straight agents is: "+cnt);
			}

		}
		// end create geometry
		
		
		public void reCreateGeometry(int _newProbability, double _newGenAngle) {// the
																				// death
																				// create
																				// geometry
			regenProb = _newProbability;
			genAngle = _newGenAngle;
			
			IVec mid = pt1.mid(pt2);
			IVec surfPt1 = surf.pt(mid.x, mid.y);
			IVec surfPt1d = surf.pt(mid.x, mid.y - offsetZ1, offsetDepth1);

			// endpoint of current agent
			IVec surfPt2 = surf.pt(pt2.x, pt2.y);
			IVec surfPt2d = surf.pt(pt2.x, pt2.y - offsetZ1, offsetDepth2);

			IVec dir = pt2.dif(pt1);

			if (IRandom.percent(probLeft + regenProb)) { // assign new
															// probability
				reCnt++;
				reLeftCnt++;

				IVec nextDir = dir.dup().rot(IG.zaxis, genAngle);

				// midpoinnt of next agent
				IVec mid2 = pt2.cp(nextDir.dup().len(lineAgentLength / 2));
				IVec surfPt3 = surf.pt(mid2.x, mid2.y);
				IVec surfPt3d = surf.pt(mid2.x, mid2.y - offsetZ1, offsetDepth1);

				IVec[][] cpts = new IVec[3][2];
				cpts[0][0] = surfPt1;
				cpts[0][1] = surfPt1d;
				cpts[1][0] = surfPt2;
				cpts[1][1] = surfPt2d;
				cpts[2][0] = surfPt3;
				cpts[2][1] = surfPt3d;

				String newID = "regeneratedLeft_" + cnt;
				new ISurface(cpts, 2, 1).clr(255, 0, 0);
				new LineAgent(pt2, dir.dup().rot(IG.zaxis, genAngle), surf, 1, newID, offsetDepth1);
				// //println("Total left agents is: "+leftCnt);
			}
			if (IRandom.percent(probRight + regenProb)) { // bend the other way
															// usually 40
				reCnt++;
				reRightCnt++;

				IVec nextDir = dir.dup().rot(IG.zaxis, -genAngle);
				// midpoint of next agent
				IVec mid2 = pt2.cp(nextDir.dup().len(lineAgentLength / 2));
				IVec surfPt3 = surf.pt(mid2.x, mid2.y);
				IVec surfPt3d = surf.pt(mid2.x, mid2.y - offsetZ1, offsetDepth1);

				IVec[][] cpts = new IVec[3][2];
				cpts[0][0] = surfPt1;
				cpts[0][1] = surfPt1d;
				cpts[1][0] = surfPt2;
				cpts[1][1] = surfPt2d;
				cpts[2][0] = surfPt3;
				cpts[2][1] = surfPt3d;

				String newID = "reGeneratedRight_" + cnt;
				new ISurface(cpts, 2, 1).clr(255, 0, 0);
				new LineAgent(pt2, dir.dup().rot(IG.zaxis, -genAngle), surf, 1, newID, offsetDepth1);
				// //println("Total right agents is: "+ rightCnt);
			}
			if (IRandom.percent(probStraight - regenProb)) {
				reCnt++;
				reStraightCnt++;

				IVec nextDir = dir;
				// midpoint of the next agent
				IVec mid2 = pt2.cp(nextDir.dup().len(lineAgentLength / 2));
				IVec surfPt3 = surf.pt(mid2.x, mid2.y);
				IVec surfPt3d = surf.pt(mid2.x, mid2.y - offsetZ1, offsetDepth1);

				IVec[][] cpts = new IVec[3][2];
				cpts[0][0] = surfPt1;
				cpts[0][1] = surfPt1d;
				cpts[1][0] = surfPt2;
				cpts[1][1] = surfPt2d;
				cpts[2][0] = surfPt3;
				cpts[2][1] = surfPt3d;

				String newID = "reGeneratedStraight_" + cnt;
				new ISurface(cpts, 2, 1).clr(255, 0, 0);
				new LineAgent(pt2, dir.dup(), surf, 1, newID);// //println("Total
																// straight
																// agents is:
																// "+leftCnt);
			}

		}// end reCreate Geometry

		

	}/// end of line agent/////////////////////////////////////////////////////

	public void draw()
	{
		/*
		println(IG.time());
		if( IG.time() ==  runDuration - 20)
				// save a frame/screenshot
		//saveFrame("C:\\Users\\sg1el\\Dropbox\\003_Agent_panel_system_dev\\java_igeo\\151107\\AgentPanel12k\\src\\data\\pic.png");
		// + "w1x+w1y+w1r+w2x+w2y+w2r_"+ ".png");
		
		{
			println("saved a frame");
			saveFrame("C:\\Users\\sg1el\\Pictures\\picture1.png");
		}*/
	}
	
	public void exitAndSave() {
		int y = 2015;
		//println("Saving geometry at: " + exportedGeoFileLoc + " and exiting applet.");
		IG.save(exportedGeoFileLoc);
		if(runIndex.equals("0") || runIndex.equals("1")){
			createXMLDoc(); // write an xml file that stores the data from the agents
			//println("Xml file with Agent Parameters was saved at: " + exportXmlFilePath);
		}	
		IG.stop(); // stop the IGEO server
		System.exit(1); // exit the system
		exit();
	}
	
	// get length of the agent
	public double getLength() {
		return panelAgentLength;
	}

	// get collision clearance
	public double getCollisionClearance() {
		return panelCollisionClearance;
	}

	// get agentID
	public int getAgentID() {
		return cnt;

	}

	// Create XML document to store the properties of the agent
	public void createXMLDoc() {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("AgentsList");
			doc.appendChild(rootElement);

			Element locationAndType;
			Element Window;
			Element isAlive;
			Element totalAgents;
			Element panelAgentLength;
			Element panelOffsetZ;
			Element uniformExtrusion;
			Element extrusion;
			Element shadeVal;
			Element panelCollisionClearance;
			Element genAngle;
			Element probRight;
			Element probLeft;
			Element probStraight;

			Element w1x;
			Element w1y;
			Element w1r;
			Element w2x;
			Element w2y;
			Element w2r;

			Attr attr;
			if (runIndex.equals("0")){
				locationAndType = doc.createElement("Agent");
				rootElement.appendChild(locationAndType);
	
				Window = doc.createElement("Window_1");
				rootElement.appendChild(Window);
	
				attr = doc.createAttribute("WLocation");
				attr.setValue(Double.toString(handler.getW1x()) + "," + Double.toString(handler.getW1y()) + ","
						+ Double.toString(handler.getW1r()));
				Window.setAttributeNode(attr);
	
				Window = doc.createElement("Window_2");
				rootElement.appendChild(Window);
	
				attr = doc.createAttribute("WLocation");
				attr.setValue(Double.toString(handler.getW2x()) + "," + Double.toString(handler.getW2y()) + ","
						+ Double.toString(handler.getW2r()));
				Window.setAttributeNode(attr);
				
			}else if (runIndex.equals("1") || runIndex.equals("2")){
				
				locationAndType = doc.createElement("Agent");
				rootElement.appendChild(locationAndType);
	
				Window = doc.createElement("Window_1");
				rootElement.appendChild(Window);
	
				reHandler = new XmlReconstructureHandlerReader( xml_filePath);
				String[] upDatedWindowParams = new String[2];
				upDatedWindowParams = reHandler.reconstructWindows();

				String[] paramsWin1 = upDatedWindowParams[0].split(",");
				double newW1x = Double.parseDouble (paramsWin1[0]);
				double newW1y = Double.parseDouble (paramsWin1[1]);
				double newW1r = Double.parseDouble (paramsWin1[2]);
				String[] paramsWin2 = upDatedWindowParams[1].split(",");
				double newW2x  = Double.parseDouble (paramsWin2[0]);
				double newW2y = Double.parseDouble (paramsWin2[1]);
				double newW2r = Double.parseDouble (paramsWin2[2]);
				
				
				attr = doc.createAttribute("WLocation");
				attr.setValue(Double.toString(newW1x) + "," + Double.toString(newW1y) + ","
						+ Double.toString(newW1r));
				Window.setAttributeNode(attr);
	
				Window = doc.createElement("Window_2");
				rootElement.appendChild(Window);
	
				attr = doc.createAttribute("WLocation");
				attr.setValue(Double.toString(newW2x) + "," + Double.toString(newW2y) + ","
						+ Double.toString(newW2r));
				Window.setAttributeNode(attr);
			
			}
			
			
			for (int i = 0; i < lineAgentList.size(); i++) {
				if ((new Integer(lineAgentList.get(i).lifeValue) == 1)) {
					if (runIndex.equals("0")){
						locationAndType = doc.createElement("Agent");
						rootElement.appendChild(locationAndType);
	
						attr = doc.createAttribute("Location");
						attr.setValue(lineAgentList.get(i).pt1.x + "," + lineAgentList.get(i).pt1.y + ","+lineAgentList.get(i).pt2.x + "," + lineAgentList.get(i).pt2.y + ","
								+ lineAgentList.get(i).typeNum);
						locationAndType.setAttributeNode(attr);
	
						isAlive = doc.createElement("isAlive");
						isAlive.appendChild(doc.createTextNode(new Integer(lineAgentList.get(i).lifeValue).toString()));
						locationAndType.appendChild(isAlive);
	
						genAngle = doc.createElement("temGenAngle");
						genAngle.appendChild(doc.createTextNode(handler.getGenAngle()));
						locationAndType.appendChild(genAngle);
	
						totalAgents = doc.createElement("totalAgents");
						totalAgents.appendChild(doc.createTextNode(Integer.toString(handler.getTotalAgents())));
						locationAndType.appendChild(totalAgents);
	
						panelAgentLength = doc.createElement("panelAgentLength");
						panelAgentLength.appendChild(doc.createTextNode(Double.toString(handler.getPanelAgentLength())));
						locationAndType.appendChild(panelAgentLength);
	
						panelOffsetZ = doc.createElement("panelOffsetZ");
						panelOffsetZ.appendChild(doc.createTextNode(Double.toString(handler.getPanelOffsetZ())));
						locationAndType.appendChild(panelOffsetZ);
	
						uniformExtrusion = doc.createElement("uniformExtrusion");
						uniformExtrusion.appendChild(doc.createTextNode(Boolean.toString(handler.getUniformExtrusion())));
						locationAndType.appendChild(uniformExtrusion);
	
						extrusion = doc.createElement("extrusion");
						extrusion.appendChild(doc.createTextNode(Double.toString(handler.getExtrusion())));
						locationAndType.appendChild(extrusion);
	
						shadeVal = doc.createElement("shadeVal");
						shadeVal.appendChild(doc.createTextNode(Double.toString(handler.getShadeVal())));
						locationAndType.appendChild(shadeVal);
	
						panelCollisionClearance = doc.createElement("panelCollisionClearance");
						panelCollisionClearance
								.appendChild(doc.createTextNode(Double.toString(handler.getPanelCollisionClearance())));
						locationAndType.appendChild(panelCollisionClearance);
	
						probRight = doc.createElement("probRight");
						probRight.appendChild(doc.createTextNode(Double.toString(handler.getProbRight())));
						locationAndType.appendChild(probRight);
	
						probLeft = doc.createElement("probLeft");
						probLeft.appendChild(doc.createTextNode(Double.toString(handler.getProbLeft())));
						locationAndType.appendChild(probLeft);
	
						probStraight = doc.createElement("probStraight");
						probStraight.appendChild(doc.createTextNode(Double.toString(handler.getProbStraight())));
						locationAndType.appendChild(probStraight);
					
					}else if((runIndex.equals("1"))){
						///maybe bad 					
						reHandler = new XmlReconstructureHandlerReader( xml_filePath);
						String[] updatedVariables = new String[11];	
						updatedVariables = reHandler.reInitiateVariables();
						//retrieve all the panel variables from the agents xml and initialize them 						
						int totalAgents1 = (int) Double.parseDouble (updatedVariables[0]);				
						double panelAgentLength1 =Double.parseDouble(updatedVariables[1]);					
						double panelOffsetZ1 = Double.parseDouble(updatedVariables[2]);					
						boolean uniformExtrusion1 = Boolean.parseBoolean(updatedVariables[3]);
						double extrusion1 = Double.parseDouble(updatedVariables[4]);					
						double shadeVal1 = Double.parseDouble(updatedVariables[5]);						
						double panelCollisionClearance1 = Double.parseDouble(updatedVariables[6]);					
						String genAngle1 = updatedVariables[7];	
						
						if( genAngle1.contains("1.57") == true )
						{
							genAngle1 = "\u03c0" + "/2";
						}
						
						int probRight1 = (int) Double.parseDouble(updatedVariables[8]);			
						int probLeft1 =(int) Double.parseDouble(updatedVariables[9]);		
						int probStraight1 =(int) Double.parseDouble(updatedVariables[10]);
						locationAndType = doc.createElement("Agent");
						rootElement.appendChild(locationAndType);
	
						attr = doc.createAttribute("Location");
						attr.setValue(lineAgentList.get(i).pt1.x + "," + lineAgentList.get(i).pt1.y + ","+lineAgentList.get(i).pt2.x + "," + lineAgentList.get(i).pt2.y + ","
								+ lineAgentList.get(i).typeNum);
						locationAndType.setAttributeNode(attr);
	
						isAlive = doc.createElement("isAlive");
						isAlive.appendChild(doc.createTextNode(new Integer(lineAgentList.get(i).lifeValue).toString()));
						locationAndType.appendChild(isAlive);
	
						genAngle = doc.createElement("temGenAngle");
						genAngle.appendChild(doc.createTextNode(genAngle1));
						locationAndType.appendChild(genAngle);
	
						totalAgents = doc.createElement("totalAgents");
						totalAgents.appendChild(doc.createTextNode(Integer.toString(totalAgents1)));
						locationAndType.appendChild(totalAgents);
	
						panelAgentLength = doc.createElement("panelAgentLength");
						panelAgentLength.appendChild(doc.createTextNode(Double.toString(panelAgentLength1)));
						locationAndType.appendChild(panelAgentLength);
	
						panelOffsetZ = doc.createElement("panelOffsetZ");
						panelOffsetZ.appendChild(doc.createTextNode(Double.toString(panelOffsetZ1 )));
						locationAndType.appendChild(panelOffsetZ);
	
						uniformExtrusion = doc.createElement("uniformExtrusion");
						uniformExtrusion.appendChild(doc.createTextNode(Boolean.toString(uniformExtrusion1)));
						locationAndType.appendChild(uniformExtrusion);
	
						extrusion = doc.createElement("extrusion");
						extrusion.appendChild(doc.createTextNode(Double.toString(extrusion1)));
						locationAndType.appendChild(extrusion);
	
						shadeVal = doc.createElement("shadeVal");
						shadeVal.appendChild(doc.createTextNode(Double.toString(shadeVal1)));
						locationAndType.appendChild(shadeVal);
	
						panelCollisionClearance = doc.createElement("panelCollisionClearance");
						panelCollisionClearance
								.appendChild(doc.createTextNode(Double.toString(panelCollisionClearance1 )));
						locationAndType.appendChild(panelCollisionClearance);
	
						probRight = doc.createElement("probRight");
						probRight.appendChild(doc.createTextNode(Double.toString(probRight1)));
						locationAndType.appendChild(probRight);
	
						probLeft = doc.createElement("probLeft");
						probLeft.appendChild(doc.createTextNode(Double.toString(probLeft1)));
						locationAndType.appendChild(probLeft);
	
						probStraight = doc.createElement("probStraight");
						probStraight.appendChild(doc.createTextNode(Double.toString(probStraight1)));
						locationAndType.appendChild(probStraight);
						
						
						
						
					}

				}

			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(exportXmlFilePath));
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void createErrorLogFile() {
		// date file with day and time stamp
		// place last n deleted agents with thier x and y coordinates
		try {

			File errFile = new File("C:\\Users\\bherr_000\\Desktop\\agent10Jar\\error.txt");
			FileOutputStream is = new FileOutputStream(errFile);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			w = new BufferedWriter(osw);

			// loop through the queue of deleted agents

		} catch (IOException e) {
			System.err.println("Problem writing to the file");
		}
	}

	// //////Main function//
	// //////////////////////////////////////////////////////////////

	public static void main(String _args[]) {

		//boolean runOnce=false;
		// prefix name for the generated geometry
		xml_filePath = _args[0];
		// Base geometry filepath
		exportXmlFilePath = _args[1];
		
		//imported geometry path
		geoFilePath = _args[2];
		// exported geometry path
		exportedGeoFileLoc = _args[3];
		
		// run index
		runIndex = _args[4];	
		////println("0".length());
		////println (_args[4].length());
		println("agent panel is running for the first time : " + _args[4].equals("0"));
		println ("xml path is:" + xml_filePath);
		// initiate variables passed from GUI to be used in the JAR
		if (runIndex.equals("0")){
			initiateVariables(xml_filePath);
		}else if (runIndex.equals("1")){
			updateVariables(xml_filePath);
		}

		PApplet.main(new String[] { agentPanel13m_JAR.AgentPanel13m_JAR.class.getName() });

		while (true) {
			if (IG.time() == runDuration - 10) {
				System.out.println("Could find valid solution based on given inputs.");
				System.exit(1);
			}

		}
	}

	// //////////////////////////////////////////////////////////////

}// ////////////// agentPanel12 Ends here/////////////////
