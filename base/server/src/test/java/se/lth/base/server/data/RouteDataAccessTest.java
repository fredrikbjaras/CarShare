package se.lth.base.server.data;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.lth.base.server.Config;
import se.lth.base.server.database.CreateSchema;

public class RouteDataAccessTest {
	RouteDataAccess routeDao = new RouteDataAccess(Config.instance().getDatabaseDriver());
	UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());
	
	
	// Reset database to avoid issues with existing users
	@Before
	public void setUp() {
		CreateSchema reset = new CreateSchema(Config.instance().getDatabaseDriver());
		reset.dropAll();
		reset.createSchema();
	}
	
	@Test
	public void addRoute() {
		User test = userDao.addUser("userName14", "password", "0700 000 000", false, "", "");
		Route route = routeDao.addRoute(test.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
	    assertTrue(routeDao.getRoute(route.getRouteID()).getDriverID() == test.getUserID());
	
	}
	@Test
	public void updateRoute() {
		User test = userDao.addUser("userName1", "password", "0700 000 000", false, "", "");
		Route route1 = routeDao.addRoute(test.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		Route route2 = routeDao.updateRoute(route1.getRouteID(), route1.getDriverID(), 1001, "Here", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		assertTrue(route2.getFreeSeats() == 1001);
	}
	
	@Test
	public void deleteRoute() {
		User test = userDao.addUser("userName2", "password", "0700 000 000", false, "", "");
		Route route1 = routeDao.addRoute(test.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		assertTrue(routeDao.deleteRoute(route1.getRouteID()));
	
	}
	
	@Test
	public void getRoute() {
		User test = userDao.addUser("userName3", "password", "0700 000 000", false, "", "");
		Route route1 = routeDao.addRoute(test.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		assertTrue(routeDao.getRoute(route1.getRouteID()).getDriverID() == test.getUserID());
	}
	
	@Test
	public void getAllRoutes() {
		User test1 = userDao.addUser("userName4", "password", "0700 000 000", false, "", "");
		User test2 = userDao.addUser("userName5", "password", "0700 000 000", false, "", "");
		routeDao.addRoute(test1.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		routeDao.addRoute(test2.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		List<Route> routes = routeDao.getAllRoutes();
		assertTrue(routes.size() > 0);
	}
	
	@Test
	public void getUsersByRouteId() {
		User test1 = userDao.addUser("userName6", "password", "0700 000 000", false, "", "");
		User test2 = userDao.addUser("userName7", "password", "0700 000 000", false, "", "");
		Route route = routeDao.addRoute(test1.getUserID(), 2, "", "",  new Timestamp(1),  new Timestamp(2), "", "",  new Timestamp(3), 0, false);
		routeDao.addPassengerToRoute(route.getRouteID(), test2.getUserID());
		List<User> usersInRoute = routeDao.getUsersByRouteId(route.getRouteID());
		int counter = 0;
		for (User user : usersInRoute) {
			if(user.getUserID() == test1.getUserID() || user.getUserID() == test2.getUserID()) {
				counter++;
			}
		}
		assertTrue(counter == 2);
	}
	
	@Test
	public void getAllRoutesFromUser() {
		User test1 = userDao.addUser("userName8", "password", "0700 000 000", false, "", "");
		Route route1 = routeDao.addRoute(test1.getUserID(), 2, "", "",  new Timestamp(1),  new Timestamp(2), "", "",  new Timestamp(3), 0, false);
		Route route2 = routeDao.addRoute(test1.getUserID(), 2, "", "",  new Timestamp(10),  new Timestamp(20), "", "",  new Timestamp(30), 0, false);
		List<Route> routes = routeDao.getAllRoutesFromUser(test1.getUserID());
		int counter = 0;
		for (Route route : routes) {
			if(route.getRouteID() == route1.getRouteID() || route.getRouteID() == route2.getRouteID()) {
				counter++;
			}
		}
		assertTrue(counter == 2);
	}
	
	@Test
	public void getAllRoutesFromLocation() {
		User test1= userDao.addUser("userName9", "password", "0700 000 000", false, "", "");
		Route route1 = routeDao.addRoute(test1.getUserID(), 2, "Lund", "",  new Timestamp(1),  new Timestamp(2), "", "",  new Timestamp(3), 0, false);
		Route route2 = routeDao.addRoute(test1.getUserID(), 2, "Lund", "",  new Timestamp(10),  new Timestamp(20), "", "",  new Timestamp(30), 0, false);
		Route route3 = routeDao.addRoute(test1.getUserID(), 2, "Lund", "",  new Timestamp(11),  new Timestamp(21), "", "",  new Timestamp(31), 0, false);
		List<Route> routes = routeDao.getAllRoutesFromLocation("Lund");
		int counter = 0;
		for (Route route : routes) {
			if (route.getLocation().compareTo("Lund") == 0) {
				counter++;
			}
		}
		
		assertEquals(3, counter);
	}
	@Test
	public void getAllRoutesFromDestination() {
		User test1= userDao.addUser("userName10", "password", "0700 000 000", false, "", "");
		Route route1 = routeDao.addRoute(test1.getUserID(), 2, "", "Lund",  new Timestamp(1),  new Timestamp(2), "", "",  new Timestamp(3), 0, false);
		Route route2 = routeDao.addRoute(test1.getUserID(), 2, "", "Lund",  new Timestamp(10),  new Timestamp(20), "", "",  new Timestamp(30), 0, false);
		Route route3 = routeDao.addRoute(test1.getUserID(), 2, "", "Lund",  new Timestamp(11),  new Timestamp(21), "", "",  new Timestamp(31), 0, false);
		List<Route> routes = routeDao.getAllRoutesFromDestination("Lund");
		int counter = 0;
		for (Route route : routes) {
			if (route.getDestination().compareTo("Lund") == 0) {
				counter++;
			}
		}
		
		assertEquals(3, counter);
	}
	
	@Test
	public void getAllRoutesFromDepartureTime() {
		User test1= userDao.addUser("userName11", "password", "0700 000 000", false, "", "");
		
		Timestamp time = new Timestamp(100);
		Timestamp tempDepTime = time;
		tempDepTime.setTime(tempDepTime.getTime()+3600*1000);
		Route route1 = routeDao.addRoute(test1.getUserID(), 2, "", "",  time,  new Timestamp(2), "", "",  new Timestamp(3), 0, false);
		Route route2 = routeDao.addRoute(test1.getUserID(), 2, "", "",  time,  new Timestamp(20), "", "",  new Timestamp(30), 0, false);
		Route route3 = routeDao.addRoute(test1.getUserID(), 2, "", "",  time,  new Timestamp(21), "", "",  new Timestamp(31), 0, false);
		List<Route> routes = routeDao.getAllRoutesFromDepartureTime(time, tempDepTime);
		int counter = 0;
		for (Route route : routes) {
			if (route.getTimeOfDeparture().compareTo(time) == 0) {
				counter++;
			}
		}
		
		assertEquals(3, counter);
	}
	@Test
	public void getAllRoutesFromArrivalTime() {
		
		User test1= userDao.addUser("userName12", "password", "0700 000 000", false, "", "");
	
		Timestamp time = new Timestamp(110);
		Timestamp tempDepTime = time;
		tempDepTime.setTime(tempDepTime.getTime()+3600*1000);
		Route route1 = routeDao.addRoute(test1.getUserID(), 2, "", "", new Timestamp(1) ,  time, "", "",  new Timestamp(3), 0, false);
		Route route2 = routeDao.addRoute(test1.getUserID(), 2, "", "",  new Timestamp(1),  time, "", "",  new Timestamp(30), 0, false);
		Route route3 = routeDao.addRoute(test1.getUserID(), 2, "", "",  new Timestamp(1),  time, "", "",  new Timestamp(31), 0, false);
		List<Route> routes = routeDao.getAllRoutesFromArrivalTime(time, tempDepTime);
		int counter = 0;
		for (Route route : routes) {
			if (route.getTimeOfArrival().compareTo(time) == 0) {
				counter++;
			}
		}
		
		assertEquals(3, counter);
	}
	
	@Test
	public void addPassengerToRoute() {
		User test1= userDao.addUser("userName13", "password", "0700 000 000", false, "", "");
		User test2 = userDao.addUser("userName15", "password", "0700 000 000", false, "", "");
		Route route = routeDao.addRoute(test2.getUserID(), 2, "", "", new Timestamp(1), new Timestamp(2), "", "", new Timestamp(5), 0, false);
		assertTrue(routeDao.addPassengerToRoute(route.getRouteID(), test2.getUserID()));
	}
	
	}
	
	
	

