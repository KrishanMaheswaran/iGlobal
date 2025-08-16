package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The AgentTableAdapter class implements the DataStore interface and provides database
 * operations (CRUD) for the Agent entity in the iGlobal system.
 *
 * This class handles adding, deleting, and retrieving Agent records from the database.
 * It uses Java's JDBC API to interact with the underlying Derby database.
 */
public class AgentTableAdapter implements DataStore {
    // Holds the database connection for performing SQL operations.
    private final Connection connection;

    /**
     * Constructor for AgentTableAdapter.
     *
     * @param conn  the JDBC Connection object
     * @param reset if true, drops the existing AGENT table and recreates it; otherwise, uses the existing table.
     * @throws SQLException if a database access error occurs.
     */
    public AgentTableAdapter(Connection conn, boolean reset) throws SQLException {
        this.connection = conn;
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Attempt to drop the AGENT table if it exists.
                stmt.execute("DROP TABLE AGENT");
            } catch (SQLException ex) {
                // Ignore exception if the table does not exist.
            }
            // Create the AGENT table with necessary columns.
            stmt.execute("CREATE TABLE AGENT (" +
                    "ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "FIRSTNAME VARCHAR(50), " +
                    "LASTNAME VARCHAR(50), " +
                    "EMAIL VARCHAR(100), " +
                    "PHONE VARCHAR(20), " +
                    "LICENSENUMBER VARCHAR(30), " +
                    "SPECIALIZATION VARCHAR(50) " +
                    ")");
        }
    }

    /**
     * Adds a new Agent record to the database.
     *
     * @param data an object that should be an instance of Agent.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void addNewRecord(Object data) throws SQLException {
        if (!(data instanceof Agent)) {
            throw new IllegalArgumentException("Invalid type, expected Agent");
        }
        Agent a = (Agent) data;
        // Prepare SQL INSERT statement with parameter placeholders.
        String insert = "INSERT INTO AGENT (FIRSTNAME, LASTNAME, EMAIL, PHONE, LICENSENUMBER, SPECIALIZATION) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(insert);
        // Set the values for the prepared statement from the Agent object.
        ps.setString(1, a.getFirstName());
        ps.setString(2, a.getLastName());
        ps.setString(3, a.getEmailAddress());
        ps.setString(4, a.getPhoneNumber());
        ps.setString(5, a.getLicenseNumber());
        ps.setString(6, a.getSpecialization());
        // Execute the insertion.
        ps.executeUpdate();
    }

    /**
     * Updates an existing Agent record.
     *
     * Currently not implemented. To support editing Agent data,
     * you would add the SQL UPDATE logic here.
     *
     * @param data an object representing the Agent to update.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void updateRecord(Object data) throws SQLException {
        // Editing agent data is not implemented.
        throw new UnsupportedOperationException("updateRecord not implemented.");
    }

    /**
     * Finds an Agent record based on a unique key.
     *
     * This method is not implemented. You could implement searching by ID or license number.
     *
     * @param key the unique key to search for.
     * @return an Agent record (if found).
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Object findOneRecord(String key) throws SQLException {
        throw new UnsupportedOperationException("findOneRecord(String) not implemented.");
    }

    /**
     * Finds an Agent record based on a referenced object.
     *
     * This method is not implemented.
     *
     * @param referencedObject an object to reference.
     * @return an Agent record (if found).
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Object findOneRecord(Object referencedObject) throws SQLException {
        throw new UnsupportedOperationException("findOneRecord(Object) not implemented.");
    }

    /**
     * Deletes an Agent record from the database.
     *
     * @param data an object that should be an instance of Agent.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        if (!(data instanceof Agent)) {
            throw new IllegalArgumentException("Invalid type, expected Agent");
        }
        Agent a = (Agent) data;
        // Prepare SQL DELETE statement using the license number as the unique identifier.
        String delete = "DELETE FROM AGENT WHERE LICENSENUMBER = ?";
        PreparedStatement ps = connection.prepareStatement(delete);
        ps.setString(1, a.getLicenseNumber());
        ps.executeUpdate();
    }

    /**
     * Deletes multiple Agent records.
     *
     * This method is not typically used for individual operations.
     *
     * @param referencedObject an object to reference for deletion.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        throw new UnsupportedOperationException("deleteRecords not implemented.");
    }

    /**
     * Retrieves a list of keys (license numbers) for all Agent records.
     *
     * @return a List of Strings representing the license numbers of Agents.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<String> getKeys() throws SQLException {
        List<String> list = new ArrayList<>();
        Statement stmt = connection.createStatement();
        // Execute SQL query to retrieve license numbers.
        ResultSet rs = stmt.executeQuery("SELECT LICENSENUMBER FROM AGENT");
        while (rs.next()) {
            list.add(rs.getString("LICENSENUMBER"));
        }
        return list;
    }

    /**
     * Retrieves all Agent records as a List of Objects.
     *
     * @return a List of Agent objects.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        Statement stmt = connection.createStatement();
        // Execute SQL query to retrieve all columns from the AGENT table.
        ResultSet rs = stmt.executeQuery("SELECT * FROM AGENT");
        while (rs.next()) {
            Agent a = new Agent();
            // Populate the Agent object with data from the ResultSet.
            a.setFirstName(rs.getString("FIRSTNAME"));
            a.setLastName(rs.getString("LASTNAME"));
            a.setEmailAddress(rs.getString("EMAIL"));
            a.setPhoneNumber(rs.getString("PHONE"));
            a.setLicenseNumber(rs.getString("LICENSENUMBER"));
            a.setSpecialization(rs.getString("SPECIALIZATION"));
            list.add(a);
        }
        return list;
    }

    /**
     * This method is not implemented. It could be used to retrieve records based on a referenced object.
     *
     * @param referencedObject an object used for filtering records.
     * @return nothing, as it is not implemented.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        throw new UnsupportedOperationException("getAllRecords(Object) not implemented.");
    }

    /**
     * Convenience method to retrieve all Agent records as an ObservableList.
     * This is particularly useful for populating JavaFX TableViews.
     *
     * @return an ObservableList of Agent objects.
     * @throws SQLException if a database access error occurs.
     */
    public ObservableList<Agent> getAllAgents() throws SQLException {
        ObservableList<Agent> list = FXCollections.observableArrayList();
        Statement stmt = connection.createStatement();
        // Execute SQL query to retrieve all Agent records.
        ResultSet rs = stmt.executeQuery("SELECT * FROM AGENT");
        while (rs.next()) {
            Agent a = new Agent();
            a.setFirstName(rs.getString("FIRSTNAME"));
            a.setLastName(rs.getString("LASTNAME"));
            a.setEmailAddress(rs.getString("EMAIL"));
            a.setPhoneNumber(rs.getString("PHONE"));
            a.setLicenseNumber(rs.getString("LICENSENUMBER"));
            a.setSpecialization(rs.getString("SPECIALIZATION"));
            list.add(a);
        }
        return list;
    }
}
