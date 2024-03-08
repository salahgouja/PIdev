package sample.pidevjava.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import sample.pidevjava.model.*;

public class EquipementCardController {
    @FXML
    public MFXButton ajout_btn;

    @FXML
    private AnchorPane box;

    @FXML
    private ImageView image;

    @FXML
    private TextField nom_txt;

    @FXML
    private TextField prix_article_txt;

    @FXML
    private TextField prix_tot_equip_txt;

    @FXML
    protected Spinner<Integer> quantite_spin;

    private Equipement equipement;
    private static int quantite;

    private LocationEquipementController locationEquipementController;

    public void setLocationEquipementController(Equipement equipement, LocationEquipementController controller) {
        this.locationEquipementController = controller;
    }
    public Integer getQuantiteSpinValue() {
        return quantite_spin.getValue();
    }

    public Equipement getEquipement() {
        return equipement;
    }




    @FXML
    public void initialize() {
        // Créer une valeur factory pour le Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1);

        // Configurer le Spinner avec la valeur factory
        quantite_spin.setValueFactory(valueFactory);

        // Définir le modèle de comportement de changement de valeur pour le Spinner
        quantite_spin.setEditable(true);
        quantite_spin.getValueFactory().valueProperty().addListener((obs, oldValue, newValue) -> {
            /*if (newValue == null || newValue == 0) {
                quantite_spin.getValueFactory().setValue(oldValue != null ? oldValue : 1);
            }*/
            // Mettre à jour la valeur de quantite à chaque changement de valeur du Spinner
            quantite = newValue;
        });
    }


    public void displayEquipementsDetails(Equipement equipement, int quantite) {
        if (equipement != null) {
            this.equipement = equipement;
            System.out.println(" this.equipement: "+equipement);
            // Afficher les détails de l'équipement dans les champs de texte correspondants
            nom_txt.setText(equipement.getNom_equipement());

            // Charger l'image à partir du chemin spécifié
            String imageName = equipement.getImageSrc();
            // Construction du chemin de l'image en fonction du sport
            String imagePath = "/img/equipement/" + imageName;
            // Charger l'image et l'afficher dans l'ImageView
            Image equipementImage = new Image(getClass().getResourceAsStream(imagePath));
            image.setImage(equipementImage);

            prix_article_txt.setText(String.valueOf(equipement.getPrix_location()));
            // Mettre à jour le champ de texte prix_tot_equip_txt si nécessaire
            // prix_tot_equip_txt.setText(String.valueOf(quantite * equipement.getPrix_location()));
        } else {
            // Gérer le cas où l'équipement est null
            System.err.println("L'équipement est null");
        }
    }

}
