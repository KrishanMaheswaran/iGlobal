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
 * Controller for managing executive profiles.
 * This class is responsible for displaying, adding, refreshing, and deleting executive records.
 */
public class ManageExecutiveProfilesController {

    // FXML components that are defined in the corresponding FXML file.
    @FXML private TableView<Executive> executiveTable;         // TableView to display executives.
    @FXML private TableColumn<Executive, String> firstNameCol;   // Column for first name.
    @FXML private TableColumn<Executive, String> lastNameCol;    // Column for last name.
    @FXML private TableColumn<Executive, String> emailCol;       // Column for email address.
    @FXML private TableColumn<Executive, String> phoneCol;       // Column for phone number.
    @FXML private TableColumn<Executive, String> privatePhoneCol; // Column for private phone number.
    @FXML private Button addExecutiveBtn, removeBtn, exitBtn;    // Buttons for adding, removing, and exiting.

    // Adapter for database operations related to executive records.
    private ExecutiveTableAdapter executiveAdapter;
    // Database connection object.
    private Connection connection;

    /**
     * Sets the database connection and initializes the executive adapter.
     * Also loads the executive records into the TableView.
     *
     * @param conn the Connection object to the database.
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
        try {
            // Initialize the ExecutiveTableAdapter with the connection.
            executiveAdapter = new ExecutiveTableAdapter(conn, false);
            // Load executive data into the TableView.
            loadExecutives();
        } catch (SQLException e) {
            // If a database error occurs, display an alert.
            showAlert("DB Error", e.getMessage());
        }
    }

    /**
     * Loads executive records from the database into the TableView.
     *
     * @throws SQLException if a database access error occurs.
     */
    private void loadExecutives() throws SQLException {
        // Retrieve all executives as an ObservableList.
        ObservableList<Executive> executives = executiveAdapter.getAllExecutives();
        // Set the TableView's items to the retrieved executive list.
        executiveTable.setItems(executives);

        // Bind each TableColumn to the corresponding property of the Executive class.
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailAddressProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        privatePhoneCol.setCellValueFactory(cellData -> cellData.getValue().privatePhoneNumberProperty());
    }

    /**
     * Opens the "Add New Executive" window.
     * Loads the FXML file, gets its controller, passes the connection and parent controller,
     * and then displays the window as a modal dialog.
     */
    @FXML
    private void openAddExecutiveWindow() {
        try {
            // Load the FXML for adding a new executive.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addNewExecutive-view.fxml"));
            Parent root = loader.load();
            // Get the controller of the add-new-executive window.
            AddNewExecutiveController controller = loader.getController();
            // Pass the database connection and set this controller as the parent.
            controller.setConnection(connection);
            controller.setParentController(this);

            // Create a new stage for the window.
            Stage stage = new Stage();
            stage.setTitle("Add New Executive");
            stage.setScene(new Scene(root));
            // Set modality so the window is modal.
            stage.initModality(Modality.APPLICATION_MODAL);
            // Show the window and wait for it to close.
            stage.showAndWait();
        } catch (Exception e) {
            // If an error occurs during loading, display an alert.
            showAlert("Load Error", e.getMessage());
        }
    }

    /**
     * Refreshes the executive TableView by reloading the executive records.
     */
    public void refreshTable() {
        try {
            loadExecutives();
        } catch (SQLException e) {
            // If an error occurs while refreshing, display an alert.
            showAlert("Refresh Error", e.getMessage());
        }
    }

    /**
     * Removes the selected executive from the database.
     * Displays an alert if no executive is selected.
     */
    @FXML
    private void removeExecutive() {
        // Get the selected executive from the TableView.
        Executive selected = executiveTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            // Display an alert if no executive is selected.
            showAlert("No Selection", "Please select an executive to remove.");
            return;
        }
        try {
            // Delete the selected executive from the database.
            executiveAdapter.deleteOneRecord(selected);
            // Refresh the table to reflect the changes.
            refreshTable();
        } catch (SQLException e) {
            // Display an alert if an error occurs during deletion.
            showAlert("Delete Error", e.getMessage());
        }
    }

    /**
     * Closes the current window.
     */
    @FXML
    private void exit() {
        // Get the current stage from the exit button and close it.
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an error alert with the provided title and message.
     *
     * @param title the title of the alert.
     * @param msg the message content of the alert.
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
