package sample.pidevjava.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class updateterraincontrolleur implements Initializable {
    @FXML
    private TextField nomTF;

    @FXML
    private TextField capaciteTF;

    @FXML
    private ChoiceBox<String>  ISecurty1;
    @FXML
    private ChoiceBox<TypeTerrain> ISecurty;

    @FXML
    private Button annuler;



    @FXML
    private Button valider;
    private Terrain terrainSelectionne;

    @FXML
    void annulet(ActionEvent event) throws IOException {
        Stage stage = (Stage) annuler.getScene().getWindow();

        try {
            // Create a new FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/dashbordterrain.fxml"));

            // Load the LoginForm.fxml filea
            Parent root = loader.load();

            // Create a new Scene with the loaded LoginForm.fxml file
            Scene scene = new Scene(root);

            // Set the newly created Scene as the scene for the current stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void updatet(ActionEvent event) throws IOException {
        Stage stage = (Stage) valider.getScene().getWindow();

        String name = nomTF.getText();
        int cap = Integer.parseInt(capaciteTF.getText());

        boolean etat = ISecurty1.getValue().equals("en fonction");


        TypeTerrain type = ISecurty.getValue();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        if (type != null) {
            TerrainController terrainController = new TerrainController(connection);
            terrainController.update(new Terrain(terrainSelectionne.getId(),name, etat, cap, type));
            System.out.println("valider");

        } else {
            System.out.println("Please select a TypeTerrain value.");
        }
        try {
            // Create a new FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/dashbordterrain.fxml"));

            // Load the LoginForm.fxml filea
            Parent root = loader.load();

            // Create a new Scene with the loaded LoginForm.fxml file
            Scene scene = new Scene(root);

            // Set the newly created Scene as the scene for the current stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        chargerDonneesTerrain();

        ISecurty.setItems(FXCollections.observableArrayList(TypeTerrain.values()));





    }

    public void setTerrainSelectionne(Terrain terrainSelectionne) {
        this.terrainSelectionne = terrainSelectionne;
        chargerDonneesTerrain();

    }

    public void chargerDonneesTerrain() {
        if (terrainSelectionne != null) {
            nomTF.setText(terrainSelectionne.getNom());
            capaciteTF.setText(String.valueOf(terrainSelectionne.getCapaciteTerrain()));
            ISecurty.setValue(terrainSelectionne.getType());
            ISecurty1.setItems(FXCollections.observableArrayList("en fonction", "en entretien"));
            ISecurty1.setValue(terrainSelectionne.isActive() ? "en fonction" : "en entretien");

        }    }
}