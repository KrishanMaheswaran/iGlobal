package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for the "Add New Property" view.
 *
 * This class handles user interactions for adding a new property record.
 * It loads necessary lookup values (such as property types, provinces, and cities),
 * ensures that new provinces or cities are added to the database if they don't exist,
 * and creates a new Property record in the database.
 */
public class AddNewPropertyController {

    // FXML controls representing various input fields in the view
    @FXML private ComboBox<String> propertyTypeCB;
    @FXML private ComboBox<String> provinceCB, cityCB;
    @FXML private TextField addressField, postalField, lotSizeField, squareFootField,
            bedroomField, bathroomField, yearBuiltField, amenitiesField, priceField;
    @FXML private TextArea descriptionField;
    @FXML private Button saveBtn, cancelBtn;

    // Adapters for interacting with the database tables
    private PropertyTableAdapter propertyAdapter;
    private ProvinceTableAdapter provinceAdapter;
    private CityTableAdapter cityAdapter;
    private PropertyTypeTableAdapter propertyTypeAdapter;
    // Reference to the parent controller to refresh the property records table after adding a record
    private ManagePropertyRecordsController parentController;
    // Database connection for executing SQL commands
    private Connection connection;

    /**
     * Sets the database connection.
     *
     * @param conn The Connection object to use for all database operations.
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    /**
     * Sets the parent controller and initializes all required adapters.
     * Also loads the values for the combo boxes.
     *
     * @param controller The parent ManagePropertyRecordsController.
     * @throws SQLException if an SQL error occurs.
     */
    public void setParentController(ManagePropertyRecordsController controller) throws SQLException {
        parentController = controller;
        // Initialize adapters with the current connection. The 'false' parameter means do not reset the tables.
        propertyAdapter = new PropertyTableAdapter(connection, false);
        provinceAdapter = new ProvinceTableAdapter(false);
        cityAdapter = new CityTableAdapter(false);
        propertyTypeAdapter = new PropertyTypeTableAdapter(false);

        // Load lookup values into the combo boxes for property types, provinces, and cities.
        loadComboBoxes();
    }

    /**
     * Loads lookup data into combo boxes.
     *
     * Loads property types, provinces, and cities (based on the currently selected province)
     * so that the user can select or type new values.
     *
     * @throws SQLException if an SQL error occurs.
     */
    private void loadComboBoxes() throws SQLException {
        // Load property types from the PropertyType table.
        List<String> propertyTypes = propertyTypeAdapter.getKeys();
        propertyTypeCB.setItems(FXCollections.observableArrayList(propertyTypes));
        propertyTypeCB.getSelectionModel().selectFirst();

        // Load provinces from the Province table.
        List<String> provinces = provinceAdapter.getKeys();
        provinceCB.setItems(FXCollections.observableArrayList(provinces));
        provinceCB.setEditable(true);  // Allow users to type in a new province if needed.

        // Load cities for the initially selected province.
        loadCities();

        // Allow users to type in a new city.
        cityCB.setEditable(true);
    }

    /**
     * Loads cities for the currently selected or typed province.
     *
     * Reads the province value from the province combo box editor,
     * retrieves the corresponding list of cities from the City table,
     * and updates the city combo box.
     */
    @FXML
    private void loadCities() {
        try {
            // Retrieve the province value from the editable combo box.
            String selectedProvince = provinceCB.getEditor().getText();
            // Get cities in that province.
            List<String> cities = cityAdapter.getCitiesInProvince(selectedProvince);
            // Update the city combo box with the retrieved cities.
            cityCB.setItems(FXCollections.observableArrayList(cities));
            if (!cities.isEmpty()) {
                cityCB.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            showAlert("City Load Error", e.getMessage());
        }
    }

    /**
     * Handles the Save button action.
     *
     * This method performs the following steps:
     * 1. Retrieves the province value from the province combo box.
     * 2. If the province does not exist in the database, it adds it.
     * 3. Retrieves the city value from the city combo box.
     * 4. If the (city, province) combination does not exist in the database, it adds it.
     * 5. Creates a new Property object using the input values.
     * 6. Inserts the new property record into the database.
     * 7. Refreshes the property records table in the parent controller.
     * 8. Closes the current window.
     */
    @FXML
    private void saveProperty() {
        try {
            // 1. Read the province value (typed or selected) from the province combo box.
            String chosenProvince = provinceCB.getEditor().getText();
            // 2. If the province is not already in the database and is not blank, insert it.
            if (!provinceAdapter.getKeys().contains(chosenProvince) && !chosenProvince.isBlank()) {
                provinceAdapter.addNewRecord(chosenProvince);
                // Update the combo box items so the new province appears for future selections.
                provinceCB.getItems().add(chosenProvince);
            }

            // 3. Read the city value (typed or selected) from the city combo box.
            String chosenCity = cityCB.getEditor().getText();
            // 4. If the (city, province) combination does not exist in the City table, insert it.
            if (!cityAdapter.cityExists(chosenCity, chosenProvince) && !chosenCity.isBlank()) {
                cityAdapter.addNewRecord(new City(chosenCity, chosenProvince));
                // Add the new city to the combo box items.
                cityCB.getItems().add(chosenCity);
            }

            // 5. Create a new Property object using the values from the view.
            Property p = new Property(
                    propertyTypeCB.getValue(),
                    Double.parseDouble(lotSizeField.getText()),
                    Double.parseDouble(squareFootField.getText()),
                    Integer.parseInt(bedroomField.getText()),
                    Double.parseDouble(bathroomField.getText()),
                    Integer.parseInt(yearBuiltField.getText()),
                    Double.parseDouble(priceField.getText()),
                    chosenProvince,
                    chosenCity,
                    addressField.getText(),
                    postalField.getText(),
                    amenitiesField.getText(),
                    descriptionField.getText()
            );

            // 6. Insert the new property record into the database.
            propertyAdapter.addNewRecord(p);
            // 7. Refresh the parent controller's property table to include the new record.
            parentController.refreshTable();
            // 8. Close the current window.
            close();
        } catch (Exception e) {
            showAlert("Input Error", e.getMessage());
        }
    }

    /**
     * Handles the Cancel button action by closing the current window.
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
     * Displays an alert dialog with a given title and message.
     *
     * @param title The title of the alert dialog.
     * @param msg   The message to display.
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
