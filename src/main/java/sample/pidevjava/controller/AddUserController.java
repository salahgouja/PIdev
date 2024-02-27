package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.User;
import sample.pidevjava.model.UserRole;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {
    @FXML

    private Label lblemail;
    @FXML

    private Label lblpassword;
    @FXML
    private Label lblphone;
    @FXML
    private BorderPane root3;
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
    private ChoiceBox<UserRole> lblrole;
    @FXML
    private Button add_user;
    @FXML
    private Button annuler;
    private UserRole role;

    public AddUserController() {}

    public void add_user(ActionEvent actionEvent) throws IOException {
        DBConnection conncexion = DBConnection.getInstance() ;
        System.out.println(conncexion);
        register();
    }

    public void register() {

        String userfirstname = firstname.getText();
        String userlastname = lastname.getText();
        String mobilephone = phone.getText();
        String useremail = email.getText();
        String pass = password.getText();
        String role = String.valueOf(lblrole.getValue());



        // Créer un objet User avec les données saisies par l'utilisateur

        User user = new User(userfirstname, userlastname, mobilephone, useremail, pass,role) ;

            try {
                Connection connection = DBConnection.getInstance().getConnection();


                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (firstname, lastname, phone, email, password,role) VALUES (?, ?, ?, ?, ?, ?)");


                preparedStatement.setString(1, userfirstname);
                preparedStatement.setString(2, userlastname);
                preparedStatement.setString(3, mobilephone);
                preparedStatement.setString(4, useremail);
                preparedStatement.setString(5, pass);
                preparedStatement.setString(6, String.valueOf(role));


                int i = preparedStatement.executeUpdate();


                if (i != 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Success");
                    alert.showAndWait();

                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("DashboardAdmin.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 1400, 800);
                    Stage primarystage = (Stage) root3.getScene().getWindow();
                    primarystage.setScene(scene);
                    primarystage.setTitle("DashboardAdmin");
                    primarystage.centerOnScreen();
                }
            }
            catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }

    }



    public void annuler(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("DashboardAdmin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = (Stage) annuler.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblrole.getItems().addAll(UserRole.values());


    }



}
