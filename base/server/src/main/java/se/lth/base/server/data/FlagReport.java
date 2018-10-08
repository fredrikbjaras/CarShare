package se.lth.base.server.data;

public class FlagReport{

	private int flagReportID;
	private int routeID;
	private int fromUserID;
	private int toUserID;
	private String comment;
	private String flags;		
	
    public FlagReport(int flagReportID, int routeID, int fromUserID, int toUserID, String comment, String flags) {
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
    
    public String getFlags() {
    	return flags;
    }
}