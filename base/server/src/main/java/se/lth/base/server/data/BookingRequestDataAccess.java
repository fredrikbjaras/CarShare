package se.lth.base.server.data;

import se.lth.base.server.database.DataAccess;
import se.lth.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class BookingRequestDataAccess extends DataAccess<BookingRequest> {


    private static class BookingMapper implements Mapper<BookingRequest> {
        @Override
        public BookingRequest map(ResultSet resultSet) throws SQLException {
            return new BookingRequest(resultSet.getInt("bookingReqID"),
            		resultSet.getInt("routeID"),
            		resultSet.getInt("fromUserID"),
            		resultSet.getInt("toUserID"),
            		resultSet.getBoolean("accepted"),
            		resultSet.getString("reason"));		//<---- Heter reason i SQL-tabellen because SQL-syntax stuff
        }

    }
    public BookingRequestDataAccess(String driverUrl) {
        super(driverUrl, new BookingMapper());
    }

    /**
     * Add a new BookingRequest to the database.
     * bookingReqID is generated automagically.
     *
     * @param routeID, fromUserID, toUserID,  and accepted.
     * @return BookingRequests object containing bookingReqID and the entered info.
     */
    public BookingRequest addBookingRequests(int routeID, int fromUserID, int toUserID, boolean accepted, String comment) {
    	 int bookingReqID = insert("INSERT INTO BookingRequests (routeID, fromUserID, toUserID, accepted, reason) VALUES (" +
                 "?,?,?,?,?)",  routeID, fromUserID, toUserID, accepted, comment);
    	 return new BookingRequest(bookingReqID, routeID, fromUserID, toUserID, accepted, comment);
    }

    public BookingRequest updateBookingRequests(int bookingReqID, int routeID, int fromUserID, int toUserID, boolean accepted, String comment) {
    	execute("UPDATE BookingRequests SET  routeID= ?, fromUserID = ?,toUserID = ?, accepted = ?, reason = ?" +
                "WHERE bookingReqID = ?",  routeID, fromUserID, toUserID, accepted, comment, bookingReqID);
        return getBookingRequests(bookingReqID);
    }

    public BookingRequest getBookingRequests(int bookingReqID) {
        return queryFirst("SELECT * FROM BookingRequests " +
                "WHERE bookingReqID = ?", bookingReqID);
    }

    public boolean deleteBookingRequests(int bookingReqID) {
        return execute("DELETE FROM BookingRequests WHERE bookingReqID = ?", bookingReqID) > 0;
    }

    /**
     * @return all BookingRequests in the system.
     */
    public List<BookingRequest> getAllBookingRequests() {
        return query("SELECT * FROM BookingRequests");
    }
    
    /**
     * @return all BookingRequests from a specified user.
     * @param UserID
     */
    public List<BookingRequest> getBookingRequestsFromUser(int fromUserID) {
        return query("SELECT * FROM BookingRequests " +
                "WHERE fromUserID = ?", fromUserID);
    }
    
    /**
     * @return all BookingRequests sent to a specified user.
     * @param UserID 
     */
    public List<BookingRequest> getBookingRequestsToUser(int toUserID) {
        return query("SELECT * FROM BookingRequests " +
                "WHERE toUserID = ?", toUserID);
    }
}

