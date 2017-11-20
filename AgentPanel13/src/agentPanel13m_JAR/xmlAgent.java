package agentPanel13m_JAR;

import igeo.ISurface;
import igeo.IVec;

public class xmlAgent{
	IVec xmlPt1;
	IVec xmlPt2; 
	ISurface surf; 
	int lifeValue; 
	String agentID;
	double extrusion;
	double extrusionVal;
	double offsetZ;
	double xmlAgentLength ;
	double xmlGenAngle ;


	public xmlAgent(IVec pt, IVec dir, ISurface s, int life, String id, double _extrusion, double _extrusionVal, double _offsetZ , double _xmlAgentLength, double _genAngle)
    {// add in life and agent id
       xmlPt1 = pt;
       xmlPt2 = dir;
       xmlGenAngle = _genAngle;
       xmlAgentLength = _xmlAgentLength;
       surf = s;
       lifeValue = life;
       agentID = id;
       extrusion = _extrusion ;
       extrusionVal = _extrusionVal; 
       offsetZ = _offsetZ; 

    }
}