package sample.pidevjava.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;

import sample.pidevjava.model.Evenement;
import sample.pidevjava.model.EvetCategory;
import javafx.scene.control.Pagination;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import sample.pidevjava.model.Participation;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static sample.pidevjava.model.EvetCategory.evetCategory;


public class EventsPageController extends ISevecesEvent  implements Initializable {
    @FXML
    private Label Menu;
    @FXML
    private AnchorPane slider;
    @FXML
    private Label MenuClose;
    @FXML
    private ImageView imageView;
    private Stage primaryStage;

    @FXML
    private Button uploadButton;

    @FXML
    private Label nbParticipation;
    @FXML
    private Label nbEvents;

    @FXML
    public ListView<Evenement> mylistview;

    @FXML
    public ListView<Participation> mylistview1;

    @FXML
    private DatePicker dateFieldPicker;

    @FXML
    private TextField searchField;
    @FXML
    private TextField searchField1;

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField prixField;

    @FXML
    private ChoiceBox<String> categorieFieldchoise ;


    @FXML
    private JFXButton logout;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            categorieFieldchoise.getItems().addAll(evetCategory);
            categorieFieldchoise.setItems(FXCollections.observableArrayList(EvetCategory.evetCategory));
        }catch (Exception e){
            System.out.println(e);

        };
        getAll();
        setEventList();
        getAllParticipation();
        setParticipationList();

    }



    int itemsPerPage = 3; // Change this value according to your requirement
    Pagination pagination;
    @FXML
    private VBox layout;


    void setEventList() {
        ObservableList<Evenement> observableList = FXCollections.observableArrayList(evenements);

        mylistview.setItems(observableList);

        nbEvents.setText(String.valueOf(evenements.size()));
        mylistview.setCellFactory(param -> new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("eventCardDash.fxml"));
                        Node node = loader.load();
                        EventCardController controller = loader.getController();
                        controller.setEventData(item); // Pass the event data to the controller
                        setGraphic(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    private void search(){
        ObservableList<Evenement> observableList = FXCollections.observableArrayList(evenements);
        mylistview.setItems(observableList);
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Evenement> filteredList = observableList.filtered(
                evenement -> evenement.getTitre().toLowerCase().contains(searchText)
                        || String.valueOf(evenement.getId_event()).contains(searchText)
                        || evenement.getDate().toLowerCase().contains(searchText)
                        || String.valueOf(evenement.getPrix()).contains(searchText)

                // Add more conditions for other fields as needed
        );
        mylistview.setItems(filteredList);
    }

    @FXML
    private void search1(){
        ObservableList<Participation> observableList = FXCollections.observableArrayList(paticipations);
        mylistview1.setItems(observableList);
        String searchText = searchField1.getText().toLowerCase();
        ObservableList<Participation> filteredList = observableList.filtered(
                p -> String.valueOf(p.getId_participation()).contains(searchText)

        );
        mylistview1.setItems(filteredList);
    }

    @FXML
    public void refrechEventlist() {


    }
    @FXML
    public void refrechParticipationlist() {
        ObservableList<Participation> observableList = FXCollections.observableArrayList(paticipations);
        ObservableList<Participation> filteredList = observableList.filtered(
                p -> p.getEtat().contains("en attend")

        );
        mylistview1.setItems(filteredList);
        mylistview1.refresh();

    }
//    void setParticipationList() {
//        ObservableList<Participation> observableList = FXCollections.observableArrayList(paticipations);
//        ObservableList<Participation> filteredList = observableList.filtered(
//                p -> p.getEtat().contains("en attend")
//        );
//
//        // Create Pagination instance and set page count
//        pagination = new Pagination((int) Math.ceil((double) filteredList.size() / itemsPerPage), 0);
//        pagination.setPageFactory(pageIndex -> createParticipationPage(pageIndex, filteredList));
//
//        // Add pagination to your layout
//        layout.getChildren().add(pagination);
//
//        nbParticipation.setText(String.valueOf(filteredList.size()));
//    }
//
//    private Node createParticipationPage(int pageIndex, ObservableList<Participation> filteredList) {
//        int fromIndex = pageIndex * itemsPerPage;
//        int toIndex = Math.min(fromIndex + itemsPerPage, filteredList.size());
//        ListView<Participation> pageListView = new ListView<>();
//        AtomicReference<Participation> selectedParticipation = new AtomicReference<>();
//        mylistview1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                selectedParticipation.set(newValue);
//                System.out.println(selectedParticipation.get());
//            }
//        });
//        // Customize your cell factory here for the pageListView if needed
//        pageListView.setCellFactory(param -> new ListCell<Participation>() {
//            @Override
//            protected void updateItem(Participation item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                    setGraphic(null);
//                } else {
//                    try {
//                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("participationcardDash.fxml"));
//                        Node node = loader.load();
//                        EventCardController controller = loader.getController();
//                        controller.setParticipationData(item);
//                        controller.setParticipation(selectedParticipation.get());
//                        setGraphic(node);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // Set items for the current page
//        pageListView.setItems(FXCollections.observableArrayList(filteredList.subList(fromIndex, toIndex)));
//
//        return pageListView;
//    }



    void setParticipationList() {
        ObservableList<Participation> observableList = FXCollections.observableArrayList(paticipations);
        ObservableList<Participation> filteredList = observableList.filtered(
                p -> p.getEtat().contains("en attend")

        );
        mylistview1.setItems(filteredList);
        //mylistview1.setItems(observableList);
        nbParticipation.setText(String.valueOf(paticipations.size()));
        AtomicReference<Participation> selectedParticipation = new AtomicReference<>();
        mylistview1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedParticipation.set(newValue);
                System.out.println(selectedParticipation.get());
            }
        });

        mylistview1.setCellFactory(param -> new ListCell<Participation>() {
            @Override
            protected void updateItem(Participation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/participationcardDash.fxml"));
                        Node node = loader.load();
                        EventCardController controller = loader.getController();
                        controller.setParticipationData(item);
                        controller.setParticipation(selectedParticipation.get());
                        setGraphic(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




    public void add() throws IOException {
        int id_event =0;
        String date = dateFieldPicker.getValue().toString();
        String titre = titreField.getText();
        String description = descriptionField.getText();
        String prix = prixField.getText();
        String categorie = categorieFieldchoise.getValue();
        // Get the image file from the ImageView
        Image image = imageView.getImage();
        File imageFile = new File("temp_image.png");


        // Save the image to a temporary file
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(bufferedImage, "png", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        //    showAlert(Alert.AlertType.ERROR, "Error", "Failed to save the image.");
            return;
        }

        // Convert the image file to Base64 encoding

        String base64Image = convertImageToBase64(imageFile);

        // Créer un nouvel objet Evenement avec les valeurs récupérées du formulaire
        Evenement nouvelEvenement = new Evenement(id_event,date, titre, description, prix, categorie,base64Image);

        // Exécuter la requête SQL pour insérer les données dans la base de données
        String qry = "INSERT INTO `evenement` (`date`, `titre`, `description`, `prix`, `categorie`,`image`) VALUES ( ?, ?,?, ?, ?, ?)";
        if (dateFieldPicker.getValue() == null ||
                titreField.getText().isEmpty() ||
                descriptionField.getText().isEmpty() ||
                categorieFieldchoise.getValue() == null ||
                imageView.getImage() == null ||
                prixField.getText()== null ) {
            System.out.println("error rmty");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Vous devez remplir tous les champs.");
            alert.showAndWait(); // This line was missing
        }
        if ( !prixField.getText().matches("\\d+") ) {
            System.out.println("error rmty");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Le champ de prix doit contenir uniquement des chiffres.");
            alert.showAndWait(); // This line was missing
        } else {
            try {
                PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
                stm.setString(1, nouvelEvenement.getDate());
                stm.setString(2, nouvelEvenement.getTitre());
                stm.setString(3, nouvelEvenement.getDescription());
                stm.setString(4, nouvelEvenement.getPrix());
                stm.setString(5, nouvelEvenement.getCategorie());
                stm.setString(6, nouvelEvenement.getImage());
                ObservableList<Evenement> observableList = FXCollections.observableArrayList(evenements);
                observableList.addListener((ListChangeListener<Evenement>) change -> {setEventList();
                });
                stm.executeUpdate();
                dateFieldPicker.setValue(null);
                titreField.clear();
                descriptionField.clear();
                prixField.clear();
                categorieFieldchoise.setValue(null);
                imageView.setImage(null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            evenements.clear();
            mylistview.getItems().clear();
            getAll();
            ObservableList<Evenement> observableList = FXCollections.observableArrayList();
            observableList.addAll(evenements);
            mylistview.setItems(observableList);
        }

    }





    public void update(Evenement evenement) throws IOException {


        int id_event =0;
        String date = dateFieldPicker.getValue().toString();
        String titre = titreField.getText();
        String description = descriptionField.getText();
        String prix = prixField.getText();
        String categorie = categorieFieldchoise.getValue();
        Image image = imageView.getImage();
        File imageFile = new File("temp_image.png");



        // Save the image to a temporary file
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(bufferedImage, "png", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            //    showAlert(Alert.AlertType.ERROR, "Error", "Failed to save the image.");
            return;
        }

        String base64Image = convertImageToBase64(imageFile);
        Evenement nouvelEvenement = new Evenement( id_event,date, titre, description, prix, categorie,base64Image);
        String query = "UPDATE evenement SET date=?, titre=?, description=?, prix=?, categorie=? ,image =? WHERE id_event=?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, dateFieldPicker.getValue().toString());
            statement.setString(2, titreField.getText());
            statement.setString(3, descriptionField.getText());
            statement.setString(4, prixField.getText());
            statement.setString(5, categorieFieldchoise.getValue());
            statement.setString(6, nouvelEvenement.getImage());
            statement.setInt(7, evenement.getId_event());
           // set the ID
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        evenements.clear();
        mylistview.getItems().clear();
        getAll();
        ObservableList<Evenement> observableList = FXCollections.observableArrayList();
        observableList.addAll(evenements);
        mylistview.setItems(observableList);
    }

//    public boolean delete(Evenement evenement) {
//        String query = "DELETE FROM evenement WHERE id_event=?";
//        try {
//            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
//            statement.setInt(1, evenement.getId_event());
//            int rowsDeleted = statement.executeUpdate();
//            return rowsDeleted > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean delete(Evenement evenement) {
        String checkParticipationQuery = "SELECT COUNT(*) FROM participation WHERE id_event=?";
        String deleteParticipationQuery = "DELETE FROM participation WHERE id_event=?";
        String deleteEvenementQuery = "DELETE FROM evenement WHERE id_event=?";

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            // Vérifier s'il y a des enregistrements associés dans la table participation
            PreparedStatement checkParticipationStatement = connection.prepareStatement(checkParticipationQuery);
            checkParticipationStatement.setInt(1, evenement.getId_event());
            ResultSet resultSet = checkParticipationStatement.executeQuery();
            resultSet.next();
            int participationCount = resultSet.getInt(1);

            if (participationCount > 0) {
                // Afficher une boîte de dialogue de confirmation
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmation de suppression");
                confirmAlert.setHeaderText("Suppression d'événement avec des enregistrements associés");
                confirmAlert.setContentText("Cet événement a des enregistrements associés. Voulez-vous supprimer les enregistrements associés également?");

                // Ajouter les boutons OK et Annuler à la boîte de dialogue
                confirmAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                Optional<ButtonType> result = confirmAlert.showAndWait();

                // Si l'utilisateur clique sur le bouton OK, supprimer les enregistrements associés et l'événement
                if (result.isPresent() && result.get() == ButtonType.YES) {
                    // Supprimer les enregistrements associés dans la table participation
                    PreparedStatement deleteParticipationStatement = connection.prepareStatement(deleteParticipationQuery);
                    deleteParticipationStatement.setInt(1, evenement.getId_event());
                    deleteParticipationStatement.executeUpdate();

                    // Supprimer l'événement de la table evenement
                    PreparedStatement deleteEvenementStatement = connection.prepareStatement(deleteEvenementQuery);
                    deleteEvenementStatement.setInt(1, evenement.getId_event());
                    int rowsDeleted = deleteEvenementStatement.executeUpdate();

                    return rowsDeleted > 0;
                } else {
                    // Si l'utilisateur clique sur le bouton Annuler, annuler la suppression
                    return false;
                }
            } else {
                // S'il n'y a pas d'enregistrements associés, supprimer simplement l'événement
                PreparedStatement deleteEvenementStatement = connection.prepareStatement(deleteEvenementQuery);
                deleteEvenementStatement.setInt(1, evenement.getId_event());
                int rowsDeleted = deleteEvenementStatement.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteParticipation(Participation p) {
        String query = "DELETE FROM participation WHERE id_event=? ";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, p.getId_event());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeEvent(ActionEvent actionEvent) {
        Evenement Selectedenvent = mylistview.getSelectionModel().getSelectedItem();
        delete(Selectedenvent);
        dateFieldPicker.setValue(null);
        titreField.clear();
        descriptionField.clear();
        prixField.clear();
        categorieFieldchoise.setValue(null);
        imageView.setImage(null);
        evenements.clear();
        mylistview.getItems().clear();
        getAll();
        ObservableList<Evenement> observableList = FXCollections.observableArrayList();
        observableList.addAll(evenements);
        mylistview.setItems(observableList);

    }




    @FXML
    public Evenement selectItem() {
        Evenement Selectedenvent = mylistview.getSelectionModel().getSelectedItem();
        if(Selectedenvent.equals(null)){
            System.out.println("not selected");
        }else {
            categorieFieldchoise.setValue(String.valueOf(Selectedenvent.getCategorie()));
            descriptionField.setText(String.valueOf(Selectedenvent.getDescription()));
            prixField.setText(String.valueOf(Selectedenvent.getPrix()));
            titreField.setText(String.valueOf(Selectedenvent.getTitre()));
            dateFieldPicker.setValue(LocalDate.parse(Selectedenvent.getDate()));
            Image image = convertBase64ToImage(Selectedenvent.getImage());
            imageView.setImage(image);
        }
        return Selectedenvent;
    }

    public void updeteEvent(ActionEvent actionEvent) throws IOException {
        Evenement Selectedenvent = mylistview.getSelectionModel().getSelectedItem();

        if(Selectedenvent.equals(null)){
            System.out.println("not selected");
        }else {
            update(Selectedenvent);
            dateFieldPicker.setValue(null);
            titreField.clear();
            descriptionField.clear();
            prixField.clear();
            categorieFieldchoise.setValue(null);
            imageView.setImage(null);
        }

    }






    /********************************image uplode*************************************/
    public void chooseAndUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            // Call method to handle file selection
            handleSelectedFile(selectedFile);
        }
    }


    private void handleSelectedFile(File file) {
        try {

            String base64Image = convertImageToBase64(file);
            Image image = convertBase64ToImage(base64Image);
            imageView.setImage(image);

        } catch (IOException e) {
            e.printStackTrace();
         //   showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while uploading the image.");
        }
    }
    public Image convertBase64ToImage(String base64Image) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        return new Image(new ByteArrayInputStream(imageBytes));
    }

    private String convertImageToBase64(File file) throws IOException {
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            byte[] imageData = new byte[(int) file.length()];
            imageInFile.read(imageData);
            return Base64.getEncoder().encodeToString(imageData);
        }
    }


    public void logout(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/LoginForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage primarystage = (Stage) logout.getScene().getWindow();
            primarystage.setScene(scene);
            primarystage.setTitle("Login Form");
            primarystage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /***************************************************************/

    @FXML
    private Button pageGestionUser;
    @FXML
    private Button pageGestionBlog;
    @FXML
    private Button pageGetionTerrain;
    @FXML
    private Button pageGetionOffre;
    @FXML
    private Button pageGestionPartenariat;
    @FXML
    private Button pageGestionReservation;
    @FXML
    private Button pageGestionEvent;

    @FXML
    private Button pageEvent;
    @FXML
    private Button pageReservation;
    @FXML
    private Button pagePartenaria;
    @FXML
    private Button pageBlog;
    @FXML
    private Button profile;



    @FXML
    private void goToPage(ActionEvent event) throws IOException {
        Stage primaryStage = null;
        if (event.getSource() instanceof Button) {
            Button button = (Button) event.getSource();
            String fxmlPath = null;
            switch (button.getId()) {
                case "pageGestionUser":
                    primaryStage = (Stage) pageGestionUser.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/DashboardAdmin.fxml";
                    break;
                case "pageGestionBlog":
                    primaryStage = (Stage) pageGestionBlog.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/DashBoard.fxml";
                    break;
                case "pageGetionTerrain":
                    primaryStage = (Stage) pageGetionTerrain.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashbordterrain.fxml";
                    break;
                case "pageGetionOffre":
                    primaryStage = (Stage) pageGetionOffre.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashbordappel.fxml";
                    break;
                case "pageGestionPartenariat":
                    primaryStage = (Stage) pageGestionPartenariat.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dash.fxml";
                    break;
                case "pageGestionReservation":
                    primaryStage = (Stage) pageGestionReservation.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashboardEvent.fxml";
                    break;
                case "pageGestionEvent":
                    primaryStage = (Stage) pageGestionEvent.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashboardEvent.fxml";
                    break;

                case "pageEvent":
                    primaryStage = (Stage) pageEvent.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/EventsPage.fxml";
                    break;

                case "pageReservation":
                    primaryStage = (Stage) pageReservation.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/dashboardEvent.fxml";
                    break;

                case "pagePartenaria":
                    primaryStage = (Stage) pagePartenaria.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/front.fxml";
                    break;

                case "pageBlog":
                    primaryStage = (Stage) pageBlog.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/GestionArticle2.fxml";
                    break;

                case "profile":
                    primaryStage = (Stage) profile.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/DashboardUser.fxml";
                    break;

                case "logout":
                    primaryStage = (Stage) logout.getScene().getWindow();
                    fxmlPath = "/sample/pidevjava/LoginFrom.fxml";
                    break;


            }
            if (fxmlPath != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
                Scene scene = new Scene(fxmlLoader.load());
                primaryStage.setScene(scene);
            }
        }
    }
}



