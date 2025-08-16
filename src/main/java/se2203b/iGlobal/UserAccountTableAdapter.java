package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserAccountTableAdapter is responsible for performing CRUD operations on the UserAccount table in the database.
 * It implements the DataStore interface and provides methods to add, update, find, and delete user account records.
 */
public class UserAccountTableAdapter implements DataStore {
    // Database connection string
    private String DB_URL = "jdbc:derby:iGlobalDB";
    // Connection object to interact with the database
    private Connection connection;

    /**
     * Constructor for UserAccountTableAdapter.
     * Establishes a database connection, optionally resets the UserAccount table, and creates the table if it does not exist.
     *
     * @param reset if true, the table will be dropped and recreated.
     * @throws SQLException if a database access error occurs.
     */
    public UserAccountTableAdapter(Boolean reset) throws SQLException {
        // Open a connection to the database.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // If reset flag is true, drop the UserAccount table if it exists.
        if (reset) {
            try {
                // Remove the UserAccount table.
                stmt.execute("DROP TABLE UserAccount");
            } catch (SQLException ex) {
                // Table did not exist; ignore the exception.
            }
        }
        // Try to create the UserAccount table.
        try {
            String command = "CREATE TABLE UserAccount ("
                    + "userAccountName VARCHAR(30) NOT NULL PRIMARY KEY, "
                    + "encryptedPassword VARCHAR(100) NOT NULL, "
                    + "passwordSalt VARCHAR(50) NOT NULL, "
                    + "accountType VARCHAR(10) NOT NULL"
                    + ")";
            stmt.execute(command);
        } catch (SQLException ex) {
            // If the table already exists, ignore the error.
        }
        // Close the connection after setting up the table.
        connection.close();
    }

    /**
     * Adds a new UserAccount record to the database.
     *
     * @param data an object that must be of type UserAccount.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void addNewRecord(Object data) throws SQLException {
        // Cast the input object to UserAccount.
        UserAccount userAccount = (UserAccount) data;
        // Open a new database connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Build the SQL INSERT command using the values from the UserAccount object.
        String command = "INSERT INTO UserAccount (userAccountName, encryptedPassword, passwordSalt, accountType) "
                + "VALUES ('"
                + userAccount.getUserAccountName() + "', '"
                + userAccount.getEncryptedPassword() + "', '"
                + userAccount.getPasswordSalt() + "', '"
                + userAccount.getAccountType() + "')";
        // Execute the SQL command.
        stmt.executeUpdate(command);
        // Close the connection.
        connection.close();
    }

    /**
     * Updates an existing UserAccount record in the database.
     *
     * @param data an object that must be of type UserAccount.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void updateRecord(Object data) throws SQLException {
        // Cast the input object to UserAccount.
        UserAccount userAccount = (UserAccount) data;
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Build the SQL UPDATE command using the values from the UserAccount object.
        String command = "UPDATE UserAccount SET "
                + "userAccountName = '" + userAccount.getUserAccountName() + "', "
                + "encryptedPassword = '" + userAccount.getEncryptedPassword() + "', "
                + "passwordSalt = '" + userAccount.getPasswordSalt() + "', "
                + "accountType = '" + userAccount.getAccountType() + "' "
                + "WHERE userAccountName = '" + userAccount.getUserAccountName() + "'";
        // Execute the UPDATE command.
        stmt.executeUpdate(command);
        // Close the connection.
        connection.close();
    }

    /**
     * Retrieves a UserAccount record from the database based on the given username.
     *
     * @param key the username (userAccountName) to search for.
     * @return a UserAccount object populated with data from the database.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Object findOneRecord(String key) throws SQLException {
        UserAccount userAccount = new UserAccount();
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Build the SQL SELECT command.
        String command = "SELECT * FROM UserAccount WHERE userAccountName = '" + key + "'";
        ResultSet rs = stmt.executeQuery(command);
        // Populate the UserAccount object with the retrieved data.
        while (rs.next()) {
            userAccount.setUserAccountName(rs.getString(1));
            userAccount.setEncryptedPassword(rs.getString(2));
            userAccount.setPasswordSalt(rs.getString(3));
            userAccount.setAccountType(rs.getString(4));
        }
        // Close the connection.
        connection.close();
        return userAccount;
    }

    /**
     * Not implemented.
     */
    @Override
    public Object findOneRecord(Object referencedObject) throws SQLException {
        // Not implemented
        return null;
    }

    /**
     * Deletes a UserAccount record from the database.
     *
     * @param data an object that must be of type UserAccount.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        UserAccount userAccount = (UserAccount) data;
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Build and execute the SQL DELETE command.
        stmt.executeUpdate("DELETE FROM UserAccount WHERE userAccountName = '"
                + userAccount.getUserAccountName() + "'");
        // Close the connection.
        connection.close();
    }

    /**
     * Not implemented.
     */
    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        // Not implemented
    }

    /**
     * Not implemented.
     */
    @Override
    public List<Object> getAllRecords() throws SQLException {
        // Not implemented (if needed, implement accordingly)
        return null;
    }

    /**
     * Not implemented.
     */
    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        // Not implemented
        return null;
    }

    /**
     * Returns a List of all user account keys (i.e., userAccountName values) from the database.
     *
     * @return List of userAccountName strings.
     * @throws SQLException if a database access error occurs.
     */
    public List<String> getAllKeys() throws SQLException {
        List<String> keys = new ArrayList<>();
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Execute the query to retrieve all userAccountName values.
        ResultSet rs = stmt.executeQuery("SELECT userAccountName FROM UserAccount");
        while (rs.next()) {
            keys.add(rs.getString("userAccountName"));
        }
        // Close the connection.
        connection.close();
        return keys;
    }

    /**
     * Implements the getKeys() method from the DataStore interface.
     *
     * @return List of userAccountName strings.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<String> getKeys() throws SQLException {
        return getAllKeys();
    }
}
