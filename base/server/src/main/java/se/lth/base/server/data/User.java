package se.lth.base.server.data;

import java.io.File;
import java.security.Principal;
import se.lth.base.server.data.Role;
public class User implements Principal {

    public static User NONE = new User(0, "NONEACCOUNT", "password",123456789,false);

    private final int userID;
    private final String userName;
    private final String password;
    private final int phoneNr;
    private File profilePicture;
    private String description = "";
    private final boolean isAdmin;
    //add more declarations
    
    public User(int userID, String userName, String password,int phoneNr,boolean isAdmin) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.phoneNr = phoneNr;
        this.isAdmin = isAdmin;
    }
    public User(int userID, String userName, String password,int phoneNr,boolean isAdmin,String description,File profilePicture) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.phoneNr = phoneNr;
        this.isAdmin = isAdmin;
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
    public int getPhoneNr() {
    	return phoneNr;
    }
    public boolean getIsAdmin() {
    	return isAdmin;
    }
    public String getDescription(){
    	return description;
    }
    public File getProfilePicture() {
    	return profilePicture;
    }
    public Role getRole() {
    	if(isAdmin) {
    	return Role.ADMIN;}
    	else {
    		return Role.USER;
    	}
    }
}
