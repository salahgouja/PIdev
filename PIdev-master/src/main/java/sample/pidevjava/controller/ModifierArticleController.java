package sample.pidevjava.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Article;

public class ModifierArticleController implements Initializable {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

    private Article articleToUpdate;

    public void setArticle(Article article) {
        this.articleToUpdate = article;
        titreField.setText(article.getTitre());
        descriptionArea.setText(article.getDescription());
    }

    @FXML
    void updatearticle(ActionEvent event) {
        if (articleToUpdate != null) {
            try {
                DBConnection dbConnection = new DBConnection();
                Connection connection = dbConnection.getConnection();

                String newTitre = titreField.getText();
                String newDescription = descriptionArea.getText();

                PreparedStatement ps = connection.prepareStatement("UPDATE article SET titre = ?, description = ?, date = ? WHERE idarticle = ?");
                ps.setString(1, newTitre);
                ps.setString(2, newDescription);
                ps.setObject(3, LocalDateTime.now());
                ps.setInt(4, articleToUpdate.getIdarticle());
                ps.executeUpdate();

                System.out.println("Article updated successfully.");
                ps.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
