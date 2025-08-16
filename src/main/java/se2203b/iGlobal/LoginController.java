package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author Abdelkader Ouda
 */
public class LoginController implements Initializable {
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label errorMsg;

    IGlobalController iGlobalController;

    private DataStore userAccountTable;
    private DataStore agentTable, executiveTable, adminTable;

    public void setDataStore(DataStore accountAdapter) {
        userAccountTable = accountAdapter;
    }

    /**
     * Check authorization credentials.
     */
    public void authorize() {
        errorMsg.setText("");
        try {
            // Get the user account information from database
            UserAccount account = (UserAccount) userAccountTable.findOneRecord(user.getText());
            if (account.getUserAccountName() == null) {
                // Account not found
                errorMsg.setText("Incorrect username");
            } else {
                // Account exist, now check the password
                String salt = account.getPasswordSalt();
                String encryptedPassword = iGlobalController.encrypt(password.getText(), salt);
                String retrievedEncryptedPassword = account.getEncryptedPassword();
                if (encryptedPassword.equals(retrievedEncryptedPassword)) {
                    // enable controls based on the account type
                    authenticated(account, account.getAccountType());
                } else {
                    // wrong password
                    errorMsg.setText("Wrong password");
                }
            }
        } catch (SQLException ex) {
            iGlobalController.displayAlert("ERROR: " + ex.getMessage());
        }


    }

    /**
     * Will show the main application screen.
     */
    public void authenticated(UserAccount userAccount, String privilege) {
        iGlobalController.setUserName(userAccount.getUserAccountName());
        if (privilege.equals("admin")) {
            try {
                adminTable = new AdministratorTableAdapter(false);
                Administrator admin = (Administrator) adminTable.findOneRecord(userAccount);
                if (userAccount.getUserAccountName().equals("admin"))
                    iGlobalController.setUserFullname("Admin");
                else
                    iGlobalController.setUserFullname(admin.getFirstName() + " " + admin.getLastName());

                iGlobalController.enableAdminControls();
            } catch (SQLException e) {
                iGlobalController.displayAlert("ERROR-Login: " + e.getMessage());
            }

        }

        // Get current stage reference
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        // Close stage
        stage.close();
    }

    public void cancel() {
        // Get current stage reference
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        // Close stage
        stage.close();
    }

    public void clearErrorMsg() {
        errorMsg.setText("");
    }

    public void setIGlobalController(IGlobalController controller) {
        iGlobalController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMsg.setText("");
    }
}