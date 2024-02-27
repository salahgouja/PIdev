package sample.pidevjava.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.pidevjava.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardEmployee implements Initializable {
    @FXML

    private JFXButton logout;

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

            slide.setOnFinished((ActionEvent e)-> {
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

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });
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
}
