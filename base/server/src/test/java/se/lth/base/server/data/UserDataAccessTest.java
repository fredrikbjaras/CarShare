package se.lth.base.server.data;

import org.junit.Test;
import se.lth.base.server.Config;
import se.lth.base.server.database.BaseDataAccessTest;
import se.lth.base.server.database.DataAccess;
import se.lth.base.server.database.DataAccessException;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Rasmus Ros, rasmus.ros@cs.lth.se
 */
public class UserDataAccessTest extends BaseDataAccessTest {

    private final UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());

    @Test
    public void addNewUser() {
        User test = userDao.addUser("userName", "password", "0700 000 000", false);
        long salt = new DataAccess<>(userDao.getDriverUrl(), (rs) -> rs.getLong(1))
				.queryFirst("SELECT salt FROM user WHERE username = ?", "userName");
        String passwordHash = test.generatePasswordHash(salt).toString();
        List<User> users = userDao.getUsers();
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("userName") && u.getPassword().equals(passwordHash) && u.getPhoneNr().equals("0700 000 000") && u.getIsAdmin() == false));
        System.out.println(passwordHash + users.get(2).getPassword());
        
    }
    	@Test
		public void updateUser() {
			File pic = new File("");
			User test1 = userDao.addUser("userName", "password", "000", false);
			int id = test1.getUserID();
			test1 = userDao.updateUser(id, "userNamee", "password", pic, "hej");
			String passwordHash = test1.getPassword();
			List<User> users = userDao.getUsers();
			User user = users.get(2);
			System.out.println(user.getUserID() + user.getDescription() + user.getUserName() + user.getIsAdmin() + user.getPassword());
			System.out.println(passwordHash);
			assertTrue(users.stream().anyMatch(u -> u.getUserID() == id && u.getUserName().compareTo("userNamee") == 0
			&& u.getPassword().equals(passwordHash) &&  u.getIsAdmin() == false));
	        //u.getPassword().compareTo(passwordHash) == 0 s(passwordHash)
	        //&& u.getDescription().compareTo("") == 0
		}
		@Test
		public void getUser() {
			User test = userDao.addUser("userName", "password", "0700 000 000", false);
			int id = test.getUserID();
			User test2 = userDao.getUser(id);
			
			assertTrue(test.getUserName().compareTo(test2.getUserName()) == 0);
		
		}
		
		@Test
		public void getUserWithName() {
			User test1 = userDao.addUser("userName", "password", "0700 000 000", false);
			String name = test1.getName();
			User test2 = userDao.getUserWithName(name);
			assertTrue(test1.getUserName().compareTo(test2.getUserName()) == 0);
		
		}
		
		@Test
		public void deleteUser() {
			User test1 = userDao.addUser("userName", "password", "0700 000 000", false);
			assertTrue(userDao.deleteUser(test1.getUserID()));
			
			

}
		@Test
		public void getUserList() {
			User test1 = userDao.addUser("userName1", "password1", "00", false);
			User test2 = userDao.addUser("userName2", "password2", "01", false);
			User test3 = userDao.addUser("userName3", "password3", "02", false);
			List<User> users = userDao.getUsers();
			System.out.println(users.size());
			assertTrue(users.size() > 0);
}
			
			
			public void getUsersByRouteId() {
				User test1 = userDao.addUser("userName", "password", "0700 000 000", false);
				User test2 = userDao.addUser("userNamee", "password", "0700 000 001", false);
				RouteDataAccess routeDao = new RouteDataAccess(Config.instance().getDatabaseDriver());
				
			
			}
			
			
			
}
//    @Test(expected = DataAccessException.class)
//    public void addDuplicatedUser() {
//        userDao.addUser(new Credentials("Gandalf", "mellon", Role.USER));
//        userDao.addUser(new Credentials("Gandalf", "vapenation", Role.USER));
//    }
//
//    @Test(expected = DataAccessException.class)
//    public void addShortUser() {
//        userDao.addUser(new Credentials("Gry", "no", Role.USER));
//    }
//
//    @Test
//    public void getUsersContainsAdmin() {
//        assertTrue(userDao.getUsers().stream().anyMatch(u -> u.getRole().equals(Role.ADMIN)));
//    }
//
//    @Test
//    public void removeNoUser() {
//        assertFalse(userDao.deleteUser(-1));
//    }
//
//    @Test
//    public void removeUser() {
//        User user = userDao.addUser(new Credentials("Sven", "a", Role.ADMIN));
//        assertTrue(userDao.getUsers().stream().anyMatch(u -> u.getName().equals("Sven")));
//        userDao.deleteUser(user.getId());
//        assertTrue(userDao.getUsers().stream().noneMatch(u -> u.getName().equals("Sven")));
//    }
//
//    @Test(expected = DataAccessException.class)
//    public void authenticateNoUser() {
//        userDao.authenticate(new Credentials("Waldo", "?", Role.NONE));
//    }
//
//    @Test
//    public void authenticateNewUser() {
//        userDao.addUser(new Credentials("Pelle", "!2", Role.USER));
//        Session pellesSession = userDao.authenticate(new Credentials("Pelle", "!2", Role.NONE));
//        assertEquals("Pelle", pellesSession.getUser().getName());
//        assertNotNull(pellesSession.getSessionId());
//    }
//
//    @Test
//    public void authenticateNewUserTwice() {
//        userDao.addUser(new Credentials("Elin", "password", Role.USER));
//
//        Session authenticated = userDao.authenticate(new Credentials("Elin", "password", Role.NONE));
//        assertNotNull(authenticated);
//        assertEquals("Elin", authenticated.getUser().getName());
//
//        Session authenticatedAgain = userDao.authenticate(new Credentials("Elin", "password", Role.NONE));
//        assertNotEquals(authenticated.getSessionId(), authenticatedAgain.getSessionId());
//    }
//
//    @Test
//    public void removeNoSession() {
//        assertFalse(userDao.removeSession(UUID.randomUUID()));
//    }
//
//    @Test
//    public void removeSession() {
//        userDao.addUser(new Credentials("MormorElsa", "kanelbulle", Role.USER));
//        Session session = userDao.authenticate(new Credentials("MormorElsa", "kanelbulle", Role.NONE));
//        assertTrue(userDao.removeSession(session.getSessionId()));
//        assertFalse(userDao.removeSession(session.getSessionId()));
//    }
//
//    @Test(expected = DataAccessException.class)
//    public void failedAuthenticate() {
//        userDao.addUser(new Credentials("steffe", "kittylover1996!", Role.USER));
//        userDao.authenticate(new Credentials("steffe", "cantrememberwhatitwas! nooo!", Role.NONE));
//    }
//
//    @Test
//    public void checkSession() {
//        userDao.addUser(new Credentials("uffe", "genius programmer", Role.ADMIN));
//        Session session = userDao.authenticate(new Credentials("uffe", "genius programmer", Role.NONE));
//        Session checked = userDao.getSession(session.getSessionId());
//        assertEquals("uffe", checked.getUser().getName());
//        assertEquals(session.getSessionId(), checked.getSessionId());
//    }
//
//    @Test
//    public void checkRemovedSession() {
//        userDao.addUser(new Credentials("lisa", "y", Role.ADMIN));
//        Session session = userDao.authenticate(new Credentials("lisa", "y", Role.NONE));
//        Session checked = userDao.getSession(session.getSessionId());
//        assertEquals(session.getSessionId(), checked.getSessionId());
//        userDao.removeSession(checked.getSessionId());
//        try {
//            userDao.getSession(checked.getSessionId());
//            fail("Should not validate removed session");
//        } catch (DataAccessException ignored) {
//        }
//    }
//
//    @Test
//    public void getUser() {
//        User user = userDao.getUser(1);
//        assertEquals(1, user.getId());
//    }
//
//    @Test(expected = DataAccessException.class)
//    public void getMissingUser() {
//        userDao.getUser(-1);
//    }
//
//    @Test(expected = DataAccessException.class)
//    public void updateMissingUser() {
//        userDao.updateUser(10, new Credentials("admin", "password", Role.ADMIN));
//    }
//
//    @Test
//    public void updateUser() {
//        User user = userDao.updateUser(2, new Credentials("test2", "newpass", Role.USER));
//        assertEquals(2, user.getId());
//        assertEquals("test2", user.getName());
//        assertEquals(Role.USER, user.getRole());
//    }
//
//    @Test
//    public void updateWithoutPassword() {
//        Session session1 = userDao.authenticate(new Credentials("Test", "password", Role.USER));
//        userDao.updateUser(2, new Credentials("test2", null, Role.USER));
//        Session session2 = userDao.authenticate(new Credentials("test2", "password", Role.USER));
//        System.out.println(session1);
//        System.out.println(session2);
//    }
//}
