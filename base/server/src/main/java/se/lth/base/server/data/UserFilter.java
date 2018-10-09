package se.lth.base.server.data;

public class UserFilter {
	
	private final String username;
	private final String telephoneNom;
	private final int routeID;
	
	public UserFilter(String username, String telephoneNom, int routeID) {
		
		this.username = username;
		this.telephoneNom = telephoneNom;
		this.routeID = routeID;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getTelephoneNom() {
		return telephoneNom;
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
		if(username != null) {// could be a empty String
			return 1; 
		}
		else if (telephoneNom != null) {
			return 2;
		}
		else if(routeID != 0) {
			return 3;
		}
		return 0;
	}



}
