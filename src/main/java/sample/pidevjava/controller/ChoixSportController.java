package sample.pidevjava.controller;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Equipement;
import sample.pidevjava.model.TypeTerrain;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class ChoixSportController {
    @FXML
    private Button reserver_btn1;

    @FXML
    private Button reserver_btn2;

    @FXML
    private Button reserver_btn3;

    @FXML
    private Button reserver_btn4;
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

    private static int selectedTerrainId;
    private static String selectedTerrainType;

    // Méthode pour récupérer l'ID du terrain sélectionné

    // Méthode pour récupérer l'ID du terrain par son nom
    private int getTerrainIdByName(String terrainName) {
        int terrainId = -1; // Valeur par défaut si le terrain n'est pas trouvé

        String qry = "SELECT id FROM terrain WHERE nom = ?";
        try (PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry)) {
            stm.setString(1, terrainName);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                terrainId = resultSet.getInt("id");
                //System.out.println("aaaaaa: "+terrainId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("getTerrainIdByName: "+terrainId);
        return terrainId;
    }

    public String  getTypeByid(int Id_terrain){
        String type="";
       // ArrayList<Equipement> equipements = new ArrayList<>();
        String qry = "SELECT type FROM terrain WHERE id = ? ";
        try (PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry)) {
            stm.setInt(1, Id_terrain);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                type = resultSet.getString("type");
                //System.out.println("aaaaaa: "+terrainId);
            }
            return type;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

            public List<String> getTerrains(TypeTerrain type) {
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
        // Charger les terrains de football dans la ComboBox foot_list
        List<String> terrainsFootballNoms = getTerrains(TypeTerrain.FOOTBALL);
        foot_list.setItems(FXCollections.observableArrayList(terrainsFootballNoms));

        // Charger les terrains de basketball dans la ComboBox basket_list
        List<String> terrainsBasketNoms = getTerrains(TypeTerrain.BASKETBALL);
        basket_list.setItems(FXCollections.observableArrayList(terrainsBasketNoms));

        // Charger les terrains de handball dans la ComboBox hand_list
        List<String> terrainsHandNoms = getTerrains(TypeTerrain.HANDBALL);
        hand_list.setItems(FXCollections.observableArrayList(terrainsHandNoms));

        // Charger les terrains de tennis dans la ComboBox tennis_list
        List<String> terrainsTennisNoms = getTerrains(TypeTerrain.TENNIS);
        tennis_list.setItems(FXCollections.observableArrayList(terrainsTennisNoms));

        // Ajouter un gestionnaire d'événements à chaque ComboBox pour afficher le prix lors de la sélection
        basket_list.setOnAction(event -> {
            // Récupérer le nom du terrain sélectionné
            String selectedTerrainName = basket_list.getValue();

            // Mettre à jour l'ID du terrain sélectionné en appelant la méthode getTerrainIdByName()
            selectedTerrainId = getTerrainIdByName(selectedTerrainName);
            selectedTerrainType=getTypeByid(selectedTerrainId);
            System.out.println("selectedTerrainType: "+selectedTerrainType);


            // Appeler la méthode afficherPrix
            afficherPrix(event);


        });
        //foot_list.setOnAction(this::afficherPrix );
        foot_list.setOnAction(event -> {
            // Récupérer le nom du terrain sélectionné
            String selectedTerrainName = foot_list.getValue();

            // Mettre à jour l'ID du terrain sélectionné en appelant la méthode getTerrainIdByName()
            selectedTerrainId = getTerrainIdByName(selectedTerrainName);
            selectedTerrainType=getTypeByid(selectedTerrainId);
            System.out.println("selectedTerrainType: "+selectedTerrainType);

            // Appeler la méthode afficherPrix
            afficherPrix(event);


        });

        hand_list.setOnAction(event -> {
            // Récupérer le nom du terrain sélectionné
            String selectedTerrainName = hand_list.getValue();

            // Mettre à jour l'ID du terrain sélectionné en appelant la méthode getTerrainIdByName()
            selectedTerrainId = getTerrainIdByName(selectedTerrainName);
            selectedTerrainType=getTypeByid(selectedTerrainId);
            System.out.println("selectedTerrainType: "+selectedTerrainType);

            // Appeler la méthode afficherPrix
            afficherPrix(event);


        });

        tennis_list.setOnAction(event -> {
            // Récupérer le nom du terrain sélectionné
            String selectedTerrainName = tennis_list.getValue();

            // Mettre à jour l'ID du terrain sélectionné en appelant la méthode getTerrainIdByName()
            selectedTerrainId = getTerrainIdByName(selectedTerrainName);
            selectedTerrainType=getTypeByid(selectedTerrainId);
            System.out.println("selectedTerrainType: "+selectedTerrainType);

            // Appeler la méthode afficherPrix
            afficherPrix(event);


        });

        // Ajouter des gestionnaires d'événements pour chaque bouton de réservation
        reserver_btn1.setOnAction(event -> loadReservationUserScene(selectedTerrainId,selectedTerrainType));
        reserver_btn2.setOnAction(event -> loadReservationUserScene(selectedTerrainId,selectedTerrainType));
        reserver_btn3.setOnAction(event -> loadReservationUserScene(selectedTerrainId,selectedTerrainType));
        reserver_btn4.setOnAction(event -> loadReservationUserScene(selectedTerrainId,selectedTerrainType));
    }

    private void loadReservationUserScene(int selectedTerrainId,String selectedTerrainType) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/ReservationUser.fxml"));
            ReservationUserController controller = new ReservationUserController(selectedTerrainId,selectedTerrainType);
            loader.setController(controller);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) reserver_btn1.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



   /* @FXML
    public int getSelectedTerrainId() {
        int selectedTerrainId = -1; // Valeur par défaut si aucun terrain n'est sélectionné

        System.out.println("foot_list: "+foot_list);
        if (foot_list != null && foot_list.isFocused() && foot_list.getValue() != null) {
            System.out.println("foot_list**: "+foot_list);
            selectedTerrainId = getTerrainIdByName(foot_list.getValue());
        }/* else if (basket_list != null && basket_list.isFocused() && basket_list.getValue() != null) {
            selectedTerrainId = getTerrainIdByName(basket_list.getValue());
        } else if (hand_list != null && hand_list.isFocused() && hand_list.getValue() != null) {
            selectedTerrainId = getTerrainIdByName(hand_list.getValue());
        } else if (tennis_list != null && tennis_list.isFocused() && tennis_list.getValue() != null) {
            selectedTerrainId = getTerrainIdByName(tennis_list.getValue());
        }

        return selectedTerrainId;
    }*/

}






