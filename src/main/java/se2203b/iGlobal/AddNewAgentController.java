package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;

/**
 * Controller for the "Add New Agent" view.
 * This class handles the creation of a new Agent record and its insertion into the database.
 * It also refreshes the parent table view after successfully adding an agent.
 */
public class AddNewAgentController {

    // FXML controls mapped from the view
    @FXML private TextField firstNameField, lastNameField, emailField, phoneField, licenseField, specializationField;
    @FXML private Button saveBtn, cancelBtn;

    // Adapter for performing database operations related to agents
    private AgentTableAdapter agentAdapter;
    // Reference to the parent controller that manages the list of agents
    private ManageAgentProfilesController parentController;
    // Database connection used for executing operations
    private Connection connection;

    /**
     * Sets the database connection and initializes the AgentTableAdapter.
     *
     * @param conn the Connection to be used for database operations
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
        try {
            // Initialize the adapter with the current connection; reset flag is false to avoid table recreation.
            agentAdapter = new AgentTableAdapter(connection, false);
        } catch (Exception e) {
            // If an error occurs during initialization, show an alert with the error message.
            showAlert("DB Error", e.getMessage());
        }
    }

    /**
     * Sets the parent controller for this view.
     * This allows the controller to refresh the agent table after a new agent is added.
     *
     * @param controller the parent ManageAgentProfilesController instance
     */
    public void setParentController(ManageAgentProfilesController controller) {
        this.parentController = controller;
    }

    /**
     * Handles the Save button click event.
     * Reads input values, creates a new Agent object, and adds it to the database.
     * Refreshes the parent table view and closes the current window on success.
     */
    @FXML
    private void saveAgent() {
        try {
            // Create a new Agent object using the input from the form fields.
            Agent agent = new Agent(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    licenseField.getText(),
                    specializationField.getText()
            );
            // Insert the new agent record into the database.
            agentAdapter.addNewRecord(agent);
            // Refresh the parent controller's table view to reflect the new addition.
            parentController.refreshTable();
            // Close the Add New Agent window.
            close();
        } catch (Exception e) {
            // Show an error alert if any exception occurs during the save operation.
            showAlert("Input Error", e.getMessage());
        }
    }

    /**
     * Handles the Cancel button click event.
     * Closes the current window without saving any changes.
     */
    @FXML
    private void cancel() {
        close();
    }

    /**
     * Closes the current window.
     */
    private void close() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert dialog with the provided title and message.
     *
     * @param title the title of the alert dialog
     * @param msg the message to be displayed in the alert dialog
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
