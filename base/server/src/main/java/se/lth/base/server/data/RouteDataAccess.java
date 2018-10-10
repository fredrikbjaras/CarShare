package se.lth.base.server.data;

import se.lth.base.server.Config;
import se.lth.base.server.database.DataAccess;
import se.lth.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RouteDataAccess extends DataAccess<Route> {
    private final UserDataAccess userDao = new UserDataAccess(Config.instance().getDatabaseDriver());

    private static class RouteMapper implements Mapper<Route> {
        @Override
        public Route map(ResultSet resultSet) throws SQLException {
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
        super(driverUrl, new RouteMapper());
    }

    /**
     * Add a new Route to the database.
     * routeID is generated automagically.
     *
     * @param driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring and finished.
     * @return Routes object containing routeID and the entered info.
     */
    public Route addRoute(int driverID, int freeSeats, String location, String destination, Timestamp timeOfDeparture, Timestamp timeOfArrival, String passengers, String description, Timestamp bookingEndTime, int recurring, boolean finished) {
    	 int routeID = insert("INSERT INTO Routes (driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished) VALUES (?,?,?,?,?,?,?,?,?,?,?)", driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished);
    	 return new Route(routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished);
    }

    public Route updateRoute(int routeID, int driverID, int freeSeats, String location, String destination, Timestamp timeOfDeparture, Timestamp timeOfArrival, String passengers, String description, Timestamp bookingEndTime, int recurring, boolean finished) {
    	execute("UPDATE Routes SET  driverID= ?, freeSeats = ?, location = ?, destination = ?, timeOfDeparture = ?, timeOfArrival = ?, passengers = ?, description = ?, bookingEndTime = ?, recurring = ?, finished = ?" +
                "WHERE routeID = ?",  driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished, routeID);
        return getRoute(routeID);
    }

    public boolean deleteRoute(int routeID) {
        return execute("DELETE FROM Routes WHERE routeID = ?", routeID) > 0;
    }
    
    /**
     * Get a specific Route from the database
     * @param routeID
     * @return Routes-object of specified routeID
     */
    public Route getRoute(int routeID) {
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
    	String[] passList = route.getPassengers().split(";"); 
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
    
    public List<Route> getAllRoutesFromLocation(String location) {
        return query("SELECT routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished FROM Routes" +
                "WHERE location = ?", location);
    }
    
    public List<Route> getAllRoutesFromDestination(String destination) {
        return query("SELECT routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished FROM Routes" +
                "WHERE destination = ?", destination);
    }
    
    public List<Route> getAllRoutesFromDepartureTime(Timestamp departureTime) {
        return query("SELECT routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished FROM Routes" +
                "WHERE timeOfDeparture = ?", departureTime);
    }
    
    public List<Route> getAllRoutesFromArrivalTime(Timestamp arrivalTime) {
        return query("SELECT routeID, driverID, freeSeats, location, destination, timeOfDeparture, timeOfArrival, passengers, description, bookingEndTime, recurring, finished FROM Routes" +
                "WHERE timeOfArrival = ?", arrivalTime);
    }
    
    
    /**
     * Adds a user as passenger to a route and updates the database
     * @param routeID
     * @param passengerID
     * @return True if successful, false if passenger is already a passenger, no free seats available, or if the passenger is also the driver for the route.
     */
    public boolean addPassengerToRoute(int routeID, int passengerID) {
    List<Route> temp = query("SELECT * FROM Routes WHERE routeID = ?", routeID);
    Route route = temp.get(0);
    String currentPassengers = route.getPassengers();;
    String passengerIDString = Integer.toString(passengerID);
    if (route.getFreeSeats() > 0 && !currentPassengers.contains(passengerIDString) && !route.getFinished()) {
    		currentPassengers = currentPassengers + passengerIDString + ";";
    		updateRoute(route.getRouteID(), route.getDriverID(), route.getFreeSeats() - 1, route.getLocation(), route.getDestination(), route.getTimeOfDeparture(), route.getTimeOfArrival(), currentPassengers, route.getDescription(),route.getBookingEndTime(), route.getRecurring(), route.getFinished());
    		return true;
    	}
    return false;
    }
    
}

