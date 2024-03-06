package sample.pidevjava.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Text;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Appeloffre;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.*;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import java.awt.Desktop;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import sample.pidevjava.model.Terrain;


public class Dashbordappelcontrolleur  implements Initializable {

    @FXML
    private Button ajouterBtn;

    private VBox selectedCard;

    Appeloffre can;


    @FXML
    private TableColumn<Appeloffre, String> cvCL;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<Appeloffre, String> nomCL;

    @FXML
    private TableColumn<Appeloffre, String> numéroCL;

    @FXML
    private TableColumn<Appeloffre, Float> prixCL;

    @FXML
    private TableColumn<Appeloffre, String> prénomCL;


    @FXML
    private TableColumn<Appeloffre, String> cvFilterCL;
    @FXML
    private TableColumn<Appeloffre, Integer> score;


    @FXML
    private TableView<Appeloffre> table;

    VBox v;


    @FXML
    private GridPane gridPane;


    @FXML
    private VBox motsScoresVBox;

    @FXML
    private TextField motTextField;

    @FXML
    private TextField scoreTextField;

    private Map<String, Integer> motsEtScores = new HashMap<>();

    private List<Integer> allScores = new ArrayList<>();
    public static final String ACCOUNT_SID = "ACb08735924618d2d7f1aa5b507aaa4c97";
    public static final String AUTH_TOKEN = "4d6518607f40221708130fb8fea5aed4";


    public void naviagteToajouter(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("formulaireappel.fxml"));
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
                AppeloffreControlleur rm = new AppeloffreControlleur(connection);
                List<Appeloffre> T = rm.getAll();
                ObservableList<Appeloffre> resData = FXCollections.observableArrayList(T);
                prénomCL.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                nomCL.setCellValueFactory(new PropertyValueFactory<>("nom"));
                numéroCL.setCellValueFactory(new PropertyValueFactory<>("numero"));
                prixCL.setCellValueFactory(new PropertyValueFactory<>("prix"));

                cvCL.setCellValueFactory(new PropertyValueFactory<>("cv"));

                cvFilterCL.setCellValueFactory(cellData -> {
                    Appeloffre appel = cellData.getValue();
                    String cheminPDF = appel.getCv();
                    System.out.println(cheminPDF);
                    return new SimpleStringProperty(filtrer(cheminPDF));

                });




                score.setCellValueFactory(new PropertyValueFactory<>("tempScore"));


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
                AppeloffreControlleur appel = new AppeloffreControlleur(connection);
                List<Appeloffre> terrains = appel.getAll();
                System.out.println(terrains);


                if (gridPane != null) {
                    gridPane.getChildren().clear();
                    // Reste du code
                } else {
                    System.err.println("Erreur : gridPane est null");
                }

