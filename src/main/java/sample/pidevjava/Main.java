package sample.pidevjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.pidevjava.controller.TerrainController;
import sample.pidevjava.controller.ajouterterraincontrolleur;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;

import java.io.IOException;
import java.sql.Connection;

import static sample.pidevjava.model.TypeTerrain.FOOTBALL;


public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("formulaireappel.fxml"));

        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        BorderPane root = new BorderPane();

        Scene scene = new Scene(fxmlLoader.load(), 800, bounds.getHeight());
        primaryStage.setScene(scene);

        // Set the stage to full-screen height
        primaryStage.setY(bounds.getMinY());
        primaryStage.setX(0);
        primaryStage.setHeight(bounds.getHeight());

        // Set minimum and maximum width to keep it resizable
        primaryStage.setMinWidth(bounds.getWidth()/2); // Set a minimum width
        primaryStage.setMaxWidth(bounds.getWidth()); // Set a maximum width
        primaryStage.setMinHeight(bounds.getHeight());
        primaryStage.setX(0);

        primaryStage.setTitle(" ActiveZone ");
        primaryStage.show();
    }
}
