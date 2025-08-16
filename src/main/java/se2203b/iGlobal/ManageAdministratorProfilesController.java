package se2203b.iGlobal;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * This controller is responsible for managing the Administrator profiles.
 * It loads administrator data into a TableView, allows for adding and removing administrators,
 * and handles refreshing and closing the view.
 */
public class ManageAdministratorProfilesController {

    // FXML fields linked to the UI elements defined in the corresponding FXML file.
    @FXML private TableView<Administrator> adminTable;
    @FXML private TableColumn<Administrator, String> firstNameCol;
    @FXML private TableColumn<Administrator, String> lastNameCol;
    @FXML private TableColumn<Administrator, String> emailCol;
    @FXML private TableColumn<Administrator, String> phoneCol;
    @FXML private TableColumn<Administrator, String> dateCreatedCol;
    @FXML private Button addAdminBtn, removeBtn, exitBtn;

    // Adapter for accessing Administrator data in the database.
    private AdministratorTableAdapter adminAdapter;
    // Database connection used to retrieve and update administrator records.
    private Connection connection;

    /**
     * Sets the database connection for this controller and initializes the AdministratorTableAdapter.
     * Then it loads the list of administrator records into the TableView.
     *
     * @param conn the Connection object to the database.
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
        try {
            // Create an instance of AdministratorTableAdapter.
            adminAdapter = new AdministratorTableAdapter(false);
            // Load administrator records from the database.
            loadAdministrators();
        } catch (SQLException e) {
            // Show an alert if there is a database error.
            showAlert("DB Error", e.getMessage());
        }
    }

    /**
     * Loads all administrator records from the database and populates the TableView.
     *
     * @throws SQLException if a database access error occurs.
     */
    private void loadAdministrators() throws SQLException {
        // Retrieve all administrator records as a List of Objects.
        List<Object> allAdmins = adminAdapter.getAllRecords();
        // Convert the List<Object> to an ObservableList<Administrator> for use in the TableView.
        ObservableList<Administrator> admins = FXCollections.observableArrayList();
        for (Object obj : allAdmins) {
            admins.add((Administrator) obj);
        }
        // Set the TableView's items to the list of administrators.
        adminTable.setItems(admins);

        // Set up the cell value factories for each column to bind to the corresponding Administrator properties.
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        // For the dateCreated column, format the Date value as a string.
        dateCreatedCol.setCellValueFactory(cellData -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            if (cellData.getValue().getDateCreated() != null) {
                return new SimpleStringProperty(sdf.format(cellData.getValue().getDateCreated()));
            } else {
                return new SimpleStringProperty("");
            }
        });
    }

    /**
     * Opens the "Add New Administrator" window by loading its FXML view and setting its controller.
     */
    @FXML
    private void openAddAdminWindow() {
        try {
            // Load the addNewAdministrator-view.fxml file.
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("addNewAdministrator-view.fxml"));
            javafx.scene.Parent root = loader.load();
            // Retrieve the controller for the add administrator view.
            AddNewAdministratorController controller = loader.getController();
            // Pass the current connection and set this controller as the parent.
            controller.setConnection(connection);
            controller.setParentController(this);

            // Create a new Stage (window) to display the add administrator view.
            Stage stage = new Stage();
            stage.setTitle("Add New Administrator");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait(); // Wait until the window is closed before returning.
        } catch (Exception e) {
            // Show an alert if there is an error loading the view.
            showAlert("Load Error", e.getMessage());
        }
    }

    /**
     * Refreshes the TableView by reloading the administrator records from the database.
     */
    public void refreshTable() {
        try {
            loadAdministrators();
        } catch (SQLException e) {
            showAlert("Refresh Error", e.getMessage());
        }
    }

    /**
     * Deletes the selected administrator record from the database.
     * If no administrator is selected, an alert is displayed.
     */
    @FXML
    private void removeAdmin() {
        Administrator selected = adminTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an administrator to remove.");
            return;
        }
        try {
            // Delete the selected administrator.
            adminAdapter.deleteOneRecord(selected);
            // Refresh the TableView after deletion.
            refreshTable();
        } catch (SQLException e) {
            showAlert("Delete Error", e.getMessage());
        }
    }

    /**
     * Closes the current window.
     */
    @FXML
    private void exit() {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Utility method to display an error alert with the specified title and message.
     *
     * @param title the title of the alert.
     * @param msg   the message content of the alert.
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
