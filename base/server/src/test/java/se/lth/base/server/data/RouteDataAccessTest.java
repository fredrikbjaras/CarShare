package se.lth.base.server.data;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

import se.lth.base.server.Config;

public class RouteDataAccessTest {
	
	RouteDataAccess routeDao = new RouteDataAccess(Config.instance().getDatabaseDriver());
	UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());
	@Test
	public void addRoute() {
		
		User test = userDao.addUser("userName", "password", "0700 000 000", false);
		Route route = routeDao.addRoute(test.getUserID(), 1000, "Here", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
	    assertTrue(routeDao.getRoute(route.getRouteID()).getDriverID() == test.getUserID());
	
	}
	@Test
	public void updateRoute() {
		
		User test = userDao.addUser("userName", "password", "0700 000 000", false);
		Route route1 = routeDao.addRoute(test.getUserID(), 1000, "Here", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		Route route2 = routeDao.updateRoute(route1.getRouteID(), route1.getDriverID(), 1001, "Here", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		assertTrue(route2.getFreeSeats() == 1001);
	}
	
	@Test
	public void deleteRoute() {
		User test = userDao.addUser("userName", "password", "0700 000 000", false);
		Route route1 = routeDao.addRoute(test.getUserID(), 1000, "Here", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		assertTrue(routeDao.deleteRoute(route1.getRouteID()));
	
	}
	}

