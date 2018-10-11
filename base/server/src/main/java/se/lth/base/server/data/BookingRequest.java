package se.lth.base.server.data;

public class BookingRequest {

	private int bookingReqID;
	private int routeID;
	private int fromUserID;
	private int toUserID;
	private boolean accepted;
	private String comment;			//<---- Heter reason i SQL-tabellen because SQL-syntax stuff
	
    public BookingRequest(int bookingReqID, int routeID, int fromUserID, int toUserID, boolean accepted, String comment) {
    this.bookingReqID = bookingReqID;
    this.routeID = routeID;
    this.fromUserID = fromUserID;
    this.toUserID = toUserID;
    this.accepted = accepted;
    this.comment = comment;
    }
    
    public int getBookingReqID() {
    	return bookingReqID;
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
    
    // Borde kanske vara isAccepted()?
    public boolean getAccepted() {
    	return accepted;
    }
    
    public String getComment() {
    	return comment;
    }
}
