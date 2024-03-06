package sample.pidevjava.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.pidevjava.db.DBConnection;

import sample.pidevjava.model.Article;
import sample.pidevjava.model.typec;

public class AjouterArticleController {



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button ajouterButton;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField titreField;

    @FXML // fx:id="choice"
    private ChoiceBox<typec> choice; // Value injected by FXMLLoader




    @FXML
    void ajouterArticle(ActionEvent event) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            String titre = titreField.getText();
            String description = descriptionArea.getText();

            if (titre.isEmpty() || description.isEmpty()) {
                System.out.println("Title or description cannot be empty. Article not added.");
                return;
            }

            int nbcommentaire = 0;
            int iduser = 1;
            int nbrlike = 0;
            int nbrdislike = 0;

            PreparedStatement ps = connection.prepareStatement("INSERT INTO article (titre, description, date, nbcommentaire, iduser, nbrlike, nbrdislike, choice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, titre);
            ps.setString(2, description);
            ps.setObject(3, LocalDateTime.now());
            ps.setInt(4, nbcommentaire);
            ps.setInt(5, iduser);
            ps.setInt(6, nbrlike);
            ps.setInt(7, nbrdislike);
            ps.setString(8, String.valueOf(choice.getValue()));
            ps.executeUpdate();


            System.out.println("Article added successfully.");
            ps.close();
            connection.close();

            // Get the stage and close it
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }






    @FXML
    void initialize() {
        assert ajouterButton != null : "fx:id=\"ajouterButton\" was not injected: check your FXML file 'AjouterArticle.fxml'.";
        assert descriptionArea != null : "fx:id=\"descriptionArea\" was not injected: check your FXML file 'AjouterArticle.fxml'.";
        assert titreField != null : "fx:id=\"titreField\" was not injected: check your FXML file 'AjouterArticle.fxml'.";
        choice.getItems().addAll(typec.values());
        choice.setValue(typec.AVIS);

    }

}