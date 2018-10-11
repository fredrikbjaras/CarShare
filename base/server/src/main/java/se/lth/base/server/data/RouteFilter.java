package se.lth.base.server.data;

import java.sql.Timestamp;

public class RouteFilter {
	
		private final String driverUserName;
		private final String location;
		private final String destination;
		private final String departureTime;
		private final String arrivalTime;
	
		public RouteFilter(String driverUserName, String location, String destination,String departureTime, String arrivalTime) {
			
			this.driverUserName = driverUserName;
			this.location = location;
			this.destination = destination;
			this.departureTime = departureTime;
			this.arrivalTime = arrivalTime;
		}
		
		public String getDriverUserName() {
			return driverUserName;
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
	 * 1 if driverID 
	 * 2 if origin
	 * 3 if destination
	 * 4 if departureTime
	 * 5 if arrivalTime
	 * 0 if nothing
	 */
	public int getFilter() {
		if(driverUserName != null && !driverUserName.equals("")) {// could be a empty String
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
