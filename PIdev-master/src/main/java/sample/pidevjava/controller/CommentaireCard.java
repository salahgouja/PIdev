package sample.pidevjava.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Commentaire;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommentaireCard extends StackPane {
    private Commentaire commentaire;
    private Button modButton;
    private Button supButton;

    public CommentaireCard(Commentaire commentaire) {
        this.commentaire = commentaire;

        Label contenuLabel = new Label(commentaire.getContenu());
        contenuLabel.setFont(Font.font("Arial", 12));
        contenuLabel.setTextFill(Color.BLACK);

        Label dateLabel = new Label("Date: " + commentaire.getDate());
        dateLabel.setFont(Font.font("Arial", 10));
        dateLabel.setTextFill(Color.DARKGRAY);

        modButton = new Button("Mod");
        modButton.setVisible(false);
        modButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 10px;");
        supButton = new Button("Sup");
        supButton.setVisible(false);
        supButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 10px;");

        modButton.setOnAction(event -> {
            TextField textField = new TextField(commentaire.getContenu());
            Button modifierButton = new Button("Modifier");
            modifierButton.setStyle("-fx-background-color: #0078d4; -fx-text-fill: white; -fx-font-size: 10px;");

            VBox modifierBox = new VBox(textField, modifierButton);
            modifierBox.setSpacing(5);
            getChildren().add(modifierBox);

            modifierButton.setOnAction(e -> {
                String newContenu = textField.getText();
                updateCommentaire(newContenu);
                contenuLabel.setText(newContenu);
                textField.clear();
                getChildren().remove(modifierBox); // Remove the modifierBox after updating
            });
        });


        supButton.setOnAction(event -> {
            int idcom = commentaire.getIdcom();
            GestionCommentaire.deleteCommentaire(idcom, commentaire.getIdarticle());
            ((VBox) getParent()).getChildren().remove(this);
        });

        HBox buttonBox = new HBox(modButton, supButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        BorderPane contentPane = new BorderPane();
        contentPane.setTop(contenuLabel);
        contentPane.setLeft(dateLabel);
        contentPane.setBottom(buttonBox);
        contentPane.setPadding(new Insets(10));
        contentPane.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px;");

        getChildren().add(contentPane);
        setPadding(new Insets(5));
        setMaxWidth(300);

        setOnMouseClicked(event -> {
            modButton.setVisible(!modButton.isVisible());
            supButton.setVisible(!supButton.isVisible());
        });
    }

    private void updateCommentaire(String newContenu) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement("UPDATE commentaire SET contenu = ? WHERE idcom = ?");
            ps.setString(1, newContenu);
            ps.setInt(2, commentaire.getIdcom());
            ps.executeUpdate();

            System.out.println("Commentaire updated successfully.");

            ps.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
