package se.lth.base.server.data;

import java.security.Principal;


import se.lth.base.server.data.Role;
public class User implements Principal {

    public static User NONE = new User(-1, "NONEACCOUNT", "password","123456789",false);

    private final int userID;
    private final String userName;
    private final String password;
    private final String phoneNr;
    private String profilePicture;
    private String description = "";
    private final Role role;
    //add more declarations
    
    public User(int userID, String userName, String password,String phoneNr,boolean isAdmin) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.phoneNr = phoneNr;
        if(isAdmin)
        	role = Role.ADMIN;
        else
        	role = Role.USER;
    }
    public User(int userID, String userName, String password, String phoneNr, boolean isAdmin, String description, String profilePicture) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.phoneNr = phoneNr;
        if(isAdmin)
        	role = Role.ADMIN;
        else
        	role = Role.USER;
        this.description = description;
        this.profilePicture = profilePicture;

    }

    public int getUserID() {
        return userID;
    }
    
    public String getPassword() {
    		return password;
    }
    
    public String getUserName() {
    		return userName;
    }
    
    public String getPhoneNr() {
    		return phoneNr;
    }
    
    public boolean getIsAdmin() {
    		if (role == Role.ADMIN) {
    			return true;
    		} else {
    			return false;
    		}
    }
    
    public String getDescription(){
    		return description;
    }
    
    public String getProfilePicture() {
    		return profilePicture;
    }
    
    public Role getRole() {
    		return role;
    }
	
    public String getName() {
		return userName;
	}
	
	public boolean validPassword() {
        return this.password.length() >= 8;
    }

    public boolean hasPassword() {
        return password != null;
    }
}
