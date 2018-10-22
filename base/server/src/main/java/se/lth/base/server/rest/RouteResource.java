package se.lth.base.server.rest;
import java.sql.Timestamp;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
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
public class RouteResource {
	private final ContainerRequestContext context;
	private final User user;
	// private final Route route;
	private final Session session;
	private final UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());
	private final RouteDataAccess routeDao = new RouteDataAccess(Config.instance().getDatabaseDriver());
	private final BookingRequestDataAccess bookDao = new BookingRequestDataAccess(
			Config.instance().getDatabaseDriver());
	public RouteResource(@Context ContainerRequestContext context) {
		this.context = context;
		this.user = (User) context.getProperty(User.class.getSimpleName());
		// this.route = (Routes) context.getProperty(User.class.getSimpleName());
		this.session = (Session) context.getProperty(Session.class.getSimpleName());
	}
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public boolean addRoute(Route route) {
		Timestamp timeStampOfDeparture = route.getTimeOfDeparture();
		Timestamp timeStampOfArrival = route.getTimeOfArrival();
		Timestamp timeStampBookingEndTime = route.getBookingEndTime();
		if (user.getIsAdmin() || user.getUserID() == route.getDriverID()) {
			List<Route> tempList = routeDao.getAllRoutesFromUser(route.getDriverID());
			for (int i = 0; i < tempList.size(); i++) {
				// checks if the driver already has a route on that specific time.
				if ((timeStampOfDeparture.before(tempList.get(i).getTimeOfDeparture())
						&& timeStampOfArrival.after(tempList.get(i).getTimeOfDeparture()))
						
						|| (timeStampOfDeparture.after(tempList.get(i).getTimeOfDeparture())
								&& timeStampOfDeparture.before(tempList.get(i).getTimeOfArrival()))
						
							|| timeStampOfDeparture.equals(tempList.get(i).getTimeOfDeparture())
									&& timeStampOfArrival.equals(tempList.get(i).getTimeOfArrival())) {
					throw new WebApplicationException("This user already has a route during the specified timeframe",
							Response.Status.BAD_REQUEST);
				}
			}
			routeDao.addRoute(route.getDriverID(), route.getFreeSeats(), route.getLocation(), route.getDestination(), timeStampOfDeparture, timeStampOfArrival, route.getPassengers(),
					route.getDescription(), timeStampBookingEndTime, route.getRecurring(), route.getFinished());
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
			
			throw new WebApplicationException("Route not found", Response.Status.BAD_REQUEST);
		} else {
			return routeDao.getRoute(routeID);
		}
	}
	
	@Path("filter")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public List<Route> getRoutes(RouteFilter filter) {
		if (user.getIsAdmin()) {
			switch (filter.getFilter()) {
				case 1:
					User driver = userDao.getUserWithName(filter.getDriverUserName());
					List<Route> routes = routeDao.getAllRoutesFromUser(driver.getUserID());
					return routes;
				case 2:
					User passenger = userDao.getUserWithName(filter.getPassengerUserName());
					List<Route> routes2 = routeDao.getRoutesWithPassenger(passenger.getUserID());
					return routes2;
				case 3:
					return routeDao.getAllRoutesFromLocation(filter.getLocation());
				case 4:
					return routeDao.getAllRoutesFromDestination(filter.getDestination());
			
				case 5:
					Timestamp tempDepTime = filter.getDepartureTime();
					tempDepTime.setTime(tempDepTime.getTime()+3600*1000);
					return routeDao.getAllRoutesFromDepartureTime(filter.getDepartureTime(), tempDepTime);
				
				case 6:
					Timestamp tempArrTime = filter.getArrivalTime();
					tempArrTime.setTime(tempArrTime.getTime()+3600*1000);
					return routeDao.getAllRoutesFromArrivalTime(filter.getArrivalTime(),tempArrTime);
				
				default:
					return routeDao.getAllRoutes();
				//	throw new WebApplicationException("Something is wonky with the filter parameters", Response.Status.BAD_REQUEST);
			}
		} else {
			
			switch (filter.getFilter()) {
			case 1: //Man ska väl inte kunna söka efter en annan driver?
				User driver = userDao.getUserWithName(filter.getDriverUserName());
				List<Route> routes = routeDao.getAllRoutesFromUser(driver.getUserID());
				return routes;
			case 2:
				User passenger = userDao.getUserWithName(filter.getPassengerUserName());
				List<Route> routes2 = routeDao.getRoutesWithPassenger(passenger.getUserID());
				return routes2;
			case 3:
				return routeDao.getAllRoutesFromLocation(filter.getLocation());
			case 4:
				return routeDao.getAllRoutesFromDestination(filter.getDestination());
		
			case 5:
				Timestamp tempDepTime = filter.getDepartureTime();
				tempDepTime.setTime(tempDepTime.getTime()+3600*1000);
				return routeDao.getAllRoutesFromDepartureTime(filter.getDepartureTime(), tempDepTime);
			
			case 6:
				Timestamp tempArrTime = filter.getArrivalTime();
				tempArrTime.setTime(tempArrTime.getTime()+3600*1000);
				return routeDao.getAllRoutesFromArrivalTime(filter.getArrivalTime(),tempArrTime);
			
			default:
				throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
		}
	}
	}
	
	@Path("{RouteID}")
	@POST
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
			routeDao.updateRoute(routeUpdate.getRouteID(), driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, routeUpdate.getFinished());
			return true;
		}
		if (routeUpdate.getDriverID() == user.getUserID()) {
			routeDao.updateRoute(routeUpdate.getRouteID(), oldRoute.getDriverID(), freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, routeUpdate.getFinished());
			return true;
		}
		List<User> usersOnRoute = routeDao.getUsersByRouteId(routeUpdate.getRouteID());
		for (User userOnRoute: usersOnRoute) {
			if(userOnRoute.getUserID() == user.getUserID()) {
				routeDao.updateRoute(routeUpdate.getRouteID(), oldRoute.getDriverID(), freeSeats, oldRoute.getLocation(), oldRoute.getDestination(), oldRoute.getTimeOfDeparture(), oldRoute.getTimeOfArrival(), passengers, oldRoute.getDescription(), oldRoute.getBookingEndTime(), oldRoute.getRecurring(), oldRoute.getFinished());
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
			routeDao.deleteRoute(routeID);
			return true;
		}
		throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
	}
	
	@Path("{RouteID}/passenger/{passengerID}")
	@PermitAll
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	//@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public boolean addPassenger(@PathParam("RouteID") int routeID, @PathParam("passengerID") int passengerID) {
		if(user.getIsAdmin() || routeDao.getRoute(routeID).getDriverID() == user.getUserID()) {
		return routeDao.addPassengerToRoute(routeID,passengerID);
		}
		else {
			throw new WebApplicationException("You are not allowed to edit this route", Response.Status.BAD_REQUEST);
		}
	}
}
