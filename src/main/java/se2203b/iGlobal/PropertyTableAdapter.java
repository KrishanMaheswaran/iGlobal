package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The PropertyTableAdapter class is responsible for handling CRUD operations
 * for the PROPERTY table in the iGlobalDB database. It implements the DataStore
 * interface and provides methods for adding, deleting, and retrieving Property records.
 */
public class PropertyTableAdapter implements DataStore {
    // Connection to the database.
    private final Connection connection;

    /**
     * Constructor for PropertyTableAdapter.
     *
     * @param conn  A live Connection object to the iGlobalDB database.
     * @param reset If true, the PROPERTY table will be dropped (if it exists)
     *              and recreated.
     * @throws SQLException if a database access error occurs.
     */
    public PropertyTableAdapter(Connection conn, Boolean reset) throws SQLException {
        this.connection = conn;
        if (reset) {
            // Create a Statement to execute SQL commands.
            Statement stmt = connection.createStatement();
            try {
                // Attempt to drop the PROPERTY table if it exists.
                stmt.execute("DROP TABLE PROPERTY");
            } catch (SQLException ex) {
                // Ignore the error if the table does not exist.
            }
            // Create the PROPERTY table with columns for various property attributes.
            stmt.execute("CREATE TABLE PROPERTY (" +
                    "ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "TYPE VARCHAR(50), " +
                    "LOTSIZE DOUBLE, " +
                    "SQUAREFOOTAGE DOUBLE, " +
                    "BEDROOMS INT, " +
                    "BATHROOMS DOUBLE, " +
                    "YEARBUILT INT, " +
                    "PRICE DOUBLE, " +
                    "PROVINCE VARCHAR(20), " +
                    "CITY VARCHAR(50), " +
                    "ADDRESS VARCHAR(100), " +
                    "POSTALCODE VARCHAR(20), " +
                    "AMENITIES VARCHAR(200), " +
                    "DESCRIPTION VARCHAR(500))"
            );
        }
    }

    /**
     * Inserts a new Property record into the database.
     *
     * @param data A Property object containing the property details.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void addNewRecord(Object data) throws SQLException {
        // Validate that the data is a Property object.
        if (!(data instanceof Property))
            throw new IllegalArgumentException("Invalid type");
        Property p = (Property) data;

        // SQL insert statement with parameter placeholders.
        String insert = "INSERT INTO PROPERTY (TYPE, LOTSIZE, SQUAREFOOTAGE, BEDROOMS, BATHROOMS, YEARBUILT, PRICE, " +
                "PROVINCE, CITY, ADDRESS, POSTALCODE, AMENITIES, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // Prepare the statement and set parameters based on the Property object.
        PreparedStatement ps = connection.prepareStatement(insert);
        ps.setString(1, p.getPropertyType());
        ps.setDouble(2, p.getLotSize());
        ps.setDouble(3, p.getSquareFootage());
        ps.setInt(4, p.getBedrooms());
        ps.setDouble(5, p.getBathrooms());
        ps.setInt(6, p.getYearBuilt());
        ps.setDouble(7, p.getPrice());
        ps.setString(8, p.getProvince());
        ps.setString(9, p.getCity());
        ps.setString(10, p.getAddress());
        ps.setString(11, p.getPostalCode());
        ps.setString(12, p.getAmenities());
        ps.setString(13, p.getDescription());
        // Execute the insertion.
        ps.executeUpdate();
    }

    /**
     * Update operation for a Property record.
     * Not implemented because editing is not supported by the UI.
     *
     * @param data A Property object containing updated details.
     * @throws SQLException Always throws an UnsupportedOperationException.
     */
    @Override
    public void updateRecord(Object data) throws SQLException {
        // Optional: Not required unless UI allows editing
        throw new UnsupportedOperationException("updateRecord not implemented.");
    }

