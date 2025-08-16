package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;

/**
 * Controller class for the "Add New Executive" view.
 * This controller handles the creation of a new Executive record.
 * It collects input from the view, creates an Executive object,
 * saves the record using the ExecutiveTableAdapter, and refreshes the parent table.
 */
public class AddNewExecutiveController {

    // FXML controls from the view for input fields and buttons
    @FXML private TextField firstNameField, lastNameField, emailField, phoneField, privatePhoneField;
    @FXML private Button saveBtn, cancelBtn;

    // Adapter used to perform database operations related to Executive records
    private ExecutiveTableAdapter executiveAdapter;
    // Reference to the parent controller that manages the list of executives
    private ManageExecutiveProfilesController parentController;
    // Database connection used to execute SQL operations
    private Connection connection;

    /**
     * Sets the database connection and initializes the ExecutiveTableAdapter.
     *
     * @param conn The Connection object to use for database operations.
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
        try {
            // Initialize the adapter without resetting the table (i.e., without recreating it)
            executiveAdapter = new ExecutiveTableAdapter(connection, false);
        } catch (Exception e) {
            showAlert("DB Error", e.getMessage());
        }
    }

    /**
     * Sets the parent controller for this view.
     * The parent controller is used to refresh the executive table after a new record is added.
     *
     * @param controller The instance of ManageExecutiveProfilesController.
     */
    public void setParentController(ManageExecutiveProfilesController controller) {
        this.parentController = controller;
    }

    /**
     * Handles the Save button action.
     * Reads the input from the text fields, creates a new Executive object,
     * saves it to the database via the ExecutiveTableAdapter, refreshes the parent table,
     * and then closes the current window.
     */
    @FXML
    private void saveExecutive() {
        try {
            // Create a new Executive object using input from the view
            Executive e = new Executive(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    privatePhoneField.getText()
            );
            // Save the new executive record in the database
            executiveAdapter.addNewRecord(e);
            // Refresh the table view in the parent controller to show the new record
            parentController.refreshTable();
            // Close the current window after saving
            close();
        } catch (Exception ex) {
            showAlert("Input Error", ex.getMessage());
        }
    }

    /**
     * Handles the Cancel button action.
     * Simply closes the current window without saving any changes.
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
     * Displays an alert dialog with the specified title and message.
     *
     * @param title The title of the alert dialog.
     * @param msg   The message to be displayed in the alert dialog.
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
