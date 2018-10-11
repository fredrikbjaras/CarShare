package se.lth.base.server.data;

public class BookingRequestFilter {
	
		private final int routeID;
		private final int fromUserID;
		private final int toUserID;
		private final boolean accepted;
	
		public BookingRequestFilter(int routeID, int fromUserID, int toUserID, boolean accepted) {
			
			this.routeID = routeID;
			this.fromUserID = fromUserID;
			this.toUserID = toUserID;
			this.accepted = accepted;
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
		public boolean getAccepted() {
			return accepted;
		}

	/*
	 * 1 if routeID
	 * 2 if fromUser
	 * 3 if toUser
	 * 0 if nothing /// unable to search by accepted
	 */
	public int getFilter() {
		if (routeID != 0) {
			return 1;
		}
		else if(fromUserID != 0) {
			return 2;
		} 
		else if (toUserID != 0) {
			return 3;
		}
		return 0;
	}
}
