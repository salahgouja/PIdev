package sample.pidevjava.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.interfaces.IController;
import sample.pidevjava.model.Reservation;
import sample.pidevjava.model.User;

import java.sql.*;
import java.util.ArrayList;
import javafx.fxml.FXML;
import sample.pidevjava.controller.ReservationController;
import sample.pidevjava.model.Reservation;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class ReservationController implements IController<Reservation> {
    @FXML
    private ImageView terrain_img;

    @FXML
    private DatePicker date_reservation_date;

    @FXML
    private TextField temps_txt;

    @FXML
    private TextField image_txt;

    @FXML
    private TextField prix_txt;

    @FXML
    private TextField id_txt;

    @FXML
    private TextField id_reservation_txt;
    @FXML
    private Button addBtn;
    @FXML
    private TableView<Reservation> table_reservation;
    @FXML
    private TableColumn<Reservation, Integer> id_reservation_col;
    @FXML
    private TableColumn<Reservation, Date> date_col;
    @FXML
    private TableColumn<Reservation, String> temps_col;

    @FXML
    private TableColumn<Reservation, Double> prix_col;
    @FXML
    private TableColumn<Reservation, Integer> id_user_col;
    //ObservableList<Reservation>reservations= FXCollections.observableArrayList();
    @FXML
    public void initialize() {   // methode edhi hya ili taaml affichage aal table taa admin
        id_reservation_col.setCellValueFactory(new PropertyValueFactory<>("id_reservation"));
        date_col.setCellValueFactory(new PropertyValueFactory<>("date_reserve"));
        temps_col.setCellValueFactory(new PropertyValueFactory<>("temps_reservation"));
       // imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        prix_col.setCellValueFactory(new PropertyValueFactory<>("prix_reservation"));
        id_user_col.setCellValueFactory(new PropertyValueFactory<>("id"));


        // Appeler votre méthode getAll() pour récupérer les données de réservation
        ArrayList<Reservation> reservations = getAll();
        ObservableList<Reservation> observableReservations = FXCollections.observableArrayList(reservations);
        table_reservation.setItems(observableReservations);

        // Ajouter les données à la TableView
        //table_reservation.getItems().addAll(reservations);
       // table_reservation.setItems((ObservableList<Reservation>) reservations);
    }



    // private User user=new User();

   /* @Override
    public void add(Reservation reservation) {
        String qry="INSERT INTO `reservation`( `date_reserve`, `temps_reservation`, `image`, `prix_reservation`,`id_user`) VALUES (?,?,?,?,?) ";
        try {
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(qry);

            stm.setDate(1,reservation.getDate_reserve());
            stm.setTime(2,reservation.getTemps_reservation());
            stm.setString(3, reservation.getImage());
            stm.setDouble(4,reservation.getPrix_reservation());
            stm.setInt(5, reservation.getId());
            stm.executeUpdate();
            //JOptionPane.showMessageDialog(null,"reservation ajouté avec succés")
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }*/
   /* public void create_btn(ActionEvent actionEvent) {
        Date date_reservation = Date.valueOf(date_reservation_date.getValue());
        Time temps_reservation = Time.valueOf(temps_txt.getText());
        String terrain = image_txt.getText();
        Double prix_reservation = Double.parseDouble(prix_txt.getText());
        int id_user = Integer.parseInt(id_txt.getText());

        Reservation reservation = new Reservation();
        reservation.setDate_reserve(date_reservation);
        reservation.setTemps_reservation(temps_reservation);
        reservation.setImage(terrain);
        reservation.setPrix_reservation(prix_reservation);
        reservation.setId(id_user);

        // Afficher l'image correspondant au terrain dans l'ImageView
        String imagePath = "/img/" + terrain + ".jpg";
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        terrain_img.setImage(image);


        // Appel de la méthode add pour ajouter la réservation à la base de données
        add(reservation);
        initialize();
    }*/

    @Override
    public void add(Reservation reservation) {

    }

    @Override
    public ArrayList<Reservation> getAll() {
        ArrayList<Reservation> reservations = new ArrayList();
        String qry ="SELECT * FROM `reservation`";
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()){
                Reservation reservation = new Reservation();
                reservation.setId_reservation(rs.getInt(1));
                reservation.setDate_reserve(rs.getDate(2));
                reservation.setTemps_reservation(rs.getTime(3));
             //   reservation.setImage(rs.getString(4));
                reservation.setPrix_reservation(rs.getDouble(5));
                reservation.setId(rs.getInt(6));


                reservations.add(reservation);
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return reservations;
    }

    @Override
    public void update(Reservation reservation) {

    }







    /*@Override
    public void update(Reservation reservation) {
        String qry = "UPDATE `reservation` SET `date_reserve`=?, `temps_reservation`=?, `image`=?, `prix_reservation`=?, `id_user`=? WHERE `id_reservation`=?";
        try {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
            stm.setDate(1, reservation.getDate_reserve());
            stm.setTime(2, reservation.getTemps_reservation());
            stm.setString(3, reservation.getImage());
            stm.setDouble(4, reservation.getPrix_reservation());
            stm.setInt(5, reservation.getId());
            stm.setInt(6, reservation.getId_reservation());

            int rowsUpdated = stm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("La réservation a été mise à jour avec succès.");
            } else {
                System.out.println("La réservation avec l'ID spécifié n'existe pas.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/




    @Override
    public boolean delete(Reservation reservation) {
        String qry = "DELETE FROM `reservation` WHERE `id_reservation`=?";
        try {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
            stm.setInt(1, reservation.getId_reservation());

            int rowsDeleted = stm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("La réservation a été supprimée avec succès.");
                return true; // La réservation a été supprimée avec succès
            } else {
                System.out.println("La réservation avec l'ID spécifié n'existe pas.");
                return false; // Aucune réservation correspondante n'a été trouvée
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; // Une erreur s'est produite lors de la suppression de la réservation
        }
    }




   /* public void update_btn(ActionEvent actionEvent) {
        int id_reservation=parseInt(id_reservation_txt.getText());
        Date date_reservation= Date.valueOf(date_reservation_date.getValue());
        String temps_reservation=temps_txt.getText();
        String image=image_txt.getText();
        Double prix_reservation=parseDouble(prix_txt.getText());
        int id_user=parseInt(id_txt.getText());

        Reservation reservation = new Reservation();
        reservation.setId_reservation(id_reservation);

        reservation.setDate_reserve(date_reservation);
        reservation.setTemps_reservation(Time.valueOf(temps_reservation));
        reservation.setImage(image);
        reservation.setPrix_reservation(prix_reservation);
        reservation.setId(id_user);

        update(reservation);
        initialize();
    }*/


   /* public void delete_btn(ActionEvent actionEvent) {
        int id_reservation = parseInt(id_reservation_txt.getText());

        // Create a Reservation object with the ID entered
        Reservation reservationToDelete = new Reservation();
        reservationToDelete.setId_reservation(id_reservation);

        // Call the delete method passing the Reservation object
        boolean deleted = delete(reservationToDelete);

        if(deleted) {

            System.out.println("Reservation deleted successfully.");
        } else {

            System.out.println("Failed to delete reservation.");
        }
    }*/
   public void delete_btn(ActionEvent actionEvent) {
       // Récupérer la réservation sélectionnée dans la table
       Reservation reservationToDelete = table_reservation.getSelectionModel().getSelectedItem();

       if (reservationToDelete != null) {
           // Appeler la méthode de suppression en passant l'objet de réservation
           boolean deleted = delete(reservationToDelete);

           if (deleted) {
               System.out.println("Reservation deleted successfully.");
           } else {
               System.out.println("Failed to delete reservation.");
           }
       } else {
           System.out.println("Veuillez sélectionner une réservation à supprimer.");
       }
       initialize();
   }
    public void selectReservation(){
        Reservation reservation= table_reservation.getSelectionModel().getSelectedItem();
       // int num=table_reservation.getSelectionModel().getFocusedIndex();
        /*if ((num-1)<-1){
            return;
        }*/
       if (reservation != null) {
        //id_reservation_txt.setText(String.valueOf(reservation.getId_reservation()));
        date_reservation_date.setValue(reservation.getDate_reserve().toLocalDate());
         //  image_txt.setText(reservation.getImage());
        //temps_txt.setText(reservation.getTemps_reservation());
        prix_txt.setText(String.valueOf(reservation.getPrix_reservation()));
        id_txt.setText(String.valueOf(reservation.getId()));
        }


    }

    // Creation des cartes

}




