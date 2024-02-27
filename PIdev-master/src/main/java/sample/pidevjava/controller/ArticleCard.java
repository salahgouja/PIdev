package sample.pidevjava.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.pidevjava.controller.GestionCommentaire;
import sample.pidevjava.controller.ModifierArticleController;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Article;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArticleCard extends StackPane {
    private Article article;

    private ImageView imageView;
    private Button modifierButton;
    private Button supprimerButton;
    private VBox content;

    private Button likeButton;
    private Button dislikeButton;

    public ArticleCard(Article article) {
        this.article = article;

        Label titleLabel = new Label(article.getTitre());
        titleLabel.getStyleClass().add("article-title");

        Label descriptionLabel = new Label(article.getDescription());
        descriptionLabel.getStyleClass().add("article-description");

        Label dateLabel = new Label("Date: " + article.getDate());
        dateLabel.getStyleClass().add("article-date");

        Label commentLabel = new Label("Comments: " + article.getNbcommentaire());
        commentLabel.getStyleClass().add("article-comments");

        Label nbrlike = new Label("Likes: " + article.getNbrlike());
        nbrlike.getStyleClass().add("article-date");
        Label nbrdislike = new Label("Dislikes: " + article.getNbrdislike());
        nbrdislike.getStyleClass().add("article-date");





        content = new VBox(titleLabel, descriptionLabel, dateLabel, commentLabel, nbrlike, nbrdislike );
        content.getStyleClass().add("article-content");

        likeButton = new Button("Like");
        likeButton.getStyleClass().add("like-button");
        likeButton.setOnAction(e -> {
            System.out.println("Like clicked for article: " + article.getIdarticle());
            try {
                DBConnection dbConnection = new DBConnection();
                Connection connection = dbConnection.getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE article SET nbrlike = nbrlike + 1 WHERE idarticle = ?");
                ps.setInt(1, article.getIdarticle());

                ps.executeUpdate();

                ps.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        dislikeButton = new Button("Dislike");
        dislikeButton.getStyleClass().add("dislike-button");
        dislikeButton.setOnAction(e -> {
            System.out.println("Dislike clicked for article: " + article.getIdarticle());
            try {
                DBConnection dbConnection = new DBConnection();
                Connection connection = dbConnection.getConnection();

                PreparedStatement ps = connection.prepareStatement("UPDATE article SET nbrdislike = nbrdislike + 1  WHERE idarticle = ?");
                ps.setInt(1, article.getIdarticle());

                ps.executeUpdate();

                ps.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        HBox likeDislikeBox = new HBox(10, likeButton, dislikeButton);
        likeDislikeBox.getStyleClass().add("like-dislike-box");
        content.getChildren().addAll(likeDislikeBox);

        modifierButton = new Button("Modifier");
        modifierButton.getStyleClass().add("modifier-button");
        modifierButton.setOnAction(e -> {
            System.out.println("Modifier clicked for article: " + article.getIdarticle());
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/ModifierArticle.fxml"));
                Parent root = loader.load();
                ModifierArticleController controller = loader.getController();
                controller.setArticle(article);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        supprimerButton = new Button("Supprimer");
        supprimerButton.getStyleClass().add("supprimer-button");
        supprimerButton.setOnAction(e -> {
            System.out.println("Supprimer clicked for article: " + article.getIdarticle());
            deleteArticle(article.getIdarticle());
            ((VBox) getParent()).getChildren().remove(this);
        });

        HBox buttonsBox = new HBox(modifierButton, supprimerButton);
        buttonsBox.getStyleClass().add("buttons-box");

        getChildren().addAll(content);

        setOnMouseClicked(e -> {
            if (getChildren().contains(content)) {
                getChildren().addAll(buttonsBox);
                getChildren().remove(content);
            } else {
                getChildren().removeAll(buttonsBox);
                getChildren().add(content);
            }
        });

        TextField commentField = new TextField();
        Button ajouterCommentaireButton = new Button("Ajouter Commentaire");
        ajouterCommentaireButton.getStyleClass().add("ajouter-commentaire-button");
        ajouterCommentaireButton.setVisible(false);

        commentField.textProperty().addListener((observable, oldValue, newValue) -> {
            ajouterCommentaireButton.setVisible(!newValue.isEmpty());
        });

        ajouterCommentaireButton.setOnAction(e -> {
            String commentaire = commentField.getText();
            if (!commentaire.isEmpty()) {
                addComment(commentaire);
                commentField.clear();
                article.setNbcommentaire(article.getNbcommentaire() + 1);
                commentLabel.setText("Comments: " + article.getNbcommentaire());
            }
        });

        HBox commentBox = new HBox(commentField, ajouterCommentaireButton);
        commentBox.getStyleClass().add("comment-box");
        content.getChildren().add(commentBox);
    }

    private void addComment(String commentaire) {
        GestionCommentaire.addComment(commentaire, article.getIdarticle(), article.getIduser());
    }

    private void deleteArticle(int articleId) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement("DELETE FROM article WHERE idarticle = ?");
            ps.setInt(1, articleId);
            ps.executeUpdate();

            System.out.println("Article deleted successfully.");

            ps.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}