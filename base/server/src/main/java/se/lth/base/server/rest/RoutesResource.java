package se.lth.base.server.rest;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.lth.base.server.Config;
import se.lth.base.server.data.BookingRequestDataAccess;
import se.lth.base.server.data.Route;
import se.lth.base.server.data.RouteDataAccess;
import se.lth.base.server.data.RouteFilter;
import se.lth.base.server.data.Session;
import se.lth.base.server.data.User;
import se.lth.base.server.data.UserDataAccess;

@Path("route")
public class RoutesResource {

	private final ContainerRequestContext context;
	private final User user;
	// private final Route route;
	private final Session session;
	private final UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());
	private final RouteDataAccess routeDao = new RouteDataAccess(Config.instance().getDatabaseDriver());
	private final BookingRequestDataAccess bookDao = new BookingRequestDataAccess(
			Config.instance().getDatabaseDriver());

	public RoutesResource(@Context ContainerRequestContext context) {
		this.context = context;
		this.user = (User) context.getProperty(User.class.getSimpleName());
		// this.route = (Routes) context.getProperty(User.class.getSimpleName());
		this.session = (Session) context.getProperty(Session.class.getSimpleName());
	}

	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public boolean addRoute(int routeID, int driverID, int freeSeats, String location, String destination,
			String timeOfDeparture, String timeOfArrival, String passengers, String description, String bookingEndTime,
			int recurring, boolean finished) {

		Timestamp timeStampOfDeparture = Timestamp.valueOf(timeOfDeparture);
		Timestamp timeStampOfArrival = Timestamp.valueOf(timeOfArrival);
		Timestamp timeStampBookingEndTime = Timestamp.valueOf(bookingEndTime);

		if (user.getIsAdmin() || user.getUserID() == driverID) {

			List<Route> tempList = routeDao.getAllRoutesFromUser(driverID);
			for (int i = 0; i < tempList.size(); i++) {
				// checks if the driver already has a route on that specific time.
				if ((tempList.get(i).getTimeOfDeparture().before(timeStampOfDeparture)
						&& tempList.get(i).getTimeOfDeparture().after(timeStampOfArrival))
						|| (tempList.get(i).getTimeOfArrival().after(timeStampOfDeparture)
								&& tempList.get(i).getTimeOfDeparture().before(timeStampOfDeparture))) {

					throw new WebApplicationException("This user already has a route during the specified timeframe",
							Response.Status.BAD_REQUEST);
				}

			}
			routeDao.addRoutes(driverID, freeSeats, location, destination, timeStampOfDeparture, timeStampOfArrival, passengers,
					description, timeStampBookingEndTime, recurring, finished);
			return true;
		}
		throw new WebApplicationException("You can't create a route where someone else is the driver",
				Response.Status.BAD_REQUEST);
	}

	@Path("{RouteID}")
	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Route getRoute(@PathParam("RouteID") int routeID) {
		if (routeDao.getRoute(routeID) == null) {
			if (user.getIsAdmin()) {
				throw new WebApplicationException("Requirements met but route not found", Response.Status.BAD_REQUEST);
			}
			throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
		}
		//What is the difference between a user call and an admin call?
		return routeDao.getRoute(routeID);

	}
	
	@Path("filter")
	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public List<Route> getRoutes(RouteFilter filter) {
		if (user.getIsAdmin()) {

			switch (filter.getFilter()) {

				case 1:
					return routeDao.getAllRoutesFromUser(filter.getdriverID());

				case 2:
					return routeDao.getAllRoutesFromLocation(filter.getLocation());

				case 3:
					return routeDao.getAllRoutesFromDestination(filter.getDestination());
			
				case 4:
					return routeDao.getAllRoutesFromDepartureTime(filter.getDepartureTime());
				
				case 5:
					return routeDao.getAllRoutesFromArrivalTime(filter.getArrivalTime());
				
				default:
					return routeDao.getAllRoutes();
				//	throw new WebApplicationException("Something is wonky with the filter parameters", Response.Status.BAD_REQUEST);

			}
		} else {
			
			switch (filter.getFilter()) {

			case 1: //Man ska väl inte kunna söka efter en annan driver?
				if (user.getUserID() == filter.getdriverID() ) {
					return routeDao.getAllRoutesFromUser(filter.getdriverID());
				} else {
					throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
				}

			case 2:
				return routeDao.getAllRoutesFromLocation(filter.getLocation());

			case 3:
				return routeDao.getAllRoutesFromDestination(filter.getDestination());
		
			case 4:
				return routeDao.getAllRoutesFromDepartureTime(filter.getDepartureTime());
			
			case 5:
				return routeDao.getAllRoutesFromArrivalTime(filter.getArrivalTime());
			
			default:
				throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);

		}

	}

	}
	
	@Path("{RouteID}")
	@PUT
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public boolean putRoute(Route routeUpdate) {
		Route oldRoute = routeDao.getRoute(routeUpdate.getRouteID());
		int driverID;
		int freeSeats;
		String location;
		String destination;
		Timestamp timeOfDeparture;
		Timestamp timeOfArrival;
		String passengers;
		String description;
		Timestamp bookingEndTime;
		int recurring;
		//Vi säger att finished inte kan vara en optional
		if (routeUpdate.getDriverID() == -1) {
			driverID = oldRoute.getDriverID();
		} else {
			driverID = routeUpdate.getDriverID();
		}
		if (routeUpdate.getFreeSeats() == -1) {
			freeSeats = oldRoute.getFreeSeats();
		} else {
			freeSeats = routeUpdate.getFreeSeats();
		}
		if (routeUpdate.getLocation() == null) {
			location = oldRoute.getLocation();
		} else {
			location = routeUpdate.getLocation();
		}
		if (routeUpdate.getDestination() == null) {
			destination = oldRoute.getDestination();
		} else {
			destination = routeUpdate.getDestination();
		}
		if (routeUpdate.getTimeOfDeparture() == null) {
			timeOfDeparture = oldRoute.getTimeOfDeparture();
		} else {
			timeOfDeparture = routeUpdate.getTimeOfDeparture();
		}
		if (routeUpdate.getTimeOfArrival() == null) {
			timeOfArrival = oldRoute.getTimeOfArrival();
		} else {
			timeOfArrival = routeUpdate.getTimeOfArrival();
		}
		if (routeUpdate.getPassengers() == null) {
			passengers = oldRoute.getPassengers();
		} else {
			passengers = routeUpdate.getPassengers();
		}
		if (routeUpdate.getDescription() == null) {
			description = oldRoute.getDescription();
		} else {
			description = routeUpdate.getDescription();
		}
		if (routeUpdate.getBookingEndTime() == null) {
			bookingEndTime = oldRoute.getBookingEndTime();
		} else {
			bookingEndTime = routeUpdate.getBookingEndTime();
		}
		if (routeUpdate.getRecurring() == -1) {
			recurring = oldRoute.getRecurring();
		} else {
			recurring = routeUpdate.getRecurring();
		}
			
		if (user.getIsAdmin()) {
			routeDao.updateRoutes(routeUpdate.getRouteID(), driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, routeUpdate.getFinished());
			return true;
		}
		if (routeUpdate.getDriverID() == user.getUserID()) {
			routeDao.updateRoutes(routeUpdate.getRouteID(), oldRoute.getDriverID(), freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, routeUpdate.getFinished());
			return true;
		}
		List<User> usersOnRoute = routeDao.getUsersByRouteId(routeUpdate.getRouteID());
		for (User userOnRoute: usersOnRoute) {
			if(userOnRoute.getUserID() == user.getUserID()) {
				routeDao.updateRoutes(routeUpdate.getRouteID(), oldRoute.getDriverID(), freeSeats, oldRoute.getLocation(), oldRoute.getDestination(), oldRoute.getTimeOfDeparture(), oldRoute.getTimeOfArrival(), passengers, oldRoute.getDescription(), oldRoute.getBookingEndTime(), oldRoute.getRecurring(), oldRoute.getFinished());
				return true;
			}
		}
		throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);

	}
	
	@Path("{RouteID}")
	@DELETE
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public boolean deleteRoute(@PathParam("RouteID") int routeID) {
		if (routeDao.getRoute(routeID) == null) {
			if (user.getIsAdmin()) {
				throw new WebApplicationException("Requirements met but route not found", Response.Status.BAD_REQUEST);
			}
			throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
		}
		if (routeDao.getRoute(routeID).getDriverID() == user.getUserID() || user.getIsAdmin()) {
			routeDao.deleteRoutes(routeID);
			return true;
		}
		throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);

	}

}
