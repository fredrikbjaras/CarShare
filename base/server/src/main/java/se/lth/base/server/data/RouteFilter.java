package se.lth.base.server.data;

import java.sql.Timestamp;

public class RouteFilter {
	
		private final int driverID;
		private final String location;
		private final String destination;
		private final Timestamp departureTime;
		private final Timestamp arrivalTime;
	
		public RouteFilter(int driverID, String location, String destination,String departureTime, String arrivalTime) {
			
			this.driverID = driverID;
			this.location = location;
			this.destination = destination;
			this.departureTime = Timestamp.valueOf(departureTime);
			this.arrivalTime = Timestamp.valueOf(arrivalTime);
		}
		
		public int getdriverID() {
			return driverID;
		}
		
		public String getLocation() {
			return location;
		}

		public String getDestination() {
			return destination;
		}
		
		public Timestamp getDepartureTime() {
			return departureTime;
		}
		public Timestamp getArrivalTime() {
			return arrivalTime;
		}

	/*
	 * 1 if driverID 
	 * 2 if origin
	 * 3 if destination
	 * 4 if departureTime
	 * 5 if arrivalTime
	 * 0 if nothing
	 */
	public int getFilter() {
		if(driverID != 0) {// could be a empty String
			return 1; 
		}
		else if (location != null) {
			return 2;
		}
		else if(destination != null) {
			return 3;
		} else if (departureTime != null) {
			return 4;
		} else if (arrivalTime != null) {
			return 5;
		}
		return 0;
	}

	

}
