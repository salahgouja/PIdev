package GUI;

import models.Partenaire;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Reponse;

public class CardFrontController {

    @FXML
    private Label etat;

    @FXML
    private Label montant;

    @FXML
    private Label quantite;

    @FXML
    private Label typee;

    @FXML
    private Label typep;
    @FXML
    private Label motif;
    public void setEventData(Partenaire event, Reponse r) {
        if (event != null) {
            typep.setText(event.getType_partenaire());
            montant.setText(String.valueOf(event.getMontant()) + " DNT");
            typee.setText(event.getType_equipement());
            quantite.setText(String.valueOf(event.getQuantite()));
            if(event.getEtat() == 0){
                etat.setText("En cours");
                motif.setText("Demande en cours");
            }
            if(event.getEtat() == 1){
                etat.setText("Acceptée");
                motif.setText("Demande accepté");
            }
            if(event.getEtat() == -1){
                etat.setText("Refusée");
                motif.setText(r.getMotif());
            }
        } else {
            // Handle null event
            clearLabels();
        }
    }

    private void clearLabels() {
        typep.setText("");
        montant.setText("");
        typee.setText("");
        quantite.setText("");
        etat.setText("");
    }
}
