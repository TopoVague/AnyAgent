package agentPanel13m_JAR;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import igeo.IG;
import igeo.IPoint;
import igeo.ISurface;
import igeo.IVec;
import processing.core.PApplet;



public class exportPts extends PApplet {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String pointFilePath = "C:\\Users\\Evangelos\\Desktop\\151021_agentPanel_jar\\pointsOnSrf.txt";
	private ISurface surf;
	public void setup() {
		String geoFilePath = "C:\\Users\\Evangelos\\Desktop\\151021_agentPanel_jar\\base_geometry\\facade_pts01.3dm";

		IG.perspective();
		IG.bg(255, 255, 255);
		size(960, 720, IG.GL);
		IG.duration(200);	
		IG.open(geoFilePath);
		IG.focus();
		
		PrintWriter output1;
		
		surf = IG.surface(0).clr(48, 144, 255, 50);
		IPoint[] pts = IG.layer("Points").points();
		IPoint[] boundaryPts = IG.layer("boundary").points();
		
		IVec [] mappedPts =  new IVec[pts.length];
		for (int i=0; i< boundaryPts.length; i++ ){
			boundaryPts[i].hide();
			boundaryPts[i] = new IPoint (boundaryPts[i].x(), boundaryPts[i].y(), boundaryPts[i].z()).clr(0,0,i*10);
			
		}
		double distX = boundaryPts[0].dist(boundaryPts[1]);
		println ("distance in X is : " + distX) ;
		double distY = boundaryPts[1].dist(boundaryPts[3]);
		println ("distance in Y is : " + distY) ;
//		// map points to surface	
//		
		NumberFormat formater = new DecimalFormat ("#0.000");
		
		output1 = createWriter("C:\\Users\\Evangelos\\Desktop\\151021_agentPanel_jar\\configFile\\ptsPositionsNEW2.txt"); 
		for (int i=0; i< pts.length; i++ ){
			//println ("imported pts are: " + pts[i]);
			
			double coordX = pts[i].x()/distX;
			double coordY = pts[i].z()/distY;
			println ("coordY is :" + coordY );

			IVec point = new IVec (coordX,coordY,0);
			String sX = formater.format(coordX);
			String sY = formater.format(coordY);
			String sZ = formater.format(0);
			//output1.println (sX + "," + sZ + "," + sY);
			output1.println (coordX + "," + pts[i].y() + "," + coordY);
			//mappedPts[i] = surf.pt(point.x, point.y);
			//println ("mapped pts are: " + mappedPts[i]);
		}

		output1.flush();  // Writes the remaining data to the file
		output1.close();  // Finishes the file
		
		
		//exportPts(surf);
		//importPts(pointFilePath);
	
	}
	
	private void exportPts(final ISurface surf2) {
		ISurface baseSurface = surf2;
		IPoint[] pts = IG.layer("Points").points();	
		PrintWriter output;
		IVec [] mappedPts =  new IVec[pts.length];
		
		output = createWriter("C:\\Users\\Evangelos\\Desktop\\151021_agentPanel_jar\\configFile\\ptsPositionsNEW.txt"); 
		// map points to surface			
		for (int i=0; i< pts.length; i++ ){
			
			println ("imported pts are: " + pts[i]);
			IPoint yo= new IPoint (pts[i].x(),pts[i].y(),pts[i].z()).clr(255,0,0);
			IVec point = new IVec (yo.x(),yo.y(),yo.z());
			mappedPts[i] = baseSurface.pt(point.x, point.y);
			output.println(mappedPts[i]);
			pts[i].del();
		}

		output.flush();  // Writes the remaining data to the file
		output.close();  // Finishes the file
		
		
	}
	
	
	// import points for window position

	public void importPts(String _filepath){
		String data[] = loadStrings (_filepath) ; 
		for (int i=0; i< data.length; i++){
			//String[] temp= split(data[i],",");
			//double xPos= Double.parseDouble(temp[0]);
			//double yPos= Double.parseDouble(temp[1]);
			//double zPos= Double.parseDouble(temp[2]);
		}
	}
	


							
	
	
	
	
	
	
}
