package sample.pidevjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.pidevjava.db.DBConnection;

import java.io.IOException;
import java.sql.Connection;


public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GestionArticle2.fxml"));

        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        BorderPane root = new BorderPane();

        Scene scene = new Scene(fxmlLoader.load(), 1300, bounds.getHeight());
        primaryStage.setScene(scene);

        primaryStage.setY(bounds.getMinY());
        primaryStage.setX(0);
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setMinWidth(bounds.getWidth() / 2);
        primaryStage.setMaxWidth(bounds.getWidth());
        primaryStage.setMinHeight(bounds.getHeight());
        primaryStage.setX(0);

        primaryStage.setTitle("ActiveZone");
        primaryStage.show();
    }
}
