package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProvinceTableAdapter manages the persistence of Province data in the iGlobalDB.
 * It implements the DataStore interface and supports basic CRUD operations for Province entries.
 */
public class ProvinceTableAdapter implements DataStore {
    // Database URL for the Derby iGlobalDB.
    private final String DB_URL = "jdbc:derby:iGlobalDB";

    /**
     * Constructor that initializes the Province table.
     * If reset is true, the table is dropped (if it exists) and recreated.
     *
     * @param reset if true, drops the existing table and recreates it.
     * @throws SQLException if a database access error occurs.
     */
    public ProvinceTableAdapter(boolean reset) throws SQLException {
        // Open a connection to the database.
        Connection connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();

        // If reset is true, attempt to drop the Province table.
        if (reset) {
            try {
                stmt.execute("DROP TABLE Province");
            } catch (SQLException e) {
                // Ignore the exception if the table does not exist.
            }
        }

        // Attempt to create the Province table.
        try {
            String command = "CREATE TABLE Province (" +
                    "provinceCode VARCHAR(20) NOT NULL PRIMARY KEY" +
                    ")";
            stmt.execute(command);
        } catch (SQLException e) {
            // If the table already exists, ignore the exception.
        }

        // Close the connection.
        connection.close();
    }

    /**
     * Adds a new province record to the database.
     *
     * @param data a String representing the province code.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void addNewRecord(Object data) throws SQLException {
        // Cast the data object to a String (province code).
        String provinceCode = (String) data;
        // Open a new connection.
        Connection conn = DriverManager.getConnection(DB_URL);
        // Prepare the INSERT SQL statement using a PreparedStatement.
        String sql = "INSERT INTO Province (provinceCode) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, provinceCode);
        ps.executeUpdate();
        // Close the connection.
        conn.close();
    }

    /**
     * Not implemented: Finds a province record by a String key.
     *
     * @param key the key to search for.
     * @return null.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Object findOneRecord(String key) throws SQLException {
        // Not implemented.
        return null;
    }

    /**
     * Not implemented: Finds a province record by an object key.
     *
     * @param key the key to search for.
     * @return null.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Object findOneRecord(Object key) throws SQLException {
        // Not implemented.
        return null;
    }

    /**
     * Retrieves all province codes from the Province table.
     *
     * @return a List of province codes.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<String> getKeys() throws SQLException {
        List<String> list = new ArrayList<>();
        // Open a new connection.
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        // Execute a query to select all province codes.
        ResultSet rs = stmt.executeQuery("SELECT provinceCode FROM Province");
        while (rs.next()) {
            list.add(rs.getString("provinceCode"));
        }
        // Close the connection.
        conn.close();
        return list;
    }

    /**
     * Retrieves all province records as a list of Objects.
     * In this implementation, each province record is represented as a String (province code).
     *
     * @return a List of province codes.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        // Retrieve keys (province codes) and add them to the list.
        for (String code : getKeys()) {
            list.add(code);
        }
        return list;
    }

    /**
     * Not implemented: Retrieves records based on a referenced object.
     *
     * @param referencedObject not used.
     * @return null.
     * @throws SQLException not implemented.
     */
    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        // Not implemented.
        return null;
    }

    /**
     * Not implemented: Updates a province record.
     *
     * @param data the data to update.
     * @throws SQLException not implemented.
     */
    @Override
    public void updateRecord(Object data) throws SQLException {
        // Not implemented.
    }

    /**
     * Not implemented: Deletes a province record.
     *
     * @param data the data to delete.
     * @throws SQLException not implemented.
     */
    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        // Not implemented.
    }

    /**
     * Not implemented: Deletes multiple records based on a referenced object.
     *
     * @param referencedObject not used.
     * @throws SQLException not implemented.
     */
    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        // Not implemented.
    }
}
