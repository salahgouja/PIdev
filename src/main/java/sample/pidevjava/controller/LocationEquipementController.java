package sample.pidevjava.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import sample.pidevjava.controller.EquipementCardController;
import sample.pidevjava.model.Equipement;
import sample.pidevjava.Services.EquipementService; // Importez votre service Equipement
import sample.pidevjava.model.EquipementLocation;
import sample.pidevjava.model.Reservation;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LocationEquipementController implements Initializable {
    private Equipement equipement;
    @FXML
    private MFXButton confirmer_btn;

    @FXML
    private FlowPane cardLayout;
    @FXML
    private MFXListView<String> equipements_choisis_list;
    private int quantite;
    private int derniereReservation;
    private String selectedTerrainType;
    private String type="";
     EquipementService equipementService;
  //  private EquipementCardController equipementCardController;

    public LocationEquipementController() {
        // Constructeur non paramétré
    }

  /*  public LocationEquipementController(EquipementService equipementService) {
        this.equipementService = equipementService;
    }*/




    public int recupererIdReservation(){
        derniereReservation = equipementService.getDerniereReservationId();
        return derniereReservation;
    }


    public String recupererType(int derniereReservation){

        Reservation reservation = equipementService.getReservationById(derniereReservation);
        selectedTerrainType=String.valueOf(reservation.getType());
        return selectedTerrainType;
    }


    // Initialisation de selectedTerrainType


   /* public LocationEquipementController(int selectedTerrainId,String selectedTerrainType) {
        this.selectedTerrainId = selectedTerrainId;
        equipementService = new EquipementService(); // Initialisez votre service Equipement
        this.selectedTerrainType=selectedTerrainType;
       // this.quantite=quantite;
    }*/

    public LocationEquipementController(int quantite){
        this.quantite=quantite;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        equipementService=new EquipementService();
        //int idDerniereReservation=recupererIdReservation();
       // String type=recupererType(idDerniereReservation);

        afficherCartes(); // Afficher les cartes d'équipement initiales

        confirmer_btn.setOnAction(event -> confirmerLocation());

    }











    private void afficherCartes() {
        int idDerniereReservation = recupererIdReservation();
        String type = recupererType(idDerniereReservation);

        ArrayList<Equipement> equipements = equipementService.getEquipementsByType(type);
        System.out.println("equipements: " + equipements);

        cardLayout.getChildren().clear();

        try {
            for (Equipement equipement : equipements) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/NewCardEquipement.fxml"));
                AnchorPane box = fxmlLoader.load();
                EquipementCardController equipementCardController = fxmlLoader.getController();
                equipementCardController.setLocationEquipementController(equipement, this);
                equipementCardController.displayEquipementsDetails(equipement, quantite);
                box.getProperties().put("controller", equipementCardController);
                cardLayout.getChildren().add(box);

                // Ajoutez un événement de clic au bouton de suppression pour chaque carte

                equipementCardController.ajout_btn.setOnAction(event -> {
                    ajouterArticleList(equipement);});
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cardLayout.setHgap(20);
        cardLayout.setVgap(20);
        cardLayout.setAlignment(Pos.TOP_LEFT);
    }


    @FXML
    private void ajouterArticleList(Equipement equipement) {
        // Vérifier si le Spinner a une valeur valide (supérieure à zéro)
        for (Node node : cardLayout.getChildren()) {
            if (node instanceof AnchorPane) {
                EquipementCardController equipementCardController = ((EquipementCardController) ((AnchorPane) node).getProperties().get("controller"));
                Integer quantite = equipementCardController.getQuantiteSpinValue();
              //  Equipement equipement = equipementCardController.getEquipement();

                System.out.println("equipement de ajouterArticleList: "+equipement);
                if (quantite != null && quantite > 0) {
                    // Récupérer les détails de l'équipement sélectionné
                    String nomEquipement = equipement.getNom_equipement();
                    float prixUnitaire = equipement.getPrix_location();

                    // Calculer le prix total
                    double prixTotal = quantite * prixUnitaire;

                    // Créer une chaîne représentant les détails de l'article
                    String articleDetails = "Article : " + nomEquipement + ", Nombre : " + quantite + ", Prix : " + prixTotal;

                    // Ajouter les détails de l'article à la ListView
                    equipements_choisis_list.getItems().add(articleDetails);
                }
            }
        }
    }
    private void confirmerLocation() {
        int id_reservation = equipementService.getDerniereReservationId();
        ObservableList<String> articles = equipements_choisis_list.getItems();

        for (String article : articles) {
            String[] details = article.split(", ");
            String nomEquipement = details[0].substring(10);
            System.out.println("nomEquipement: "+nomEquipement);
            int quantite = Integer.parseInt(details[1].substring(8).trim());

            double prixTotal = Double.parseDouble(details[2].substring(7));

            // Récupérer l'id de l'équipement correspondant au nom
            int id_equipement =equipementService.getIdEquipementByNom(nomEquipement);

            // Ajouter une ligne à la table de location pour chaque article
            equipementService. ajouterLocation(id_reservation, id_equipement, quantite);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText("Reservation d'equipement ajoutée avec succés");
            alert.setContentText("A bientot !");

            // Ajouter une image à la boîte de dialogue
            DialogPane dialogPane = alert.getDialogPane();
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/succes.jpg")));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            dialogPane.setGraphic(imageView);

            // Afficher la boîte de dialogue
            alert.showAndWait();
        }
    }



    // 1) get locations equipements by reservation id   ***done***
    //2) for each (nakhou id equipement naamilou get prixEquipement ById   **done**
    //3) nadhrab quantite * prix equipemnt w nhothom fil somme



    public float calculerPrixTotalLocation(List<EquipementLocation> equipementLocations) {
        float prixTotal = 0;
           int id_reservation=equipementService.getDerniereReservationId();
        for (EquipementLocation location : equipementLocations) {
            if (location.getId_reservation() == id_reservation) {
                int quantite = location.getQuantite_location();
                int id_equipement = location.getId_equipemment();
                float prixUnitaire = equipementService.getPrixLocationByIDEquipement(id_equipement);
                float prixLocation = prixUnitaire * quantite;
                prixTotal += prixLocation;
            }
            }

        return prixTotal;
    }
    public Equipement getEquipementById(int id, List<Equipement> equipements) {
        for (Equipement equipement : equipements) {
            if (equipement.getId_equipement() == id) {
                return equipement;
            }
        }
        return null; // Retourner null si aucun équipement trouvé avec cet ID
    }
}

