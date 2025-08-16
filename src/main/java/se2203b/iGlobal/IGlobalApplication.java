package se2203b.iGlobal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IGlobalApplication extends Application {

    private static Connection connection;
    private static final String DB_URL = "jdbc:derby:iGlobalDB;create=true";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(IGlobalApplication.class.getResource("iGlobal-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("iGlobal");
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void main(String[] args) {
        launch();
    }
}
