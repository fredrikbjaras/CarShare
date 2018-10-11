package se.lth.base.server.data;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class UserTest {
	
	File pic = new File("");
	User test = new User(1, "userName", "Password", "0700 000 000", false, "", "");

	@Test
	public void getID() {
		assertEquals(1, test.getUserID());
	}
	
	@Test
	public void getPassword() {
		assertEquals("Password", test.getPassword());
	}
	
	@Test
	public void getName() {
		assertEquals("userName", test.getName());
	}
	
	@Test
	public void getPhoneNumber() {
		assertEquals("0700 000 000", test.getPhoneNr());
	}
	
	@Test
	public void getIsAdmin() {
		assertFalse(test.getIsAdmin());
	}

	@Test
	public void getDescription() {
		assertEquals("", test.getDescription());
	}
	
	@Test
	public void getProfilePicture() {
		assertEquals("", test.getProfilePicture());
	}
	
}