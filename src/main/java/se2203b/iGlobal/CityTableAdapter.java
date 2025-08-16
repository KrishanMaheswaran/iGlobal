package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The CityTableAdapter class provides the database access functionality for
 * the City entity in the iGlobal system. It implements the DataStore interface
 * to perform CRUD operations on the City table.
 */
public class CityTableAdapter implements DataStore {
    // Database URL for the iGlobalDB Derby database
    private final String DB_URL = "jdbc:derby:iGlobalDB";

    // Connection object to be used for database operations
    private final Connection connection;

    /**
     * Constructs a new CityTableAdapter.
     * If the reset flag is true, it drops the City table if it exists and then creates a new one.
     *
     * @param reset if true, the City table is dropped and re-created.
     * @throws SQLException if a database access error occurs.
     */
    public CityTableAdapter(boolean reset) throws SQLException {
        // Establish a connection to the database
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();

        // If reset is true, drop the City table if it exists
        if (reset) {
            try {
                stmt.execute("DROP TABLE City");
            } catch (SQLException e) {
                // Ignore the exception if the table does not exist.
            }
        }

        // Create the City table if it doesn't exist already
        try {
            String command = "CREATE TABLE City (" +
                    "cityName VARCHAR(50) NOT NULL, " +
                    "provinceCode VARCHAR(20) NOT NULL, " +
                    "PRIMARY KEY (cityName, provinceCode), " +
                    "FOREIGN KEY (provinceCode) REFERENCES Province(provinceCode)" +
                    ")";
            stmt.execute(command);
        } catch (SQLException e) {
            // Ignore the exception if the table already exists.
        }

        // Close the connection since we're done with initialization.
        connection.close();
    }

    /**
     * Adds a new City record to the database.
     *
     * @param data an object expected to be an instance of City.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void addNewRecord(Object data) throws SQLException {
        // Cast the data object to a City instance.
        City city = (City) data;
        // Open a new connection for the insertion.
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        // Build the SQL insert command using the city name and province code.
        String command = "INSERT INTO City (cityName, provinceCode) VALUES ('"
                + city.getCityName() + "', '" + city.getProvinceCode() + "')";
        stmt.executeUpdate(command);
        // Close the connection after the operation.
        conn.close();
    }

    /**
     * Retrieves a list of city names that belong to the specified province.
     *
     * @param provinceCode the code of the province.
     * @return a list of city names.
     * @throws SQLException if a database access error occurs.
     */
    public List<String> getCitiesInProvince(String provinceCode) throws SQLException {
        List<String> list = new ArrayList<>();
        // Open a new connection.
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        // Execute a query to retrieve city names for the given province code.
        ResultSet rs = stmt.executeQuery("SELECT cityName FROM City WHERE provinceCode = '" + provinceCode + "'");
        while (rs.next()) {
            // Add each city name to the list.
            list.add(rs.getString("cityName"));
        }
        // Close the connection and return the list.
        conn.close();
        return list;
    }

    /**
     * Checks if a city exists for a given province.
     *
     * @param cityName     the name of the city to check.
     * @param provinceCode the province code for the city.
     * @return true if the city exists, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean cityExists(String cityName, String provinceCode) throws SQLException {
        // Open a new connection.
        Connection conn = DriverManager.getConnection(DB_URL);
        // Use a PreparedStatement to safely query the database.
        String sql = "SELECT 1 FROM City WHERE cityName = ? AND provinceCode = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cityName);
        ps.setString(2, provinceCode);
        ResultSet rs = ps.executeQuery();
        // If rs.next() returns true, the city exists.
        boolean exists = rs.next();
        conn.close();
        return exists;
    }

    // The following methods are part of the DataStore interface.
    // They are not implemented in this adapter because they are not required for the current use case.

    @Override
    public Object findOneRecord(String key) {
        return null;
    }

    @Override
    public Object findOneRecord(Object object) {
        return null;
    }

    @Override
    public List<String> getKeys() {
        return null;
    }

    @Override
    public List<Object> getAllRecords() {
        return null;
    }

    @Override
    public List<Object> getAllRecords(Object object) {
        return null;
    }

    @Override
    public void updateRecord(Object data) {}

    @Override
    public void deleteOneRecord(Object data) {}

    @Override
    public void deleteRecords(Object referencedObject) {}
}
