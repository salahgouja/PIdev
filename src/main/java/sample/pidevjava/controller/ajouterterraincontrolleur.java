package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ajouterterraincontrolleur implements Initializable {

    @FXML
    private TextField nomTF;

    @FXML
    private TextField capaciteTF;

    @FXML
    private ChoiceBox<String> ISecurty1;

    @FXML
    private ChoiceBox<TypeTerrain> ISecurty;

    @FXML
    private Button valider;
    @FXML

    private Button annuler;


    private  Connection cnx;

    public ajouterterraincontrolleur(Connection cnx) {
        this.cnx = cnx;
    }

    public ajouterterraincontrolleur() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ISecurty.getItems().addAll(TypeTerrain.values());

        ISecurty.setValue(TypeTerrain.FOOTBALL);

        ISecurty1.getItems().addAll("en fonction","en entretien");
        ISecurty1.setValue("en fonction");



    }

    @FXML
    public void ajouter(ActionEvent event) throws IOException {
        String name = nomTF.getText();
        String capaciteText = capaciteTF.getText();

        if (name.isEmpty() || capaciteText.isEmpty()) {
            afficherAlerte("Veuillez remplir tous les champs.");
            return;
        }

        int cap;
        try {
            cap = Integer.parseInt(capaciteText);
        } catch (NumberFormatException e) {
            afficherAlerte("La capacité doit être un nombre entier.");
            return;
        }

        boolean etat = ISecurty1.getValue().equals("en fonction");

        TypeTerrain type = ISecurty.getValue();

        if (type != null) {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();
            TerrainController terrainController = new TerrainController(connection);
            terrainController.add(new Terrain(name, etat, cap, type));

            afficherAlerte("Terrain ajouté avec succès !");
        } else {
            afficherAlerte("Veuillez sélectionner une valeur TypeTerrain.");
        }
    }

    private void afficherAlerte(String message) throws IOException {

        Stage stage = (Stage) valider.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/dashbordterrain.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void annuler(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) annuler.getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/dashbordterrain.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}