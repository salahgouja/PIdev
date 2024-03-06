package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.w3c.dom.events.MouseEvent;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Article;
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

public class DashboardController implements Initializable {

    @FXML
    private ListView<Article> articleListView; // Ensure this field is correctly annotated with @FXML

    @FXML
    private VBox articleList;


    private List<Article> articles;
    @FXML
    private TextField search;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadArticles(); // Load articles from the database

        // Set cell factory to customize how each article is displayed in the list view
        articleListView.setCellFactory(new Callback<ListView<Article>, ListCell<Article>>() {
            @Override
            public ListCell<Article> call(ListView<Article> param) {
                return new ListCell<Article>() {
                    @Override
                    protected void updateItem(Article article, boolean empty) {
                        super.updateItem(article, empty);
                        if (empty || article == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            HBox content = new HBox(); // Use HBox to align items horizontally
                            content.setSpacing(5);

                            Label titleLabel = new Label(article.getTitre());
                            titleLabel.setStyle("-fx-font-weight: bold");

                            Label descriptionLabel = new Label(article.getDescription());

                            Label dateLabel = new Label("Date: " + article.getDate());

                            Label commentsLabel = new Label("Comments: " + article.getNbcommentaire());

                            Button deleteButton = new Button("Supprimer");
                            deleteButton.setStyle("-fx-background-color: #DE2667; -fx-text-fill: white;");
                            deleteButton.setOnAction(event -> {
                                // Handle delete action here
                                articles.remove(article); // Remove the article from the list
                                articleListView.getItems().remove(article); // Remove the article from the ListView
                                // You may also want to delete the article from the database
                                deleteArticle(article);
                            });

                            content.getChildren().addAll(titleLabel, descriptionLabel, dateLabel, commentsLabel, deleteButton);

                            setGraphic(content);
                        }
                    }





                };
            }
        });
    }

    @FXML
    public void openAjouterArticle(ActionEvent event) {
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


    private void deleteArticle(Article article) {
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM article WHERE idarticle = ?")) {
            ps.setInt(1, article.getIdarticle());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Article deleted successfully from the database");
            } else {
                System.out.println("Failed to delete article from the database");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }





    @FXML
    public void stat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/StatsPage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadArticles() {
        articles = new ArrayList<>(); // Initialize articles list

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM article");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idarticle = rs.getInt("idarticle");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                String date = rs.getString("date");
                int nbcommentaire = rs.getInt("nbcommentaire");
                int iduser = rs.getInt("iduser");
                int nbrlike = rs.getInt("nbrlike");
                int nbrdislike = rs.getInt("nbrdislike");
                String imageFileName = rs.getString("imageFileName");
                typec t = typec.valueOf(rs.getString("choice"));

                Article article = new Article(idarticle, titre, description, date, nbcommentaire, iduser, nbrlike, nbrdislike, imageFileName, t);
                articles.add(article);
            }

            articleListView.getItems().addAll(articles); // Add all articles to the list view
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }





    @FXML
    public void btnsearch(javafx.scene.input.MouseEvent mouseEvent) {
        if (search != null) {
            String searchText = search.getText().toLowerCase(); // Convert search text to lowercase for case-insensitive search
            List<Article> filteredArticles = articles.stream()
                    .filter(article -> article.getTitre().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());

            // Clear current items in the ListView and add the filtered articles
            articleListView.getItems().clear();
            articleListView.getItems().addAll(filteredArticles);

            System.out.println("Searching for: " + searchText);
        } else {
            System.out.println("Search field is null");
        }
    }


}
