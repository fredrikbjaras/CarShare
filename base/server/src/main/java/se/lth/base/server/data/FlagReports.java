package se.lth.base.server.data;

import java.lang.reflect.Array;

public class FlagReports{

	private int flagReportID;
	private int routeID;
	private int fromUserID;
	private int toUserID;
	private String comment;
	private Array flags;		//Need to test with this Array type, might not work as intended
	
    public FlagReports(int flagReportID, int routeID, int fromUserID, int toUserID, String comment, Array flags) {
    	this.flagReportID = flagReportID;
    	this.routeID = routeID;
    	this.fromUserID = fromUserID;
    	this.toUserID = toUserID;
    	this.comment = comment;
    	this.flags = flags;
    }

    public int getFlagReportID() {
    	return flagReportID;
    }
    
    public int getRouteID() {
    	return routeID;
    }
    
    public int getFromUserID() {
    	return fromUserID;
    }
    
    public int getToUserID() {
    	return toUserID;
    }
    
    public String getComment() {
    	return comment;
    }
    
    public Array getFlags() {
    	return flags;
    }
}