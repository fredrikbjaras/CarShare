package se.lth.base.server.rest;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.lth.base.server.Config;
import se.lth.base.server.data.BookingRequest;
import se.lth.base.server.data.BookingRequestDataAccess;
import se.lth.base.server.data.BookingRequestFilter;
import se.lth.base.server.data.Route;
import se.lth.base.server.data.RouteDataAccess;
import se.lth.base.server.data.Session;
import se.lth.base.server.data.User;
import se.lth.base.server.data.UserDataAccess;
@Path("booking-request")
public class BookingRequestResource {
	private final ContainerRequestContext context;
	private final User user;
	// private final Route route;
	private final Session session;
	private final UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());
	private final RouteDataAccess routeDao = new RouteDataAccess(Config.instance().getDatabaseDriver());
	private final BookingRequestDataAccess bookDao = new BookingRequestDataAccess(
			Config.instance().getDatabaseDriver());
	
	public BookingRequestResource(@Context ContainerRequestContext context) {
		this.context = context;
		this.user = (User) context.getProperty(User.class.getSimpleName());
		// this.route = (Routes) context.getProperty(User.class.getSimpleName());
		this.session = (Session) context.getProperty(Session.class.getSimpleName());
	}
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public boolean addRequest(BookingRequest request) { 
		Route route = routeDao.getRoute(request.getRouteID());
		String[] passengers = route.getPassengers().split(";");
		User fromUser = userDao.getUser(request.getFromUserID());
		User toUser = userDao.getUser(request.getToUserID());
		
		for(String passenger : passengers) {
			if(fromUser.getUserID() == Integer.parseInt(passenger)) {
				throw new WebApplicationException("This user is already on the specified route. ",
						Response.Status.BAD_REQUEST);
			}
		}
		
		Timestamp current = new Timestamp(System.currentTimeMillis());
		if(current.after(route.getBookingEndTime())) {
			throw new WebApplicationException("You cannot make a booking request this close to departure. ",
					Response.Status.BAD_REQUEST);
		}
		
		List<BookingRequest> requestsMade = bookDao.getBookingRequestsFromUser(fromUser.getUserID());
		for(BookingRequest br : requestsMade) {
			if(br.getRouteID() == route.getRouteID()) {
				throw new WebApplicationException("This user has already sent a booking request for this route. ",
						Response.Status.BAD_REQUEST);
			}
		}
		
		
		//success
		bookDao.addBookingRequests(route.getRouteID(), fromUser.getUserID(),
				toUser.getUserID(), request.getAccepted());
		return true;
	}
	@Path("{BookingRequestID}")
	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public BookingRequest getRequest(@PathParam("BookingRequestID") int requestID) {
		if (bookDao.getBookingRequests(requestID) == null) {
			throw new WebApplicationException("Booking request not found", Response.Status.BAD_REQUEST);
		} else {
			BookingRequest request = bookDao.getBookingRequests(requestID);
			if (user.getIsAdmin() || request.getFromUserID() == user.getUserID()
					|| request.getToUserID() == user.getUserID()) {
				return request;
			} else {
				throw new WebApplicationException("You don't have perssmision to see this Booking Request",
						Response.Status.BAD_REQUEST);
			}
		}
	}
	
	@Path("filter")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public List<BookingRequest> getRequests(BookingRequestFilter filter) {
		if (user.getIsAdmin()) {
			switch (filter.getFilter()) {
			case 1:
				return bookDao.getBookingRequestsByRoute(filter.getRouteID());
			case 2:
				return bookDao.getBookingRequestsFromUser(filter.getFromUserID());
			case 3:
				return bookDao.getBookingRequestsToUser(filter.getToUserID());
			default:
				return bookDao.getAllBookingRequests();
			}
		} else {
			switch (filter.getFilter()) {
			case 1:
				if (routeDao.getRoute(filter.getRouteID()).getDriverID() == user.getUserID()) {
					return bookDao.getBookingRequestsByRoute(filter.getRouteID());
				} else {
					throw new WebApplicationException("You are not the Driver of this Route",
							Response.Status.BAD_REQUEST);
				}
			case 2:
				if (filter.getFromUserID() == user.getUserID()) {
					return bookDao.getBookingRequestsFromUser(filter.getFromUserID());
				} else {
					throw new WebApplicationException("You are not the User whom these BookingRequest was sent from",
							Response.Status.BAD_REQUEST);
				}
			case 3:
				if (filter.getToUserID() == user.getUserID()) {
					return bookDao.getBookingRequestsToUser(filter.getToUserID());
				} 
				else {
					throw new WebApplicationException("You are not the User whom these BookingRequest was sent to",
							Response.Status.BAD_REQUEST);
				}
			default:
				throw new WebApplicationException("You don't have permission to see these requests",
						Response.Status.BAD_REQUEST);

			}
		}
	}
	
	/*
	 * Det enda man kan ändra är ens kommentar. 
	 * Ska någon av de andra parametrarna ändras, görs det via delete och sedan ny request. 
	 * Accepted sköts via acceptRequest. (ńamn?)
	 */
	
	@Path("{RequestID}")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public boolean putRequest(BookingRequest requestUpdate) { 
		BookingRequest oldRequest= bookDao.getBookingRequests(requestUpdate.getBookingReqID());
		if(user.getUserID() != oldRequest.getFromUserID() && !user.getIsAdmin()) {
			throw new WebApplicationException("Unable to edit others' requests. ", Response.Status.BAD_REQUEST);
		}
		bookDao.updateBookingRequests(oldRequest.getBookingReqID(), oldRequest.getRouteID(), 
				oldRequest.getFromUserID(), oldRequest.getToUserID(), oldRequest.getAccepted());
		return true; 
	}
	
	@Path("{RequestID}")
	@DELETE
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public boolean deleteRequest(@PathParam("RequestID") int requestID) {
		
		
		if (bookDao.getBookingRequests(requestID) == null) {
			if (user.getIsAdmin()) {
				throw new WebApplicationException("Request not found", Response.Status.BAD_REQUEST);
			}
			throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
		}
		if (bookDao.getBookingRequests(requestID).getFromUserID() == user.getUserID() || user.getIsAdmin()) {
			bookDao.deleteBookingRequests(requestID);
			return true;
		}
		throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
	}
	
	@Path("{RequestID}")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public boolean acceptRequest(@PathParam("RequestID") int requestID) {
		BookingRequest br = bookDao.getBookingRequests(requestID);
		
		if (br == null) {
			throw new WebApplicationException("Request not found", Response.Status.BAD_REQUEST);
		}
		if (br.getAccepted()) {
			throw new WebApplicationException("User already accepted", Response.Status.BAD_REQUEST);
		}
		
		if (bookDao.getBookingRequests(requestID).getFromUserID() == user.getUserID() || user.getIsAdmin()) {
			//bookDao.updateBookingRequests(br.getBookingReqID(), br.getRouteID(), br.getFromUserID(), br.getToUserID(),
				//	true, br.getComment());
			if(routeDao.addPassengerToRoute(br.getRouteID(), br.getFromUserID())) {
				return true;
			}
			throw new WebApplicationException("Failed to add passenger", Response.Status.BAD_REQUEST);
		}
		throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
	}
}



