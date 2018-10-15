package se.lth.base.server.data;

public class UserFilter {
	
	private final String userName;
	private final String phoneNr;
	private final int routeID;
	
	public UserFilter(String username, String telephoneNom, int routeID) {
		
		this.userName = username;
		this.phoneNr = telephoneNom;
		this.routeID = routeID;
	}
	
	public String getUsername() {
		return userName;
	}
	
	public String getTelephoneNom() {
		return phoneNr;
	}

	public int getRouteID() {
		return routeID;
	}
	
		/*
	 * 1 if username 
	 * 2 if telephone number
	 * 3 if routeID
	 * 0 if nothing
	 */
	public int getFilter() {
		if(userName != null) {// could be a empty String
			return 1; 
		}
		else if (phoneNr != null) {
			return 2;
		}
		else if(routeID != -1) {
			return 3;
		}
		return 0;
	}



}
