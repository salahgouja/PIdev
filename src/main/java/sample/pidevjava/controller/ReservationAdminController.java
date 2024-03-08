package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Reservation;
import sample.pidevjava.model.TypeTerrain;
import sample.pidevjava.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReservationAdminController implements Initializable {
    @FXML
    private ImageView loopIcon;
    @FXML
    private TextField searchField;

    @FXML
    private FlowPane cardLyout;
    private ArrayList<Reservation> reservations=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        afficherCartes(); // Afficher les cartes initiales

        // Ajouter un écouteur de changement de texte sur le TextField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifier si le nouveau texte correspond à un format de date
            if (newValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
                // Si la chaîne saisie correspond à un format de date (YYYY-MM-DD),
                // effectuez une recherche par date.
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = dateFormat.parse(newValue);
                    java.sql.Date dateRecherche = new java.sql.Date(parsedDate.getTime());
                    rechercherParDate(dateRecherche);
                } catch (ParseException e) {
                    System.err.println("Format de date invalide.");
                }
            } else {
                // Si le nouveau texte ne correspond pas à un format de date,
                // effectuez une recherche par nom.
                rechercherParNom(newValue);
            }
        });
    }



    // Méthode pour afficher les cartes
    private void afficherCartes() {
        ArrayList<Reservation> allReservations = getAll();
        cardLyout.getChildren().clear(); // Effacer toutes les cartes précédemment affichées
        try {
            for (Reservation reservation : allReservations) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/Card.fxml"));
                AnchorPane cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setReservation(reservation); // Associez la réservation à la carte
                cardController.displayReservationDetails(reservation);
                cardLyout.getChildren().add(cardBox);

                // Ajoutez un événement de clic au bouton de suppression pour chaque carte
                cardController.del_btn.setOnAction(event -> supprimerCarte(reservation)); // Utilisez la réservation associée à la carte correspondante
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Appliquer l'espace entre les cartes après avoir ajouté toutes les cartes au FlowPane
        cardLyout.setHgap(20); // Espacement horizontal
        cardLyout.setVgap(20); // Espacement vertical
        cardLyout.setAlignment(Pos.TOP_LEFT);
    }

    // Méthode pour supprimer une carte
    public void supprimerCarte(Reservation reservation) {
        if (reservation != null) {
            delete(reservation); // Supprimer la réservation de la base de données
            afficherCartes(); // Rafraîchir l'affichage des cartes après suppression
        } else {
            System.out.println("Aucune réservation associée à cette carte.");
        }
    }


    public ArrayList<Reservation> getAll() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        String qry = "SELECT * FROM `reservation`";
        Connection connection = null;
        try {
            // Initialiser la connexion dans le bloc try
            connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId_reservation(rs.getInt(1));
                reservation.setDate_reserve(rs.getDate(2));
                reservation.setTemps_reservation(rs.getTime(3));

                String typeValue = rs.getString("type");
                TypeTerrain typeTerrain = TypeTerrain.valueOf(typeValue);
                reservation.setType(typeTerrain);

                reservation.setPrix_reservation(rs.getFloat(5));
                reservation.setId(rs.getInt(6));
                reservation.setId_terrain(rs.getInt(7));
                reservation.setDuree_reservation(rs.getTime(8));

              //  System.out.println(reservation);

                reservations.add(reservation);
              //  System.out.println("mrgl");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }
   // @Override
    public boolean delete(Reservation reservation) {
        String qry = "DELETE FROM `reservation` WHERE `id_reservation`=?";
        try {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
            stm.setInt(1, reservation.getId_reservation());

            int rowsDeleted = stm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("La réservation a été supprimée avec succès.");
                return true; // La réservation a été supprimée avec succès
            } else {
                System.out.println("La réservation avec l'ID spécifié n'existe pas.");
                return false; // Aucune réservation correspondante n'a été trouvée
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; // Une erreur s'est produite lors de la suppression de la réservation
        }
    }

    public void rechercherParDate(Date dateRecherche) {
        ArrayList<Reservation> reservationsFiltrees = new ArrayList<>();
        reservations=getAll();

        for (Reservation reservation : reservations) {
            Date dateReservation = reservation.getDate_reserve();
            if (dateReservation.equals(dateRecherche)) {
                System.out.println("compare");
                reservationsFiltrees.add(reservation);
                System.out.println("reservationsFiltrees: "+reservationsFiltrees);
            }
        }
        afficherCartesRecherches(reservationsFiltrees); // Afficher les cartes filtrées
    }
    private void afficherCartesRecherches(ArrayList<Reservation> reservations) {
        cardLyout.getChildren().clear(); // Effacer toutes les cartes précédemment affichées
        try {
            for (Reservation reservation : reservations) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/Card.fxml"));
                AnchorPane cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setReservation(reservation); // Associez la réservation à la carte
                cardController.displayReservationDetails(reservation);
                cardLyout.getChildren().add(cardBox);
                System.out.println("123");

                // Ajoutez un événement de clic au bouton de suppression pour chaque carte
                cardController.del_btn.setOnAction(event -> supprimerCarte(reservation)); // Utilisez la réservation associée à la carte correspondante
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Appliquer l'espace entre les cartes après avoir ajouté toutes les cartes au FlowPane
        cardLyout.setHgap(20); // Espacement horizontal
        cardLyout.setVgap(20); // Espacement vertical
        cardLyout.setAlignment(Pos.TOP_LEFT);
    }
    public void rechercherParNom(String nomUtilisateur) {
        ArrayList<Reservation> reservationsFiltresParNoms = new ArrayList<>();
        reservations = getAll(); // Récupérer toutes les réservations


        nomUtilisateur = nomUtilisateur.toLowerCase(); // Convertir la valeur saisie en minuscules pour une comparaison insensible à la casse

        for (Reservation reservation : reservations) {
            UserController userController = new UserController();
            User user = userController.getUserById(reservation.getId());

            if (user != null && (user.getFirstname().toLowerCase().contains(nomUtilisateur) ||
                    user.getLastname().toLowerCase().contains(nomUtilisateur) ||
                    (user.getFirstname() + " " + user.getLastname()).toLowerCase().contains(nomUtilisateur))) {
                reservationsFiltresParNoms.add(reservation); // Ajouter la réservation à la liste filtrée
            }
        }

        afficherCartesRecherches(reservationsFiltresParNoms); // Afficher les cartes filtrées
    }





}


