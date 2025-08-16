package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for managing property types.
 * This controller handles the creation, editing, and deletion of property types.
 * It interacts with a DataStore that manages PropertyType entities.
 */
public class ManagePropertyTypesController implements Initializable {

    // FXML elements from the corresponding FXML file
    @FXML private Button addTypeBtn;                            // Button to add a new property type
    @FXML private TableView propertyTypeTableView;              // TableView to display property types
    @FXML private TableColumn<PropertyType, String> typeCodeCol;  // Column for the property type code
    @FXML private TableColumn<PropertyType, String> typeNameCol;  // Column for the property type name
    @FXML private TextField typeCode;                           // TextField for inputting a new type code
    @FXML private TextField typeName;                           // TextField for inputting a new type name

    // ObservableList to hold the property types displayed in the TableView
    private ObservableList<Object> propertyTypeViewData = FXCollections.observableArrayList();

    // DataStore for property type records; injected by the main controller
    private DataStore propertyTypeDataStore;
    // Reference to the main IGlobalController for displaying alerts
    private IGlobalController iGlobalController;

    @FXML private Button removeTypeBtn; // Button to delete a selected property type
    @FXML private Button exitBtn;       // Button to close the form

    /**
     * Sets the reference to the main controller.
     *
     * @param controller The IGlobalController instance.
     */
    public void setIGlobalController(IGlobalController controller) {
        iGlobalController = controller;
    }

    /**
     * Sets the DataStore for property types and builds the initial data list.
     *
     * @param propertyType The DataStore instance for property types.
     */
    public void setDataStore(DataStore propertyType) {
        propertyTypeDataStore = propertyType;
        buildPropertyTypeData();
    }

    /**
     * Handles the event when the "Add" button is clicked.
     * Creates a new PropertyType object from the user input, adds it to the TableView,
     * and stores it in the DataStore.
     *
     * @param actionEvent The action event triggered by clicking the add button.
     */
    @FXML
    public void addTypeBtnClicked(ActionEvent actionEvent) {
        // Create a new PropertyType object from the text fields
        PropertyType propertyType = new PropertyType(typeCode.getText(), typeName.getText());
        // Wrap the new property type in a list for adding to the observable list
        List<Object> list = new ArrayList<>();
        list.add(propertyType);
        ObservableList<Object> observableArrayList = FXCollections.observableArrayList(list);
        // Add the new property type to the view data list
        propertyTypeViewData.addAll(observableArrayList);
        try {
            // Insert the new record into the database via the DataStore
            propertyTypeDataStore.addNewRecord(propertyType);
        } catch (SQLException e) {
            iGlobalController.displayAlert("ERROR: " + e.getMessage());
        }
        // Clear the text fields after adding
        typeCode.setText("");
        typeName.setText("");
    }

    /**
     * Builds the property type data by retrieving all records from the DataStore
     * and populating the observable list.
     */
    private void buildPropertyTypeData() {
        try {
            // Retrieve all property type records from the DataStore
            List<Object> list = propertyTypeDataStore.getAllRecords();
            ObservableList<Object> observableArrayList = FXCollections.observableArrayList(list);
            // Add retrieved records to the view data list
            propertyTypeViewData.addAll(observableArrayList);
        } catch (SQLException ex) {
            iGlobalController.displayAlert("ERROR: " + ex.getMessage());
        }
    }

    /**
     * Handles editing of the type code in the TableView.
     * This method is called when a user finishes editing a cell in the typeCode column.
     *
     * @param event The CellEditEvent containing the new type code value.
     */
    @FXML
    public void onTypeCodeEdit(TableColumn.CellEditEvent<PropertyType, String> event) {
        try {
            // Retrieve the edited PropertyType object and the new value entered by the user
            PropertyType propertyType = event.getRowValue();
            String newValue = event.getNewValue();
            // Update the property type code with the new value
            propertyType.setTypeCode(newValue);
            // Update the record in the database
            propertyTypeDataStore.updateRecord(propertyType);
        } catch (SQLException e) {
            iGlobalController.displayAlert("ERROR: " + e.getMessage());
        }
    }

    /**
     * Handles editing of the type name in the TableView.
     * This method is called when a user finishes editing a cell in the typeName column.
     *
     * @param event The CellEditEvent containing the new type name value.
     */
    @FXML
    public void onTypeNameEdit(TableColumn.CellEditEvent<PropertyType, String> event) {
        try {
            // Retrieve the edited PropertyType object and the new value entered by the user
            PropertyType propertyType = event.getRowValue();
            String newValue = event.getNewValue();
            // Update the property type name with the new value
            propertyType.setTypeName(newValue);
            // Update the record in the database
            propertyTypeDataStore.updateRecord(propertyType);
        } catch (SQLException e) {
            iGlobalController.displayAlert("ERROR: " + e.getMessage());
        }
    }

    /**
     * Initializes the controller.
     * Binds TableView columns to the corresponding properties in the PropertyType entity
     * and sets the TableView's data source.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind the type code column to the typeCode property of the PropertyType entity
        typeCodeCol.setCellValueFactory(cellData -> cellData.getValue().typeCodeProperty());
        // Enable in-place editing for type code
        typeCodeCol.setCellFactory(TextFieldTableCell.forTableColumn());

        // Bind the type name column to the typeName property of the PropertyType entity
        typeNameCol.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        // Enable in-place editing for type name
        typeNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set the TableView's items to the observable list that holds property types
        propertyTypeTableView.setItems(propertyTypeViewData);
    }

    /**
     * Deletes the selected property type record.
     * This method is invoked when the user clicks the "Remove" button.
     *
     * @param actionEvent The action event triggered by clicking the remove button.
     */
    @FXML
    public void deletePropertyType(ActionEvent actionEvent) {
        // Get the selected PropertyType from the TableView
        PropertyType selectedItem = (PropertyType) propertyTypeTableView.getSelectionModel().getSelectedItem();
        try {
            // Delete the record from the DataStore
            propertyTypeDataStore.deleteOneRecord(selectedItem);
            // Remove the item from the TableView
            propertyTypeTableView.getItems().remove(selectedItem);
        } catch (SQLException e) {
            iGlobalController.displayAlert("ERROR: " + e.getMessage());
        }
    }

    /**
     * Closes the Manage Property Types form.
     * This method is linked to the "Exit" button's action.
     *
     * @param actionEvent The action event triggered by clicking the exit button.
     */
    @FXML
    public void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }
}
