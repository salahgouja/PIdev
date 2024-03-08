package sample.pidevjava.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class Dashbordterraincontrolleur  implements Initializable {

    @FXML
    private TableColumn<Terrain, Integer> capaciteCL;

    @FXML
    private TableColumn<Terrain, Integer> idCL;

    @FXML
    private Button ajouterBtn;

    @FXML
    private TableColumn<Terrain, String> nomCL;

    @FXML
    private TableView<Terrain> table;

    @FXML
    private TableColumn<Terrain, TypeTerrain> typeCL;

    @FXML
    private TableColumn<Terrain, Boolean> étatCL;

    @FXML
    private Button updatetn;


    @FXML
    private GridPane gridPane;





    @FXML
    private FlowPane carteflow;


    VBox v ;

    private Timeline clickTimeline;




    public void naviagteToajouter(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/ajouterTerrain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = (Stage) ajouterBtn.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("go");
        primaryStage.centerOnScreen();

    }

    /*
        public void loadMenuInTable() {
            try {
                DBConnection dbConnection = new DBConnection();
                Connection connection = dbConnection.getConnection();

                if (connection != null) {
                    TerrainController rm = new TerrainController(connection);
                    List<Terrain> T = rm.getAll();
                    ObservableList<Terrain> resData = FXCollections.observableArrayList(T);
                    idCL.setCellValueFactory(new PropertyValueFactory<>("id"));
                    nomCL.setCellValueFactory(new PropertyValueFactory<>("nom"));
                    capaciteCL.setCellValueFactory(new PropertyValueFactory<>("capaciteTerrain"));
                    étatCL.setCellValueFactory(new PropertyValueFactory<>("active"));
                    étatCL.setCellFactory(col -> new TableCell<Terrain, Boolean>() {
                        @Override
                        protected void updateItem(Boolean isActive, boolean empty) {
                            super.updateItem(isActive, empty);
                            if (empty || isActive == null) {
                                setText(null);
                            } else {
                                setText(isActive ? "en fonction" : "en entretien");
                            }
                        }
                    });
                    typeCL.setCellValueFactory(new PropertyValueFactory<>("type"));

                    table.setItems(resData);
                } else {
                    System.err.println("Erreur lors de l'établissement de la connexion à la base de données.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

       */
    public void loadMenuInTable() {
        try (Connection connection = new DBConnection().getConnection()) {
            if (connection != null) {
                TerrainController terrainController = new TerrainController(connection);
                List<Terrain> terrains = terrainController.getAll();

                if (gridPane != null) {
                    gridPane.getChildren().clear();
                    // Reste du code
                } else {
                    System.err.println("Erreur : gridPane est null");
                }

                for (int i = 0; i < terrains.size(); i++) {
                    Terrain terrain = terrains.get(i);
                    v  = createCarteView(terrain);



                    gridPane.add(v, i, 0);

                    System.out.println("Carte ajoutée au GridPane : " + i);
                }


                gridPane.requestLayout();

                System.out.println("Cartes ajoutées avec succès au GridPane.");
            } else {
                System.err.println("Erreur lors de l'établissement de la connexion à la base de données.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private VBox createCarteView(Terrain terrain) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/carteview.fxml"));
        VBox v1 = fxmlLoader.load();
        System.out.println("Chargement du FXML de Carteview réussi.");

        Carteview carteview = fxmlLoader.getController();
        carteview.setVisible(true);

        carteview.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000;");

        carteview.updateNom(terrain.getNom());
        carteview.updateEtat(String.valueOf(terrain.isActive()));
        if (terrain.isActive()) {
            carteview.updateEtat("en fonction");
        } else {
            carteview.updateEtat("en entretien");
        }
        carteview.updateCapacite(String.valueOf(terrain.getCapaciteTerrain()));
        carteview.updateType(String.valueOf(terrain.getType()));

        System.out.println("Chargement du FXML de Carteview réussi.");

        v1.setOnMouseClicked(event -> {
            if (clickTimeline == null) {
                clickTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                    try {
                        redirectToUpdateScreen(terrain);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    clickTimeline = null;
                }));
                clickTimeline.play();
            } else {
                clickTimeline.stop();
                clickTimeline = null;
                try {
                    handleCarteClick(terrain);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return v1;
    }



    private void handleCarteClick(Terrain terrain) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Voulez-vous supprimer ce terrain ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try (Connection connection = new DBConnection().getConnection()) {
                if (connection != null) {
                    TerrainController terrainController = new TerrainController(connection);
                    terrainController.delete(terrain);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Supprimer");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Terrain supprimé avec succès!");
                    successAlert.show();

                    removeCardFromGrid(terrain);
                    loadMenuInTable();

                } else {
                    System.err.println("Erreur lors de l'établissement de la connexion à la base de données.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void removeCardFromGrid(Terrain terrain) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof VBox) {
                VBox card = (VBox) node;
                Terrain cardTerrain = (Terrain) card.getUserData();
                if (cardTerrain != null && cardTerrain.equals(terrain)) {
                    gridPane.getChildren().remove(node);
                    break;
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBConnection dbConnection = new DBConnection();
        gridPane.setMinSize(1000, 400);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.setHgap(10);




        loadMenuInTable();


    }





    public void supprimer(ActionEvent actionEvent) {
        Terrain terrainSelectionne = table.getSelectionModel().getSelectedItem();

        if (terrainSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un terrain à supprimer.");
            alert.show();
            return;
        }

        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("Confirmation");
        alert2.setHeaderText("Voulez-vous supprimer ce terrain ?");
        Optional<ButtonType> result = alert2.showAndWait();
        if (result.get() == ButtonType.OK) {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            TerrainController rs = new TerrainController(connection);
            rs.delete(terrainSelectionne);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Supprimer");
            alert.setHeaderText(null);
            alert.setContentText("Terrain supprimé avec succès!");
            alert.show();


            loadMenuInTable();


        } else {
            alert2.close();
        }
    }


    public void update(ActionEvent actionEvent) throws IOException {


        Terrain terrainSelectionne = table.getSelectionModel().getSelectedItem();

        if (terrainSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un terrain à mettre à jour.");
            alert.show();
            return;


        } else {

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("updateTerrain.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            updateterraincontrolleur updateTerrainController = fxmlLoader.getController();
            updateTerrainController.setTerrainSelectionne(terrainSelectionne);

            Stage primaryStage = (Stage) updatetn.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("go");
            primaryStage.centerOnScreen();


        }
    }




    private void redirectToUpdateScreen(Terrain terrain) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("updateTerrain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        updateterraincontrolleur updateTerrainController = fxmlLoader.getController();
        updateTerrainController.setTerrainSelectionne(terrain);

        Stage primaryStage = (Stage) updatetn.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("go");
        primaryStage.centerOnScreen();
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
    private Button logout;


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
