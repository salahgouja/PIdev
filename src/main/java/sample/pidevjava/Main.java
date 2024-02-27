package sample.pidevjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.pidevjava.controller.HoraireController;
import sample.pidevjava.controller.ReservationController;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.*;
import sample.pidevjava.controller.ChoixSportController;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import static sample.pidevjava.model.Jours.MARDI;

public class Main extends Application {

    public static void main(String[] args) {


        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ReservationAdmin2.fxml"));
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


        // Creating a sample HoraireTravail object
        //HoraireTravail horaire = new HoraireTravail("MARDI","15:00","16:00",false );

        //System.out.println(horaire);

        // Creating an instance of DBConnection
       // DBConnection.getInstance().getConnection(); // Assuming you have a connect() method to establish the database connection

        // Creating an instance of your class where the add method is defined
       // HoraireController instance = new HoraireController(); // Replace YourClassNameHere with the actual class name
       //  instance.update("12:00", "14:00",false);

        // Testing the add method
        //System.out.println(instance.jourExisteDeja(horaire.getJour()));




}
}