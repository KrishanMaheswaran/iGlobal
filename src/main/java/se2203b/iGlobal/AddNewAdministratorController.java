package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

/**
 * Controller class for adding a new Administrator.
 * This class is responsible for handling user input from the Add New Administrator view,
 * creating a new Administrator object, and adding it to the database via the AdministratorTableAdapter.
 */
public class AddNewAdministratorController {

    // FXML injected controls from the view.
    @FXML private TextField firstNameField, lastNameField, emailField, phoneField;
    @FXML private DatePicker dateCreatedPicker;
    @FXML private Button saveBtn, cancelBtn;

    // Reference to the table adapter for Administrator, used to perform DB operations.
    private AdministratorTableAdapter adminAdapter;
    // Reference to the parent controller to allow refreshing of the table after a new record is added.
    private ManageAdministratorProfilesController parentController;
    // Database connection instance.
    private Connection connection;

    /**
     * Sets the database connection and initializes the AdministratorTableAdapter.
     *
     * @param conn The database connection.
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
        try {
            // Initialize the adapter with reset=false (do not reset the DB table).
            adminAdapter = new AdministratorTableAdapter(false);
        } catch (SQLException e) {
            // Show an alert if there is a database error.
            showAlert("DB Error", e.getMessage());
        }
    }

    /**
     * Sets the parent controller, which allows for refreshing the administrator table in the parent view.
     *
     * @param controller The ManageAdministratorProfilesController instance.
     */
    public void setParentController(ManageAdministratorProfilesController controller) {
        this.parentController = controller;
    }

    /**
     * Handler for the Save button.
     * This method reads the input from the form, creates a new Administrator object, and saves it to the database.
     */
    @FXML
    private void saveAdministrator() {
        try {
            // Create a new Administrator instance.
            Administrator admin = new Administrator();

            // Generate a new unique ID for the administrator.
            // Here we use a naive approach: current key count + 2 (adjust as necessary).
            int newID = adminAdapter.getKeys().size() + 2;
            admin.setID(String.valueOf(newID));

            // Set administrator details from form input.
            admin.setFirstName(firstNameField.getText());
            admin.setLastName(lastNameField.getText());
            admin.setEmail(emailField.getText());
            admin.setPhone(phoneField.getText());

            // Convert the value from the DatePicker to a java.util.Date.
            if (dateCreatedPicker.getValue() != null) {
                Date dateHired = Date.from(dateCreatedPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                admin.setDateCreated(dateHired);
            }

            // Add the new administrator record to the database.
            adminAdapter.addNewRecord(admin);
            // Refresh the table in the parent controller to display the new record.
            parentController.refreshTable();
            // Close the current window.
            close();
        } catch (Exception e) {
            // Show an alert if any input error occurs.
            showAlert("Input Error", e.getMessage());
        }
    }

    /**
     * Handler for the Cancel button.
     * Closes the Add New Administrator window.
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
     * Utility method to display an alert dialog.
     *
     * @param title The title of the alert.
     * @param msg The message to be displayed.
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
