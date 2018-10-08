package se.lth.base.server.rest;

import java.lang.reflect.Array;
import java.sql.Timestamp;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.lth.base.server.Config;
import se.lth.base.server.data.Routes;
import se.lth.base.server.data.RoutesDataAccess;
import se.lth.base.server.data.Session;
import se.lth.base.server.data.User;
import se.lth.base.server.data.UserDataAccess;
import java.util.List;

@Path("route")
public class RoutesResource {

	private final ContainerRequestContext context;
	private final User user;
	// private final Route route;
	private final Session session;
	private final UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());
	private final RoutesDataAccess routeDao = new RoutesDataAccess(Config.instance().getDatabaseDriver());
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
	public boolean addRoute(int routeID, int driverID, int freeSeats, Array location, Array destination,
			Timestamp timeOfDeparture, Timestamp timeOfArrival, Array passengers, String description,
			Timestamp bookingEndTime, int recurring, boolean finished) {
		return false;

	}

	@Path("{RouteID}")
	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Routes getRoute(int routeID) {
		return null;

	}
	
	@Path("filter")
	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public List<Routes> getRoutes(RouteFilter filter) {
		return null;

	}
	
	@Path("{RouteID}")
	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public boolean putRoute(Routes routeUpdate) {
		return false;

	}
	
	@Path("{RouteID}")
	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON + ";charsert=utf-8")
	public boolean deleteRoute(@PathParam("RouteID") int routeID) {
		return false;

	}

}
