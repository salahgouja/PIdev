package sample.pidevjava.controller;
import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.pidevjava.Main;
import sample.pidevjava.model.User;



import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardEmployee implements Initializable {

    @FXML
    private JFXButton logout;
    @FXML
    private JFXButton profile;
    @FXML
    private JFXButton add_user;
    @FXML
    private TableColumn<User, String> clfirstname;
    @FXML
    private TableColumn<User, String> cllastname;
    @FXML
    private TableColumn<User, String> clphone;
    @FXML
    private TableColumn<User, String> clemail;

    @FXML
    private TableColumn<User, String> clpassword;
    @FXML
    private TableColumn<User, String> clrole;
    @FXML
    private Button add;
    @FXML
    private Button delete;
    @FXML
    private Button update;
    @FXML
    private TableView<User> table;




    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });
        clfirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        cllastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        clphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        clemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        clpassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        clrole.setCellValueFactory(new PropertyValueFactory<>("role"));
        table.setVisible(true);
        loadMenuInTable();


    }

    public void logout(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 14000, 800);
            Stage primarystage = (Stage) logout.getScene().getWindow();
            primarystage.setScene(scene);
            primarystage.setTitle("Login Form");
            primarystage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void profile(ActionEvent actionEvent) {
    }



    public void loadMenuInTable() {
        try {
            UserController userController = new UserController();
            List<User> userList = userController.getAllUsers();
            ObservableList<User> users = FXCollections.observableArrayList(userList);

            clfirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            cllastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            clphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            clemail.setCellValueFactory(new PropertyValueFactory<>("email"));
            clpassword.setCellValueFactory(new PropertyValueFactory<>("password"));
            clrole.setCellValueFactory(new PropertyValueFactory<>("role"));
            System.out.println(users);
            table.setItems(users);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void delete_user(ActionEvent actionEvent) {
        User user =  table.getSelectionModel().getSelectedItem();

        if (user == null) {
            // Aucune ligne sélectionnée, afficher un message d'erreur ou une alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText(" sélectionner un user à supprimer.");
            alert.show();
            return;
        }

        UserController userController = new UserController();
        userController.delete(user);

    }
    public void update_user(ActionEvent actionEvent) {
        User user = (User) table.getSelectionModel().getSelectedItem();

        if (user == null) {
            // Aucune ligne sélectionnée, afficher un message d'erreur ou une alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText(" sélectionner un user à mettre à jour.");
            alert.show();
            return;
        }
        UserController userController = new UserController();
        userController.update(user);
    }

    public void navigatetoadduser(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddUserForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = (Stage) add.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("AddUserForm");
        primaryStage.centerOnScreen();
    }
}
