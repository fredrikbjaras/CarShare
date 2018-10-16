package se.lth.base.server.data;

import java.sql.Timestamp;

public class RouteFilter {
	
		private final String driverUserName;
		private final String passengerUserName;
		private final String location;
		private final String destination;
		private final String departureTime;
		private final String arrivalTime;
	
		public RouteFilter(String driverUserName, String location, String destination,String timeOfDeparture, String timeOfArrival, String passengerUserName) {
			this.driverUserName = driverUserName;
			this.passengerUserName = passengerUserName;
			this.location = location;
			this.destination = destination;
			this.departureTime = timeOfDeparture;
			this.arrivalTime = timeOfArrival;
		}
		
		public String getDriverUserName() {
			return driverUserName;
		}
	
		public String getPassengerUserName() {
			return passengerUserName;
		}
		
		public String getLocation() {
			return location;
		}

		public String getDestination() {
			return destination;
		}
		
		public Timestamp getDepartureTime() {
			return Timestamp.valueOf(departureTime);
		}
		public Timestamp getArrivalTime() {
			return Timestamp.valueOf(arrivalTime);
		}

	/*
	 * 1 if driverID (driverUserName)
	 * 2 if passengerUserName 
	 * 3 if origin
	 * 4 if destination
	 * 5 if departureTime
	 * 6 if arrivalTime
	 * 0 if nothing
	 */
	public int getFilter() {
		if(driverUserName != null && !driverUserName.equals("")) {// could be a empty String
			return 1; 
		} else if (passengerUserName != null && !passengerUserName.equals("")) {
			return 2;
		} else if (location != null) {
			return 3;
		} else if(destination != null) {
			return 4;
		} else if (departureTime != null) {
			return 5;
		} else if (arrivalTime != null) {
			return 6;
		}
		return 0;
	}

	

}
