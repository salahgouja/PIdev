package sample.pidevjava.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.pidevjava.db.DBConnection;

import sample.pidevjava.model.Article;

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




    @FXML
    void ajouterArticle(ActionEvent event) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            String titre = titreField.getText();
            String description = descriptionArea.getText();


            int nbcommentaire = 0;

            int iduser = 1;
            int nbrlike = 0;
            int nbrdislike = 0;

            PreparedStatement ps = connection.prepareStatement("INSERT INTO article (titre, description, date, nbcommentaire, iduser, nbrlike, nbrdislike) VALUES (?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, titre);
            ps.setString(2, description);
            ps.setObject(3, LocalDateTime.now());
            ps.setInt(4, nbcommentaire);
            ps.setInt(5, iduser);
            ps.setInt(6, nbrlike);
            ps.setInt(7,nbrdislike);
            ps.executeUpdate();

            System.out.println("Article added successfully.");
            ps.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    @FXML
    void initialize() {
        assert ajouterButton != null : "fx:id=\"ajouterButton\" was not injected: check your FXML file 'AjouterArticle.fxml'.";
        assert descriptionArea != null : "fx:id=\"descriptionArea\" was not injected: check your FXML file 'AjouterArticle.fxml'.";
        assert titreField != null : "fx:id=\"titreField\" was not injected: check your FXML file 'AjouterArticle.fxml'.";

    }

}