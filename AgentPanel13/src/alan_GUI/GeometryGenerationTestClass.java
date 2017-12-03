package alan_GUI;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;

import igeo.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.swing.JPanel;
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
public class GeometryGenerationTestClass extends PApplet implements GeoGenerationComponent {
	private static final long serialVersionUID = 1L;
	public static String runIndex;

	public static boolean recreatedBool = false;
	public static boolean hillClimbComplete = false;

	public static int PINT_cnt = 0;// counter for the agent ids had to be placed here
	public static int leftCnt = 0;
	public static int rightCnt = 0;
	public static int straightCnt = 0;
	public static int currAgent = 0;
	public static int failCounter = 0;
	public static int tripleFail = 0;
	public IVec[] failHolder = new IVec[3];
	public boolean failFlag;
	public boolean lifeFlag;
	public boolean changeExtrusion;

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
	public static String fileName = "";

	// location to retrieve input geometry for growing the panels
	public static String FILE_geoFilePath;
	// location to save the generated Geometry
	public static String DIR_exportedGeoFileLoc;
	// location to save the xml with the agents information, save geometry and
	// agents.xml in the same folder
	public static String exportXmlFilePath;
	// location to save a screenshot from each run
	public static String screenshotFileLocation;

	// variables for the PANEL Agent 1) totalAgents 2) panelAgentLength 3)
	// panelOffsetZ 4)panelCollisionClearance 5)uniformExtrusion 6)extrusion 7)
	// shadeVal 8 )genAngle 9) Type (left,right,straight)
	public static int totalAgents;
	public static double panelAgentLength;// length in u-v space, less than //
											// 1.0
	public static double panelCollisionClearance; // less than length
	public static double genAngle; // PI/2, PI/4 , PI/4 etc

	public static boolean uniformExtrusion;
	public static double extrusion;
	public static double shadeVal;
	public static double SLD_panelOffsetZ;

	// probabilty for each agent Type at each timestep
	static int probRight;
	static int probLeft;
	static int probStraight;

	public static boolean stuck = false;
	public static int CrashCounter = 0;

	// parameters for WINDOW Agents-Position
	public static double DBL_w1x;
	public static double w1y;
	public static double w1r;
	public static double w2x;
	public static double w2y;
	public static double w2r;
	public static double tempWindowFix = 0.5;
	
	// extra variable for window block agent
	// number of windows
	double windowWidth = 1.4;
	double windowDepth = 1;
	double windowHeight = 2;
	public static int delay = 0;

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
	private static Writer w;
	public static IVec temp;

	public void setup() {

		IG.perspective();
		IG.bg(255, 255, 255);
		size(1920, 1080, IG.GL);
		IG.duration(runDuration);
		IG.open(FILE_geoFilePath);
		IG.focus();
		ISurface surf = IG.surface(0).clr(48, 144, 255, 50);
		IPoint[] suppPoints = IG.layer("support_points").points();
		IPoint[] boundaryPoints = IG.layer("boundary").points();
		IVec[] supportVec = new IVec[10];
		IVec[] dirVec = new IVec[10];
		/// read point for window positioning
		// initial position change based on
		double initX;
		double initDirX;
		double initDirY;
		if ((DBL_w1x < 0.5) && (w1y < 0.5) && (w2y < 0.5)) {
			initX = (DBL_w1x + w2x) / 2;
			initDirX = initX;
			initDirY = 1;
		} else if ((w1y < 0.25) && (w2y > 0.25)) {
			initX = (DBL_w1x + w2x) / 2;
			initDirX = initX + w2x;
			initDirY = w2y;
		} else if ((w1y > 0.25) && (w2y < 0.25)) {
			initX = (DBL_w1x) / 2;
			initDirX = 1 + DBL_w1x;
			initDirY = w1y;
		} else if ((w1y > 0.25) && (w2y > 0.25) && (DBL_w1x < 0.25)) {
			initX = (DBL_w1x + w2x) / 2;
			initDirX = 1 + w2x;
			initDirY = 1;
		} else {
			initX = 0;
			initDirX = 1;
			initDirY = 1;
		}
		IVec initialAgentPos = new IVec(initX, 0, 0);
		IVec initialAgentDir = new IVec(initDirX, initDirY, 0);
		// map points to surface
		IVec surfWin1 = surf.pt(DBL_w1x, w1y);
		IVec surfWin2 = surf.pt(w2x, w2y);
		IVec surfWin3 = surf.pt(w2x + tempWindowFix, w2y);
	}

