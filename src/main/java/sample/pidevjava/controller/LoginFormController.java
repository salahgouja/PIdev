package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.UserRole;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LoginFormController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML

    private ToggleButton showPasswordToggle;
    @FXML

    private TextField visiblePasswordTextField;
    @FXML


    private BorderPane root;


    public void btnSignup(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("RegistrationForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),600, 400);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register Form");
        primaryStage.centerOnScreen();

    }
    public void btnforgetpassword(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ForgetPassword.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),600, 400);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("ForgetPassword");
        primaryStage.centerOnScreen();
    }

    public void btnLogin(ActionEvent event) throws IOException {


        try {
            String Username = username.getText();
            String Password = HashPasswordController.hashPassword(password.getText());
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user where email =? and password = ?");
            preparedStatement.setString(1, Username);
            preparedStatement.setString(2, Password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                UserRole role = UserRole.valueOf(resultSet.getString("role")); // Fetch user role from database

                switch (role) {
                    case USER:
                        loadHomePage();
                        break;
                    case ADMIN:
                        loadDashboardAdmin();
                        break;
                    case EMPLOYEE:
                        loadDashboardEmployee();
                        break;
                    default:
                        break;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "UserName or Password Do not Match");
                alert.showAndWait();
                username.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void loadHomePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("DashboardUser.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard User");
        primaryStage.centerOnScreen();
    }

    private void loadDashboardAdmin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("DashboardAdmin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.centerOnScreen();
    }

    private void loadDashboardEmployee() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("DashboardEmployee.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Employee Dashboard");
        primaryStage.centerOnScreen();
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