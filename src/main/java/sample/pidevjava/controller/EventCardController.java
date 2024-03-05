package sample.pidevjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Evenement;
import sample.pidevjava.model.Participation;
import sample.pidevjava.model.user;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

public class EventCardController extends  ISevecesEvent{
    @FXML
    private Label eventPrix ;

    @FXML
    private Label eventDesc;
    @FXML
    private Label eventDate;



    @FXML
    private ImageView eventImg;

    @FXML
    private Label eventTitre;

    private Evenement eventData;


    @FXML
    private Label paticipationDate;

    @FXML
    private Label paticipationEvnt;


    @FXML
    private Label paticipationUser;

    public Participation selectedParticipation;




    public void setEventData(Evenement event) {

        eventImg.setFitHeight(30);
        eventImg.setFitWidth(30);
        eventImg.setPreserveRatio(true);
        // Set event data to the labels
        eventPrix.setText(event.getPrix()+ " DT");
        eventDate.setText(event.getDate());
       // eventId.setText(String.valueOf(event.getId_event()));
        eventTitre.setText(event.getTitre());
        Image image = convertBase64ToImage(event.getImage());
        eventImg.setImage(image);
    }
    public void setEventDataUser(Evenement event) {
        eventData = event;
        eventImg.setFitHeight(100);
        eventImg.setFitWidth(300);
        eventImg.setPreserveRatio(true);
        // Set event data to the labels
        eventPrix.setText(event.getPrix()+ " DT");
        eventDesc.setText(event.getDescription());
        eventDate.setText(event.getDate());
        eventTitre.setText(event.getTitre());
        Image image = convertBase64ToImage(event.getImage());
        eventImg.setImage(image);
    }

    public Image convertBase64ToImage(String base64Image) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        return new Image(new ByteArrayInputStream(imageBytes));
    }


    public void setParticipationData(Participation p) {
        user user = getUserById(p.getId());
        Evenement evenement = getEventById(p.getId_event());
        paticipationEvnt.setText(String.valueOf(evenement.getTitre()));
        paticipationDate.setText(p.getDateDeCreation());
        paticipationUser.setText(user.getName() +" "+ user.getSurname());
    }


    public void setParticipation(Participation participation) {
        selectedParticipation = participation;
    }







    public void handleSelectItem() {

        // Handle the button click here
        if (eventData != null) {
            user cutentUser = getUserById(2);
            String userName = cutentUser.getName() + cutentUser.getSurname();

            int id_participation = 0;
            final int eventId = eventData.getId_event();
            int id_user = 5;
            String etat ="en attend";
            String dateDeCreation = LocalDate.now().toString();
            Participation participation = new Participation(id_participation,etat,eventId, id_user ,dateDeCreation);
            String qry = "INSERT INTO `participation` ( `etat`, `id_event`, `id`,`dateDeCreation`) VALUES ( ?, ?,?, ?)";
            showAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Confirmation Dialog",
                     cutentUser.getName() +" "+ cutentUser.getSurname() +"\nÊtes-vous sur de vouloir continuer? \n",
                    "oui",
                    (_void) -> {
                        try {
                            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
                            stm.setString(1, participation.getEtat());
                            stm.setInt(2, participation.getId_event());
                            stm.setInt(3, participation.getId());
                            stm.setString(4, participation.getDateDeCreation());
                            stm.executeUpdate();

                            showAlert(
                                    Alert.AlertType.CONFIRMATION,
                                    "Confirmation Dialog",
                                    "Votre participation a été créée avec succès.\n" +
                                            "Nous vous contacterons dans les plus brefs délais par email\n" +
                                            "à l'adresse e-mail fournie "+cutentUser.getEmail() +
                                            "\nou en vous appelant sur votre numéro de\n téléphone "+cutentUser.getPhone(),

                                    "ok",
                                    (e) -> {
                                        System.out.println("Confirmed!");
                                        return null;
                                    }
                            );

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
            );

        } else {
            System.out.println("No item selected.");
        }
    }




    /********************************participation recpance******************************/


    @FXML
    void participationaccepter() throws Exception {

        int id;
        String query = "UPDATE participation SET etat=? WHERE id_participation=?";

        id = selectedParticipation.getId_participation();
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, "accepter");
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String qrCodeData = "https://example.com";
        int qrCodeSize = 250;
        Image qrCodeImage = QRCodeGenerator.generateQRCodeImage(qrCodeData, qrCodeSize);
        PDFCreator.createPdfAccepted(qrCodeImage);
        JavaMailUtil.sendMail("chebili335@gmail.com"," Réponse  du complexe sportif concernant votre demande de participation à un événement","HelloWorld_out.pdf");
    }

    @FXML
    void participationRefuser(){
        int id;
        String query = "UPDATE participation SET etat=? WHERE id_participation=?";

        id = selectedParticipation.getId_participation();
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, "refuser");
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }








}


//System.out.println("Selected item: " + eventData.getTitre());