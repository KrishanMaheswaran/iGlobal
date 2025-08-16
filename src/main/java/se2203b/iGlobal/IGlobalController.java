package se2203b.iGlobal;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class IGlobalController implements Initializable {

    @FXML private Menu aboutMenu;
    @FXML private MenuItem aboutusMenuItem;
    @FXML private MenuItem changePasswordMenuItem;
    @FXML private MenuItem closeMenuItem;
    @FXML private Menu fileMenu;
    @FXML private Menu agentPortalMenu;
    @FXML private MenuItem loginMenuItem;
    @FXML private MenuItem logoutMenuItem;
    @FXML private MenuBar mainMenu;
    @FXML private Menu executivePortalMenu;
    @FXML private Menu adminPortalMenu;
    @FXML private Menu userMenuItem;

    private String userAccountName;

    @FXML private MenuItem statusCodes;
    @FXML private MenuItem propertyTypes;
    @FXML private MenuItem documentTypes;
    @FXML private Menu configSystemCodes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ImageView face = new ImageView(new Image("file:src/main/resources/se2203b/iGlobal/UserIcon.png", 20, 20, true, true));
        userMenuItem.setGraphic(face);
        disableMenuItems();
        try {
            new AdministratorTableAdapter(false);
        } catch (SQLException ex) {
            displayAlert(ex.getMessage());
        }
    }

    @FXML
    public void showAbout() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about-view.fxml"));
        Parent about = fxmlLoader.load();
        Scene scene = new Scene(about);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("About Us");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void login() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent login = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setIGlobalController(this);
        loginController.setDataStore(new UserAccountTableAdapter(false));
        Stage stage = new Stage();
        stage.setScene(new Scene(login));
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Login to iGlobal");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void changePassword() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChangePassword-view.fxml"));
        Parent changePassword = fxmlLoader.load();
        ChangePasswordController changePasswordController = fxmlLoader.getController();
        changePasswordController.setIGlobalController(this);
        changePasswordController.setDataStore(new UserAccountTableAdapter(false));
        Stage stage = new Stage();
        stage.setScene(new Scene(changePassword));
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Change Password");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void logout() {
        disableMenuItems();
    }

    @FXML
    public void managePropertyTypes() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("managePropertyTypes-view.fxml"));
        Parent propertyType = fxmlLoader.load();
        ManagePropertyTypesController managePropertyTypesController = fxmlLoader.getController();
        managePropertyTypesController.setIGlobalController(this);
        managePropertyTypesController.setDataStore(new PropertyTypeTableAdapter(false));
        Stage stage = new Stage();
        stage.setScene(new Scene(propertyType));
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Manage Property Types");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void managePropertyRecords() throws Exception {
        System.out.println("managePropertyRecords() called.");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("managePropertyRecords-view.fxml"));
        Parent propertyRecords = fxmlLoader.load();
        ManagePropertyRecordsController controller = fxmlLoader.getController();
        controller.setConnection(IGlobalApplication.getConnection());
        Stage stage = new Stage();
        stage.setScene(new Scene(propertyRecords));
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Manage Property Records");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void manageAgentProfiles() throws Exception {
        System.out.println("manageAgentProfiles() called.");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("manageAgentProfiles-view.fxml"));
        Parent agentProfiles = fxmlLoader.load();
        ManageAgentProfilesController controller = fxmlLoader.getController();
        controller.setConnection(IGlobalApplication.getConnection());
        Stage stage = new Stage();
        stage.setScene(new Scene(agentProfiles));
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Manage Agent Profiles");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void manageExecutives() throws Exception {
        System.out.println("manageExecutives() called.");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("manageExecutiveProfiles-view.fxml"));
        Parent execProfiles = fxmlLoader.load();
        ManageExecutiveProfilesController controller = fxmlLoader.getController();
        controller.setConnection(IGlobalApplication.getConnection());
        Stage stage = new Stage();
        stage.setScene(new Scene(execProfiles));
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Manage Executive Profiles");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void manageAdministrators() throws Exception {
        System.out.println("manageAdministrators() called.");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("manageAdministratorProfiles-view.fxml"));
        Parent adminProfiles = fxmlLoader.load();
        ManageAdministratorProfilesController controller = fxmlLoader.getController();
        controller.setConnection(IGlobalApplication.getConnection());
        Stage stage = new Stage();
        stage.setScene(new Scene(adminProfiles));
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Manage Administrator Profiles");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void manageUserAccounts() throws Exception {
        System.out.println("manageUserAccounts() called.");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("manageUserAccounts-view.fxml"));
        Parent userAccountsView = fxmlLoader.load();
        ManageUserAccountsController controller = fxmlLoader.getController();
        controller.setDataStore(new UserAccountTableAdapter(false));
        Stage stage = new Stage();
        stage.setScene(new Scene(userAccountsView));
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Manage User Accounts");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void enableAdminControls() {
        agentPortalMenu.setDisable(true);
        executivePortalMenu.setDisable(true);
        loginMenuItem.setDisable(true);
        fileMenu.setDisable(false);
        logoutMenuItem.setDisable(false);
        mainMenu.setDisable(false);
        closeMenuItem.setDisable(false);
        adminPortalMenu.setDisable(false);
        userMenuItem.setVisible(true);
    }

    public void disableMenuItems() {
        agentPortalMenu.setDisable(true);
        executivePortalMenu.setDisable(true);
        adminPortalMenu.setDisable(true);
        logoutMenuItem.setDisable(true);
        userMenuItem.setVisible(false);
        fileMenu.setDisable(false);
        mainMenu.setDisable(false);
        closeMenuItem.setDisable(false);
        loginMenuItem.setDisable(false);
    }

    public void setUserFullname(String name) {
        userMenuItem.setText(name);
    }

    public void setUserName(String userAccountName) {
        this.userAccountName = userAccountName;
    }

    public String getUserFullname() {
        return userMenuItem.getText();
    }

    public String getUserAccountName() {
        return userAccountName;
    }

    public void displayAlert(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alert-View.fxml"));
            Parent alertWindow = fxmlLoader.load();
            AlertController alertController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(alertWindow));
            stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
            alertController.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex1) {
            System.out.println("Error in Display Alert " + ex1);
        }
    }

    public String encrypt(String password, String salt) {
        try {
            String saltedPassword = password + salt;
            MessageDigest crypto = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = saltedPassword.getBytes();
            byte[] passHash = crypto.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
