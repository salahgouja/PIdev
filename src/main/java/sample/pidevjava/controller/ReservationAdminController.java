package sample.pidevjava.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Reservation;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReservationAdminController implements Initializable {

    @FXML
    private HBox cardLayout;
    private ArrayList<Reservation> reservations=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Reservation> allReservations = getAll();
        try {
            for (int i = 0; i < allReservations.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                // Spécifiez le chemin correct du fichier FXML en utilisant getResource() avec le chemin absolu
                fxmlLoader.setLocation(getClass().getResource("/sample/pidevjava/Card.fxml"));

                AnchorPane cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();

                cardController.displayReservationDetails(allReservations.get(i));
                cardLayout.getChildren().add(cardBox);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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

                reservation.setPrix_reservation(rs.getDouble(5));
                reservation.setId(rs.getInt(6));
                reservation.setId_terrain(rs.getInt(7));
                reservation.setDuree_reservation(rs.getTime(8));

              //  System.out.println(reservation);

                reservations.add(reservation);
              //  System.out.println("mrgl");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return reservations;
    }



}


