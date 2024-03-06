package sample.pidevjava;

import com.aspose.pdf.Document;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.pidevjava.controller.EventsPageController;
import sample.pidevjava.controller.JavaMailUtil;
import sample.pidevjava.controller.PDFCreator;
import sample.pidevjava.controller.QRCodeGenerator;
import sample.pidevjava.model.Evenement;

import org.xml.sax.helpers.DefaultHandler;
import sample.pidevjava.controller.UserController;
import sample.pidevjava.model.User;
import sample.pidevjava.model.UserRole;

import java.io.IOException;
import java.util.ArrayList;

import static sample.pidevjava.model.UserRole.ADMIN;

public class Main extends Application {
    double x, y = 0;

    public static void main(String[] args) {
        launch();
    }

    @Override

    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/LoginForm.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        scene.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        primaryStage.setTitle("LOGIN");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
        System.out.println("Scene displayed successfully.");
        // Test the UserController
        // testUserController();

        // Get the screen bounds
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // Create a BorderPane for the root of the scene
        BorderPane root = new BorderPane();

        // Create a Scene with root and set the width to be resizable
        primaryStage.setScene(scene);

        // Set the stage to full-screen height
        primaryStage.setY(bounds.getMinY());
        primaryStage.setX(0);
        primaryStage.setHeight(bounds.getHeight());

        // Set minimum and maximum width to keep it resizable
        primaryStage.setMinWidth(bounds.getWidth() / 2); // Set a minimum width
        primaryStage.setMaxWidth(bounds.getWidth()); // Set a maximum width
        primaryStage.setMinHeight(bounds.getHeight());
        primaryStage.setX(0);

        primaryStage.setTitle(" ActiveZone ");
        primaryStage.show();

    }
    /*
     * private static void testUserController() {
     * 
     * UserController userController = new UserController();
     * String useremailtofind = "salah@gmail.com"; // Change this to the email of
     * the user you want to retrieve
     * // Call the getUserById method
     * User findUser = userController.getUserByEmail(useremailtofind);
     * if (findUser != null) {
     * System.out.println("User found:" + findUser);
     * } else {
     * System.out.println("User with id " + useremailtofind + " not found.");
     * }
     * 
     * 
     * /*
     * // Create UserController instance
     * UserController userController = new UserController();
     * 
     * // Test adding a user
     * User newUser = new User("salah", "gouja", "salah@gmail.com", "923456789",
     * "password123", UserRole.USER);
     * userController.add(newUser);
     * System.out.println("User added successfully.");
     * 
     * //Test getting all users
     * ArrayList<User> allUsers = userController.getAllUsers();
     * System.out.println("All Users before delete:");
     * for (User user : allUsers) {
     * System.out.println(user);
     * }
     * 
     * 
     * //Test deleting a user
     * int userIdToDelete = 3; // 1 the id of the user you want to delete
     * // Create a temporary User object with the id
     * User userToDelete = new User();
     * userToDelete.setId(userIdToDelete);
     * userController.delete(userToDelete);
     * 
     * ArrayList<User> Users = userController.getAllUsers();
     * System.out.println("All Users after delete:");
     * for (User user : Users) {
     * System.out.println(user);
     * }
     * 
     * int userIdtofind = 2; // Change this to the id of the user you want to
     * retrieve
     * // Call the getUserById method
     * User findUser = userController.getUserById(userIdtofind);
     * if (findUser != null) {
     * System.out.println("User found:" + findUser);
     * } else {
     * System.out.println("User with id " + userIdtofind + " not found.");
     * }
     * 
     * int userIdToUpdate = 2; // the id of the user to update
     * 
     * // Retrieve the user by id
     * User userToUpdate = userController.getUserById(userIdToUpdate);
     * 
     * // Check if the user exists
     * if (userToUpdate != null) {
     * // Modify user's information
     * userToUpdate.setFirstname("UpFirstname");
     * userToUpdate.setLastname("UpLastname");
     * userToUpdate.setPhone("98765432");
     * userToUpdate.setEmail("hhh@gmail.com");
     * userToUpdate.setPassword("updatedPassword");
     * userToUpdate.setRole(String.valueOf(UserRole.ADMIN)); // Change this to the
     * desired role
     * 
     * userController.update(userToUpdate);
     * System.out.println("User with id " + userIdToUpdate +
     * " has been successfully updated.");
     * } else {
     * System.out.println("User with id " + userIdToUpdate +
     * " not found. Update operation aborted.");
     * }
     * ArrayList<User> Users1 = userController.getAllUsers();
     * System.out.println("All Users after update:");
     * for (User user : Users1) {
     * System.out.println(user);
     * }
     * 
     * 
     * }
     */

}
