package sample.pidevjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.pidevjava.controller.EventsPageController;
import sample.pidevjava.model.Evenement;

import java.io.IOException;
import java.util.ArrayList;



public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboardEvent.fxml"));
        //Scene scene = new Scene(fxmlLoader.load());


        // Get the screen bounds
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // Create a BorderPane for the root of the scene
        BorderPane root = new BorderPane();

        // Create a Scene with root and set the width to be resizable
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