package se.lth.base.server.rest;

import se.lth.base.server.Config;
import se.lth.base.server.data.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Path("user")
public class UserResource {

    public static final String USER_TOKEN = "USER_TOKEN";

    private final ContainerRequestContext context;
    private final User user;
    private final Session session;
    private final UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());

    public UserResource(@Context ContainerRequestContext context) {
        this.context = context;
        this.user = (User) context.getProperty(User.class.getSimpleName());
        this.session = (Session) context.getProperty(Session.class.getSimpleName());
    }

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public User currentUser() {
        return user;
    }

    @Path("login")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response login(Credentials credentials,
                          @QueryParam("remember") @DefaultValue("false") boolean rememberMe)
            throws URISyntaxException {
        Session newSession = userDao.authenticate(credentials);
        int maxAge = rememberMe ? (int) TimeUnit.DAYS.toSeconds(7) : NewCookie.DEFAULT_MAX_AGE;
        return Response.noContent().cookie(newCookie(newSession.getSessionId().toString(), maxAge, null)).build();
    }

    private NewCookie newCookie(String value, int maxAge, Date expiry) {
        return new NewCookie(USER_TOKEN,
                value,                                          // value
                "/rest",                                        // path
                context.getUriInfo().getBaseUri().getHost(),    // host
                NewCookie.DEFAULT_VERSION,                      // version
                "",                                             // comment
                maxAge,                                         // max-age
                expiry,                                         // expiry
                false,                                          // secure
                true);                                          // http-onle

    }

    @Path("logout")
    @POST
    @PermitAll
    public Response logout() {
        userDao.removeSession(session.getSessionId());
        return Response.noContent().cookie(newCookie("", 0, new Date(0L))).build();
    }

    @Path("roles")
    @GET
    @RolesAllowed(Role.Names.ADMIN)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Set<Role> getRoles() {
        return Role.ALL_ROLES;
    }

    /**
     * @param credentials
     * @return returns the user added
     * 
     * Ska userDao.addUser(...) ha credentials eller ej? Går ej att inputta lösenord om inte parametrarna i denna addUser ändras.
     * Om userDao.addUser ger en DataAccessException, är inte det onödigt?
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.ADMIN)
    public User addUser(Credentials credentials) {
    	try {
    		if (userDao.getUserWithName(credentials.getUsername()).getUserName() == credentials.getUsername()) {
    			throw new WebApplicationException("Username is taken", Response.Status.BAD_REQUEST);
    		}
    	} catch (DataAccessException e) {
            if (!credentials.hasPassword() || !credentials.validPassword()) {
                throw new WebApplicationException("Password too short", Response.Status.BAD_REQUEST);
            }
            return userDao.addUser(credentials);
    	}
    }
    
    //dosen't work yet, Filter class has to be added for the search Algorithm to work.
   @Path("{filter}") // object
	@GET
	@RolesAllowed(Role.Names.ADMIN)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<User> getUsers(@PathParam("filter") UserFilter filter) { // filter object could contain String name, int
																			// phoneNr, RouteID

		if (user.getIsAdmin()) {
			UserSearch search = new UserSearch(userDao, routeDao, filter);
			return search.returnSortedList();

		} else {
			UserSearch search = new UserSearch(userDao, routeDao, filter, user.getUserID());
			List<User> temp = search.returnSortedList();
			if (temp.get(0).getUserID() == -1) {
				throw new WebApplicationException("You are not a part of this Route", Response.Status.BAD_REQUEST);
				List<User> emptyUserList = new ArrayList<User>();
				return emptyUserList;
			} else {
				return temp;
			}
		}

	}

    /**
     * @param userId
     * @return returns the user with the userId
     * 
     * En ny user med konstruktorn (-1, null, null, phoneNr, false) returneras när man enbart vill returnera telefonnumret.
     * En metod checkIfOnSameActiveRoute bör finnas med i någon klass, Route?, User? för att kolla om två users befinner/befann sig på
     * samma route, detta kan vara värt då detta behöver kollas i flera metoder i *-Resource-klasserna.
     * Bör passagerarna kunna få andra passagerares telefonnummer?
    */
    @Path("{id}")
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public User getUser(@PathParam("id") int userId) {
    	try {
    		if(user.getIsAdmin() || user.getUserID() == userId) {
            		return userDao.getUser(userId);
        	} else if (user.checkIfOnSameActiveRoute(userId)) {
            		return new User(-1, null, null, userDao.getUser(userId).getPhoneNr(), false);
        	} else {
        		throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
        	}
    	} catch (DataAccessException e) {
    		//Man kanske inte behöver if-else-satserna i catch-blocket.
    		if(user.getIsAdmin() || user.getUserID() == userId || user.checkIfOnSameActiveRoute(userId)) {
    			throw new WebApplicationException("Requirements met but User not found", Response.Status.BAD_REQUEST);
    		} else {
    			throw new WebApplicationException("Requirements not met", Response.Status.BAD_REQUEST);
    		}
    	}
    }

    @Path("{id}")
    @RolesAllowed(Role.Names.ADMIN)
    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public User putUser(@PathParam("id") int userId, Credentials credentials) {
        if (credentials.hasPassword() && !credentials.validPassword()) {
            throw new WebApplicationException("Password too short", Response.Status.BAD_REQUEST);
        }
        if (userId == user.getId() && user.getRole().getLevel() > credentials.getRole().getLevel()) {
            throw new WebApplicationException("Cant't demote yourself", Response.Status.BAD_REQUEST);
        }
        return userDao.updateUser(userId, credentials);
    }
	@Path("{username}")
	@PermitAll
	@DELETE
	public boolean deleteUser(int userId) {

		if (user.getIsAdmin()) {
			if (userId == currentUser().getUserID()) {
				throw new WebApplicationException("Don't delete yourself!, you are an Admin",
						Response.Status.BAD_REQUEST);
				return false;
			}
			if (!userDao.deleteUser(userId)) {
				throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
				return false;
			} else {
				return true;
			}
		} else {

			if (userId == currentUser().getUserID()) {
				if (!userDao.deleteUser(userId)) {
					throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
					return false;
				} else {
					// also all booking request made by user should be deleted 
					// Maybe the use should be automatically logged out?
					return true;
				}
			}
			throw new WebApplicationException("You don't have the permisson to delete this user", Response.Status.BAD_REQUEST);
			return false;
		}
	}
}
