package se.lth.base.server.data;

import se.lth.base.server.database.DataAccess;
import se.lth.base.server.database.DataAccessException;
import se.lth.base.server.database.ErrorType;
import se.lth.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

//Not implemented yet

public class BookingRequestsDataAccess extends DataAccess<BookingRequests> {


    private static class BookingMapper implements Mapper<BookingRequests> {
        // Feel free to change this to a lambda expression
        @Override
        public BookingRequests map(ResultSet resultSet) throws SQLException {
            return new BookingRequests(//not implemented yet
            		);
        }

    }
    public BookingRequestsDataAccess(String driverUrl) {
        super(driverUrl, new BookingMapper());
    }

    /**
     * Add a new user to the system.
     *
     * @param credentials of the new BookingRequests, containing name, role, and password.
     * @throws DataAccessException if duplicated BookingRequestsname or too short BookingRequests names.
     */
    public BookingRequests addBookingRequests(Credentials credentials) {
        long salt = Credentials.generateSalt();
        int BookingRequestsId = insert("INSERT INTO BookingRequests (role_id, BookingRequestsname, password_hash, salt) VALUES ((" +
                        "SELECT role_id FROM BookingRequests_role WHERE BookingRequests_role.role=?),?,?,?)",
                credentials.getRole().name(), credentials.getBookingRequestsname(), credentials.generatePasswordHash(salt), salt);
        return new BookingRequests(userId, credentials.getRole(), credentials.getUsername());
    }

    public BookingRequests updateBookingRequests(int userId, Credentials credentials) {
        if (credentials.hasPassword()) {
            long salt = Credentials.generateSalt();
            execute("UPDATE BookingRequests SET username = ?, password_hash = ?, salt = ?, role_id = (" +
                            "    SELECT user_role.role_id FROM user_role WHERE user_role.role = ?) " +
                            "WHERE user_id = ?",
                    credentials.getUsername(), credentials.generatePasswordHash(salt), salt,
                    credentials.getRole().name(), userId);
        } else {
            execute("UPDATE user SET username = ?, role_id = (" +
                            "    SELECT user_role.role_id FROM user_role WHERE user_role.role = ?) " +
                            "WHERE user_id = ?",
                    credentials.getUsername(), credentials.getRole().name(), userId);
        }
        return getUser(userId);
    }

    public BookingRequests getBookingRequests(int userId) {
        return queryFirst("SELECT user_id, role, username FROM user, user_role " +
                "WHERE user.user_id = ? AND user.role_id = user_role.role_id", userId);
    }

    public boolean deleteBookingRequests(int userId) {
        return execute("DELETE FROM user WHERE user_id = ?", userId) > 0;
    }

    /**
     * @return all users in the system.
     */
    public List<BookingRequests> getBookingRequestss() {
        return query("SELECT user_id, username, role FROM user, user_role " +
                "WHERE user.role_id = user_role.role_id");
    }

    /**
     * Fetch session and the corresponding user.
     *
     * @param sessionId globally unqiue identifier, stored in the client.
     * @return session object wrapping the user.
     * @throws DataAccessException if the session is not found.
     */
    public Session getSession(UUID sessionId) {
        User user = queryFirst("SELECT user.user_id, username, role FROM user, user_role, session " +
                "WHERE user_role.role_id = user.role_id " +
                "    AND session.user_id = user.user_id " +
                "    AND session.session_uuid = ?", sessionId);
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
        return execute("DELETE FROM session WHERE session_uuid = ?", sessionId) > 0;
    }

    /**
     * Login a user.
     *
     * @param credentials username and plain text password.
     * @return New user session, consisting of a @{@link UUID} and @{@link User}.
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
}

