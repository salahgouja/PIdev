package sample.pidevjava.controller;

import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import sample.pidevjava.Services.ReservationService;
import sample.pidevjava.model.HoraireTravail;
import sample.pidevjava.model.Reservation;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservationUserController implements Initializable {
    @FXML
    private javafx.scene.text.Text email_val_msg;
    @FXML
    private MFXButton equipement_btn;
    @FXML
    private MFXTextField prix_terrain_duree;

    @FXML
    private javafx.scene.text.Text duree_valmsg;
    @FXML
    private javafx.scene.text.Text heure_reserv_valmsg;
    @FXML
    private MFXButton confirmer_btn;

    @FXML
    private MFXTextField email_txt;

    @FXML
    private MFXListView<String> time_slots_available;

    @FXML
    private MFXDatePicker datePicker;

    @FXML
    private MFXTextField dure_res_txt;

    @FXML
    private MFXTextField heure_txt;
    @FXML
    private MFXToggleButton equi_btn;


    ReservationService service;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePicker.setOnAction(event -> updateListeView());
        service=new ReservationService();
        //System.out.println("1");

        //System.out.println("2");
        dure_res_txt.textProperty().addListener((observable, oldValue, newValue) -> {
            // Votre logique ici pour mettre à jour le prix lorsque la durée change
            if (!newValue.isEmpty()) {
                // Vérifier si la nouvelle valeur est dans le format de temps attendu
                if (validateHeure(newValue)) {
                    // La nouvelle valeur est dans le format attendu, vous pouvez maintenant la convertir en objet Time
                    Time nouvelleDuree = Time.valueOf(newValue);

                    // Calculer le prix pour la nouvelle durée
                    float prix_par_duree = calculeLocationTerrainPourDureeChoisie(nouvelleDuree, selectedTerrainId);

                    // Mettre à jour le champ prix_terrain_duree avec le nouveau prix calculé
                    prix_terrain_duree.setText(String.valueOf(prix_par_duree));
                } else {
                    // La nouvelle valeur n'est pas dans le format de temps attendu, vous pouvez gérer cela en conséquence
                    // Par exemple, afficher un message d'erreur à l'utilisateur
                }

            }
        });


    }
        private int selectedTerrainId;
        private String selectedTerrainType;

        public ReservationUserController(int selectedTerrainId, String selectedTerrainType) {
            this.selectedTerrainId = selectedTerrainId;
            this.selectedTerrainType=selectedTerrainType;

        }

        ChoixSportController choixSportController=new ChoixSportController();
  //int selectedTerrain=choixSportController.getSelectedTerrainId();

    UserController userController=new UserController();
    TerrainController terrainController=new TerrainController();
   // int selectedTerrainId = choixSportController.getSelectedTerrainId();



    public boolean verifierDisponibilite(LocalDate selectedDate, LocalTime selectedTime, Time duree) {
        // Ton code actuel de vérification de disponibilité
     //   System.out.println("*** ene tw fi verifierDisponibilite");
        String jourSelectionne = selectedDate.getDayOfWeek().toString();

        System.out.println("date selectionne : "+selectedDate);
        System.out.println("heure selectionne : "+selectedTime);
        System.out.println("duree selectionne : "+duree);
        System.out.println("*** Jour selectionne : "+jourSelectionne);

        // Vérifier si c'est un jour de repos
        if (service.estJourDeRepos(jourSelectionne)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Ce jour est un jour de repos");
            alert.setContentText("Veuillez choisir une autre date");

            // Ajouter une image à la boîte de dialogue
            DialogPane dialogPane = alert.getDialogPane();
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/error.png")));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            dialogPane.setGraphic(imageView);

            // Afficher la boîte de dialogue
            alert.showAndWait();

            return false; // La disponibilité n'est pas confirmée
        }




        // Récupérer les plages horaires de travail pour le jour sélectionné
        List<HoraireTravail> horairesTravail = getHorairesTravail(jourSelectionne);

        // Vérifier si les heures de réservation sont dans les plages horaires de travail
        boolean heuresDansPlagesHoraires = verifierHeuresDansPlagesHoraires(horairesTravail, selectedTime.toString(), duree.toString());
       // System.out.println(heuresDansPlagesHoraires+" : heuresDansPlagesHoraires" );
        if (!heuresDansPlagesHoraires){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("heure choisi n'est pas dans plage horaire de travail de notre complexe");
            alert.setContentText("Veuillez choisir un autre horaire");

            // Ajouter une image à la boîte de dialogue
            DialogPane dialogPane = alert.getDialogPane();
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/error.png")));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            dialogPane.setGraphic(imageView);

            // Afficher la boîte de dialogue
            alert.showAndWait();

            System.out.println("heure choisi n'est pas dans plage horaire de travail de notre complexe");
            return false;
        }





        return true;
    }

    public boolean verifierPresenceChevauchemment(ArrayList<Reservation>reservations) {
        for (Reservation reservation : reservations) {
            System.out.println("pp");
            int heureMinutes = convertirEnMinutes(String.valueOf(reservation.getTemps_reservation())); //heure existante dans la base

            System.out.println("heureMinutes: " + heureMinutes);
            int dureeMinutes = convertirEnMinutes(String.valueOf(reservation.getDuree_reservation()));


            System.out.println("dureeMinutes: " + dureeMinutes);
            int heureFinReservation = heureMinutes + dureeMinutes;
            int heure_saisie_Minutes = convertirEnMinutes(heure_txt.getText());
            int duree_saisie_Minutes = convertirEnMinutes(dure_res_txt.getText());
            int heure_fin_saisie=duree_saisie_Minutes+heure_saisie_Minutes;
            if ((heure_saisie_Minutes >= heureMinutes && heure_saisie_Minutes < heureFinReservation) ||
                    (heureMinutes >= heure_saisie_Minutes && heureMinutes < heure_fin_saisie)) {
                return true; // Il y a chevauchement
            }


        }
        return false;
    }

    public void mettreAJourTimeSlotsDisponibles(LocalDate selectedDate, List<HoraireTravail> horairesTravail) {
        // Mettre à jour la liste des créneaux horaires disponibles
        updateAvailableTimeSlots(selectedDate, horairesTravail);
      //  System.out.println("************");
        System.out.println(selectedDate);
       // System.out.println("horairesTravail :" +horairesTravail);
        //System.out.println("************");
    }



    private List<HoraireTravail> getHorairesTravail(String jour) {
        List<HoraireTravail> horairesTravail;

        try  {
            horairesTravail = service.getHorairesTravail(jour);
         //   System.out.println("Les horaires de travail sont : "+ horairesTravail);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            horairesTravail = new ArrayList<HoraireTravail>();
        }
        return horairesTravail;
    }


    private boolean verifierHeuresDansPlagesHoraires(List<HoraireTravail> horairesTravail, String heureReservation, String dureeReservation) {
        // Convertir les heures de réservation en minutes pour faciliter la comparaison
        int heureReserveMinutes = convertirEnMinutes(heureReservation);
        int dureeReserveMinutes = convertirEnMinutes(dureeReservation);
        int heureFinReservation = heureReserveMinutes + dureeReserveMinutes;
      //  System.out.println("ene tw fi verifierHeuresDansPlagesHoraires ");

       // System.out.println(" heureReserveMinutes: "+heureReserveMinutes + "dureeReserveMinutes: "+dureeReserveMinutes +"heureFinReservation: "+heureFinReservation);
        // Parcourir toutes les plages horaires de travail
        for (HoraireTravail horaireTravail : horairesTravail) {
            // Convertir les heures de début et de fin de la plage horaire en minutes
            int heureDebutPlage = convertirEnMinutes(horaireTravail.getHeure_debut());
            int heureFinPlage = convertirEnMinutes(horaireTravail.getHeure_fin());

            // Vérifier si les heures de réservation sont dans la plage horaire
            if (heureReserveMinutes >= heureDebutPlage && heureFinReservation <= heureFinPlage) {
                return true; // Les heures de réservation sont dans une plage horaire de travail valide
            }
        }

        return false; // Aucune plage horaire valide n'a été trouvée pour les heures de réservation
    }

    private int convertirEnMinutes(String heure) {
        // Diviser l'heure en parties (heures et minutes)
        String[] partiesHeure = heure.split(":");
        int heures = Integer.parseInt(partiesHeure[0]);
        int minutes = Integer.parseInt(partiesHeure[1]);

        // Convertir l'heure en minutes
        return heures * 60 + minutes;
    }

