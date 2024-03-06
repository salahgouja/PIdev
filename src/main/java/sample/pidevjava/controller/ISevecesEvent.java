package sample.pidevjava.controller;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.interfaces.IServices;
import sample.pidevjava.model.Evenement;
import sample.pidevjava.model.Participation;
import sample.pidevjava.model.user;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Function;

public class ISevecesEvent implements Initializable {

    ArrayList<Evenement> evenements = new ArrayList<>();
    ArrayList<Participation> paticipations = new ArrayList<>();
    ArrayList<user> users = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void showAlert(Alert.AlertType alertType, String title, String content, String buttonText , Function<Void, Void> buttonFonction) {

        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);

        alertStage.setTitle(title);
        Label messageLabel = new Label(content);

        Button Button = new Button(buttonText);
        Button.setOnAction( e -> {
            buttonFonction.apply(null); // Execute the provided button action
            alertStage.close(); // Close the alert after action execution
        });

        messageLabel.getStyleClass().add("message-label");
        Button.getStyleClass().add("button");
      //  alertStage.getStyleClass().add("custom-alert");


        VBox alertLayout = new VBox(10);

        alertLayout.setPadding(new javafx.geometry.Insets(10));
        alertLayout.getChildren().addAll(messageLabel,Button);

//        Scene sceneAlert = new Scene(root, 300, 200);
//        sceneAlert.getStylesheets().add(Main.class.getResource("eventCssFile.css").toExternalForm());
//
//        alertStage.setScene(new Scene(alertLayout));
//        alertStage.showAndWait();



        Scene scene = new Scene(alertLayout,300,200);
       // scene.getStylesheets().add(Main.class.getResource("@style/eventCssFile.css").toExternalForm()); // Load CSS file
        alertStage.setScene(scene);
        alertStage.showAndWait();
    }


    public ArrayList<Evenement> getAll() {
        String query = "SELECT * FROM evenement";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Evenement evenement = new Evenement();
                evenement.setId_event(resultSet.getInt("id_event"));
                evenement.setDate(resultSet.getString("date"));
                evenement.setTitre(resultSet.getString("titre"));
                evenement.setDescription(resultSet.getString("description"));
                evenement.setPrix(resultSet.getString("prix"));
                evenement.setCategorie(resultSet.getString("categorie"));
                evenement.setImage(resultSet.getString("image"));
                evenements.add(evenement);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenements;
    }

    public ArrayList<Participation> getAllParticipation() {
        String query = "SELECT * FROM participation";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Participation participation = new Participation();
                participation.setId_participation(resultSet.getInt("id_participation"));
                participation.setEtat(resultSet.getString("etat"));
                participation.setId_event(resultSet.getInt("id_event"));
                participation.setId(resultSet.getInt("id"));
                participation.setDateDeCreation(resultSet.getString("dateDeCreation"));
                paticipations.add(participation);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paticipations;
    }


    public user getUserById(int userId) {
        String query = "SELECT * FROM user WHERE id = ?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user user = new user();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setName(resultSet.getString("firstname"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setSurname(resultSet.getString("lastname"));
                resultSet.close();
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user with specified ID is not found
    }

    public Evenement getEventById(int eventId) {
        String query = "SELECT * FROM evenement WHERE id_event = ?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Evenement e = new Evenement();
                e.setId_event(resultSet.getInt("id_event"));
                e.setDate(resultSet.getString("date"));
                e.setTitre(resultSet.getString("titre"));
                e.setDescription(resultSet.getString("description"));
                e.setPrix(resultSet.getString("prix"));
                e.setCategorie(resultSet.getString("categorie"));
                e.setImage(resultSet.getString("image"));
                resultSet.close();
                return e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user with specified ID is not found
    }

}
