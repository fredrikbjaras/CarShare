/*package se.lth.base.server.rest;

import java.lang.reflect.Array;
import java.sql.Timestamp;

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
import se.lth.base.server.data.*;
import java.util.List;

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

	@POST
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
		throw new WebApplicationException("You can't create a route where someonelse expect you is the driver",
				Response.Status.BAD_REQUEST);
	}

	@Path("{RouteID}")
	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Route getRoute(int routeID) {
		//mabye should it be a difference between a Admin call and a user call 
		return routeDao.getRoutes(routeID);

	}
	
	@Path("filter")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public List<Route> getRoutes(RouteFilter filter) {
		return null;

	}
	
	@Path("{RouteID}")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public boolean putRoute(Route routeUpdate) {
		return false;

	}
	
	@Path("{RouteID}")
	@DELETE
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public boolean deleteRoute(@PathParam("RouteID") int routeID) {
		return false;

	}

}*/
