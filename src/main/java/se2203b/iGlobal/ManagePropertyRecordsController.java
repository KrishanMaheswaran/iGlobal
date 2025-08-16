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
 * Controller class for managing property records.
 * It handles displaying property records in a TableView, opening dialogs to add new properties,
 * removing selected records, and refreshing the displayed list.
 */
public class ManagePropertyRecordsController {

    // FXML components defined in the corresponding FXML file.

    @FXML private TableView<Property> propertyTable; // Table to display property records.
    @FXML private TableColumn<Property, String> typeCol; // Column for property type.
    @FXML private TableColumn<Property, Double> lotSizeCol; // Column for lot size.
    @FXML private TableColumn<Property, Double> squareFootageCol; // Column for square footage.
    @FXML private TableColumn<Property, Integer> bedroomsCol; // Column for number of bedrooms.
    @FXML private TableColumn<Property, Double> bathroomsCol; // Column for number of bathrooms.
    @FXML private TableColumn<Property, Integer> yearBuiltCol; // Column for year built.
    @FXML private TableColumn<Property, Double> priceCol; // Column for property price.
    @FXML private TableColumn<Property, String> provinceCol; // Column for province.
    @FXML private TableColumn<Property, String> cityCol; // Column for city.
    @FXML private TableColumn<Property, String> addressCol; // Column for address.
    @FXML private TableColumn<Property, String> postalCodeCol; // Column for postal code.
    @FXML private Button addNewPropertyBtn, removeBtn, exitBtn; // Buttons for actions.

    // Adapter for property database operations.
    private PropertyTableAdapter propertyAdapter;
    // Database connection.
    private Connection connection;

    /**
     * Sets the database connection and initializes the PropertyTableAdapter.
     * Then, loads the properties from the database into the TableView.
     *
     * @param conn The active database connection.
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
        try {
            propertyAdapter = new PropertyTableAdapter(conn, false);
            loadProperties();
        } catch (SQLException e) {
            showAlert("DB Error", e.getMessage());
        }
    }

    /**
     * Loads property records from the database and binds them to the TableView.
     * Each TableColumn is bound to a property of the Property entity.
     *
     * @throws SQLException if a database access error occurs.
     */
    private void loadProperties() throws SQLException {
        // Retrieve properties from the database as an ObservableList.
        ObservableList<Property> properties = propertyAdapter.getAllProperties();
        propertyTable.setItems(properties);

        // Bind each TableColumn to its corresponding Property attribute.
        typeCol.setCellValueFactory(cellData -> cellData.getValue().propertyTypeProperty());
        lotSizeCol.setCellValueFactory(cellData -> cellData.getValue().lotSizeProperty().asObject());
        squareFootageCol.setCellValueFactory(cellData -> cellData.getValue().squareFootageProperty().asObject());
        bedroomsCol.setCellValueFactory(cellData -> cellData.getValue().bedroomsProperty().asObject());
        bathroomsCol.setCellValueFactory(cellData -> cellData.getValue().bathroomsProperty().asObject());
        yearBuiltCol.setCellValueFactory(cellData -> cellData.getValue().yearBuiltProperty().asObject());
        priceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        provinceCol.setCellValueFactory(cellData -> cellData.getValue().provinceProperty());
        cityCol.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        addressCol.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        postalCodeCol.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
    }

    /**
     * Opens the dialog window to add a new property.
     * This method is triggered by the "Add New Property" button (onAction="#openAddNewPropertyWindow").
     */
    @FXML
    private void openAddNewPropertyWindow() {
        openAddPropertyDialog();
    }

    /**
     * Loads and displays the Add New Property dialog.
     * It passes the database connection and parent controller to the dialog controller.
     */
    private void openAddPropertyDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addNewProperty-view.fxml"));
            Parent root = loader.load();
            // Get the controller for the add property dialog.
            AddNewPropertyController controller = loader.getController();
            // Set the database connection and parent controller for callback.
            controller.setConnection(connection);
            controller.setParentController(this);

            // Create a new stage and display the dialog modally.
            Stage stage = new Stage();
            stage.setTitle("Add New Property");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            showAlert("Load Error", e.getMessage());
        }
    }

    /**
     * Removes the selected property record from the database.
     * This method is linked to the "Remove" button's onAction event.
     */
    @FXML
    private void removeSelectedRecord() {
        // Retrieve the selected property from the TableView.
        Property selected = propertyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a property to remove.");
            return;
        }
        try {
            // Call the adapter method to delete the property using its address.
            propertyAdapter.deletePropertyByAddress(selected.getAddress());
            // Refresh the TableView to update the list.
            refreshTable();
        } catch (SQLException e) {
            showAlert("Delete Error", e.getMessage());
        }
    }

    /**
     * Closes the Manage Property Records window.
     * This method is linked to the "Exit" button's onAction event.
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Refreshes the property records in the TableView.
     * It reloads the properties from the database.
     */
    public void refreshTable() {
        try {
            loadProperties();
        } catch (SQLException e) {
            showAlert("Refresh Error", e.getMessage());
        }
    }

    /**
     * Displays an alert dialog with a specified title and message.
     *
     * @param title The title of the alert dialog.
     * @param msg   The message to display in the alert dialog.
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
