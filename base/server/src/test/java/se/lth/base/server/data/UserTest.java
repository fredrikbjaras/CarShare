package se.lth.base.server.data;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class UserTest {
	
	File pic = new File("");
	User test = new User(1, "name", "Password", 00, false, "hello", pic);

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
		assertEquals("name", test.getName());
	}
	
	@Test
	public void getPhoneNumber() {
		assertEquals(00, test.getPhoneNr());
	}
	
	@Test
	public void getIsAdmin() {
		assertFalse(test.getIsAdmin());
	}

	@Test
	public void getDescription() {
		assertEquals("hello", test.getDescription());
	}
	
	@Test
	public void getProfilePicture() {
		assertEquals(pic, test.getProfilePicture());
	}
	
	@Test
	public void getRole() {
		assertEquals(Role.USER, test.getRole());
	}
}