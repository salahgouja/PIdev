package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.User;
import sample.pidevjava.validator.UserValidator;

import java.io.IOException;
import java.sql.*;

public class RegistrationFormController {
    public TextField lastname;
    public TextField phone;
    public TextField email;
    public PasswordField password;
    public PasswordField passwordconfirm;
    public Label lblpassword;
    public Label lblpasswordconfirm;

    public BorderPane root2 ;
    public Label lblemail;
    public Label lblphone;
    public TextField firstname;


    public void initialize() throws SQLException {
        lblpassword.setVisible(false);
        lblpasswordconfirm.setVisible(false);
        lblemail.setVisible(false);
        lblphone.setVisible(false);
        setBorderColor("transparent");

        getId();
    }


    public int getId() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select id from user order by id desc limit 1");
        if (resultSet.next()) {
            int intId = resultSet.getInt(1);
            intId++;
            return intId;
        } else {
            return 1; // If no records found, start with 1
        }
    }

    public void btnSignup(ActionEvent event)  {
        register();

        DBConnection DB = DBConnection.getInstance() ;
        System.out.println(DB);
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


    public void register(){

        String userfirstname = firstname.getText();
        String userlastname = lastname.getText();
        String mobilephone = phone.getText();
        String useremail = email.getText();
        String pass = password.getText();
        String newpassword = password.getText();
        String newpasswordconfirm = passwordconfirm.getText();


        if (!UserValidator.isValidName(userfirstname)) {
            firstname.requestFocus();
        }else {
            setBorderColor("red");
            firstname.requestFocus();
        }
        if (!UserValidator.isValidName(userlastname)) {
            lastname.requestFocus();
        }else {
            setBorderColor("red");
            lastname.requestFocus();
        }
        if (!UserValidator.isValidEmail(useremail)) {

            lblemail.setVisible(true);
            email.requestFocus();
        }else {
            setBorderColor("red");
            lblemail.setVisible(false);
            email.requestFocus();
        }
        if (!UserValidator.isValidPhone(mobilephone)) {

            lblphone.setVisible(true);
            phone.requestFocus();
        }else {
            setBorderColor("red");
            lblphone.setVisible(false);
            phone.requestFocus();
        }

        if (newpassword.equals(newpasswordconfirm)) {

            lblpassword.setVisible(false);
            lblpasswordconfirm.setVisible(false);
            password.requestFocus();
        }
        else{
            setBorderColor("red");
            lblpassword.setVisible(true);
            lblpasswordconfirm.setVisible(true);
            password.requestFocus();
        }


           // Créer un objet User avec les données saisies par l'utilisateur
            User user = new User(pass, userfirstname, userlastname, useremail, mobilephone);
            // Valider l'utilisateur en utilisant UserValidator cree separement
            if (UserValidator.validate(user)) {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                int id = getId();

                PreparedStatement preparedStatement = connection.prepareStatement("insert into user(id,firstname,lastname,phone,email,password)values(?,?,?,?,?,?) ");
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, userfirstname);
                preparedStatement.setString(3, userlastname);
                preparedStatement.setString(4, mobilephone);
                preparedStatement.setString(5, useremail);
                preparedStatement.setString(6, pass);
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
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
             }


    }

    private void setBorderColor(String color) {
        password.setStyle(" -fx-border-color: "+color);
        passwordconfirm.setStyle("-fx-border-color:"+color);
        email.setStyle("-fx-border-color:"+color);
        phone.setStyle("-fx-border-color:"+color);
        firstname.setStyle("-fx-border-color:"+color);
        lastname.setStyle("-fx-border-color:"+color);


    }

}

