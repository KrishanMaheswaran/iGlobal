package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The PropertyTypeTableAdapter class manages the persistence of PropertyType
 * entities in the iGlobal database. It implements the DataStore interface and
 * provides methods to create, read, update, and delete records from the PropertyType table.
 */
public class PropertyTypeTableAdapter implements DataStore {

    // Database connection URL for the iGlobalDB Derby database.
    private String DB_URL = "jdbc:derby:iGlobalDB";

    // Connection object used for database operations.
    private Connection connection;

    /**
     * Constructor for the PropertyTypeTableAdapter.
     * Establishes a connection to the database and, if reset is true, drops the existing
     * PropertyType table and creates a new one.
     *
     * @param reset if true, the existing PropertyType table is dropped and recreated.
     * @throws SQLException if a database access error occurs.
     */
    public PropertyTypeTableAdapter(Boolean reset) throws SQLException {
        // Open a connection to the database.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();

        // If reset flag is true, drop the existing table.
        if (reset) {
            try {
                stmt.execute("DROP TABLE PropertyType");
            } catch (SQLException ex) {
                // If the table does not exist, ignore the exception.
            }
        }

        // Attempt to create the PropertyType table.
        try {
            String command = "CREATE TABLE PropertyType (" +
                    "typeCode VARCHAR(9) NOT NULL PRIMARY KEY, " +
                    "typeName VARCHAR(50)" +
                    ")";
            stmt.execute(command);
        } catch (SQLException ex) {
            // If the table already exists, ignore the exception.
        }

        // Close the connection after table creation.
        connection.close();
    }

    /**
     * Adds a new PropertyType record to the database.
     *
     * @param data an object expected to be of type PropertyType.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void addNewRecord(Object data) throws SQLException {
        // Cast the object to a PropertyType.
        PropertyType propertyType = (PropertyType) data;
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        try {
            // Build and execute the INSERT SQL statement.
            String command = "INSERT INTO PropertyType (typeCode, typeName) VALUES ('" +
                    propertyType.getTypeCode() + "', '" +
                    propertyType.getTypeName() + "')";
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            // Wrap and re-throw the exception as a runtime exception.
            throw new RuntimeException(e);
        } finally {
            // Ensure the connection is closed.
            connection.close();
        }
    }

    /**
     * Updates an existing PropertyType record in the database.
     *
     * @param data an object expected to be of type PropertyType with updated data.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void updateRecord(Object data) throws SQLException {
        // Cast the object to a PropertyType.
        PropertyType propertyType = (PropertyType) data;
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Build the UPDATE SQL command using the typeCode as the key.
        String command = "UPDATE PropertyType SET typeCode = '" + propertyType.getTypeCode() + "', " +
                "typeName = '" + propertyType.getTypeName() + "' " +
                "WHERE typeCode = '" + propertyType.getTypeCode() + "'";
        stmt.executeUpdate(command);
        // Close the connection.
        connection.close();
    }

    /**
     * Deletes a specific PropertyType record from the database.
     *
     * @param data an object expected to be of type PropertyType.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        // Cast the object to a PropertyType.
        PropertyType propertyType = (PropertyType) data;
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Execute the DELETE SQL command using the typeCode.
        stmt.executeUpdate("DELETE FROM PropertyType WHERE typeCode = '" + propertyType.getTypeCode() + "'");
        connection.close();
    }

    /**
     * Not implemented. Deletes multiple records based on a referenced object.
     *
     * @param referencedObject not used.
     * @throws SQLException not implemented.
     */
    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        // Not implemented.
    }

    /**
     * Retrieves a list of property type names from the database.
     *
     * @return a List of property type names.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<String> getKeys() throws SQLException {
        // Create a list to store the keys.
        List<String> list = new ArrayList<>();
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Execute a SELECT query to retrieve all type names.
        String command = "SELECT typeName FROM PropertyType";
        ResultSet rs = stmt.executeQuery(command);
        // Iterate through the result set and add each typeName to the list.
        while (rs.next()) {
            list.add(rs.getString(1));
        }
        // Close the connection.
        connection.close();
        return list;
    }

    /**
     * Finds a PropertyType record by its typeCode.
     *
     * @param key the typeCode to search for.
     * @return a PropertyType object with the matching typeCode, or an empty PropertyType if not found.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Object findOneRecord(String key) throws SQLException {
        // Create a new PropertyType object.
        PropertyType propertyType = new PropertyType();
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Execute a SELECT query for the given typeCode.
        String command = "SELECT * FROM PropertyType WHERE typeCode = '" + key + "'";
        ResultSet rs = stmt.executeQuery(command);
        // If a matching record is found, populate the PropertyType object.
        while (rs.next()) {
            propertyType.setTypeCode(rs.getString("typeCode"));
            propertyType.setTypeName(rs.getString("typeName"));
        }
        connection.close();
        return propertyType;
    }

    /**
     * Finds a PropertyType record by comparing the typeCode of a given PropertyType object.
     *
     * @param object a PropertyType object with a specified typeCode.
     * @return a PropertyType object with matching data.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Object findOneRecord(Object object) throws SQLException {
        // Create a new PropertyType object.
        PropertyType propertyType = new PropertyType();
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Build and execute the SELECT query using the typeCode from the provided PropertyType object.
        String command = "SELECT * FROM PropertyType WHERE typeCode = '" + ((PropertyType)object).getTypeCode() + "'";
        ResultSet rs = stmt.executeQuery(command);
        while (rs.next()) {
            propertyType.setTypeCode(rs.getString("typeCode"));
            propertyType.setTypeName(rs.getString("typeName"));
        }
        connection.close();
        return propertyType;
    }

    /**
     * Retrieves all PropertyType records from the database.
     *
     * @return a List of PropertyType objects.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<Object> getAllRecords() throws SQLException {
        // Create a list to store PropertyType objects.
        List<Object> list = new ArrayList<>();
        // Open a new connection.
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Execute a SELECT query to retrieve all records.
        String command = "SELECT * FROM PropertyType";
        ResultSet rs = stmt.executeQuery(command);
        // Populate each PropertyType object from the result set and add it to the list.
        while (rs.next()) {
            PropertyType propertyType = new PropertyType();
            propertyType.setTypeCode(rs.getString("typeCode"));
            propertyType.setTypeName(rs.getString("typeName"));
            list.add(propertyType);
        }
        connection.close();
        return list;
    }

    /**
     * Not implemented. Retrieves records based on a referenced object.
     *
     * @param referencedObject not used.
     * @return null.
     * @throws SQLException not implemented.
     */
    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        return null;
    }
}