@FXML // edhi affichage aal list view
    private void updateAvailableTimeSlots(LocalDate selectedDate, List<HoraireTravail> horairesTravail) {
        // Récupérer les réservations pour le terrain sélectionné à la date sélectionnée
      //  System.out.println(" ene tw fi updateAvailableTimeSlots");
        ArrayList<Reservation> reservations =service.getReservationsByTerrainID(selectedDate, selectedTerrainId);
    System.out.println("**reservation**: "+reservations);

        // Liste des créneaux horaires disponibles
      //  ArrayList<String> creneauxDisponibles = new ArrayList<>();
        ArrayList<String> creneauxnonDisponibles = new ArrayList<>();


        // Ajouter les créneaux horaires de travail dans la liste
      /*  for (HoraireTravail horaireTravail : horairesTravail) {
            creneauxDisponibles.add(horaireTravail.getHeure_debut() + " - " + horaireTravail.getHeure_fin());
            System.out.println("creneauxDisponibles 1: "+creneauxDisponibles);
        }*/

        // Éliminer les horaires réservés de la liste des créneaux horaires disponibles
        for (Reservation reservation : reservations) {
          //  System.out.println("pp");
            int heureMinutes = convertirEnMinutes(String.valueOf(reservation.getTemps_reservation()));

         //   System.out.println("heureMinutes: "+heureMinutes);
            int dureeMinutes = convertirEnMinutes(String.valueOf(reservation.getDuree_reservation()));


         //   System.out.println("dureeMinutes: "+dureeMinutes);
            int heureFinReservation = heureMinutes + dureeMinutes;
            int heureFin = heureFinReservation / 60; // Obtenir les heures
            int minuteFin = heureFinReservation % 60; // Obtenir les minutes restantes
            int secondeFin = 0; // Initialiser les secondes à zéro

            String temps_fin_reservation = String.format("%02d:%02d:%02d", heureFin, minuteFin, secondeFin);
            System.out.println("Temps de fin de réservation : " + temps_fin_reservation);
            creneauxnonDisponibles.add(reservation.getTemps_reservation()+" - "+temps_fin_reservation);

         /*   // verification d'un chevauchement possible
            int heure_saisie_Minutes = convertirEnMinutes(dure_res_txt.getText());
            int duree_saisie_Minutes = convertirEnMinutes(heure_txt.getText());
              if ((heure_saisie_Minutes-heureMinutes)<60 || (heureMinutes-heure_saisie_Minutes<60)||(heure_saisie_Minutes-heureFinReservation<0)){
                  Alert alert = new Alert(Alert.AlertType.INFORMATION);
                  alert.setTitle("Error");
                  alert.setHeaderText("heure choisi n'est peut causer un chauvechement avec autres reservations");
                  alert.setContentText("Veuillez choisir un autre horaire");

                  // Ajouter une image à la boîte de dialogue
                  DialogPane dialogPane = alert.getDialogPane();
                  ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/error.png")));
                  imageView.setFitWidth(50);
                  imageView.setFitHeight(50);
                  dialogPane.setGraphic(imageView);

                  // Afficher la boîte de dialogue
                  alert.showAndWait();
                  return;
              }*/

        }







        // Mettre à jour la liste des créneaux horaires disponibles dans time_slots_available
        ObservableList<String> observableCreneauxnonDisponibles = FXCollections.observableArrayList(creneauxnonDisponibles);
        time_slots_available.setItems(observableCreneauxnonDisponibles);
      //  System.out.println("mrgl");

    }







  // int terrainId=choixSportController.getSelectedTerrainId();


   /* public void add() {
        System.out.println("ene tw fil add");
        // Récupérer les valeurs des champs
        LocalDate selectedDate = datePicker.getValue();
        LocalTime selectedTime = LocalTime.parse(heure_txt.getText());
        Time duree = Time.valueOf(dure_res_txt.getText());
       User user= userController.getUserByEmail(email_txt.getText());
        Terrain terrain=terrainController.getById(selectedTerrainId);

        System.out.println("selectedDate: "+selectedDate +"selectedTime: "+selectedTime  +"duree: "+duree);
        System.out.println("user: "+user);
        System.out.println("terrain: "+terrain);


        // Appliquer la méthode verifierDisponibilite
        if (!verifierDisponibilite(selectedDate, selectedTime, duree)) {
            // Si la disponibilité n'est pas confirmée, ne rien faire
            return;
        }

        // Récupérer les plages horaires de travail pour le jour sélectionné
        String jourSelectionne = selectedDate.getDayOfWeek().toString();
        List<HoraireTravail> horairesTravail = getHorairesTravail(jourSelectionne);

        // Mettre à jour la liste des créneaux horaires disponibles
        mettreAJourTimeSlotsDisponibles(selectedDate, horairesTravail);
        System.out.println("ggggg");

        // Si la disponibilité est confirmée, ajouter une ligne dans la table de réservation
        String qry = "INSERT INTO reservation (date_reserve, temps_reservation,type,prix_reservation,id,id_terrain, duree_reservation) VALUES (?, ?, ?,?, ?, ?,?,?)";
        try {
            // obtenir la connexion à la base de données
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
            stm.setDate(1, Date.valueOf(selectedDate));
            stm.setTime(2, Time.valueOf(selectedTime));
            stm.setString(3,String.valueOf(terrain.getId()));
            stm.setFloat(4,terrain.getPrix_location_terrain());
            stm.setInt(5,user.getId());
            stm.setInt(6,terrain.getId());
            stm.setTime(7, duree);
            stm.executeUpdate();
            System.out.println("cbon");
            // Fermer la connexion et rafraîchir l'affichage de la table si nécessaire
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception d'une manière appropriée
        }
    }*/
   /* @FXML
    public void initialize() {
        // Liaison de l'action du bouton "Confirmer" à la méthode add()
        confirmer_btn.setOnAction(event -> add());

        // Gestionnaire d'événements sur le DatePicker pour mettre à jour les créneaux horaires disponibles
        datePicker.setOnAction(event -> updateListeView());
    }*/

    private void updateListeView() { //pour la listeView
        // Récupérer la date sélectionnée
        LocalDate selectedDate = datePicker.getValue();

        // Vérifier si une date a été sélectionnée
        if (selectedDate == null) {
            // Si aucune date n'est sélectionnée, ne rien faire
            return;
        }

        // Récupérer les plages horaires de travail pour le jour sélectionné
        String jourSelectionne = selectedDate.getDayOfWeek().toString();
        List<HoraireTravail> horairesTravail = getHorairesTravail(jourSelectionne);

        // Mettre à jour la liste des créneaux horaires disponibles dans time_slots_available
        updateAvailableTimeSlots(selectedDate, horairesTravail);
    }
    @FXML
    private void confirmerButtonClicked() {
        try{

            System.out.println("ene tw fil add");
            // Récupérer les valeurs des champs
            //********* formated date de jj/mm/aaaa en aaaa-mm-jj pour l'inserer dans la base
            LocalDate selectedDate = datePicker.getValue();
            java.sql.Date sqlDate = java.sql.Date.valueOf(selectedDate);
            String tempsReservation = heure_txt.getText();
            String duree_txt= dure_res_txt.getText();
            if (!validerHeuresEtAfficherMessages(tempsReservation,duree_txt)){
                return;
            }


            LocalTime selectedTime = LocalTime.parse(tempsReservation);
            java.sql.Time sqlTime = java.sql.Time.valueOf(selectedTime);
            Time duree = Time.valueOf(duree_txt);

            String email=email_txt.getText();

            if (!validateEmail(email) || email_txt.getText()==null){
                email_val_msg.setVisible(true);


                return;
            }

            User user = userController.getUserByEmail(email_txt.getText());




            Terrain terrain = terrainController.getById(selectedTerrainId);

            System.out.println("selectedDate: " + selectedDate + "selectedTime: " + selectedTime + "duree: " + duree);
            System.out.println("user: " + user);
            System.out.println("terrain: " + terrain);


            // Appliquer la méthode verifierDisponibilite
            if (!verifierDisponibilite(selectedDate, selectedTime, duree)) {
                // Si la disponibilité n'est pas confirmée, ne rien faire
                return;
            }
            if (verifierPresenceChevauchemment(service.getReservationsByTerrainID( selectedDate,selectedTerrainId))){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("heure choisi peut causer un chauvechement avec autres reservations");
                alert.setContentText("Veuillez choisir un autre horaire");

                // Ajouter une image à la boîte de dialogue
                DialogPane dialogPane = alert.getDialogPane();
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/error.png")));
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                dialogPane.setGraphic(imageView);

                // Afficher la boîte de dialogue
                alert.showAndWait();
                return ;
            }

            // Récupérer les plages horaires de travail pour le jour sélectionné
            String jourSelectionne = selectedDate.getDayOfWeek().toString();
            List<HoraireTravail> horairesTravail = getHorairesTravail(jourSelectionne);

            // Mettre à jour la liste des créneaux horaires disponibles
            mettreAJourTimeSlotsDisponibles(selectedDate, horairesTravail);
            System.out.println("ggggg");
            //*********Location Equipement*********************
           /* if (equi_btn_clicked()){
                confirmer_btn.setOnAction(event -> loadLocationEquipementScene(selectedTerrainId));

            }*/

            float prix_par_duree=calculeLocationTerrainPourDureeChoisie(duree,selectedTerrainId);
            prix_terrain_duree.setText(String.valueOf(prix_par_duree));



            Reservation reservation=new Reservation(sqlDate,sqlTime,terrain.getType(),prix_par_duree,user.getId(),terrain.getId(),duree);

            service.add(reservation);
          /*  if (equi_btn.isSelected()){
                try {
                    loadLocationEquipementScene(selectedTerrainId,selectedTerrainType);
                    System.out.println("IN IF *********");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {
                System.out.println("IN ELSE *********");

            }*/



            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText("Reservation ajoutée avec succés");
            alert.setContentText("A bientot !");

            // Ajouter une image à la boîte de dialogue
            DialogPane dialogPane = alert.getDialogPane();
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/succes.jpg")));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            dialogPane.setGraphic(imageView);

            // Afficher la boîte de dialogue
            alert.showAndWait();
            updateListeView();

            equipement_btn.setVisible(false);
            if (equi_btn_clicked()){
                equipement_btn.setVisible(true);

            }
            equipement_btn.setOnAction(event -> {
                try {
                    showEquipementInterface();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });







        }catch(Exception e){
            System.out.println("Encountered an exception in confirmerButtonClicked");
            e.printStackTrace();
        }
    }

    // ceci est un controleur de saisie
    private boolean validateHeure(String heure) {
        // Expression régulière pour le format "hh:mm"
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(heure);
        return matcher.matches();
    }


    private boolean validateEmail(String email) {
        // Expression régulière pour valider l'e-mail
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validerHeuresEtAfficherMessages(String tempsReservation, String duree) {
        boolean heureDebutValide = validateHeure(tempsReservation);
        boolean dureeValide = validateHeure(duree);

        // Afficher les messages d'erreur si les heures ne sont pas valides

        if (!heureDebutValide || heure_txt.getText()==null ) {
            heure_reserv_valmsg.setVisible(true);
        }

        if (!dureeValide || dure_res_txt.getText()==null) {
            duree_valmsg.setVisible(true);
        }


        // Si au moins une des heures est invalide, retourner false
        if (!heureDebutValide || !dureeValide) {
          //  duree_valmsg.setVisible(true);
           // heure_reserv_valmsg.setVisible(true);

            return false;
        }

        // Si les deux heures sont valides, retourner true
        heure_reserv_valmsg.setVisible(false);
        duree_valmsg.setVisible(false);
        return true;
    }

    // ************** Location Equipement******************
   private boolean equi_btn_clicked(){
        if (equi_btn.isSelected()){
           return true;

        }
       return false;
    }
    //************************************************

  /*  private void loadLocationEquipementScene(int selectedTerrainId, String selectedTerrainType) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/LocationEquipement.fxml"));
            LocationEquipementController controller = new LocationEquipementController(selectedTerrainId, selectedTerrainType);
            fxmlLoader.setController(controller);

            System.out.println("controller: " + controller);

            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) equi_btn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



    public float calculeLocationTerrainPourDureeChoisie(Time duree,int selectedTerrainId){
        int dureeMinutes = convertirEnMinutes(dure_res_txt.getText());
      Terrain terrain=terrainController.getById(selectedTerrainId);
      float prix_Terrain=terrain.getPrix_location_terrain();
      float prix_Terrain_parDuree= (prix_Terrain/60)*dureeMinutes;
      return prix_Terrain_parDuree;
    }

    public void showEquipementInterface()throws IOException{
        Stage stage=(Stage) equipement_btn.getScene().getWindow();
        stage.close();
        Stage primaryStage=new Stage();
        Parent root=FXMLLoader.load(getClass().getResource("/sample/pidevjava/LocationEquipement.fxml"));
        primaryStage.setTitle("Location des equipements");
        primaryStage.setScene(new Scene(root,900,750));
        primaryStage.show();
    }


}














