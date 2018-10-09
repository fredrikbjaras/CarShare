package se.lth.base.server.data;

import se.lth.base.server.database.DataAccess;
import se.lth.base.server.database.DataAccessException;
import se.lth.base.server.database.ErrorType;
import se.lth.base.server.database.Mapper;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

 /* @author Jonathan Jakobsson
 * @see DataAccess
 */
public class UserDataAccess extends DataAccess<User> {

    private static class UserMapper implements Mapper<User> {
        @Override
        public User map(ResultSet resultSet) throws SQLException {
            return new User(resultSet.getInt("userID"),
                    resultSet.getString("password"),
                    resultSet.getString("userName"),
                    resultSet.getString("phoneNr"),
                    resultSet.getBoolean("isAdmin"));
        }
    }
    public UserDataAccess(String driverUrl) {
        super(driverUrl, new UserMapper());
    }

    /**
     * Add a new user to the system.
     *
     * @param  userName password phoneNr and isAdmin of a new User
     * @return Key (userID) of user created.
     * @throws DataAccessException if duplicated userName,too short password or too short user names.
     * Note: removed param Credentials as it is only user for security. 
     */
    public User addUser(String userName,String password,String phoneNr,boolean isAdmin) {
        int userId = insert("INSERT INTO User (userName, password,phoneNr,isAdmin) VALUES (" +
                        "?,?,?,?)",
                userName, password, phoneNr, isAdmin);
        return new User(userId, userName, password,phoneNr,isAdmin);
    }
    /**
     *Update information about a user in the system.
     *
     * @param  userName password phoneNr and isAdmin of a existing User
     * @return Returns the user created.
     * @throws DataAccessException if duplicated userName,too short password,userId does not exist or too short user names.
     * Note: removed param Credentials as it is only user for security. 
     */
    public User updateUser(int userID, String userName,String password,File profilePicture,String description,boolean isAdmin) {
        if (password!="" && password.length()>7) {
            execute("UPDATE User SET  userName= ?, password = ?,profilePicture = ?,description = ?, phoneNr = ?" +
                            "WHERE userID = ?",
                    userName, password, profilePicture,
                    description,isAdmin, userID);
        } else {
            execute("UPDATE User SET userName = ?, profilePicture = ?, description = ?, isAdmin = ?"+
                            "WHERE userID = ?",
                    userName, profilePicture, description,isAdmin,userID);
        }
        return getUser(userID);
    }
    /**
     *Retrieve a user with a specific userId.
     *
     * @param  userID
     * @return Returns the user with the ID.
     * @throws DataAccessException if userID does not exist.
     * 
     */
    public User getUser(int userID) {
        return queryFirst("SELECT userID, userName, password,phoneNr,isAdmin FROM User " +
                "WHERE userID = ?", userID);
    }
   /**
     * Retrieves a list of users beginning with the search "name". 
     *
     * @param  name
     * @return Returns the list of found users. 
     * 
     */
    public List<User> getUsersByName(String name) {
        return query("SELECT user_id, username, role FROM user, user_role " +
                "WHERE user.role_id = user_role.role_id AND username LIKE ?%", name);
    }
    /**
     *Retrieve a user with a specific phone number.
     *
     * @param  number
     * @return Returns the user with the phone number
     * 
     */
    public List<User> getUsersByNumber(String number) {
        return query("SELECT user_id, username, role FROM user, user_role " +
                "WHERE user.role_id = user_role.role_id AND phoneNr = ?", number); //Alltid singular? Namn på number i db?
    }

    
    /* @param  userName
    * @return Returns the user with the ID.
    * @throws DataAccessException if userID does not exist.
    * 
    */
   public User getUserWithName(String userName) {
       return queryFirst("SELECT userID, userName, password,phoneNr,isAdmin FROM User " +
               "WHERE userName = ?", userName);
   }
    /**
     *Delete a user with a specific userId.
     *
     * @param  userID
     * @return Returns true if successful.
     * @throws DataAccessException if userID does not exist.
     * 
     */
    public boolean deleteUser(int userID) {
        return execute("DELETE FROM User WHERE UserID = ?", userID) > 0;
    }

    /**
     * @return all users in the system.
     */
    public List<User> getUsers() {
        return query("SELECT userID, userName, password,phoneNr,isAdmin,description,profilePicture FROM User");
    }

    /**
     * Fetch session and the corresponding user.
     *
     * @param sessionId globally unqiue identifier, stored in the client.
     * @return session object wrapping the user.
     * @throws DataAccessException if the session is not found.
     */
    public Session getSession(UUID sessionId) {
        User user = queryFirst("SELECT User.user_id, userName, isAdmin FROM user, Session " +
                "WHERE isAdmin = User.isAdmin " +
                "    AND Session.userID = User.userID " +
                "    AND Session.session_uuid = ?", sessionId);
        execute("UPDATE session SET last_seen = CURRENT_TIMESTAMP() " +
                "WHERE session_uuid = ?", sessionId);
        return new Session(sessionId, user);
    }

    /**
     * Logout a user. This method is idempotent, meaning it is safe to repeat indefinitely.
     *
     * @param sessionId session to remove
     * @return true if the session was found, false otherwise.
     */
    public boolean removeSession(UUID sessionId) {
        return execute("DELETE FROM Session WHERE session_uuid = ?", sessionId) > 0;
    }

    /**
     * Login a user.
     *
     * @param credentials username and plain text password.
     * @return New user session, consisting of a @{@link UUID}
     * @throws DataAccessException if the username or password does not match.
     */ 
    public Session authenticate(Credentials credentials) {
        Supplier<DataAccessException> onError = () ->
                new DataAccessException("Username or password incorrect", ErrorType.DATA_QUALITY);
        long salt = new DataAccess<>(getDriverUrl(), (rs) -> rs.getLong(1))
                .queryFirst("SELECT salt FROM user WHERE username = ?", credentials.getUsername());
        UUID hash = credentials.generatePasswordHash(salt);
        User user = queryFirst("SELECT user_id, username, role FROM user, user_role " +
                "WHERE user_role.role_id = user.role_id " +
                "    AND username = ? " +
                "    AND password_hash = ?", credentials.getUsername(), hash);
        UUID sessionId = insert("INSERT INTO session (user_id) " +
                "SELECT user_id from USER WHERE username = ?", user.getName());
        return new Session(sessionId, user);
    }
    
    public boolean sharesRoute(int userID1, int userID2) {
        return execute("SELECT * FROM routes WHERE  user_ID = ? INTERSECT SELECT * FROM routes WHERE  user_ID = ?", userID1, userID2) > 0;
    }

}
