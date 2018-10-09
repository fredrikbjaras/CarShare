package se.lth.base.server.data;

import se.lth.base.server.database.DataAccess;
import se.lth.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class RouteDataAccess extends DataAccess<Route> {


    private static class RoutesMapper implements Mapper<Route> {
        @Override
        public Route map(ResultSet resultSet) throws SQLException {//<------Fix management of arrays from the database
        	int routeID = resultSet.getInt("routeID");
        	int driverID = resultSet.getInt("driverID");
        	int freeSeats = resultSet.getInt("freeSeats");
        	String location = resultSet.getString("location");
        	String destination = resultSet.getString("destination");
        	Timestamp timeOfDeparture = resultSet.getTimestamp("timeOfDeparture");
        	Timestamp timeOfArrival = resultSet.getTimestamp("TimeOfArrival");
        	String passengers = resultSet.getString("passengers");
        	String description = resultSet.getString("description");
        	Timestamp bookingEndTime = resultSet.getTimestamp("bookingEndTime");
        	int recurring = resultSet.getInt("recurring");
        	boolean finished = resultSet.getBoolean("finished");
        	
            return new Route(routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished);
        }

    }
    public RouteDataAccess(String driverUrl) {
        super(driverUrl, new RoutesMapper());
    }

    /**
     * Add a new Route to the database.
     * routeID is generated automagically.
     *
     * @param driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring and finished.
     * @return Routes object containing routeID and the entered info.
     */
    public Route addRoutes(int driverID, int freeSeats, String location, String destination, Timestamp timeOfDeparture, Timestamp timeOfArrival, String passengers, String description, Timestamp bookingEndTime, int recurring, boolean finished) {
    	 int routeID = insert("INSERT INTO Routes (driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished) VALUES ((" +
                 "?,?,?,?,?,?,?,?,?)", driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished);
    	 return new Route(routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished);
    }

    public Route updateRoutes(int routeID, int driverID, int freeSeats, double[] location, double[] destination, Timestamp timeOfDeparture, Timestamp timeOfArrival, int[] passengers, String description, Timestamp bookingEndTime, int recurring, boolean finished) {
    	execute("UPDATE Routes SET  driverID= ?, freeSeats = ?, location = ?, destination = ?, timeOfDeparture = ?, timeOfArrival = ?, passengers = ?, description = ?, bookingEndTime = ?, recurring = ?, finished = ?" +
                "WHERE routeID = ?",  driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished, routeID);
        return getRoutes(routeID);
    }

    public boolean deleteRoutes(int routeID) {
        return execute("DELETE FROM Routes WHERE routeID = ?", routeID) > 0;
    }
    
    /**
     * Get a specific Route from the database
     * @param routeID
     * @return Routes-object of specified routeID
     */
    public Route getRoutes(int routeID) {
        return queryFirst("SELECT routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished FROM Routes " +
                "WHERE routeID = ?", routeID);
    }

    /**
     * @return all Routes in the database.
     */
    public List<Route> getAllRoutes() {
        return query("SELECT routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished FROM Routes");
    }
    
    
    /**
     * Returns ALL users of a route, including the driver. 
     * @param routeId
     * @return list of users
     */
    public List<User> getUsersByRouteId(int routeId) {
    	Route route = getRoute(routeId);
    	String[] passList = route.getPassengers().split(","); // separeras på ,??
    	List<User> userList = new ArrayList<User>();
    	userList.add(userDao.getUser(route.getDriverID()));
    	for(String user : passList) {
    		userList.add(userDao.getUser(Integer.parseInt(user)));
    	}
        return userList;
    }
    
    
    /**
     * @param UserID of the driver
     * @return all Routes from a specific user.
     */
    public List<Route> getAllRoutesFromUser(int UserID) {
        return query("SELECT routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished FROM Routes" +
                "WHERE driverID = ?", UserID);
    }
    
}
