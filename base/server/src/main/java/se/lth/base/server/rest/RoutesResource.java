package se.lth.base.server.rest;

import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import se.lth.base.server.Config;
import se.lth.base.server.data.Session;
import se.lth.base.server.data.User;
import se.lth.base.server.data.UserDataAccess;


@Path("route")
public class RoutesResource {
	
	private final ContainerRequestContext context;
	private final User user;
	private final Route route;
	private final Session session;
	private final UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());
	private final RouteDataAcces routeDao = new RouteDataAcces(Config.instance().getDatabaseDriver());
	private final BookingRequestDataAccess bookDao = new BookingRequestDataAccess(
			Config.instance().getDatabaseDriver());

	public RoutesResource(@Context ContainerRequestContext context) {
		this.context = context;
		this.user = (User) context.getProperty(User.class.getSimpleName());
		this.route = (Route) context.getProperty(User.class.getSimpleName());
		this.session = (Session) context.getProperty(Session.class.getSimpleName());
	}


}
