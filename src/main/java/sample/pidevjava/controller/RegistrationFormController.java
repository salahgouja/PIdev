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
import sample.pidevjava.model.User;
import sample.pidevjava.model.UserRole;
import sample.pidevjava.validator.UserValidator;

import java.io.IOException;
import java.sql.*;

public class RegistrationFormController {
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField passwordconfirm;
    @FXML
    private Label lblpassword;
    @FXML
    private Label lblpasswordconfirm;
    @FXML
    private BorderPane root2 ;
    @FXML
    private Label lblemail;
    @FXML
    private Label lblphone;
    private UserRole userRole;


    public void initialize()  {
        lblpassword.setVisible(false);
        lblpasswordconfirm.setVisible(false);
        lblemail.setVisible(false);
        lblphone.setVisible(false);
        setBorderColor("transparent");
    }




    public void btnSignup(ActionEvent event)  {

        DBConnection conncexion = DBConnection.getInstance() ;
        System.out.println(conncexion);
        register();
    }

    public void btnLogin(ActionEvent event) throws IOException {
        // Load the login form
        FXMLLoader loginLoader = new FXMLLoader(Main.class.getResource("LoginForm.fxml"));
        Scene loginScene = new Scene(loginLoader.load(), 600, 400);

        // Get the primary stage
        Stage primaryStage = (Stage) root2.getScene().getWindow();

        // Set the login scene on the primary stage
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login Form");
        primaryStage.centerOnScreen();
    }

    public void passwordconfirmOnAction(ActionEvent event) {
    }

    public void register() {

        String userfirstname = firstname.getText();
        String userlastname = lastname.getText();
        String mobilephone = phone.getText();
        String useremail = email.getText();
        String pass = HashPasswordController.hashPassword(password.getText());
        String newpasswordconfirm = HashPasswordController.hashPassword(passwordconfirm.getText());



        boolean isValid = true;

        if (!UserValidator.isValidName(userfirstname)) {
            firstname.requestFocus();
            isValid = false;

        }

        if (!UserValidator.isValidName(userlastname)) {
            lastname.requestFocus();
            isValid = false;

        }

        if (!UserValidator.isValidEmail(useremail)) {
            lblemail.setVisible(true);
            email.requestFocus();
            isValid = false;

        }
        else {
            lblemail.setVisible(false);

        }
        if (!UserValidator.isValidPhone(mobilephone)) {
            lblphone.setVisible(true);
            phone.requestFocus();
            isValid = false;

        }
        else {
            lblphone.setVisible(false);
        }

        if (!isValid) {
            setBorderColor("red");
            return; // Stop registration process if any validation fails
        }
        if (pass.equals(newpasswordconfirm)) {
            lblpassword.setVisible(true);
            lblpasswordconfirm.setVisible(true);
            password.requestFocus();
        }
        else {
            lblpassword.setVisible(false);
            lblpasswordconfirm.setVisible(false);
            password.requestFocus();
            return; // Stop registration process if any validation fails

        }




        // Créer un objet User avec les données saisies par l'utilisateur
        User user = new User(userfirstname, userlastname, mobilephone, useremail, pass,String.valueOf(userRole.USER)) ;
        if (!UserValidator.validate(user)) {
            try {
                Connection connection = DBConnection.getInstance().getConnection();


                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (firstname, lastname, phone, email, password,role) VALUES (?, ?, ?, ?, ?, ?)");

                preparedStatement.setString(1, userfirstname);
                preparedStatement.setString(2, userlastname);
                preparedStatement.setString(3, mobilephone);
                preparedStatement.setString(4, useremail);
                preparedStatement.setString(5, pass);
                preparedStatement.setString(6, String.valueOf(userRole.USER));


                int i = preparedStatement.executeUpdate();


                if (i != 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Success");
                    alert.showAndWait();

                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginForm.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                    Stage primarystage = (Stage) root2.getScene().getWindow();
                    primarystage.setScene(scene);
                    primarystage.setTitle("Login Form");
                    primarystage.centerOnScreen();
                }
            }
            catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

        private void setBorderColor (String color){
            password.setStyle(" -fx-border-color: " + color);
            passwordconfirm.setStyle("-fx-border-color:" + color);
            email.setStyle("-fx-border-color:" + color);
            phone.setStyle("-fx-border-color:" + color);
            firstname.setStyle("-fx-border-color:" + color);
            lastname.setStyle("-fx-border-color:" + color);
        }
    }