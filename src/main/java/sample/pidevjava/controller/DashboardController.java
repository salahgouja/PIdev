package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    private ListView<Article> articleListView;

    @FXML
    private VBox articleList;

    private List<Article> articles;

    @FXML
    private TextField search;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadArticles(); // Load all articles, including those with accepted set to 0

        articleListView.setCellFactory(new Callback<ListView<Article>, ListCell<Article>>() {
            @Override
            public ListCell<Article> call(ListView<Article> param) {
                return new ListCell<Article>() {
                    protected void updateItem(Article article, boolean empty) {
                        super.updateItem(article, empty);
                        if (empty || article == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            VBox content = new VBox(); // Use VBox to stack items vertically
                            content.setSpacing(10); // Add spacing between items
                            content.setPadding(new Insets(10)); // Add padding around items

                            Label titleLabel = new Label(article.getTitre());
                            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");

                            Label descriptionLabel = new Label(article.getDescription());
                            descriptionLabel.setWrapText(true); // Wrap text if it exceeds the width

                            Label dateLabel = new Label("Date: " + article.getDate());
                            dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888");

                            Label commentsLabel = new Label("Comments: " + article.getNbcommentaire());
                            commentsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888");

                            HBox buttonBox = new HBox(); // HBox to hold the buttons
                            buttonBox.setSpacing(10);
                            buttonBox.setAlignment(Pos.TOP_RIGHT); // Align buttons to the top right corner
                            buttonBox.setPadding(new Insets(10, 0, 0, 0));

                            if (!article.isAccepted()) {
                                Button accepterButton = new Button("Accepter");
                                accepterButton.setStyle("-fx-background-color: linear-gradient(to bottom, #4CAF50, #2E7D32); -fx-text-fill: white;");
                                accepterButton.setOnAction(event -> acceptArticle(article));
                                buttonBox.getChildren().add(accepterButton);

                                Button rejeterButton = new Button("Rejeter");
                                rejeterButton.setStyle("-fx-background-color: linear-gradient(to bottom, #FF5733, #E57300); -fx-text-fill: white;");
                                rejeterButton.setOnAction(event -> deleteArticle(article));
                                buttonBox.getChildren().add(rejeterButton);
                            } else {
                                Button supprimerButton = new Button("Supprimer");
                                supprimerButton.setStyle("-fx-background-color: linear-gradient(to bottom, #DE2667, #BA005E); -fx-text-fill: white;");
                                supprimerButton.setOnAction(event -> deleteArticle(article));
                                buttonBox.getChildren().add(supprimerButton);
                            }

                            content.getChildren().addAll(titleLabel, descriptionLabel, dateLabel, commentsLabel, buttonBox);

                            setGraphic(content);
                        }
                    }

                };
            }
        });

    }


    private void deleteArticle(Article article) {
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM article WHERE idarticle = ?")) {
            ps.setInt(1, article.getIdarticle());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Article deleted successfully from the database");
                articles.remove(article); // Remove the article from the local list
                articleListView.getItems().remove(article); // Remove the article from the ListView
            } else {
                System.out.println("Failed to delete article from the database");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
                boolean accepted = rs.getInt("accepted") == 1;
                typec t = typec.valueOf(rs.getString("choice"));

                Article article = new Article(idarticle, titre, description, date, nbcommentaire, iduser, nbrlike, nbrdislike, imageFileName, accepted, t);
                articles.add(article);
            }

            articleListView.getItems().addAll(articles); // Add all articles to the list view
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void acceptArticle(Article article) {
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE article SET accepted = 1 WHERE idarticle = ?")) {
            ps.setInt(1, article.getIdarticle());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Article accepted successfully.");
                article.setAccepted(true); // Update the accepted attribute locally
                // Refresh the list view to reflect the change
                articleListView.refresh();
            } else {
                System.out.println("Failed to accept article.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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

    /********************/
    @FXML
    private Button profile;
    @FXML
    private Button pageGestionUser;
    @FXML
    private Button pageGestionBlog;
    @FXML
    private Button pageGetionTerrain;
    @FXML
    private Button pageGetionOffre;
    @FXML
    private Button pageGestionPartenariat;
    @FXML
    private Button pageGestionReservation;
    @FXML
    private Button pageGestionEvent;

    @FXML
    private Button pageEvent;
    @FXML
    private Button pageReservation;
    @FXML
    private Button pagePartenaria;
    @FXML
    private Button pageBlog;
    @FXML
    private Button logout;



    @FXML
    private void goToPage(ActionEvent event) throws IOException {
        Stage primaryStage = null;
        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            String fxmlPath = null;
            switch (button.getId()) {
                case "pageGestionUser":
                    primaryStage = (Stage) pageGestionUser.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/DashboardAdmin.fxml";
                    break;
                case "pageGestionBlog":
                    primaryStage = (Stage) pageGestionBlog.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/DashBoard.fxml";
                    break;
                case "pageGetionTerrain":
                    primaryStage = (Stage) pageGetionTerrain.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashbordterrain.fxml";
                    break;
                case "pageGetionOffre":
                    primaryStage = (Stage) pageGetionOffre.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashbordappel.fxml";
                    break;
                case "pageGestionPartenariat":
                    primaryStage = (Stage) pageGestionPartenariat.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dash.fxml";
                    break;
                case "pageGestionReservation":
                    primaryStage = (Stage) pageGestionReservation.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashboardEvent.fxml";
                    break;
                case "pageGestionEvent":
                    primaryStage = (Stage) pageGestionEvent.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashboardEvent.fxml";
                    break;

                case "pageEvent":
                    primaryStage = (Stage) pageEvent.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/EventsPage.fxml";
                    break;

                case "pageReservation":
                    primaryStage = (Stage) pageReservation.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashboardEvent.fxml";
                    break;

                case "pagePartenaria":
                    primaryStage = (Stage) pagePartenaria.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/front.fxml";
                    break;

                case "pageBlog":
                    primaryStage = (Stage) pageBlog.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/GestionArticle2.fxml";
                    break;

                case "profile":
                    primaryStage = (Stage) profile.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/DashboardUser.fxml";
                    break;

                case "logout":
                    primaryStage = (Stage) logout.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/LoginFrom.fxml";
                    break;


            }
            if (fxmlPath != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                Scene scene = new Scene(fxmlLoader.load());
                primaryStage.setScene(scene);
            }
        }
    }

}




