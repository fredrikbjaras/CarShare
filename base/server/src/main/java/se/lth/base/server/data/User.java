package se.lth.base.server.data;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import se.lth.base.server.data.Role;
public class User implements Principal {

    public static User NONE = new User(-1, "NONEACCOUNT", "password","123456789",false);

    private final int userID;
    private final String userName;
    private final String password;
    private final String phoneNr;
    private File profilePicture;
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
    public User(int userID, String userName, String password,String phoneNr,boolean isAdmin,String description,File profilePicture) {
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
    	if (role == Role.ADMIN) return true;
    	else return false;
    }
    public String getDescription(){
    	return description;
    }
    public File getProfilePicture() {
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
