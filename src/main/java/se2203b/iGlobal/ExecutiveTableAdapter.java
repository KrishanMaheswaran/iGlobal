package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The ExecutiveTableAdapter class implements the DataStore interface to provide
 * CRUD operations for the Executive entity in the iGlobal system.
 * It interacts with a Derby database to perform operations such as adding,
 * deleting, and retrieving Executive records.
 */
public class ExecutiveTableAdapter implements DataStore {
    // The connection used to interact with the database.
    private final Connection connection;

    /**
     * Constructor for ExecutiveTableAdapter.
     * If reset is true, drops and recreates the EXECUTIVE table.
     *
     * @param conn  the database connection to use.
     * @param reset if true, resets (drops and creates) the EXECUTIVE table.
     * @throws SQLException if a database access error occurs.
     */
    public ExecutiveTableAdapter(Connection conn, boolean reset) throws SQLException {
        this.connection = conn;
        Statement stmt = connection.createStatement();
        if (reset) {
            // Attempt to drop the table if it exists; ignore any exception if it does not.
            try {
                stmt.execute("DROP TABLE EXECUTIVE");
            } catch (SQLException ex) {
                // Table does not exist, so ignore the exception.
            }
            // Create the EXECUTIVE table with the specified columns and primary key.
            stmt.execute("CREATE TABLE EXECUTIVE (" +
                    "ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "FIRSTNAME VARCHAR(50), " +
                    "LASTNAME VARCHAR(50), " +
                    "EMAIL VARCHAR(100), " +
                    "PHONE VARCHAR(20), " +
                    "PRIVATEPHONE VARCHAR(20), " +
                    "PRIMARY KEY (ID)" +
                    ")");
        }
    }

    /**
     * Adds a new Executive record to the database.
     *
     * @param data the Executive object to add.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void addNewRecord(Object data) throws SQLException {
        if (!(data instanceof Executive)) {
            throw new IllegalArgumentException("Invalid type, expected Executive");
        }
        Executive e = (Executive) data;
        // SQL insert statement with placeholders for parameters.
        String insert = "INSERT INTO EXECUTIVE (FIRSTNAME, LASTNAME, EMAIL, PHONE, PRIVATEPHONE) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(insert);
        // Set parameters based on the properties of the Executive object.
        ps.setString(1, e.getFirstName());
        ps.setString(2, e.getLastName());
        ps.setString(3, e.getEmailAddress());
        ps.setString(4, e.getPhoneNumber());
        ps.setString(5, e.getPrivatePhoneNumber());
        ps.executeUpdate();
    }

    /**
     * This method is not implemented for updating Executive records.
     *
     * @param data the object containing updated data.
     * @throws SQLException always thrown as updateRecord is not implemented.
     */
    @Override
    public void updateRecord(Object data) throws SQLException {
        throw new UnsupportedOperationException("updateRecord not implemented.");
    }

    /**
     * This method is not implemented for finding a record using a String key.
     *
     * @param key the key to search by.
     * @return never returns normally.
     * @throws SQLException always thrown as findOneRecord(String) is not implemented.
     */
    @Override
    public Object findOneRecord(String key) throws SQLException {
        throw new UnsupportedOperationException("findOneRecord(String) not implemented.");
    }

    /**
     * This method is not implemented for finding a record using an Object reference.
     *
     * @param referencedObject the object to search by.
     * @return never returns normally.
     * @throws SQLException always thrown as findOneRecord(Object) is not implemented.
     */
    @Override
    public Object findOneRecord(Object referencedObject) throws SQLException {
        throw new UnsupportedOperationException("findOneRecord(Object) not implemented.");
    }

    /**
     * Deletes an Executive record from the database.
     * Uses the EMAIL field as the unique identifier.
     *
     * @param data the Executive object to delete.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        if (!(data instanceof Executive)) {
            throw new IllegalArgumentException("Invalid type, expected Executive");
        }
        Executive e = (Executive) data;
        // SQL delete statement that deletes based on the EMAIL column.
        String delete = "DELETE FROM EXECUTIVE WHERE EMAIL = ?";
        PreparedStatement ps = connection.prepareStatement(delete);
        ps.setString(1, e.getEmailAddress());
        ps.executeUpdate();
    }

    /**
     * This method is not implemented for deleting multiple records.
     *
     * @param referencedObject the object used as a reference for deletion.
     * @throws SQLException always thrown as deleteRecords is not implemented.
     */
    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        throw new UnsupportedOperationException("deleteRecords not implemented.");
    }

    /**
     * Retrieves a list of keys (emails) for all Executive records.
     *
     * @return a List of email addresses.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<String> getKeys() throws SQLException {
        List<String> list = new ArrayList<>();
        Statement stmt = connection.createStatement();
        // Execute a query to get all email addresses from the EXECUTIVE table.
        ResultSet rs = stmt.executeQuery("SELECT EMAIL FROM EXECUTIVE");
        while (rs.next()) {
            list.add(rs.getString("EMAIL"));
        }
        return list;
    }

    /**
     * Retrieves all Executive records from the database.
     *
     * @return a List of Executive objects.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        Statement stmt = connection.createStatement();
        // Execute a query to retrieve all columns from the EXECUTIVE table.
        ResultSet rs = stmt.executeQuery("SELECT * FROM EXECUTIVE");
        while (rs.next()) {
            Executive e = new Executive();
            e.setFirstName(rs.getString("FIRSTNAME"));
            e.setLastName(rs.getString("LASTNAME"));
            e.setEmailAddress(rs.getString("EMAIL"));
            e.setPhoneNumber(rs.getString("PHONE"));
            e.setPrivatePhoneNumber(rs.getString("PRIVATEPHONE"));
            list.add(e);
        }
        return list;
    }

    /**
     * This method is not implemented for retrieving records based on a referenced object.
     *
     * @param referencedObject the object used for filtering records.
     * @return never returns normally.
     * @throws SQLException always thrown as getAllRecords(Object) is not implemented.
     */
    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        throw new UnsupportedOperationException("getAllRecords(Object) not implemented.");
    }

    /**
     * Convenience method to return an ObservableList of Executive objects.
     * This is used primarily for JavaFX TableView data binding.
     *
     * @return an ObservableList containing all Executive records.
     * @throws SQLException if a database access error occurs.
     */
    public ObservableList<Executive> getAllExecutives() throws SQLException {
        ObservableList<Executive> list = FXCollections.observableArrayList();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM EXECUTIVE");
        while (rs.next()) {
            Executive e = new Executive();
            e.setFirstName(rs.getString("FIRSTNAME"));
            e.setLastName(rs.getString("LASTNAME"));
            e.setEmailAddress(rs.getString("EMAIL"));
            e.setPhoneNumber(rs.getString("PHONE"));
            e.setPrivatePhoneNumber(rs.getString("PRIVATEPHONE"));
            list.add(e);
        }
        return list;
    }
}