    /**
     * Finds a Property record by its address.
     *
     * @param key The address of the property to find.
     * @return A Property object if found, or null if no matching record exists.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Object findOneRecord(String key) throws SQLException {
        // SQL query to find a property by its address.
        String sql = "SELECT * FROM PROPERTY WHERE ADDRESS = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, key);
        ResultSet rs = ps.executeQuery();

        // If a matching record is found, populate a Property object.
        if (rs.next()) {
            Property p = new Property();
            populateFromResult(p, rs);
            return p;
        }
        // If no record is found, return null.
        return null;
    }

    /**
     * Not implemented: Finds a record based on a referenced object.
     *
     * @param referencedObject The referenced object.
     * @return Not implemented.
     * @throws SQLException always throws an UnsupportedOperationException.
     */
    @Override
    public Object findOneRecord(Object referencedObject) throws SQLException {
        throw new UnsupportedOperationException("findOneRecord(Object) not implemented.");
    }

    /**
     * Deletes a Property record from the database based on its address.
     *
     * @param data A Property object to delete.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        // Validate that the object is a Property.
        if (!(data instanceof Property))
            throw new IllegalArgumentException("Invalid type");
        Property p = (Property) data;
        // Delete the record by its address.
        deletePropertyByAddress(p.getAddress());
    }

    /**
     * Not implemented: Deletes records based on a referenced object.
     *
     * @param referencedObject The referenced object.
     * @throws SQLException always throws an UnsupportedOperationException.
     */
    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        throw new UnsupportedOperationException("deleteRecords not implemented.");
    }

    /**
     * Retrieves a list of addresses for all Property records.
     *
     * @return A List of addresses as Strings.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<String> getKeys() throws SQLException {
        List<String> keys = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT ADDRESS FROM PROPERTY");

        // Add each address to the list.
        while (rs.next())
            keys.add(rs.getString("ADDRESS"));
        return keys;
    }

    /**
     * Retrieves all Property records as a list of Objects.
     *
     * @return A List of Property objects.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> result = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PROPERTY");

        // For each record in the result set, create a Property object and populate it.
        while (rs.next()) {
            Property p = new Property();
            populateFromResult(p, rs);
            result.add(p);
        }

        return result;
    }

    /**
     * Not implemented: Retrieves all records based on a referenced object.
     *
     * @param referencedObject The referenced object.
     * @return Not implemented.
     * @throws SQLException always throws an UnsupportedOperationException.
     */
    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        throw new UnsupportedOperationException("getAllRecords(Object) not implemented.");
    }

    /**
     * Retrieves all Property records as an ObservableList (useful for TableView).
     *
     * @return An ObservableList of Property objects.
     * @throws SQLException if a database access error occurs.
     */
    public ObservableList<Property> getAllProperties() throws SQLException {
        ObservableList<Property> list = FXCollections.observableArrayList();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PROPERTY");

        // Populate the ObservableList with Property objects.
        while (rs.next()) {
            Property p = new Property();
            populateFromResult(p, rs);
            list.add(p);
        }

        return list;
    }

    /**
     * Deletes a Property record based on its address.
     *
     * @param address The address of the property to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deletePropertyByAddress(String address) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM PROPERTY WHERE ADDRESS = ?");
        ps.setString(1, address);
        ps.executeUpdate();
    }

    /**
     * Helper method to populate a Property object from a ResultSet.
     *
     * @param p  The Property object to populate.
     * @param rs The ResultSet containing the property data.
     * @throws SQLException if a database access error occurs.
     */
    private void populateFromResult(Property p, ResultSet rs) throws SQLException {
        p.setPropertyType(rs.getString("TYPE"));
        p.setLotSize(rs.getDouble("LOTSIZE"));
        p.setSquareFootage(rs.getDouble("SQUAREFOOTAGE"));
        p.setBedrooms(rs.getInt("BEDROOMS"));
        p.setBathrooms(rs.getDouble("BATHROOMS"));
        p.setYearBuilt(rs.getInt("YEARBUILT"));
        p.setPrice(rs.getDouble("PRICE"));
        p.setProvince(rs.getString("PROVINCE"));
        p.setCity(rs.getString("CITY"));
        p.setAddress(rs.getString("ADDRESS"));
        p.setPostalCode(rs.getString("POSTALCODE"));
        p.setAmenities(rs.getString("AMENITIES"));
        p.setDescription(rs.getString("DESCRIPTION"));
    }
}
