package se.lth.base.server.data;
import java.sql.Timestamp;

public class Routes {
	
	private int routeID;
	private int driverID;
	private int freeSeats;
	private String location;				//Need to test with this Array type, might not work as intended
	private String destination;			//See above
	private Timestamp timeOfDeparture;
	private Timestamp timeOfArrival;
    public Routes(int routeID, int driverID, int freeSeats, String location, String destination, Timestamp timeOfDeparture, Timestamp timeOfArrival, String passengers, String description, Timestamp bookingEndTime, int recurring, boolean finished) {
       this.routeID = routeID;
    }

	public int getRouteID() {
		return routeID;
	}

    //Methods
}