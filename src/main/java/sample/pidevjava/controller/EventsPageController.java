package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.interfaces.IServices;
import sample.pidevjava.model.Evenement;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;


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


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Evenement, Integer> idColumn;

    @FXML
    private TableColumn<Evenement, String> categorieColumn;

    @FXML
    private TableColumn<Evenement, String> dateColumn;

    @FXML
    private TableColumn<Evenement, String> descriptionColumn;


    @FXML
    private TableView<Evenement> eventTableView;

    @FXML
    private TableColumn<Evenement, String> prixColumn;

    @FXML
    private TableColumn<Evenement, String> titreColumn;

    @FXML
    private TableColumn<Evenement, String> typeColumn;

    @FXML
    void initialize() {
        assert idColumn != null;
        assert categorieColumn != null;
        assert dateColumn != null;
        assert descriptionColumn != null;
        assert eventTableView != null;
        assert prixColumn != null;
        assert titreColumn != null;
        assert typeColumn != null;

        // Define cell value factories for each column
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_event"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Load data into the TableView
        eventTableView.getItems().addAll(getAll());
    }




    @Override
    public void add() {
        int id_event =0;
        String date = dateField.getText();
        String titre = titreField.getText();
        String description = descriptionField.getText();
        String prix = prixField.getText();
        String type = typeField.getText();
        String categorie = categorieField.getText();

        // Créer un nouvel objet Evenement avec les valeurs récupérées du formulaire
        Evenement nouvelEvenement = new Evenement(id_event,date, titre, description, prix, type, categorie);

        // Exécuter la requête SQL pour insérer les données dans la base de données
        String qry = "INSERT INTO `evenement` (`date`, `titre`, `description`, `prix`, `type`, `categorie`) VALUES (?, ?, ?, ?, ?, ?)";
        if(dateField.getText().isEmpty()||titreField.getText().isEmpty()||descriptionField.getText().isEmpty()||prixField.getText().isEmpty()||typeField.getText().isEmpty()||categorieField.getText().isEmpty()){
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setContentText("empty");
            alert.showAndWait();
        }else {
            try {
                PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
                stm.setString(1, nouvelEvenement.getDate());
                stm.setString(2, nouvelEvenement.getTitre());
                stm.setString(3, nouvelEvenement.getDescription());
                stm.setString(4, nouvelEvenement.getPrix());
                stm.setString(5, nouvelEvenement.getType());
                stm.setString(6, nouvelEvenement.getCategorie());
                stm.executeUpdate();
                eventTableView.getItems().addAll(nouvelEvenement);
                eventTableView.refresh();
                dateField.clear();
                titreField.clear();
                descriptionField.clear();
                prixField.clear();
                typeField.clear();
                categorieField.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
                evenement.setId_event(resultSet.getInt("id_event"));
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


        int id_event =0;
        String date = dateField.getText();
        String titre = titreField.getText();
        String description = descriptionField.getText();
        String prix = prixField.getText();
        String type = typeField.getText();
        String categorie = categorieField.getText();
        Evenement nouvelEvenement = new Evenement( id_event,date, titre, description, prix, type, categorie);
        String query = "UPDATE evenement SET date=?, titre=?, description=?, prix=?, type=?, categorie=? WHERE id_event=?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, nouvelEvenement.getDate());
            statement.setString(2, nouvelEvenement.getTitre());
            statement.setString(3, nouvelEvenement.getDescription());
            statement.setString(4, nouvelEvenement.getPrix());
            statement.setString(5, nouvelEvenement.getType());
            statement.setString(6, nouvelEvenement.getCategorie());
            evenement.getId_event();
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


    public void removeEvent(ActionEvent actionEvent) {
        Evenement Selectedenvent = eventTableView.getSelectionModel().getSelectedItem();
        delete(Selectedenvent);
        eventTableView.getItems().remove(Selectedenvent);
    }

    public Evenement selectItem(MouseEvent mouseEvent) {
        TableView.TableViewSelectionModel<Evenement> Selectedenvent = eventTableView.getSelectionModel();
        Evenement event = Selectedenvent.getSelectedItem();
        if(Selectedenvent.isEmpty()){
            System.out.println("not selected");
        }else {
            categorieField.setText(String.valueOf(event.getCategorie()));
            dateField.setText(String.valueOf(event.getDate()));
            descriptionField.setText(String.valueOf(event.getDescription()));
            prixField.setText(String.valueOf(event.getPrix()));
            titreField.setText(String.valueOf(event.getTitre()));
            typeField.setText(String.valueOf(event.getType()));
        }
        return event;
    }

    public void updeteEvent(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel<Evenement> Selectedenvent = eventTableView.getSelectionModel();
        Evenement event = Selectedenvent.getSelectedItem();
        if(Selectedenvent.isEmpty()){
            System.out.println("not selected");
        }else {
            update(event);
        }

    }
}
