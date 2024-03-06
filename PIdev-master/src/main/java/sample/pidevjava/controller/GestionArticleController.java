package sample.pidevjava.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Article;
import sample.pidevjava.model.Commentaire;
import sample.pidevjava.model.typec;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class GestionArticleController implements Initializable {


    @FXML
    private TilePane articleContainer;

    @FXML
    private TextField searchField;

    private List<Article> articles;
    @FXML
    private ChoiceBox<typec> typeFilterChoiceBox;

    @FXML
    private TextField filterValueTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadArticles();
        populateTypeFilterChoiceBox();
    }

    private void populateTypeFilterChoiceBox() {
        List<typec> types = articles.stream()
                .map(Article::getT)
                .distinct()
                .collect(Collectors.toList());

        types.add(0, typec.All); // Add "All" at the beginning of the list

        typeFilterChoiceBox.getItems().addAll(types);
    }



    @FXML
    public void handleFilter(ActionEvent event) {
        typec selectedType = typeFilterChoiceBox.getValue();

        List<Article> filteredArticles;
        if (selectedType == typec.All) {
            filteredArticles = articles;
        } else {
            filteredArticles = articles.stream()
                    .filter(article -> article.getT() == selectedType)
                    .collect(Collectors.toList());
        }

        articleContainer.getChildren().clear();
        for (Article article : filteredArticles) {
            ArticleCard card = new ArticleCard(article);
            articleContainer.getChildren().add(card);
        }
    }







    private void loadArticles() {
        articles = new ArrayList<>(); // Initialize articles list
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM article");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idarticle = rs.getInt("idarticle");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                String date = rs.getString("date");
                int nbcommentaire = rs.getInt("nbcommentaire");
                int iduser = rs.getInt("iduser");
                int nbrlike = rs.getInt("nbrlike");
                int nbrdislike = rs.getInt("nbrdislike");
                String imageFileName = rs.getString("imageFileName");// Fetch image file name from the database
                typec t = typec.valueOf(rs.getString("choice"));

                Article article = new Article(idarticle, titre, description, date, nbcommentaire, iduser, nbrlike, nbrdislike, imageFileName, t);
                articles.add(article);

                VBox articleBox = new VBox();
                ArticleCard articleCard = new ArticleCard(article);
                articleBox.getChildren().add(articleCard);
                displayComments(article, articleBox);

                articleContainer.getChildren().add(articleBox);
            }

            ps.close();
            rs.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @FXML
    public void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        List<Article> filteredArticles = articles.stream()
                .filter(article -> article.getTitre().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        articleContainer.getChildren().clear();
        for (Article article : filteredArticles) {
            ArticleCard card = new ArticleCard(article);
            articleContainer.getChildren().add(card);
        }
    }



    private void displayComments(Article article, VBox articleBox) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM commentaire WHERE idarticle = ?");
            ps.setInt(1, article.getIdarticle());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idcom = rs.getInt("idcom");
                String contenu = rs.getString("contenu");
                String date = rs.getString("date");
                int iduser = rs.getInt("iduser");
                int nblike = rs.getInt("nblike");
                int nbdislike = rs.getInt("nbdislike");

                Commentaire commentaire = new Commentaire(idcom, contenu, date, article.getIdarticle(), iduser, nblike, nbdislike);
                CommentaireCard commentaireCard = new CommentaireCard(commentaire);
                articleBox.getChildren().add(commentaireCard);
            }

            ps.close();
            rs.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        articleContainer.getChildren().clear();
        loadArticles();
    }



    @FXML
    void handleAjouterArticle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/AjouterArticle.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}