                for (int i = 0; i < terrains.size(); i++) {
                    Appeloffre go = terrains.get(i);
                    v = createCarteView(go);


                    gridPane.add(v, 0, i);

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBConnection dbConnection = new DBConnection();
        loadMenuInTable();


    }


    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void accepter(ActionEvent event) {
        Appeloffre appelSelectionne = (Appeloffre) selectedCard.getUserData();
        String numero = appelSelectionne.getNumero();
        System.out.println(numero);
        if (!numero.startsWith("+216")) {
            numero = "+216" + numero;
        }
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + numero),
                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                "mabrouk aalik offre si montassar hamza ").create();

        System.out.println(message.getSid());

        // Affichez un message de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Accepter");
        alert.setHeaderText(null);
        alert.setContentText("Message WhatsApp envoyé avec succès!");
        alert.show();
    }

    public void removeSelectedCard() {
        if (selectedCard != null) {
            // Récupérer les données de la carte sélectionnée
            Appeloffre appelSelectionne = (Appeloffre) selectedCard.getUserData();

            // Supprimer la carte de la base de données ou de toute autre source de données
            // Exemple: Vous devez implémenter la logique de suppression dans la classe AppeloffreControlleur
            // appeloffreControlleur.delete(appelSelectionne);

            // Supprimer la carte du GridPane
            gridPane.getChildren().remove(selectedCard);

            // Désélectionner la carte
            selectedCard = null;

            // Afficher une alerte de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression");
            alert.setHeaderText(null);
            alert.setContentText("La carte a été supprimée avec succès!");
            alert.show();
        } else {
            // Afficher une alerte si aucune carte n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une carte à supprimer.");
            alert.show();
        }
    }


    public void apercu(Appeloffre appelSelectionne) throws IOException {
        if (appelSelectionne != null) {
            String s = appelSelectionne.getCv();

            if (s != null && !s.isEmpty()) {
                try {
                    Desktop.getDesktop().open(new File(s));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une carte pour ouvrir le fichier.");
            alert.show();
        }
    }


    String filtrer(String cheminPDF) {
        try {
            if (cheminPDF != null && !cheminPDF.isEmpty()) {
                File pdfFile = new File(cheminPDF);
                if (pdfFile.exists()) {
                    PDDocument document = PDDocument.load(pdfFile);
                    PDFTextStripper textStripper = new PDFTextStripper();
                    String text = textStripper.getText(document).toLowerCase();
                    document.close();

                    int debutExperience = text.indexOf("experience");
                    if (debutExperience != -1) {
                        String texteApresExperience = text.substring(debutExperience + "experience".length());

                        List<String> motsAExclure = Arrays.asList("éducation", "projets", "langues","certificats ");

                        int indexPremierMot = texteApresExperience.length();
                        for (String mot : motsAExclure) {
                            int indexMot = texteApresExperience.indexOf(mot);
                            if (indexMot != -1 && indexMot < indexPremierMot) {
                                indexPremierMot = indexMot;
                            }
                        }

                        String texteFiltre = texteApresExperience.substring(0, indexPremierMot);
                        return texteFiltre.trim();
                    } else {
                        return "Expérience non trouvé dans le texte.";
                    }
                } else {
                    return "Le fichier PDF n'existe pas.";
                }
            } else {
                return "Chemin du fichier PDF invalide.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors du chargement du fichier PDF.";
        }
    }


    @FXML
    void trii(ActionEvent actionEvent) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            if (connection != null) {
                AppeloffreControlleur appeloffreControlleur = new AppeloffreControlleur(connection);
                List<Appeloffre> appeloffres = appeloffreControlleur.getAll();

                Collections.sort(appeloffres, Comparator.comparingDouble(Appeloffre::getPrix));
                gridPane.getChildren().clear();

                for (int i = 0; i < appeloffres.size(); i++) {
                    Appeloffre go = appeloffres.get(i);
                    VBox v = createCarteView(go);
                    gridPane.add(v, 0, i);
                }


                System.out.println("Liste triée avec succès en fonction du prix.");
                showAlert("Succès", "Succès!", Alert.AlertType.INFORMATION);

            } else {
                System.err.println("Erreur lors de l'établissement de la connexion à la base de données.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterMotScore(ActionEvent event) {
        try {
            String mot = motTextField.getText();
            String scoreStr = scoreTextField.getText();

            if (!mot.isEmpty() && !scoreStr.isEmpty()) {
                int score = Integer.parseInt(scoreStr);

                motsEtScores.put(mot, score);

                motTextField.clear();
                scoreTextField.clear();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validation");
                alert.setHeaderText(null);
                alert.setContentText("Valide !");
                alert.show();
            } else {
                System.err.println("Veuillez remplir les champs Mot et Score.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Score invalide. Assurez-vous d'entrer un nombre entier.");
            e.printStackTrace(); // Imprime la trace de l'exception

            // Afficher une alerte d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de la saisie. Assurez-vous d'entrer un nombre entier pour le score.");
            alert.show();
        }
    }

    public int calculerScore(String cheminCV) {
        int scoreTotal = 0;

        try {
            if (cheminCV != null && !cheminCV.isEmpty()) {
                File pdfFile = new File(cheminCV);
                if (pdfFile.exists()) {
                    PDDocument document = PDDocument.load(pdfFile);
                    PDFTextStripper textStripper = new PDFTextStripper();
                    String texte = textStripper.getText(document).toLowerCase();
                    document.close();

                    for (Map.Entry<String, Integer> entry : motsEtScores.entrySet()) {
                        String motRecherche = entry.getKey().toLowerCase();
                        int scoreMot = entry.getValue();

                        if (texte.contains(motRecherche)) {
                            scoreTotal += scoreMot;
                        }
                    }
                } else {
                    System.err.println("Le fichier PDF n'existe pas : " + cheminCV);
                }
            } else {
                System.err.println("Chemin du fichier PDF invalide.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement du fichier PDF.");
        }

        return scoreTotal;
    }


    public boolean recherche(String motRecherche) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            if (connection != null) {
                AppeloffreControlleur appeloffreControlleur = new AppeloffreControlleur(connection);
                List<Appeloffre> appeloffres = appeloffreControlleur.getAll();

                for (Appeloffre appel : appeloffres) {
                    String cheminCV = appel.getCv();

                    if (cheminCV != null && !cheminCV.isEmpty()) {
                        File pdfFile = new File(cheminCV);
                        if (pdfFile.exists()) {
                            PDDocument document = PDDocument.load(pdfFile);
                            PDFTextStripper textStripper = new PDFTextStripper();
                            String text = textStripper.getText(document);
                            document.close();

                            if (text.contains(motRecherche)) {
                                return true;
                            }
                        } else {
                            System.err.println("Le fichier PDF n'existe pas: " + cheminCV);
                        }
                    } else {
                        System.err.println("Chemin du fichier PDF invalide.");
                    }
                }
            } else {
                System.err.println("Erreur lors de l'établissement de la connexion à la base de données.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement du fichier PDF.");
        }

        return false;
    }

    @FXML
    private void handleCalculButtonAction(ActionEvent event) throws IOException {
        // Clear the existing scores when recalculating
        allScores.clear();

        for (Node node : gridPane.getChildren()) {
            if (node instanceof VBox) {
                VBox card = (VBox) node;
                Appeloffre appel = (Appeloffre) card.getUserData();

                String cheminCV = appel.getCv();

                int scoreTotal = calculerScore(cheminCV);
                System.out.println(scoreTotal);

                allScores.add(scoreTotal);


            }
        }
        System.out.println(allScores);

        loadMenuInTable();
        motsEtScores.clear();
    System.out.println(allScores);

    }




    private VBox createCarteView(Appeloffre appel) throws IOException {
        // Charger le fichier FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/pidevjava/appelview.fxml"));
        VBox v = fxmlLoader.load();
        System.out.println("Chargement du FXML de Carteview réussi.");

        Appelview carteview = fxmlLoader.getController();


        v.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000;");

        carteview.updateNom(appel.getNom());
        carteview.updatePrenom(appel.getPrenom());
        carteview.updatePrix(String.valueOf(appel.getPrix()));
        carteview.updateCV(appel.getCv());
        carteview.updateNumero(appel.getNumero());
        carteview.updateFiltre(filtrer(appel.getCv()));
        if (!allScores.isEmpty()) {
            carteview.updateScore(String.valueOf(allScores.get(0)));
            System.out.println(allScores.get(0));
            // Remove the first score from the list since it's used
            allScores.remove(0);
        }

        v.setOnMouseClicked(event -> {
            handleCardClick(v, appel);
        });
        v.setUserData(appel);


        System.out.println("Chargement du FXML de Carteview réussi.");

        return v;
    }

    private Appeloffre handleCardClick(VBox card, Appeloffre appel) {
        if (selectedCard != null) {
            selectedCard.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000;");
        }

        card.setStyle("-fx-background-color: #a0a0a0; -fx-border-color: #000000;");

        selectedCard = card;

        return appel;
    }


    public void apercu(ActionEvent actionEvent) {


        Appeloffre appelSelectionne = (Appeloffre) selectedCard.getUserData();

        if (appelSelectionne != null) {
            String s = appelSelectionne.getCv();

            if (s != null && !s.isEmpty()) {
                try {
                    Desktop.getDesktop().open(new File(s));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une ligne pour ouvrir le fichier.");
            alert.show();
        }
    }


    @FXML
    void fil(ActionEvent actionEvent) {
        Appeloffre appelSelectionne = (Appeloffre) selectedCard.getUserData();

        if (appelSelectionne != null) {
            String s = appelSelectionne.getCv();

            if (s != null && !s.isEmpty()) {
                // Filtrer le CV
                String filtre = filtrer(s);
                System.out.println(filtre);


                // Mettez à jour le champ de texte dans la carte sélectionnée

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une ligne pour ouvrir le fichier.");
            alert.show();
        }
    }

    private void updateFiltreDansCarte(String filtre) {
        if (selectedCard != null) {
            // Assurez-vous que le premier enfant est un AnchorPane
            if (!selectedCard.getChildren().isEmpty() && selectedCard.getChildren().get(0) instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) selectedCard.getChildren().get(0);

                // Obtenez le contrôleur de la carte (Appelview)
                Appelview appelview = (Appelview) anchorPane.getProperties().get("controller");

                if (appelview != null) {
                    // Mettez à jour le champ de texte dans la carte
                    appelview.updateFiltre("gfgnfgnf");

                } else {
                    System.err.println("appelview est null. Assurez-vous qu'il est correctement initialisé.");
                }
            }
        }
    }




    private void removeCardFromGrid(Appeloffre appel) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof VBox) {
                VBox card = (VBox) node;
                Appeloffre cardTerrain = (Appeloffre) card.getUserData();
                if (cardTerrain != null && cardTerrain.equals(appel)) {
                    gridPane.getChildren().remove(node);
                    break;
                }
            }
        }
    }

    public void supprimer(ActionEvent actionEvent) {
        Appeloffre appelSelectionne = (Appeloffre) selectedCard.getUserData();

        if (appelSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un appel à supprimer.");
            alert.show();
            return;
        }

        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("Confirmation");
        alert2.setHeaderText("Voulez-vous supprimer ce appel ?");
        Optional<ButtonType> result = alert2.showAndWait();
        if (result.get() == ButtonType.OK) {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            AppeloffreControlleur rs = new AppeloffreControlleur(connection);
            rs.delete(appelSelectionne);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Supprimer");
            alert.setHeaderText(null);
            alert.setContentText("Terrain supprimé avec succès!");
            alert.show();


            removeCardFromGrid(appelSelectionne);

        } else {
            alert2.close();
        }
    }


}





