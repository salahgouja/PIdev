package sample.pidevjava.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.interfaces.IServices;
import sample.pidevjava.model.Evenement;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventsPageController implements IServices<Evenement> {

    @FXML
    private TextField dateField;

    @FXML
    private TextField titreField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField prixField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField categorieField;
////////////////////////////////////////////////////////////////////////////////
//@FXML
//private TableView<Evenement> eventTableView;
//
//    @FXML
//    private TableColumn<Evenement, Integer> idColumn;
//
//    @FXML
//    private TableColumn<Evenement, String> dateColumn;
//
//    @FXML
//    private TableColumn<Evenement, String> titreColumn;
//
//    @FXML
//    private TableColumn<Evenement, String> descriptionColumn;
//
//    @FXML
//    private TableColumn<Evenement, String> prixColumn;
//
//    @FXML
//    private TableColumn<Evenement, String> typeColumn;
//
//    @FXML
//    private TableColumn<Evenement, String> categorieColumn;
//    @FXML
//    private void initialize() {
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_event"));
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
//        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
//        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
//        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
//        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
//
//        loadEventData();
//    }
//
//    private void loadEventData() {
//        // Connect to your database and fetch event data
//        try {
//            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:/pidev","root","");
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM evenement");
//
//            ArrayList<Evenement> events = new ArrayList<>();
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id_event");
//                String date = resultSet.getString("date");
//                String titre = resultSet.getString("titre");
//                String description = resultSet.getString("description");
//                String prix = resultSet.getString("prix");
//                String type = resultSet.getString("type");
//                String categorie = resultSet.getString("categorie");
//
//                Evenement event = new Evenement();
//                events.add(event);
//            }
//
//            eventTableView.getItems().addAll(events);
//
//            resultSet.close();
//            statement.close();
//            connection.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(EventsPageController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void add() {

        String date = dateField.getText();
        String titre = titreField.getText();
        String description = descriptionField.getText();
        String prix = prixField.getText();
        String type = typeField.getText();
        String categorie = categorieField.getText();

        // Créer un nouvel objet Evenement avec les valeurs récupérées du formulaire
        Evenement nouvelEvenement = new Evenement(date, titre, description, prix, type, categorie);

        // Exécuter la requête SQL pour insérer les données dans la base de données
        String qry = "INSERT INTO `evenement` (`date`, `titre`, `description`, `prix`, `type`, `categorie`) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
            stm.setString(1, nouvelEvenement.getDate());
            stm.setString(2, nouvelEvenement.getTitre());
            stm.setString(3, nouvelEvenement.getDescription());
            stm.setString(4, nouvelEvenement.getPrix());
            stm.setString(5, nouvelEvenement.getType());
            stm.setString(6, nouvelEvenement.getCategorie());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public ArrayList<Evenement> getAll() {
        ArrayList<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenement";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Evenement evenement = new Evenement();
                evenement.setDate(resultSet.getString("date"));
                evenement.setTitre(resultSet.getString("titre"));
                evenement.setDescription(resultSet.getString("description"));
                evenement.setPrix(resultSet.getString("prix"));
                evenement.setType(resultSet.getString("type"));
                evenement.setCategorie(resultSet.getString("categorie"));
                evenements.add(evenement);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenements;
    }

    @Override
    public void update(Evenement evenement) {
        String query = "UPDATE evenement SET date=?, titre=?, description=?, prix=?, type=?, categorie=? WHERE id_event=?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, evenement.getDate());
            statement.setString(2, evenement.getTitre());
            statement.setString(3, evenement.getDescription());
            statement.setString(4, evenement.getPrix());
            statement.setString(5, evenement.getType());
            statement.setString(6, evenement.getCategorie());
            statement.setInt(7, evenement.getId_event());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Evenement evenement) {
        String query = "DELETE FROM evenement WHERE id_event=?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, evenement.getId_event());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
