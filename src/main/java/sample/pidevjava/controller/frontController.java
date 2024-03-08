package sample.pidevjava.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import sample.pidevjava.model.Partenaire;
import sample.pidevjava.model.Reponse;
import sample.pidevjava.controller.PartenaireService;
import sample.pidevjava.controller.ReponseService;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class frontController implements Initializable {

    @FXML
    private HBox HM;

    @FXML
    private HBox HQ;

    @FXML
    private HBox HT;

    @FXML
    private TextField email;

    @FXML
    private TextField montant;

    @FXML
    private Label montant1;

    @FXML
    private ListView<Partenaire> mylistview;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField quantite;

    @FXML
    private Label quantite2;

    @FXML
    private Label quantite3;

    @FXML
    private TextField tel;

    @FXML
    private ComboBox<String> typeP;

    @FXML
    private TextField typee;

    @FXML
    private Label typee1;

    @FXML
    private Label typep;

    int id_connected_user = 1;
    ArrayList<Partenaire> parterres = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HM.setVisible(false);
        HT.setVisible(false);
        HQ.setVisible(false);
        ObservableList<String> list = FXCollections.observableArrayList("Equipement","Financement"," ");
        typeP.setItems(list);
        typeP.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ("Financement".equals(newValue)) {
                    HM.setVisible(true);
                    HT.setVisible(false);
                    HQ.setVisible(false);
                } else if ("Equipement".equals(newValue)) {
                    HM.setVisible(false);
                    HT.setVisible(true);
                    HQ.setVisible(true);
                }
            }
        });

        PartenaireService par = new PartenaireService();
        try {
            parterres =  par.recuperer(id_connected_user);
            setEventList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void refresh() {
        try {
            PartenaireService par = new PartenaireService();
            parterres = par.recuperer(id_connected_user);
            ObservableList<Partenaire> observableList = FXCollections.observableArrayList(parterres);
            mylistview.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void setEventList() {
        ObservableList<Partenaire> observableList = FXCollections.observableArrayList();

        // Filter partners with etat == 1
        List<Partenaire> filteredPartenaires = parterres.stream()
                .filter(partenaire -> partenaire.getEtat() == 1)
                .collect(Collectors.toList());

        observableList.addAll(parterres);

        mylistview.setItems(observableList);

        mylistview.setCellFactory(param -> new ListCell<Partenaire>() {
            @Override
            protected void updateItem(Partenaire item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/CardFront.fxml"));
                        Node node = loader.load();
                        CardFrontController controller = loader.getController();
                        ReponseService repp = new ReponseService();
                        Reponse r = repp.rechercherParIdPar(item.getId_partenaire());

                        controller.setEventData(item,r);
                        setGraphic(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        PartenaireService par = new PartenaireService();

        String comb = typeP.getValue();
        String nomText = nom.getText();
        String prenomText = prenom.getText();
        String telText = tel.getText();
        String emailText = email.getText();
        String montantText = montant.getText();
        String typeeText = typee.getText();
        String quantiteText = quantite.getText();

        // Check if any of the required fields are empty
        if (comb == null || nomText.isEmpty() || prenomText.isEmpty() || telText.isEmpty() || emailText.isEmpty()) {
            // Show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields", "Please fill in all required fields.");
            return;
        }

        // Validate the telephone number
        if (!telText.matches("\\d{8}")) {
            // Show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Telephone Number", "Please enter a valid 8-digit telephone number.");
            return;
        }

        // Validate the email address
        if (!isValidEmailAddress(emailText)) {
            // Show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Email Address", "Please enter a valid email address.");
            return;
        }

        // Validate the montant field if "Financement" is selected
        if (Objects.equals(comb, "Financement") && (montantText == null || montantText.isEmpty())) {
            // Show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Montant", "Please enter a value for Montant.");
            return;
        }

        // Validate the typee and quantite fields if "Equipement" is selected
        if (Objects.equals(comb, "Equipement") && (typeeText == null || typeeText.isEmpty() || quantiteText == null || quantiteText.isEmpty())) {
            // Show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Type or Quantite", "Please enter values for Type and Quantite.");
            return;
        }

        // Add the partner
        if (Objects.equals(comb, "Financement")) {
            Partenaire p = new Partenaire(nomText, prenomText, Integer.parseInt(telText), emailText, comb, Integer.parseInt(montantText), 0, id_connected_user);
            par.ajouter(p);
            email(p);
            refresh();
        } else {
            Partenaire p = new Partenaire(nomText, prenomText, Integer.parseInt(telText), emailText, comb, typeeText, Integer.parseInt(quantiteText), 0, id_connected_user);
            par.ajouter(p);
            email(p);
            refresh();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private boolean isValidEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    void email(Partenaire p){
        // Set the SMTP host and port for sending the email
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "arco.sc0156@gmail.com";
        String password = "hghseksuroiqviag";

        // Set the properties for the email session
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true"); // Enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS encryption

        // Create a new email session using the specified properties
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a new email message
            Message msg = new MimeMessage(session);

            // Set the "From" address for the email
            // msg.setFrom(new InternetAddress("ahmed.benabid2503@gmail.com"));

            // Add the "To" address for the email (including the recipient's name)
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(p.getEmail()));

            // Set the subject and body text for the email
            msg.setSubject("Demande partenariat");
            msg.setText("Bonjour MR/Mme ,vous pouvez voir un nouveau demande à partire de "+p.getNom()+" "+p.getPrenom()+" de type: "+p.getType_partenaire()+".");

            // Create an alert to notify the user that the email was sent successfully

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation d'envoie");
            alert.setHeaderText("Voulez-vous envoyez ce mail?");
            alert.setContentText("Cette action est requise.");

            // Show the confirmation dialog and wait for the user's response
            Optional<ButtonType> resultt = alert.showAndWait();

            // Send the email

            if (resultt.get() == ButtonType.OK) {
                System.out.println("En cours d'envoie...");
                Transport.send(msg);
                System.out.println("Envoyé avec succès !");

            } else {
                // Close the dialog and do nothing
                alert.close();
                System.out.println("Echec d'envoie!");
            }


            // Print a message to the console to indicate that the email was sent successfully





        } catch (AddressException e) {
            // Create an alert to notify the user that there was an error with the email address
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
    @FXML
    void selectItem(MouseEvent event) {
        // Implement your item selection logic here
    }
}
