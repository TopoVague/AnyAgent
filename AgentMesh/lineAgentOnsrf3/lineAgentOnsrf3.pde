import processing.opengl.*;
import igeo.*;

void setup() {
  size(960, 720, IG.GL);
  IG.background(255);
  IG.duration(2000);
  IG.open("agentBoundarySpace2floorsfull.3dm");
  
  ISurface [] facadeSrfs = IG.surfaces();
  
   for(int j=0; j<facadeSrfs.length; j++){
     for (int i = 0; i < 20; i++){
           println("got in :step" +i);
          new LineAgent(new IVec(IRandom.get(),0,0),//random only in x
                            new IVec(0,0.09,0),//direction is y. length is less than 1.0 
                            facadeSrfs[j] ).clr(0);
           //srf.del();     
      }            
   }
}

static class LineAgent extends IAgent{
  static double length = 0.05; //length in u-v space, less than 1.0
  static double clearance = 0.0099; //less than length

  IVec pt1, pt2;
  boolean isColliding=false;
  ISurface surf;
  ISurface[] facadeSrfs;

  LineAgent(IVec pt, IVec dir, ISurface s){
    pt1 = pt;
    pt2 = pt.dup().add(dir.dup().len(length));
    surf = s;

  }

  void interact(IDynamics agent){
    if(time == 0){ //only in the first time
      if(agent instanceof LineAgent){
        LineAgent lineAgent = (LineAgent)agent;
        // checking clearance of end point
        if(lineAgent.pt2.dist(pt2) < clearance && lineAgent.surf == surf){
          isColliding=true;
        }
      }
    }
  }

  void update(){
    // is inside surface?
    if(pt2.x < 0.0||pt2.x > 1.0||pt2.y < 0.0||pt2.y > 1.0){
      isColliding = true;
    }
    
    if(isColliding){
      del();
    }
    else if(time == 0){ //if not colliding
      IVec surfPt1 = surf.pt(pt1.x, pt1.y);
      IVec surfPt2 = surf.pt(pt2.x, pt2.y);
      
      double offsetDepth = .1;
      IVec surfPt1d = surf.pt(pt1.x, pt1.y, offsetDepth);
      IVec surfPt2d = surf.pt(pt2.x, pt2.y, offsetDepth);
      new ISurface(surfPt1,surfPt2,surfPt2d,surfPt1d).clr(clr());

      // slightly chaning the gray color
      int gray = (clr().getRed()+clr().getGreen()+clr().getBlue())/3;
       gray += IRandom.getInt(-10, 10);

       IVec dir = pt2.dif(pt1); 
        if(IRandom.percent(50)){ //bend
            new LineAgent(pt2, dir.dup().rot(IG.zaxis,PI/10), surf).clr(255,0,0);
         }  
      if(IRandom.percent(50)){ //bend the other way
            new LineAgent(pt2, dir.dup().rot(IG.zaxis,-PI/10), surf).clr(0,255,0);
        }
      if(IRandom.percent(80)){ //straight
            new LineAgent(pt2, dir.dup(), surf).clr(gray);
         }  
      
    }
  }
}


