package sample.pidevjava.controller;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.TypeTerrain;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class ChoixSportController {
    @FXML
    private ComboBox<String> basket_list;

    @FXML
    private ComboBox<String> foot_list;

    @FXML
    private ComboBox<String> hand_list;

    @FXML
    private ComboBox<String> tennis_list;
    @FXML
    private MFXTextField prix_txt_basket;

    @FXML
    private MFXTextField prix_txt_foot;

    @FXML
    private MFXTextField prix_txt_hand;

    @FXML
    private MFXTextField prix_txt_tennis;


    public List<String> getTerrainsFootball(TypeTerrain type) {
        List<String> terrainNoms = new ArrayList<>();
        String qry = "SELECT nom FROM terrain WHERE type = ? AND active = ?";

        PreparedStatement stm = null;
        ResultSet resultSet = null;
        try {
            stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
            stm.setString(1, type.toString());
            stm.setBoolean(2, true);

            resultSet = stm.executeQuery();

            while (resultSet.next()) {
                String terrainNom = resultSet.getString("nom");
                terrainNoms.add(terrainNom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return terrainNoms;
    }

    @FXML
    public void afficherPrix(ActionEvent event) {
        String sportSelectionne = ((ComboBox) event.getSource()).getId();
        MFXTextField textField = null;

        switch (sportSelectionne) {
            case "basket_list":
                textField = prix_txt_basket;
                break;
            case "foot_list":
                textField = prix_txt_foot;
                break;
            case "hand_list":
                textField = prix_txt_hand;
                break;
            case "tennis_list":
                textField = prix_txt_tennis;
                break;
        }

        if (textField != null) {
            String terrainSelectionne = ((ComboBox<String>) event.getSource()).getValue();
            float prix = getPrixLocationTerrain(terrainSelectionne);
            textField.setText(String.valueOf(prix));
        }
    }

    public float getPrixLocationTerrain(String terrainNom) {
        String qry = "SELECT prix_location_terrain FROM terrain WHERE nom = ?";

        PreparedStatement stm = null;
        ResultSet resultSet = null;
        try {
            stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
            stm.setString(1, terrainNom);

            resultSet = stm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getFloat("prix_location_terrain");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return 0.0f;
    }

    @FXML
    public void initialize() {
        {
        // Appel de votre méthode pour récupérer les noms des terrains de football
        List<String> terrainsFootballNoms = getTerrainsFootball(TypeTerrain.FOOTBALL);

        // Convertir la liste en une liste observable
        ObservableList<String> observableTerrainsFootballNoms = FXCollections.observableArrayList(terrainsFootballNoms);

        // Ajouter les noms des terrains à la ComboBox
        foot_list.setItems(observableTerrainsFootballNoms);

            // Appel de votre méthode pour récupérer les noms des terrains de football
            List<String> terrainsBasketNoms = getTerrainsFootball(TypeTerrain.BASKETBALL);

            // Convertir la liste en une liste observable
            ObservableList<String> observableTerrainsBasketNoms = FXCollections.observableArrayList(terrainsBasketNoms);

            // Ajouter les noms des terrains à la ComboBox
            basket_list.setItems(observableTerrainsBasketNoms);
            List<String> terrainsHandNoms = getTerrainsFootball(TypeTerrain.HANDBALL);

            // Convertir la liste en une liste observable
            ObservableList<String> observableTerrainsHandNoms = FXCollections.observableArrayList(terrainsHandNoms);

            // Ajouter les noms des terrains à la ComboBox
            hand_list.setItems(observableTerrainsHandNoms);
            List<String> terrainsTennisNoms = getTerrainsFootball(TypeTerrain.TENNIS);

            // Convertir la liste en une liste observable
            ObservableList<String> observableTerrainsTennisNoms = FXCollections.observableArrayList(terrainsTennisNoms);

            // Ajouter les noms des terrains à la ComboBox
            tennis_list.setItems(observableTerrainsTennisNoms);

            basket_list.setOnAction(this::afficherPrix);
            foot_list.setOnAction(this::afficherPrix);
            hand_list.setOnAction(this::afficherPrix);
            tennis_list.setOnAction(this::afficherPrix);
    }
    }

}




