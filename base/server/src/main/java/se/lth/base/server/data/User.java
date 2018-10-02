package se.lth.base.server.data;

import java.awt.Image;

import java.security.Principal;


public class User implements Principal {

    public static User NONE = new User(0, Role.NONE, "-");

    private final int id;
    private final Role role;
    private final String username;
    private Image profilePicture;
    private int phoneNr;
    private String description;
	private Boolean isAdmin;


    public User(int id,Role role, String userName) {
        this.id = id;
        this.role = role;
        this.username = userName;
        if(role == Role.ADMIN ) {
        	isAdmin = true;
        }
        else {
        	isAdmin = false;
        }
   }

    public Role getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return username;
    }
    
    public Image getProfilePicture() {
    	return profilePicture;
    }
    
    public void setProfilePicture(Image im) {
    	profilePicture = im;
    }
    
    
    public int getPhoneNr() {
    	return phoneNr;
    }
    
    public void setPhoneNr(int phoneNr) {
    	this.phoneNr = phoneNr;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String de) {
    	description = de;
    }
    
    public boolean getIsAdmin() {
    	return isAdmin;
    }
    
}
