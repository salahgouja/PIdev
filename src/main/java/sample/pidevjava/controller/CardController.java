package sample.pidevjava.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import sample.pidevjava.model.Reservation;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;
import sample.pidevjava.model.User;
import javafx.scene.image.Image;

import java.awt.*;
import java.sql.SQLException;

public class CardController {
    @FXML
    private AnchorPane box;

    @FXML
    private Label date_label;

    @FXML
    private TextField date_txt;

    @FXML
    private MFXButton del_btn;

    @FXML
    private Label duree_label;

    @FXML
    private TextField duree_txt;

    @FXML
    private ImageView image;

    @FXML
    private Label prix_label;

    @FXML
    private TextField prix_txt;

    @FXML
    private Label terrain_label;
    @FXML
    private TextField temps_txt;

    @FXML
    private TextField terrain_txt;

    @FXML
    private Label user_label;

    @FXML
    private TextField user_txt;

    private String[] colors= {"B9E5FF", "BDB2FE", "FB9AA8","FF5856"};

    public void displayReservationDetails(Reservation reservation) {
        // Afficher les détails de la réservation dans les champs de texte correspondants
        date_txt.setText(reservation.getDate_reserve().toString());
        temps_txt.setText(reservation.getTemps_reservation().toString());
        if (reservation.getDuree_reservation() != null) {
            duree_txt.setText(reservation.getDuree_reservation().toString());
        } else {
            duree_txt.setText("Durée non spécifiée");
        }
        terrain_txt.setText(String.valueOf(reservation.getId_terrain()));
        prix_txt.setText(String.valueOf(reservation.getPrix_reservation()));

        // Récupérer l'utilisateur associé à cette réservation
        UserController userController = new UserController();
        User user = userController.getUserById(reservation.getId());
        if (user != null) {
            user_txt.setText(user.getFirstname() + " " + user.getLastname());
        } else {
            user_txt.setText("Utilisateur introuvable");
        }

        // Récupérer le terrain associé à cette réservation
        TerrainController terrainController = new TerrainController();
        TypeTerrain typeTerrain = getTypeTerrainFromReservation(reservation.getId_terrain());
        if (typeTerrain != null) {
            terrain_txt.setText(typeTerrain.name());
            // Construction du chemin de l'image en fonction du sport
            String sportImagePath = "/img/" + typeTerrain.name() + ".jpg";
            // Charger l'image et l'afficher dans l'ImageView
            Image sportImage = new Image(getClass().getResourceAsStream(sportImagePath));
            image.setImage(sportImage);
        } else {
            terrain_txt.setText("Terrain introuvable");

        }

        // Appliquer une couleur de fond aléatoire au conteneur
        box.setStyle("-fx-background-color: " + Color.web(colors[(int) (Math.random() * colors.length)]));
    }

    public TypeTerrain getTypeTerrainFromReservation(int Id_terrain) {
        TerrainController terrainController = new TerrainController();
        Terrain terrain = terrainController.getById(Id_terrain);
        System.out.println(terrain);
        if (terrain != null) {
            return terrain.getType();
        } else {
            throw new IllegalArgumentException("Terrain non trouvé pour l'ID : " + Id_terrain);
        }
    }
}

