package se.lth.base.server.data;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.lth.base.server.Config;
import se.lth.base.server.database.CreateSchema;

public class BookingRequestDataAccessTest {
	RouteDataAccess routeDao = new RouteDataAccess(Config.instance().getDatabaseDriver());
	UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());
	BookingRequestDataAccess bookDao = new BookingRequestDataAccess(Config.instance().getDatabaseDriver());

	@Before
	public void setUp() {
		CreateSchema reset = new CreateSchema(Config.instance().getDatabaseDriver());
		reset.dropAll();
		reset.createSchema();
	}
	
	@Test
	public void addBookingRequest() {
		User test1 = userDao.addUser("userName16", "password", "0700 000 000", false, "", "");
		User test2= userDao.addUser("userName17", "password", "0700 000 000", false, "", "");
		Route route = routeDao.addRoute(test1.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		BookingRequest request = bookDao.addBookingRequests(route.getRouteID(), test2.getUserID(), test1.getUserID(), true);
		List<BookingRequest> requests = bookDao.getAllBookingRequests();
		assertTrue(requests.stream().anyMatch(b -> b.getRouteID() == route.getRouteID() && b.getFromUserID() == test2.getUserID() && b.getToUserID() == test1.getUserID()
				&& b.getAccepted() == true));
        
	}
	
	@Test
	public void updateBookingRequest() {
		User test1 = userDao.addUser("userName18", "password", "0700 000 000", false, "", "");
		User test2= userDao.addUser("userName19", "password", "0700 000 000", false, "", "");
		Route route = routeDao.addRoute(test1.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		BookingRequest request = bookDao.addBookingRequests(route.getRouteID(), test2.getUserID(), test1.getUserID(), false);
		bookDao.updateBookingRequests(request.getBookingReqID(), route.getRouteID(), test2.getUserID(), test1.getUserID(), true);
		request = bookDao.getBookingRequests(request.getBookingReqID());
		assertTrue(request.getAccepted());
	}
	
	@Test
	public void getBookingRequest() {
		User test1 = userDao.addUser("userName20", "password", "0700 000 000", false, "", "");
		User test2= userDao.addUser("userName21", "password", "0700 000 000", false, "", "");
		Route route = routeDao.addRoute(test1.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		BookingRequest request = bookDao.addBookingRequests(route.getRouteID(), test2.getUserID(), test1.getUserID(), false);
		int id = request.getBookingReqID();
		BookingRequest serverRequest = bookDao.getBookingRequests(id);
		assertTrue(request.getRouteID() == serverRequest.getRouteID() && request.getFromUserID() == serverRequest.getFromUserID());
	}
	
	@Test
	public void deleteBookingRequest() {
		
		User test1 = userDao.addUser("userName22", "password", "0700 000 000", false, "", "");
		User test2= userDao.addUser("userName23", "password", "0700 000 000", false,"" ,"");
		Route route = routeDao.addRoute(test1.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		BookingRequest request = bookDao.addBookingRequests(route.getRouteID(), test2.getUserID(), test1.getUserID(), false);
		assertTrue(bookDao.deleteBookingRequests(request.getBookingReqID()));
	}
	
	@Test
	public void getAllBookingRequests() {
		User test1 = userDao.addUser("userName24", "password", "0700 000 000", false, "", "");
		User test2= userDao.addUser("userName25", "password", "0700 000 000", false, "", "");
		User test3 = userDao.addUser("userName26", "password", "0700 000 000", false, "", "");
		User test4= userDao.addUser("userName27", "password", "0700 000 000", false, "", "");
		Route route1 = routeDao.addRoute(test1.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		Route route2 = routeDao.addRoute(test2.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		BookingRequest request1 = bookDao.addBookingRequests(route1.getRouteID(), test2.getUserID(), test1.getUserID(), false);
		BookingRequest request2 = bookDao.addBookingRequests(route2.getRouteID(), test4.getUserID(), test3.getUserID(), false);
		List<BookingRequest> requests = bookDao.getAllBookingRequests();
		int counter = 0;
		for (BookingRequest request : requests) {
			if (request.getBookingReqID() == request1.getBookingReqID() || request.getBookingReqID() == request2.getBookingReqID()) {
				counter++;
			}

		}	
		assertEquals(2, counter);
	}
	
	@Test
	public void getBookingRequestsFromUser() {
		User test1 = userDao.addUser("userName27", "password", "0700 000 000", false, "", "");
		User test2= userDao.addUser("userName28", "password", "0700 000 000", false, "", "");
		User test3 = userDao.addUser("userName29", "password", "0700 000 000", false, "" ,"");
		Route route1 = routeDao.addRoute(test1.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		Route route2 = routeDao.addRoute(test2.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);
		BookingRequest request1 = bookDao.addBookingRequests(route1.getRouteID(), test3.getUserID(), test1.getUserID(), false);
		BookingRequest request2 = bookDao.addBookingRequests(route2.getRouteID(), test3.getUserID(), test2.getUserID(), false);
		List<BookingRequest> requests = bookDao.getBookingRequestsFromUser(test3.getUserID());
		int counter = 0;
		for (BookingRequest request : requests) {
			if (request.getFromUserID() == test3.getUserID()) {
				counter++;
			}

		}	
		assertEquals(2, counter);
	}
	
	@Test
	public void getBookingRequestsToUser() {
		User test1 = userDao.addUser("userName30", "password", "0700 000 000", false, "" ,"");
		User test2= userDao.addUser("userName31", "password", "0700 000 000", false, "", "");
		User test3 = userDao.addUser("userName32", "password", "0700 000 000", false, "" ,"");
		Route route1 = routeDao.addRoute(test1.getUserID(), 1000, "", "There", new Timestamp(1),new Timestamp(2), "", "Ride", new Timestamp(3), 0, false);

		BookingRequest request1 = bookDao.addBookingRequests(route1.getRouteID(), test2.getUserID(), test1.getUserID(), false);
		BookingRequest request2 = bookDao.addBookingRequests(route1.getRouteID(), test3.getUserID(), test1.getUserID(), false);
		List<BookingRequest> requests = bookDao.getBookingRequestsToUser(test1.getUserID());
		int counter = 0;
		for (BookingRequest request : requests) {
			if (request.getToUserID() == test1.getUserID()) {
				counter++;
			}

		}	
		assertEquals(2, counter);
	}

}
