package se.lth.base.server.data;

import java.sql.Timestamp;

public class RouteFilter {
	
		private final int driverID;
		private final String orgin;
		private final String destination;
		private final Timestamp departureTime;
		private final Timestamp arrivalTime;
	
		public RouteFilter(int driverID, String orgin, String destination,String departureTime, String arrivalTime) {
			
			this.driverID = driverID;
			this.orgin = orgin;
			this.destination = destination;
			this.departureTime = Timestamp.valueOf(departureTime);
			this.arrivalTime = Timestamp.valueOf(arrivalTime);
		}
		
		public int getdriverID() {
			return driverID;
		}
		
		public String getOrgin() {
			return orgin;
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


	

}
