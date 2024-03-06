package sample.pidevjava.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
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

    public CommentaireCard() {
        // This constructor can be empty or initialized with default values
    }

    public CommentaireCard(Commentaire commentaire) {
        this.commentaire = commentaire;

        Label contenuLabel = new Label(commentaire.getContenu());
        contenuLabel.setFont(Font.font("Arial", 12));
        contenuLabel.setTextFill(Color.BLACK);

        Label dateLabel = new Label("Date: " + commentaire.getDate());
        dateLabel.setFont(Font.font("Arial", 10));
        dateLabel.setTextFill(Color.DARKGRAY);

        FontAwesomeIconView commentIcon = new FontAwesomeIconView(FontAwesomeIcon.COMMENT_ALT);
        commentIcon.setFill(Color.DARKGOLDENROD);
        commentIcon.setSize("16px");

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
                getChildren().remove(modifierBox);
            });
        });

        supButton.setOnAction(event -> {
            int idcom = commentaire.getIdcom();
            GestionCommentaire.deleteCommentaire(idcom, commentaire.getIdarticle());
            ((VBox) getParent()).getChildren().remove(this);
        });

        HBox buttonBox = new HBox(modButton, supButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        BorderPane headerPane = new BorderPane();
        headerPane.setLeft(contenuLabel);
        headerPane.setBottom(dateLabel);
        headerPane.setRight(commentIcon);
        headerPane.setPadding(new Insets(5));
        headerPane.setStyle("-fx-background-color: #cce5ff; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-color: #99c2ff; -fx-border-width: 1px;");
        headerPane.setEffect(new DropShadow());

        BorderPane contentPane = new BorderPane();
        contentPane.setTop(headerPane);
        contentPane.setBottom(buttonBox);
        contentPane.setPadding(new Insets(5));
        contentPane.setStyle("-fx-background-color: transparent;");

        setPadding(new Insets(0, 5, 5, 5));
        setMaxWidth(300);
        getChildren().add(contentPane);

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
