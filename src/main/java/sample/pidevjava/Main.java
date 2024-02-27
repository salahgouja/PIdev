package sample.pidevjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    public void start(Stage primaryStage)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginForm.fxml"));

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
            //testUserController();

        } catch (Exception e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }

    }
/*
    private static void testUserController() {

        UserController userController = new UserController();
        String useremailtofind = "salah@gmail.com"; // Change this to the email of the user you want to retrieve
        // Call the getUserById method
        User findUser = userController.getUserByEmail(useremailtofind);
        if (findUser != null) {
            System.out.println("User found:" + findUser);
        } else {
            System.out.println("User with id " + useremailtofind + " not found.");
        }


        /*
        // Create UserController instance
        UserController userController = new UserController();

        // Test adding a user
        User newUser = new User("salah", "gouja", "salah@gmail.com", "923456789", "password123", UserRole.USER);
        userController.add(newUser);
        System.out.println("User added successfully.");

         //Test getting all users
        ArrayList<User> allUsers = userController.getAllUsers();
        System.out.println("All Users before delete:");
        for (User user : allUsers) {
            System.out.println(user);
        }


         //Test deleting a user
        int userIdToDelete = 3; // 1 the id of the user you want to delete
        //  Create a temporary User object with the id
        User userToDelete = new User();
        userToDelete.setId(userIdToDelete);
        userController.delete(userToDelete);

        ArrayList<User> Users = userController.getAllUsers();
        System.out.println("All Users after delete:");
        for (User user : Users) {
            System.out.println(user);
        }

        int userIdtofind = 2; // Change this to the id of the user you want to retrieve
        // Call the getUserById method
        User findUser = userController.getUserById(userIdtofind);
        if (findUser != null) {
            System.out.println("User found:" + findUser);
        } else {
            System.out.println("User with id " + userIdtofind + " not found.");
        }

        int userIdToUpdate = 2; //  the id of the user to update

        // Retrieve the user by id
        User userToUpdate = userController.getUserById(userIdToUpdate);

        // Check if the user exists
        if (userToUpdate != null) {
            // Modify user's information
            userToUpdate.setFirstname("UpFirstname");
            userToUpdate.setLastname("UpLastname");
            userToUpdate.setPhone("98765432");
            userToUpdate.setEmail("hhh@gmail.com");
            userToUpdate.setPassword("updatedPassword");
            userToUpdate.setRole(String.valueOf(UserRole.ADMIN)); // Change this to the desired role

            userController.update(userToUpdate);
            System.out.println("User with id " + userIdToUpdate + " has been successfully updated.");
        } else {
            System.out.println("User with id " + userIdToUpdate + " not found. Update operation aborted.");
        }
        ArrayList<User> Users1 = userController.getAllUsers();
        System.out.println("All Users after update:");
        for (User user : Users1) {
           System.out.println(user);
        }


    }*/

}



