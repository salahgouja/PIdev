package sample.pidevjava.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.Text;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Jours;
import sample.pidevjava.interfaces.IHoraire;
import sample.pidevjava.model.HoraireTravail;
import javafx.scene.control.ComboBox;
//import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HoraireController implements IHoraire<HoraireTravail> {
    @FXML
    private MFXButton maj_btn;
   @FXML
    private MFXButton confirmation_btn;

    @FXML
    private MFXTextField heure_debut_txt;

    @FXML
    private MFXTextField heure_fin_txt;
    @FXML
    private MFXToggleButton repos_btn;
    @FXML
    private MFXComboBox<String> jours_list;
    @FXML
    private javafx.scene.text.Text heure_debut_valmsg;
    @FXML
    private javafx.scene.text.Text heure_fin_valmsg;


    /* @FXML
  private Button confirmation_btn;

    @FXML
    private TextField heure_debut_txt;

    @FXML
    private TextField heure_fin_txt;

    @FXML
    private ComboBox<String> jours_list;

   /* @FXML
    private ToggleButton repos_btn;*/



   /*
    private MFXLegacyComboBox<String> jours_list;*/
   @FXML
  /*  public void initialize() {
        // Convertir les valeurs de l'énumération Jours en une liste observable
        ObservableList<String> joursList = FXCollections.observableArrayList(        "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche");

        // Ajouter les valeurs de l'énumération Jours à la ComboBox jours_list
        jours_list.setItems(joursList);
    }*/

  public void initialize() {
       // Convertir les valeurs de l'énumération Jours en une liste observable
       ObservableList<String> joursList = FXCollections.observableArrayList();

       // Itérer sur les valeurs de l'énumération Jours et les ajouter à la liste
       for (Jours jour : Jours.values()) {
           joursList.add(jour.toString());
       }
       jours_list.setItems(joursList);

   }


       @Override
    public void add(HoraireTravail horaireTravail) {
        PreparedStatement stm = null;
        String qry = "";

        try {
            qry = "INSERT INTO horairetravaille (Jour, heure_debut, heure_fin, repos) VALUES (?, ?, ?, ?)";
            stm = DBConnection.getInstance().getConnection().prepareStatement(qry);

            // Set parameters based on whether it's a day off or a work schedule
            if (horaireTravail.isRepos()) {
                // For a day off, only set the day and the 'repos' flag
                stm.setString(1, horaireTravail.getJour());
                stm.setNull(2, Types.NULL); // Null for heure_debut
                stm.setNull(3, Types.NULL); // Null for heure_fin
                stm.setBoolean(4, true); // repos is true
            } else {
                // For a work schedule, set all fields including heure_debut and heure_fin
                stm.setString(1, horaireTravail.getJour());
                stm.setString(2, horaireTravail.getHeure_debut());
                stm.setString(3, horaireTravail.getHeure_fin());
                stm.setBoolean(4, false); // repos is false
            }

            // Execute the query
            stm.executeUpdate();
            System.out.println("Horaire ajouté avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the PreparedStatement in a finally block to avoid resource leaks
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }






    @FXML
    private void addHoraireTravail() {
        // Cacher les messages d'erreur précédents
        heure_debut_valmsg.setVisible(false);
        heure_fin_valmsg.setVisible(false);

        // Get the selected day from the ComboBox
        String jour = jours_list.getValue();

        // Vérifier si le jour existe déjà dans la table horairetravaille
        if (jourExisteDeja(jour)) {
            // Afficher un message d'erreur
            System.out.println("Ce jour existe déjà dans la table horairetravaille. Impossible d'ajouter une nouvelle entrée.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Ce jour existe déjà");
            alert.setContentText("Impossible d'ajouter une nouvelle entrée");

            // Ajouter une image à la boîte de dialogue
            DialogPane dialogPane = alert.getDialogPane();
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/error.png")));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            dialogPane.setGraphic(imageView);

            // Afficher la boîte de dialogue
            alert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }

        // Get the text from the input fields for start and end times
        String heureDebutString = heure_debut_txt.getText();
        String heureFinString = heure_fin_txt.getText();
        // Get the state of the toggle button
        boolean repos = repos_btn.isSelected();



        // Check if the day off toggle is selected
        if (repos) {
            // If it is, create a HoraireTravail object for a day off
            HoraireTravail horaireTravail = new HoraireTravail(jour, null, null, true);
            System.out.println("HoraireTravail: " + horaireTravail);

            // Call the add method with the HoraireTravail object
            add(horaireTravail);
            System.out.println("Successfully added.");
            afficherMessageSucces("Ajouté avec succès.");
            getHoraireTravail( jour);
        } else {
            // Check if start and end times are provided
            if (!heureDebutString.isEmpty() && !heureFinString.isEmpty()) {
                // Valider les heures et afficher les messages d'erreur si nécessaire
                if (!validerHeuresEtAfficherMessages(heureDebutString, heureFinString)) {
                    return; // Arrêter l'exécution de la méthode si les heures ne sont pas valides
                }
                // Create a HoraireTravail object for a working day
                HoraireTravail horaireTravail = new HoraireTravail(jour, heureDebutString, heureFinString, false);
                System.out.println("HoraireTravail: " + horaireTravail);

                // Call the add method with the HoraireTravail object
                add(horaireTravail);
                System.out.println("Successfully added.");
                afficherMessageSucces("Ajouté avec succès.");
                getHoraireTravail( jour);
            } else {
                // Handle the case where start or end time is missing
                System.out.println("Start time or end time is missing. Unable to add work schedule.");
            }
        }
    }


    private boolean jourExisteDeja(String jour) {
        try {
            // Créer la requête SQL pour récupérer toutes les lignes de la table horairetravaille
            String query = "SELECT jour FROM horairetravaille";

            // Exécuter la requête
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Itérer sur les résultats pour comparer les jours
            while (resultSet.next()) {
                String jourDB = resultSet.getString("jour").toUpperCase();
                System.out.println(jourDB);
                if (jourDB.equals(jour)) {
                    // Si le jour existe déjà dans la table, retourner true
                    return true;
                }
            }

            // Si le jour n'est pas trouvé dans la table, retourner false
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HoraireTravail getHoraireTravail(String jour) {
        try {
            // Créer la requête SQL pour récupérer les valeurs de heure_debut, heure_fin et repos pour le jour donné
            String query = "SELECT heure_debut, heure_fin, repos FROM horairetravaille WHERE jour = ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, jour);

            // Exécuter la requête
            ResultSet resultSet = statement.executeQuery();

            // Vérifier si le résultat contient des données
            if (resultSet.next()) {
                // Extraire les valeurs de heure_debut, heure_fin et repos
                String heureDebut = resultSet.getString("heure_debut");
                String heureFin = resultSet.getString("heure_fin");
                boolean repos = resultSet.getBoolean("repos");

                // Créer et retourner un objet HoraireTravail avec les valeurs récupérées
                return new HoraireTravail(jour, heureDebut, heureFin, repos);
            } else {
                // Si le jour n'existe pas dans la table horairetravaille, retourner null
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void affichageHoraireTravail() {
        heure_debut_valmsg.setVisible(false);
        heure_fin_valmsg.setVisible(false);
        // Récupérer le jour sélectionné dans la ComboBox
        String jourSelectionne = jours_list.getSelectionModel().getSelectedItem();

        // Vérifier si un jour a été sélectionné
        if (jourSelectionne != null) {
            // Appeler getHoraireTravail avec le jour sélectionné
            HoraireTravail horaireTravail = getHoraireTravail(jourSelectionne);
            System.out.println(horaireTravail);

            // Vérifier si un horaire de travail a été trouvé pour le jour sélectionné
            if (horaireTravail != null) {
                // Vérifier si c'est un jour de repos
                if (horaireTravail.isRepos()) {
                    // Afficher "c'est un jour de repos" dans les champs de texte
                    heure_debut_txt.setText("c'est un jour de repos");
                    heure_fin_txt.setText("c'est un jour de repos");
                } else {
                    // Afficher les valeurs de heure_debut et heure_fin
                    heure_debut_txt.setText(horaireTravail.getHeure_debut());
                    heure_fin_txt.setText(horaireTravail.getHeure_fin());
                }
            } else {
                // Si aucun horaire de travail n'a été trouvé, effacer les champs de texte
                heure_debut_txt.clear();
                heure_fin_txt.clear();
            }
        } else {
            // Si aucun jour n'a été sélectionné, effacer les champs de texte
            heure_debut_txt.clear();
            heure_fin_txt.clear();
        }
    }







    @Override
    public void update(HoraireTravail horaireTravail) {
        try {
            String query;
            PreparedStatement statement;

            // Si le jour est un jour de repos, mettre à jour heure_debut et heure_fin à NULL
            if (horaireTravail.isRepos()) {
                query = "UPDATE horairetravaille SET heure_debut = NULL, heure_fin = NULL, repos = true WHERE jour = ?";
                statement = DBConnection.getInstance().getConnection().prepareStatement(query);
                statement.setString(1, horaireTravail.getJour());
            } else {
                // Sinon, mettre à jour heure_debut, heure_fin et repos
                query = "UPDATE horairetravaille SET heure_debut = ?, heure_fin = ?, repos = false WHERE jour = ?";
                statement = DBConnection.getInstance().getConnection().prepareStatement(query);
                statement.setString(1, horaireTravail.getHeure_debut());
                statement.setString(2, horaireTravail.getHeure_fin());
                statement.setString(3, horaireTravail.getJour());
            }

            // Exécuter la requête
            int rowsAffected = statement.executeUpdate();

            // Vérifier si la mise à jour a été effectuée avec succès
            if (rowsAffected > 0) {
                System.out.println("Les valeurs pour le jour " + horaireTravail.getJour() + " ont été mises à jour avec succès.");
            } else {
                System.out.println("Aucun enregistrement mis à jour pour le jour " + horaireTravail.getJour() + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validerHeuresEtAfficherMessages(String heureDebut, String heureFin) {
        boolean heureDebutValide = validateHeure(heureDebut);
        boolean heureFinValide = validateHeure(heureFin);

        // Afficher les messages d'erreur si les heures ne sont pas valides
        if (!heureDebutValide) {
            heure_debut_valmsg.setVisible(true);
        }
        if (!heureFinValide) {
            heure_fin_valmsg.setVisible(true);
        }

        // Si au moins une des heures est invalide, retourner false
        if (!heureDebutValide || !heureFinValide) {
            return false;
        }

        // Si les deux heures sont valides, retourner true
        return true;
    }

    @FXML
    private void handleUpdateButton() {
        // Cacher les messages d'erreur précédents
        heure_debut_valmsg.setVisible(false);
        heure_fin_valmsg.setVisible(false);

        // Récupérer le jour sélectionné dans la ComboBox
        String jourSelectionne = jours_list.getSelectionModel().getSelectedItem();

        // Vérifier si un jour a été sélectionné
        if (jourSelectionne != null) {
            // Récupérer les valeurs actuelles des champs de texte
            String heureDebut = heure_debut_txt.getText();
            String heureFin = heure_fin_txt.getText();
            boolean repos = repos_btn.isSelected(); // Si le jour est un jour de repos

            // Si c'est un jour de repos, modifier les champs de texte et passer à la suite
            if (repos) {
                heure_debut_txt.setText("C'est un jour de repos");
                heure_fin_txt.setText("C'est un jour de repos");
            } else {
                // Valider les heures et afficher les messages d'erreur si nécessaire
                if (!validerHeuresEtAfficherMessages(heureDebut, heureFin)) {
                    // Si au moins une des heures est invalide, arrêter l'exécution
                    return;
                }
            }

            // Créer un objet HoraireTravail avec les valeurs récupérées
            HoraireTravail horaireTravail = new HoraireTravail(jourSelectionne, heureDebut, heureFin, repos);

            // Appeler la méthode update pour mettre à jour les valeurs dans la base de données
            update(horaireTravail);

            // Afficher un message de succès
            afficherMessageSucces("mise à jour effectuée avec succès.");
            getHoraireTravail( jourSelectionne);
        } else {
            // Si aucun jour n'a été sélectionné, afficher un message d'erreur
            System.out.println("Veuillez sélectionner un jour.");
        }
    }


    // Méthode pour afficher un message de succès
    private void afficherMessageSucces(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @Override
    public ArrayList<HoraireTravail> getByID() {
        return null;
    }
    // ceci est un controleur de saisie
    private boolean validateHeure(String heure) {
        // Expression régulière pour le format "hh:mm"
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(heure);
        return matcher.matches();
    }



}

