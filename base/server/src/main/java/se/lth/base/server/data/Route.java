package se.lth.base.server.data;
import java.sql.Timestamp;

public class Route {
	
	private int routeID;
	private int driverID;
	private int freeSeats;
	private String location;				
	private String destination;			
	private String timeOfDeparture;
	private String timeOfArrival;	private String passengers = "";		private String description;	private String bookingEndTime;	private int recurring;	private boolean finished;		//Recurring ENUM mapped to int.	//Description VARCHAR mapped to String.	//All TIMESTAMPS mapped to java.sql.Timestamp.	//All of this according to official H2 data types documentation	public Route(int routeID, int driverID, int freeSeats, String location, String destination, String timeOfDeparture, String timeOfArrival, String passengers, String description, String bookingEndTime, int recurring, boolean finished) {	       this.routeID = routeID;	       this.driverID = driverID;	       this.freeSeats = freeSeats;	       this.location = location;	       this.destination = destination;	       this.timeOfDeparture = timeOfDeparture;	       this.timeOfArrival = timeOfArrival;	       this.passengers = passengers;	       this.description = description;	       this.bookingEndTime = bookingEndTime;	       this.recurring = recurring;	       this.finished = finished;	    }	
    public Route(int routeID, int driverID, int freeSeats, String location, String destination, Timestamp timeOfDeparture, Timestamp timeOfArrival, String passengers, String description, Timestamp bookingEndTime, int recurring, boolean finished) {
       this.routeID = routeID;       this.driverID = driverID;       this.freeSeats = freeSeats;       this.location = location;       this.destination = destination;       this.timeOfDeparture = timeOfDeparture.toString();       this.timeOfArrival = timeOfArrival.toString();       this.passengers = passengers;       this.description = description;       this.bookingEndTime = bookingEndTime.toString();       this.recurring = recurring;       this.finished = finished;
    }

	public int getRouteID() {
		return routeID;
	}		public int getDriverID() {		return driverID;	}		public int getFreeSeats() {		return freeSeats;	}		public String getLocation() {		return location;	}		public String getDestination() {		return destination;	}		public Timestamp getTimeOfDeparture() {		return Timestamp.valueOf(timeOfDeparture);	}		public Timestamp getTimeOfArrival() {		return Timestamp.valueOf(timeOfArrival);	}		public String getPassengers() {		return passengers;	}		public String getDescription() {		return description;	}		@SuppressWarnings("deprecation")	public Timestamp getBookingEndTime() {		//Helt gaaaaalen fulfix, men tar för lång tid att fixa grundläggande problemet nu. Jag mår illa.		if(bookingEndTime.contains("-")) {			int uschÅr = Integer.parseInt(bookingEndTime.substring(0,4));			uschÅr = uschÅr - 1900;			int fyMånad = Integer.parseInt(bookingEndTime.substring(5, 7));			fyMånad = fyMånad - 1;			int faraoDag = Integer.parseInt(bookingEndTime.substring(8,10));			int vadTimmar = Integer.parseInt(bookingEndTime.substring(11, 13));			int fultMinuter = Integer.parseInt(bookingEndTime.substring(14, 16));			return new Timestamp(uschÅr, fyMånad, faraoDag, vadTimmar, fultMinuter, 0, 0);		}		Timestamp time = getTimeOfDeparture();		long temp = time.getTime() - (Long.parseLong(bookingEndTime) * 60 * 1000);		return new Timestamp(temp);	}			//Kanske borde heta isRecurring() istället?	public int getRecurring() {		return recurring;	}			// isFinished()?	public boolean getFinished() {		return finished;	}		public void changeFreeSeats(int number) {		freeSeats = number;	}

}