	// reconstruct geometry from agents.xml
	public void createGeometryFromXml(ISurface _surf) {
		ISurface s1 = _surf;
//
//		xmlAgent reGenAgent = reHandler.getAgent(i);
//		// //println("agent ID is : " + two.agentID);
//		double newOffsetDepth2 = reGenAgent.extrusion * reGenAgent.extrusionVal;
//
//		IVec mid = reGenAgent.xmlPt1.mid(reGenAgent.xmlPt2);
//		IPoint midPt = new IPoint(mid.x(), mid.y(), mid.z());
//		// IVec surfPt1 = new IVec (mid.x, mid.y,mid.z);
//		// IVec surfPt1d = s1.pt(mid.x,mid.y-reGenAgent.extrusion,
//		// reGenAgent.offsetZ);
//
//		// working version
//		IVec surfPt1 = reGenAgent.xmlPt1;
//		IVec surfPt1d = new IVec(surfPt1.x(), surfPt1.y() - reGenAgent.extrusion, surfPt1.z() - reGenAgent.offsetZ);
//		IPoint xmlPt1 = new IPoint(surfPt1.x(), surfPt1.y(), surfPt1.z()).clr(0, 255, 0);
//		IPoint xmlPt1d = new IPoint(surfPt1d.x(), surfPt1d.y(), surfPt1d.z()).clr(0, 0, 255);
//		IVec surfPt2 = reGenAgent.xmlPt2;
//		IVec surfPt2d = new IVec(reGenAgent.xmlPt2.x(), reGenAgent.xmlPt2.y() - reGenAgent.extrusion,
//				reGenAgent.xmlPt2.z() - reGenAgent.offsetZ);
//
//		IVec xmlMidPt = reGenAgent.xmlPt1.mid(reGenAgent.xmlPt2);
//		// IPoint midPoint = new IPoint(xmlMidPt .x(), xmlMidPt .y(),
//		// xmlMidPt .z()).clr(0,255,0);
//		IVec xmlMidPt3D = new IVec(xmlMidPt.x(), xmlMidPt.y() - newOffsetDepth2, xmlMidPt.z() - reGenAgent.offsetZ);
//		IVec surfPt3 = new IVec(xmlMidPt.x(), xmlMidPt.y(), xmlMidPt.z());
//		IVec surfPt3d = new IVec(xmlMidPt3D.x(), xmlMidPt3D.y(), xmlMidPt3D.z());
//		// IPoint nextPoint = new IPoint(surfPt3.x(), surfPt3.y(),
//		// surfPt3.z()).clr(255,0,0);
//		// IPoint xmlMidPt3d = new IPoint(surfPt3d.x(), surfPt3d.y(),
//		// surfPt3d.z()).clr(255,0,0);
//
//		IVec[][] cpts = new IVec[3][2];
//		cpts[0][0] = surfPt2;
//		cpts[0][1] = surfPt2d;
//		cpts[1][0] = surfPt3;
//		cpts[1][1] = surfPt3d;
//		cpts[2][0] = surfPt1;
//		cpts[2][1] = surfPt1d;
//		new ISurface(cpts, 2, 1).clr(255, 255, 0);		

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
		double offsetZ1 = SLD_panelOffsetZ;
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
			// dir.dup().rot(IG.zaxis, genAngle);
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
			if (life == 2) {
				extrusion = 0.001;
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
						IVec openingPt = surf.pt(opening.posW.x(), opening.posW.y());
						IPoint p1 = new IPoint(surfPt1.x(), surfPt1.y(), surfPt1.z()).clr(255, 0, 255);
						IPoint p2 = new IPoint(surfPt2.x(), surfPt2.y(), surfPt2.z()).clr(0, 255, 0);
						p1.hide();
						p2.hide();
						double dist1 = opening.posW.dist(p2);

						// this.offsetDepth2 = 1 - dist1 ;

						if (dist1 < opening.wWidth / 2 || dist1 < opening.wHeight / 2 || dist1 < opening.wDepth / 2) {
							isInBlockedArea = true;
							lifeValue = 0;
							println("agent " + this.agentID + "  was inside the window");// deletes
																							// agents
																							// inside
																							// the
																							// window
							if (lifeFlag == true) {
								CrashCounter++;
								// println("found one from triple");
								println("Current crashCount is " + CrashCounter);
								lifeFlag = false;
							}

							// case override here with 3extrusion
							if (CrashCounter > 2) {
								this.offsetDepth1 = 0.001;
								isInBlockedArea = false;
								lifeValue = 1;
								println("case Override");
								// }else{
								// println("case change extrusion");
								// this.offsetDepth1 =dist1*2;
							}
						}
						if (this.offsetDepth1 != 0.001) {
							// println ("got in");
							// this.offsetDepth1 = dist1;
							// this.offsetDepth1 = dist1 * 0.03;
							// isInBlockedArea = false;
							// lifeValue = 1;
						}

					} else if (agents.get(i) instanceof LineAgent) {
						LineAgent lineAgent = (LineAgent) agents.get(i);

						// checking clearance of end point
						if (lineAgent != this) {
							if (lineAgent.pt2.dist(pt2) < clearance) {
								// println("agent "+ this.agentID+ " didnt have
								// enough clearance");
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
				// //println("Agent with id" + agentID + " was outside bounding
				// box");
				isColliding = true;
				lifeValue = 0;
			}

			if (isColliding || lifeValue == 0 || isInBlockedArea) {

				if (failHolder[0] == failHolder[1] && failHolder[1] == failHolder[2]) {
					// println( "A Triple!");
					lifeFlag = true;
					failCounter = 0;
					failFlag = true;
				}

				if (failFlag == false) {
					deletedAgents.add(this);
					this.del();
					deathCount++;
					currAgent--;
					IVec surfPt1 = surf.pt(pt1.x, pt1.y);
					IVec surfPt2 = surf.pt(pt2.x, pt2.y);
					IPoint p1 = new IPoint(surfPt1.x(), surfPt1.y(), surfPt1.z()).clr(0, 255, 0);
					// IPoint p2 = new IPoint(surfPt2.x(), surfPt2.y(),
					// surfPt2.z()).clr(255, 0, 0);
					// println("Deleted agent at : "+ pt1);
				}

				if (failCounter > 2) {
					failHolder[0] = failHolder[1];
					failHolder[1] = failHolder[2];
					failCounter = 2;

				}
				failHolder[failCounter] = pt1;
				failCounter++;
				failFlag = false;

			} else if (time == 0) { // if not colliding needs more rules
				// check if we are above max agents

				if ((currAgent >= totalAgents) && (runIndex.equals("0"))) {
					// println("Death count is now: " + deathCount);
					println("current number of agents is bigger than the max agents allowed");
					exitAndSave();

				} else if (currAgent < totalAgents) {

					probStraight = probStraight + IG.rand(10, -10);
					createGeometry(probStraight, probLeft, probRight);

				}

			}

			if (IG.time() == runDuration - 20) {
				println("Ran out of time");
				exitAndSave();
			}

			if ((recreatedBool == true)) {
				delay++;
				if (delay == 20) {
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
				PINT_cnt++;
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
				String newID = "left_" + PINT_cnt;
				new ISurface(cpts, 2, 1).clr(0, 0, 255);

				new LineAgent(pt2, dir.dup().rot(IG.zaxis, genAngle), surf, 1, newID);
				// //println("Total left agents is: "+leftCnt);
			}
			if (IRandom.percent(probRight)) { // bend the other way usually 40
				PINT_cnt++;
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
				String newID = "right_" + PINT_cnt;
				new ISurface(cpts, 2, 1).clr(0, 255, 0);
				new LineAgent(pt2, dir.dup().rot(IG.zaxis, -genAngle), surf, 1, newID, offsetDepth1);
				// //println("Total right agents is: "+ rightCnt);
			}
			if (IRandom.percent(probStraight)) {
				PINT_cnt++;
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

				String newID = "straight_" + PINT_cnt;
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

				String newID = "regeneratedLeft_" + PINT_cnt;
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

				String newID = "reGeneratedRight_" + PINT_cnt;
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

				String newID = "reGeneratedStraight_" + PINT_cnt;
				new ISurface(cpts, 2, 1).clr(255, 0, 0);
				new LineAgent(pt2, dir.dup(), surf, 1, newID);// //println("Total
																// straight
																// agents is:
																// "+leftCnt);
			}

		}// end reCreate Geometry

	}/// end of line agent/////////////////////////////////////////////////////

	public void exitAndSave() {
		int y = 2015;
		// println("Saving geometry at: " + exportedGeoFileLoc + " and exiting
		// applet.");
		IG.save(DIR_exportedGeoFileLoc);
		if (runIndex.equals("0") || runIndex.equals("1")) {
			// createXMLDoc(); // write an xml file that stores the data from
			// the agents
			// println("Xml file with Agent Parameters was saved at: " +
			// exportXmlFilePath);
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
		return PINT_cnt;

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

		// boolean runOnce=false;
		// prefix name for the generated geometry
		xml_filePath = _args[0];
		// Base geometry filepath
		exportXmlFilePath = _args[1];

		// imported geometry path
		FILE_geoFilePath = _args[2];
		// exported geometry path
		DIR_exportedGeoFileLoc = _args[3];

		// run index
		runIndex = _args[4];
		//// println("0".length());
		//// println (_args[4].length());
		println("agent panel is running for the first time : " + _args[4].equals("0"));
		println("xml path is:" + xml_filePath);

		PApplet.main(new String[] { agentPanel13m_JAR.AgentPanel13m_JAR.class.getName() });

		while (true) {
			if (IG.time() == runDuration - 10) {
				System.out.println("Could find valid solution based on given inputs.");
				System.exit(1);
			}

		}
	}


	@Override
	public String SaveConfiguration(String filepath) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String Run(HashMap<String, NameToVariableClass> guiVariables) {
		// TODO Auto-generated method stub
		return null;
	}
}
