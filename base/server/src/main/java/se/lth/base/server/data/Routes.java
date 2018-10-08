package se.lth.base.server.data;
import java.sql.Timestamp;

public class Routes {
	
	private int routeID;
	private int driverID;
	private int freeSeats;
	private String location;				//Need to test with this Array type, might not work as intended
	private String destination;			//See above
	private Timestamp timeOfDeparture;
	private Timestamp timeOfArrival;	private String passengers;			//See above	private String description;	private Timestamp bookingEndTime;	private int recurring;	private boolean finished;		//Recurring ENUM mapped to int.	//Description VARCHAR mapped to String.	//All TIMESTAMPS mapped to java.sql.Timestamp.	//All of this according to official H2 data types documentation	
    public Routes(int routeID, int driverID, int freeSeats, String location, String destination, Timestamp timeOfDeparture, Timestamp timeOfArrival, String passengers, String description, Timestamp bookingEndTime, int recurring, boolean finished) {
       this.routeID = routeID;       this.driverID = driverID;       this.freeSeats = freeSeats;       this.location = location;       this.destination = destination;       this.timeOfDeparture = timeOfDeparture;       this.timeOfArrival = timeOfArrival;       this.passengers = passengers;       this.description = description;       this.bookingEndTime = bookingEndTime;       this.recurring = recurring;       this.finished = finished;
    }

	public int getRouteID() {
		return routeID;
	}		public int getDriverID() {		return driverID;	}		public int getFreeSeats() {		return freeSeats;	}		public String getLocation() {		return location;	}		public String getDestination() {		return destination;	}		public Timestamp getTimeOfDeparture() {		return timeOfDeparture;	}		public Timestamp getTimeOfArrival() {		return timeOfArrival;	}		public String getPassengers() {		return passengers;	}		public String getDescription() {		return description;	}		public Timestamp getBookingEndTime() {		return bookingEndTime;	}			//Kanske borde heta isRecurring() istället?	public int getRecurring() {		return recurring;	}			// isFinished()?	public boolean getFinished() {		return finished;	}	

    //Methods
}