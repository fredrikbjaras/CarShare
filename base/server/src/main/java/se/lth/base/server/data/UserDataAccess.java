package se.lth.base.server.data;

import se.lth.base.server.database.DataAccess;
import se.lth.base.server.database.DataAccessException;
import se.lth.base.server.database.ErrorType;
import se.lth.base.server.database.Mapper;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/* @author Jonathan Jakobsson
* @see DataAccess
*/
public class UserDataAccess extends DataAccess<User> {

	private static class UserMapper implements Mapper<User> {
		@Override
		public User map(ResultSet resultSet) throws SQLException {
			return new User(resultSet.getInt("userID"),
					resultSet.getString("userName"), 
					resultSet.getObject("password_hash").toString(),
					resultSet.getString("phoneNr"), 
					resultSet.getBoolean("isAdmin"),
					resultSet.getString("description"),
					resultSet.getString("profilePicture"));
		}
	}

	public UserDataAccess(String driverUrl) {
		super(driverUrl, new UserMapper());
	}

	/**
	 * Add a new user to the system.
	 *
	 * @param userName
	 *            password phoneNr and isAdmin of a new User
	 * @return Key (userID) of user created.
	 * @throws DataAccessException
	 *             if duplicated userName,too short password or too short user
	 *             names. Note: removed param Credentials as it is only user for
	 *             security.
	 */
	public User addUser(String userName, String password, String phoneNr, boolean isAdmin, String description, String profilePicture) {
		long salt = Credentials.generateSalt();
		int userId = insert("INSERT INTO User (userName,salt,password_hash,phoneNr,isAdmin, description, profilePicture) VALUES (?,?,?,?,?,?,?)", userName, 1, UUID.randomUUID(), phoneNr, isAdmin, "", "");
		User temp = new User(userId, userName, password, phoneNr, isAdmin);
		execute("UPDATE User SET  password_hash = ?, salt = ?"
				+ "WHERE userID = ?", generatePasswordHash(salt, temp.getPassword()), salt, userId);
		return temp;
	}

	/**
	 * Update information about a user in the system.
	 *
	 * @param userName
	 *            password phoneNr and isAdmin of a existing User
	 * @return Returns the user created.
	 * @throws DataAccessException
	 *             if duplicated userName,too short password,userId does not exist
	 *             or too short user names. Note: removed param Credentials as it is
	 *             only user for security.
	 */
	public User updateUser(int userID, String userName, String password, String phoneNr, String profilePicture, String description) {
		User user = getUser(userID);
		if (password != null && password != "" && password.length() > 7) {
			long salt = Credentials.generateSalt();
			execute("UPDATE User SET  userName= ?, salt = ?,password_hash = ?, profilePicture = ?,description = ?, phoneNr = ?"
					+ "WHERE userID = ?", userName, salt, generatePasswordHash(salt,password), profilePicture, description, phoneNr, userID);
			System.out.println(password);
			System.out.println("updating to: " + salt + ", " + generatePasswordHash(salt, password));
		} else {
			execute("UPDATE User SET userName = ?, profilePicture = ?, description = ?, phoneNr = ?, description = ?, profilePicture = ?"
					+ "WHERE userID = ?", userName, profilePicture, description, phoneNr, userID, profilePicture);
		}
		user = getUser(userID);
		return user;
	}

	/**
	 * Retrieve a user with a specific userId.
	 *
	 * @param userID
	 * @return Returns the user with the ID.
	 * @throws DataAccessException
	 *             if userID does not exist.
	 * 
	 */
	public User getUser(int userID) {
		return queryFirst("SELECT * FROM User " + "WHERE userID = ?", userID);

	}

	public List<User> getUsersByName(String name) {
		return query("SELECT userID, username, role FROM user, user_role "
				+ "WHERE user.role_id = user_role.role_id AND username LIKE ?%", name);
	}

	public List<User> getUsersByNumber(String number) {
		return query("SELECT userID, username, role FROM user, user_role "
				+ "WHERE user.role_id = user_role.role_id AND number = ?", number); // Alltid singular? Namn på number i
																					// db?
	}

	public List<User> getUsersByRouteId(String routeId) {
		return query("SELECT passengers FROM Routes WHERE routeID = ? ", routeId);
		// Kommer passengers som List<User då? Står array
	}

