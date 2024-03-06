package sample.pidevjava.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.pidevjava.Main;
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

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashbord terrain .fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = (Stage) annuler.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("go");
        primaryStage.centerOnScreen();

    }

    @FXML
    void updatet(ActionEvent event) throws IOException {

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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashbord terrain .fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = (Stage) valider.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("go");
        primaryStage.centerOnScreen();



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
