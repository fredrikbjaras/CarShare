package se.lth.base.server.data;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

public class RouteTest {
	
	Route route = new Route(1, 2, 3, "Här", "Där", new Timestamp(1), new Timestamp(2), "passengers", "description", new Timestamp(3), 0, false) ;
	
	@Test
	public void getRouteId() {
		assertEquals(1, route.getRouteID());
	}
	@Test
	public void getDriverId() {
		assertEquals(2, route.getDriverID());
	}
	@Test
	public void getFreeSeats() {
		assertEquals(3, route.getFreeSeats());
	}
	@Test
	public void getLocation() {
		assertEquals("Här", route.getLocation());
	}
	@Test
	public void getDestination() {
		assertEquals("Där", route.getDestination());
	}
	@Test
	public void getDepartureTime() {
		
		assertEquals(new Timestamp(1).toString(), route.getTimeOfDeparture().toString());
	}
	@Test
	public void getArrivalTime() {
		
		assertEquals(new Timestamp(2).toString(), route.getTimeOfArrival().toString());
	}
	@Test
	public void getPassengers() {
		
		assertEquals("passengers", route.getPassengers());
	}
	@Test
	public void getDescription() {
		
		assertEquals("description", route.getDescription());
	}
	
	@Test
	public void getBookingEndTime() {
		assertTrue(new Timestamp(3).toString().equals(route.getBookingEndTime().toString()));
	}
	
	@Test
	public void getRecurring() {
		
		assertEquals(0, route.getRecurring());
	}
	
	@Test
	public void getFinished() {
		
		assertEquals(false, route.getFinished());
	}
	
	@Test
	public void changeFreeSeats(){
		route.changeFreeSeats(4);
		assertEquals(4, route.getFreeSeats());
	}
	


}
