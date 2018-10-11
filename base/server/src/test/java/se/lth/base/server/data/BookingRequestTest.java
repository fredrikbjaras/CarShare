package se.lth.base.server.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class BookingRequestTest {
	
	BookingRequest request = new BookingRequest(1, 2, 3, 4, false, "My comment");

	@Test
	public void getBookingReqID() {
		assertEquals(1, request.getBookingReqID());
	}

	@Test
	public void getRouteID() {
		assertEquals(2, request.getRouteID());
	}
	
	@Test
	public void getFromUserID() {
		assertEquals(3, request.getFromUserID());
	}
	
	@Test
	public void getToUserID() {
		assertEquals(4, request.getToUserID());
	}
	
	@Test
	public void getAccepted() {
		assertFalse(request.getAccepted());
	}
	
	@Test
	public void getComment() {
		assertEquals("My comment", request.getComment() );
	}
	
	
}