	/*
	 * @param userName
	 * 
	 * @return Returns the user with the ID.
	 * 
	 * @throws DataAccessException if userID does not exist.
	 * 
	 */
	public User getUserWithName(String userName) {
		return queryFirst("SELECT * FROM User " + "WHERE userName = ?",
				userName);
	}

	/**
	 * Delete a user with a specific userId.
	 *
	 * @param userID
	 * @return Returns true if successful.
	 * @throws DataAccessException
	 *             if userID does not exist.
	 * 
	 */
	public boolean deleteUser(int userID) {
		return execute("DELETE FROM User WHERE UserID = ?", userID) > 0;
	}

	/**
	 * @return all users in the system.
	 */
	public List<User> getUsers() {
		return query("SELECT * FROM User");
	}

	/**
	 * Fetch session and the corresponding user.
	 *
	 * @param sessionId
	 *            globally unqiue identifier, stored in the client.
	 * @return session object wrapping the user.
	 * @throws DataAccessException
	 *             if the session is not found.
	 */
	public Session getSession(UUID sessionId) {
		User user = queryFirst(
				"SELECT User.userID, userName, User.isAdmin, User.password_hash, User.phoneNr , User.profilePicture, User.description FROM user, Session " + "WHERE Session.isAdmin = User.isAdmin "
						+ "    AND Session.userID = User.userID " + "    AND Session.session_uuid = ?",
				sessionId);
		execute("UPDATE session SET last_seen = CURRENT_TIMESTAMP() " + "WHERE session_uuid = ?", sessionId);
		return new Session(sessionId, user);
	}

	/**
	 * Logout a user. This method is idempotent, meaning it is safe to repeat
	 * indefinitely.
	 *
	 * @param sessionId
	 *            session to remove
	 * @return true if the session was found, false otherwise.
	 */
	public boolean removeSession(UUID sessionId) {
		return execute("DELETE FROM Session WHERE session_uuid = ?", sessionId) > 0;
	}

	/**
	 * Login a user.
	 *
	 * @param credentials
	 *            username and plain text password.
	 * @return New user session, consisting of a @{@link UUID}
	 * @throws DataAccessException
	 *             if the username or password does not match.
	 */
	public Session authenticate(Credentials credentials) {
		Supplier<DataAccessException> onError = () -> new DataAccessException("Username or password incorrect",
				ErrorType.DATA_QUALITY);
		long salt = new DataAccess<>(getDriverUrl(), (rs) -> rs.getLong(1))
				.queryFirst("SELECT salt FROM user WHERE username = ?", credentials.getUsername());
		UUID hash = credentials.generatePasswordHash(salt);
		System.out.println("updating to: " + salt + ", " + hash);
		User user = getUserWithName(credentials.getUsername());
		UUID pwHash= new DataAccess<>(getDriverUrl(), (rs) -> ((UUID) rs.getObject("password_hash")))
				.queryFirst("SELECT password_hash FROM user WHERE userID = ?", user.getUserID());
		if (hash.compareTo(pwHash) != 0) {
			throw new DataAccessException("Username or password incorrect", ErrorType.DATA_QUALITY);
		}
		boolean isAdmin = user.getIsAdmin();
		UUID sessionID = insert("INSERT INTO Session (userID,isAdmin) VALUES (?,?)", user.getUserID(), isAdmin);
		return new Session(sessionID, user);
	}

	public boolean sharesRoute(int userID1, int userID2) {
		return execute("SELECT * FROM routes WHERE  userID = ? INTERSECT SELECT * FROM routes WHERE  userID = ?",
				userID1, userID2) > 0;
	}
	
	private static final int SIZE = 256;
    private static final int ITERATION_COST = 16;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     * Hash password using hashing algorithm intended for this purpose.
     *
     * @return base64 encoded hash result.
     */
    UUID generatePasswordHash(long salt, String password) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(),
                    ByteBuffer.allocate(8).putLong(salt).array(),
                    ITERATION_COST, SIZE);
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] blob = f.generateSecret(spec).getEncoded();
            LongBuffer lb = ByteBuffer.wrap(blob).asLongBuffer();
            return new UUID(lb.get(), lb.get());
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
        } catch (InvalidKeySpecException ex) {
            throw new IllegalStateException("Invalid SecretKeyFactory", ex);
        }
    }

}
