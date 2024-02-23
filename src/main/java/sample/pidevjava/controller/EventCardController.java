package sample.pidevjava.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Evenement;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

public class EventCardController {
    @FXML
    private Label eventPrix ;

    @FXML
    private Label eventDate;

    @FXML
    private Label eventId;

    @FXML
    private ImageView eventImg;

    @FXML
    private Label eventTitre;



    public void setEventData(Evenement event) {


        // Set event data to the labels
        eventPrix.setText(event.getPrix());
        eventDate.setText(event.getDate());
        eventId.setText(String.valueOf(event.getId_event()));
        eventTitre.setText(event.getTitre());
        Image image = convertBase64ToImage(event.getImage());
        eventImg.setImage(image);
    }


    private Image convertBase64ToImage(String base64Image) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        return new Image(new ByteArrayInputStream(imageBytes));
    }

}
