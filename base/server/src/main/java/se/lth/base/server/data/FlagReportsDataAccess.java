package se.lth.base.server.data;

import se.lth.base.server.database.DataAccess;
import se.lth.base.server.database.DataAccessException;
import se.lth.base.server.database.ErrorType;
import se.lth.base.server.database.Mapper;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class FlagReportsDataAccess extends DataAccess<FlagReports> {


    private static class FlagReportsMapper implements Mapper<FlagReports> {
        @Override
        public FlagReports map(ResultSet resultSet) throws SQLException {
        	int flagReportID = resultSet.getInt("flagReportID");
        	int routeID = resultSet.getInt("routeID");
        	int fromUserID = resultSet.getInt("fromUserID");
        	int toUserID = resultSet.getInt("toUserID");
        	String reason = resultSet.getString("reason");
        	Array flags = (Array) resultSet.getArray("flags").getArray(); //Need to test with this Array type, might not work as intended
        	
        	
        	
            return new FlagReports(flagReportID, routeID, fromUserID, toUserID, reason, flags);
        }
    }
    public FlagReportsDataAccess(String driverUrl) {
        super(driverUrl, new FlagReportsMapper());
    }

    /**
     * Add a new FlagsReport to the database.
     * flagReportID is generated automagically.
     *
     * @param routeID, fromUserID, toUserID, reason and flags[].
     * @return FlagReports object containing flagReportID and the entered info.
     */
    public FlagReports addFlagReports(int routeID, int fromUserID, int toUserID, String reason, Array flags) {
    	 int flagReportID = insert("INSERT INTO FlagReports (routeID, fromUserID, toUserID, reason, flags) VALUES ((" +
                 "?,?,?,?,?)",  routeID, fromUserID, toUserID, reason, flags);
    	 return new FlagReports(flagReportID, routeID, fromUserID, toUserID, reason, flags);
    }

    
    
    public FlagReports updateFlagReports(int flagReportID, int routeID, int fromUserID, int toUserID, String reason, boolean[] flags) {
        execute("UPDATE FlagReports SET  routeID= ?, fromUserID = ?,toUserID = ?,reason = ?, flags = ?" +
                "WHERE flagReportID = ?",  routeID, fromUserID, toUserID, reason, flags, flagReportID);
        return getFlagReport(flagReportID);
    }

    
    
    public FlagReports getFlagReport(int flagReportID) {
        return queryFirst("SELECT flagReportID, routeID, fromUserID, toUserID, reason, flags FROM FlagReports " +
                "WHERE FlagReportID = ?", flagReportID);
    }

    
    
    public boolean deleteFlagReports(int flagReportID) {
        return execute("DELETE FROM FlagReports WHERE FlagReportID = ?", flagReportID) > 0;
    }

    
    
    /**
     * @return all flagReports given to a specific user.
     * @param UserID
     */
    public List<FlagReports> getFlagReportsToSpecificUser(int toUserID) {
        return query("SELECT flagReportID, routeID, fromUserID, toUserID, reason, flags FROM flagReports " +
                "WHERE toUserID = ?", toUserID);
    }
    
    /**
     * @return all flagReports from a specific user.
     * @param UserID
     */
    public List<FlagReports> getFlagReportsFromSpecificUser(int fromUserID) {
        return query("SELECT flagReportID, routeID, fromUserID, toUserID, reason, flags FROM flagReports " +
                "WHERE fromUserID = ?", fromUserID);
    }
    
}
