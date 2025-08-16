package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing user accounts.
 * This controller allows the administrator to view, create, update, and delete user accounts.
 * It interacts with the UserAccountTableAdapter to perform database operations.
 */
public class ManageUserAccountsController {

    // FXML controls bound from the corresponding FXML file.
    @FXML private ComboBox<String> userAccountCombo;  // ComboBox for selecting existing accounts or "New" for a new account.
    @FXML private TextField emailField;               // TextField for displaying/editing the email address.
    @FXML private TextField usernameField;            // TextField for displaying/editing the username.
    @FXML private PasswordField passwordField;        // PasswordField for entering the password.
    @FXML private PasswordField confirmPasswordField; // PasswordField for confirming the new password.
    @FXML private Button saveBtn;                     // Button to save new or updated account data.
    @FXML private Button deleteBtn;                   // Button to delete the selected user account.
    @FXML private Button cancelBtn;                   // Button to cancel and close the form.

    // Adapter for database operations on user accounts.
    private UserAccountTableAdapter userAccountAdapter;
    // Observable list that holds the user account names for the ComboBox.
    private ObservableList<String> userList = FXCollections.observableArrayList();

    // Flag indicating if a new user account is being created.
    private boolean creatingNewUser = false;
    // Stores the currently loaded user account when an existing account is selected.
    private UserAccount currentUserAccount = null;

    /**
     * Sets the DataStore (UserAccountTableAdapter) used to interact with the user account data.
     *
     * @param adapter The UserAccountTableAdapter instance.
     */
    public void setDataStore(UserAccountTableAdapter adapter) {
        this.userAccountAdapter = adapter;
        loadUserList();
    }

    /**
     * Loads the list of user accounts from the database and populates the ComboBox.
     * It adds an option "New" to allow creation of a new user account.
     */
    private void loadUserList() {
        try {
            // Retrieve all user account keys (usernames) from the adapter.
            List<String> allUsers = userAccountAdapter.getAllKeys(); // or getKeys()
            // Clear the current list and add "New" as the first item.
            userList.clear();
            userList.add("New");
            // Add all user account names retrieved from the database.
            userList.addAll(allUsers);
            // Set the items in the ComboBox.
            userAccountCombo.setItems(userList);
            // Select the first item ("New") by default.
            userAccountCombo.getSelectionModel().selectFirst();
            // Handle the selection to update the form fields accordingly.
            handleUserSelection();
        } catch (SQLException e) {
            showAlert("Load Error", e.getMessage());
        }
    }

    /**
     * Handles the event when the user selects an item from the ComboBox.
     * If "New" is selected, clears the form fields for new account entry.
     * Otherwise, loads the selected account details from the database.
     */
    @FXML
    private void handleUserSelection() {
        String selected = userAccountCombo.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (selected.equals("New")) {
            // Prepare form for creating a new user account.
            creatingNewUser = true;
            currentUserAccount = null;
            emailField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            deleteBtn.setDisable(true); // Disable delete button since there is no account to delete.
        } else {
            // Load the details of the selected existing user account.
            creatingNewUser = false;
            try {
                // Retrieve the user account from the database based on the username.
                currentUserAccount = (UserAccount) userAccountAdapter.findOneRecord(selected);
                if (currentUserAccount == null) {
                    showAlert("Load Error", "User account not found.");
                    return;
                }
                // Set the form fields with data from the retrieved account.
                // (Email field is cleared here, adjust if email data is stored elsewhere.)
                emailField.setText("");
                usernameField.setText(currentUserAccount.getUserAccountName());
                passwordField.setText(""); // For security, do not display the encrypted password.
                confirmPasswordField.setText("");
                deleteBtn.setDisable(false); // Enable delete button for existing account.
            } catch (SQLException e) {
                showAlert("Load Error", e.getMessage());
            }
        }
    }

    /**
     * Saves a new user account or updates an existing one.
     * Validates the password fields and performs the appropriate database operation.
     */
    @FXML
    private void saveUser() {
        try {
            // Validate that the password and confirm password fields match.
            if (passwordField.getText() == null || !passwordField.getText().equals(confirmPasswordField.getText())) {
                showAlert("Password Error", "Passwords do not match.");
                return;
            }
            if (creatingNewUser) {
                // Create a new user account.
                UserAccount newAccount = new UserAccount();
                newAccount.setUserAccountName(usernameField.getText());
                // For this example, we store the raw password; in production, perform encryption.
                newAccount.setEncryptedPassword(passwordField.getText());
                newAccount.setPasswordSalt("someSalt"); // Replace with proper salt generation.
                newAccount.setAccountType("agent"); // Optionally, set account type (agent, executive, etc.)
                userAccountAdapter.addNewRecord(newAccount);
                showAlert("Success", "New user account created.");
            } else {
                // Update the existing user account.
                currentUserAccount.setUserAccountName(usernameField.getText());
                currentUserAccount.setEncryptedPassword(passwordField.getText());
                currentUserAccount.setPasswordSalt("someSalt"); // Replace with proper salt generation.
                userAccountAdapter.updateRecord(currentUserAccount);
                showAlert("Success", "User account updated.");
            }
            // Reload the list of user accounts and re-select the updated/created account.
            loadUserList();
            userAccountCombo.getSelectionModel().select(usernameField.getText());
            handleUserSelection();
        } catch (Exception e) {
            showAlert("Save Error", e.getMessage());
        }
    }

    /**
     * Deletes the currently loaded user account.
     */
    @FXML
    private void deleteUser() {
        if (currentUserAccount == null) {
            showAlert("No Selection", "No user selected to delete.");
            return;
        }
        try {
            userAccountAdapter.deleteOneRecord(currentUserAccount);
            showAlert("Success", "User account deleted.");
            loadUserList();
        } catch (SQLException e) {
            showAlert("Delete Error", e.getMessage());
        }
    }

    /**
     * Closes the Manage User Accounts window.
     */
    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title The title of the alert dialog.
     * @param msg   The message content of the alert dialog.
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
