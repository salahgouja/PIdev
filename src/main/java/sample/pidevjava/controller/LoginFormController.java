package sample.pidevjava.controller;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.User;
import sample.pidevjava.model.UserRole;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static sample.pidevjava.controller.UserSession.*;

public class LoginFormController {
    @FXML
    private Button scanQRCodeButton;
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/RegistrationForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),600, 400);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register Form");
        primaryStage.centerOnScreen();

    }
    public void btnforgetpassword(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/ForgetPassword.fxml"));
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
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(String.valueOf(role));

                setCurrentUser(user);

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
        System.out.println("User logged in: " + getCurrentUser().getEmail());

    }
    private void loadHomePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/DashboardUser.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);

        // Get the DashboardUser controller
        DashboardUser dashboardUserController = fxmlLoader.getController();

        // Set the user object in the DashboardUser controller
        dashboardUserController.setUser(UserSession.getCurrentUser());
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard User");
        primaryStage.centerOnScreen();
    }

    private void loadDashboardAdmin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/DashboardAdmin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);

        // Get the DashboardUser controller
        DashboardAdmin dashboardAdminController = fxmlLoader.getController();
        // Set the user object in the DashboardUser controller
        dashboardAdminController.setUser(UserSession.getCurrentUser());

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.centerOnScreen();
    }

    private void loadDashboardEmployee() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/DashboardEmployee.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);
        DashboardEmployee dashboardEmployeeController = fxmlLoader.getController();
        // Set the user object in the DashboardUser controller
        dashboardEmployeeController.setUser(UserSession.getCurrentUser());
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
    public void scanQRCode(ActionEvent event) {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();

        Result result = null;
        do {
            BufferedImage image = webcam.getImage();
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                result = new MultiFormatReader().decode(bitmap);
            } catch (NotFoundException e) {
                // QR code not found in the image
            }
        } while (result == null);

        webcam.close();

        String[] parts = result.getText().split(":");
        String email = parts[0];
        String password = parts[1];

        // Fill the username and password fields
        username.setText(email);
        this.password.setText(password);    }

}