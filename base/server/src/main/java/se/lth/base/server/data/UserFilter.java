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


}
