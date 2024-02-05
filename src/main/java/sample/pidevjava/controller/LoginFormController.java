package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LoginFormController {
    public TextField username;
    public PasswordField password;
    public ToggleButton showPasswordToggle;
    public TextField visiblePasswordTextField;


    public BorderPane root;


    public void btnSignup(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("RegistrationForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());


        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register Form");
        primaryStage.centerOnScreen();

    }

    public void btnLogin(ActionEvent event) throws IOException {


        try {
            String Username = username.getText();
            String Password = password.getText();
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user where email =? and password = ?");
            preparedStatement.setString(1, Username);
            preparedStatement.setString(2, Password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainForm.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);

                Stage primarystage = (Stage) root.getScene().getWindow();
                primarystage.setScene(scene);
                primarystage.setTitle("Main Form");
                primarystage.centerOnScreen();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "UserName or Password Do not Match");
                alert.showAndWait();
                username.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private boolean isPasswordVisible = false;

    public void initialize() {
        // This method is called when the FXML file is loaded.
        ToggleGroup toggleGroup = new ToggleGroup();
        showPasswordToggle.setToggleGroup(toggleGroup);
        showPasswordToggle.setSelected(false); // Default: password is hidden


        // Initially hide the visiblePasswordTextField
        visiblePasswordTextField.setManaged(false);
        visiblePasswordTextField.setVisible(false);

        showPasswordToggle.setOnMousePressed(event -> togglePasswordVisibility());
        showPasswordToggle.setOnMouseReleased(event -> togglePasswordVisibility() );

    }

    public void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        password.setManaged(!isPasswordVisible);
        password.setVisible(!isPasswordVisible);

        visiblePasswordTextField.setManaged(isPasswordVisible);
        visiblePasswordTextField.setVisible(isPasswordVisible);

        if (isPasswordVisible) {
            visiblePasswordTextField.setText(password.getText());
        } else {
            password.setText(visiblePasswordTextField.getText());
        }
    }
}