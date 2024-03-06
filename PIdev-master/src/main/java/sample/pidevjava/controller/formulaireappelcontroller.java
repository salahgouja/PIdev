package sample.pidevjava.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Appeloffre;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.PrivateKey;
import java.sql.Connection;
import java.util.ResourceBundle;

public class formulaireappelcontroller implements Initializable {

    @FXML
    private Button annuler;

    @FXML
    private TextField capaciteTF;

    @FXML
    private TextField capaciteTF1;

    @FXML
    private TextField capaciteTF11;

    @FXML
    private TextField nomTF;

    @FXML
    private Button valider;

    @FXML
    private Button doc;

    private String cvFilePath;

    @FXML
    void ajouter(ActionEvent event) throws IOException {
        String name = nomTF.getText();
        String cap = capaciteTF.getText();
        String numero = capaciteTF1.getText();
        float prix = Float.parseFloat(capaciteTF11.getText());



        if (cvFilePath != null && !cvFilePath.isEmpty()) {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();
            AppeloffreControlleur appel = new AppeloffreControlleur(connection);

            // Ajouter l'appel d'offres
            appel.add(new Appeloffre(name, cap, prix, numero, cvFilePath,50));
            System.out.println("Appel offre ajouté");

            // Afficher une alerte de succès
            showAlert("Succès", "Demande ajoutée avec succès!", Alert.AlertType.INFORMATION);
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void annuler(ActionEvent event) {
    }

    @FXML
    void insrer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            cvFilePath = selectedFile.getAbsolutePath();
            System.out.println("Fichier sélectionné : " + cvFilePath);
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}