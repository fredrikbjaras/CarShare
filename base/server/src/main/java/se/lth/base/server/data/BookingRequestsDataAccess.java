package se.lth.base.server.data;

import se.lth.base.server.database.DataAccess;
import se.lth.base.server.database.DataAccessException;
import se.lth.base.server.database.ErrorType;
import se.lth.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class BookingRequestsDataAccess extends DataAccess<BookingRequests> {


    private static class BookingMapper implements Mapper<BookingRequests> {
        @Override
        public BookingRequests map(ResultSet resultSet) throws SQLException {
            return new BookingRequests(resultSet.getInt("bookingReqID"),
            		resultSet.getInt("routeID"),
            		resultSet.getInt("fromUserID"),
            		resultSet.getInt("toUserID"),
            		resultSet.getBoolean("accepted"));
        }

    }
    public BookingRequestsDataAccess(String driverUrl) {
        super(driverUrl, new BookingMapper());
    }

    /**
     * Add a new BookingRequest to the database.
     * bookingReqID is generated automagically.
     *
     * @param routeID, fromUserID, toUserID,  and accepted.
     * @return BookingRequests object containing bookingReqID and the entered info.
     */
    public BookingRequests addBookingRequests(int routeID, int fromUserID, int toUserID, boolean accepted) {
    	 int bookingReqID = insert("INSERT INTO BookingRequests (routeID, fromUserID, toUserID, accepted) VALUES ((" +
                 "?,?,?,?)",  routeID, fromUserID, toUserID, accepted);
    	 return new BookingRequests(bookingReqID, routeID, fromUserID, toUserID, accepted);
    }

    public BookingRequests updateBookingRequests(int bookingReqID, int routeID, int fromUserID, int toUserID, boolean accepted) {
    	execute("UPDATE BookingRequests SET  routeID= ?, fromUserID = ?,toUserID = ?, accepted = ?" +
                "WHERE bookingReqID = ?",  routeID, fromUserID, toUserID, accepted, bookingReqID);
        return getBookingRequests(bookingReqID);
    }

    public BookingRequests getBookingRequests(int bookingReqID) {
        return queryFirst("SELECT bookingReqID, routeID, fromUserID, toUserID, accepted FROM BookingRequests " +
                "WHERE bookingReqID = ?", bookingReqID);
    }

    public boolean deleteBookingRequests(int bookingReqID) {
        return execute("DELETE FROM BookingRequests WHERE bookingReqID = ?", bookingReqID) > 0;
    }

    /**
     * @return all BookingRequests in the system.
     */
    public List<BookingRequests> getAllBookingRequests() {
        return query("SELECT bookingReqID, routeID, fromUserID, toUserID, accepted FROM BookingRequests");
    }
    
    /**
     * @return all BookingRequests from a specified user.
     * @param UserID
     */
    public List<BookingRequests> getBookingRequestsFromUser(int fromUserID) {
        return query("SELECT bookingReqID, routeID, fromUserID, toUserID, accepted FROM BookingRequests " +
                "WHERE fromUserID = ?", fromUserID);
    }
    
    /**
     * @return all BookingRequests sent to a specified user.
     * @param UserID 
     */
    public List<BookingRequests> getBookingRequestsToUser(int toUserID) {
        return query("SELECT bookingReqID, routeID, fromUserID, toUserID, accepted FROM BookingRequests " +
                "WHERE toUserID = ?", toUserID);
    }
}

