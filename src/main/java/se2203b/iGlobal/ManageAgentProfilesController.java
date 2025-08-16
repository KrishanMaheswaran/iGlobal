package se2203b.iGlobal;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Controller for managing agent profiles.
 * It loads agent data into a TableView, allows adding and removing agents,
 * and provides a method to refresh the displayed data.
 */
public class ManageAgentProfilesController {

    // FXML fields corresponding to the UI components in the FXML file.
    @FXML private TableView<Agent> agentTable;             // Table to display agents
    @FXML private TableColumn<Agent, String> firstNameCol;   // Column for first name
    @FXML private TableColumn<Agent, String> lastNameCol;    // Column for last name
    @FXML private TableColumn<Agent, String> emailCol;       // Column for email address
    @FXML private TableColumn<Agent, String> phoneCol;       // Column for phone number
    @FXML private TableColumn<Agent, String> licenseCol;     // Column for license number
    @FXML private TableColumn<Agent, String> specializationCol; // Column for specialization

    @FXML private Button addAgentBtn, removeBtn, exitBtn;    // Buttons for add, remove, and exit actions

    // Adapter for accessing agent records in the database.
    private AgentTableAdapter agentAdapter;
    // Database connection to be used by this controller.
    private Connection connection;

    /**
     * Sets the database connection and initializes the agent adapter.
     * Also loads the agent records into the TableView.
     *
     * @param conn the Connection object to the database.
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
        try {
            // Create a new AgentTableAdapter using the provided connection.
            agentAdapter = new AgentTableAdapter(conn, false);
            // Load agent records into the TableView.
            loadAgents();
        } catch (SQLException e) {
            // Show an error alert if a database error occurs.
            showAlert("DB Error", e.getMessage());
        }
    }

    /**
     * Loads agent records from the database using the adapter and sets them into the TableView.
     *
     * @throws SQLException if a database access error occurs.
     */
    private void loadAgents() throws SQLException {
        // Retrieve all agents as an ObservableList.
        ObservableList<Agent> agents = agentAdapter.getAllAgents();
        // Set the items of the TableView to the list of agents.
        agentTable.setItems(agents);

        // Bind each table column to the corresponding property of the Agent class.
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailAddressProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        licenseCol.setCellValueFactory(cellData -> cellData.getValue().licenseNumberProperty());
        specializationCol.setCellValueFactory(cellData -> cellData.getValue().specializationProperty());
    }

    /**
     * Opens a new window for adding a new agent.
     * Loads the addNewAgent-view.fxml file and sets the controller's connection and parent controller.
     */
    @FXML
    private void openAddAgentWindow() {
        try {
            // Load the FXML view for adding a new agent.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addNewAgent-view.fxml"));
            Parent root = loader.load();
            // Retrieve the controller associated with the add agent view.
            AddNewAgentController controller = loader.getController();
            // Set the database connection and parent controller for the add agent controller.
            controller.setConnection(connection);
            controller.setParentController(this);

            // Create a new Stage (window) to display the add agent form.
            Stage stage = new Stage();
            stage.setTitle("Add New Agent");
            stage.setScene(new Scene(root));
            // Set modality so that the new window must be closed before returning to the main window.
            stage.initModality(Modality.APPLICATION_MODAL);
            // Display the window and wait for it to close.
            stage.showAndWait();
        } catch (Exception e) {
            // If any exception occurs during loading, show an error alert.
            showAlert("Load Error", e.getMessage());
        }
    }

    /**
     * Refreshes the TableView by reloading the agent data from the database.
     */
    public void refreshTable() {
        try {
            loadAgents();
        } catch (SQLException e) {
            // Show an error alert if there is an error refreshing the data.
            showAlert("Refresh Error", e.getMessage());
        }
    }

    /**
     * Removes the selected agent from the database.
     * If no agent is selected, displays an alert to the user.
     */
    @FXML
    private void removeAgent() {
        // Get the selected agent from the TableView.
        Agent selected = agentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            // If no agent is selected, show an alert.
            showAlert("No Selection", "Please select an agent to remove.");
            return;
        }
        try {
            // Delete the selected agent record using the adapter.
            agentAdapter.deleteOneRecord(selected);
            // Refresh the TableView after deletion.
            refreshTable();
        } catch (SQLException e) {
            // Show an error alert if deletion fails.
            showAlert("Delete Error", e.getMessage());
        }
    }

    /**
     * Closes the current window.
     */
    @FXML
    private void exit() {
        // Get the current stage (window) from the exit button.
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        // Close the stage.
        stage.close();
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title the title of the alert.
     * @param msg the message to display in the alert.
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
