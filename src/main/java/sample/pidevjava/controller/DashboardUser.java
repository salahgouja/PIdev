package sample.pidevjava.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Random;
import java.util.ResourceBundle;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

import static sample.pidevjava.controller.UserSession.currentUser;


public class DashboardUser implements Initializable {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField PasswordField;

    @FXML
    private TextField PasswordField1;

    @FXML
    private TextField PhoneField;

    @FXML
    private AnchorPane VBoxupdatepwd;

    @FXML
    private Button annuler;

    @FXML
    private Button confirm;

    @FXML
    private Label lblPhoneField;

    @FXML
    private Label lblPhoneField1;


    @FXML
    private Button downloadpdf;
    @FXML
    private VBox VBoxprofile;

    @FXML
    private AnchorPane VBoxupdateprofile;
    @FXML
    private Button uploadButton;
    @FXML
    private ImageView imageView1;
    @FXML
    private TextField FirstnameField1;
    @FXML
    private TextField LastnameField1;
    @FXML
    private TextField PhoneField1;
    @FXML
    private TextField EmailField1;
    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;
    @FXML
    private JFXButton logout;
    @FXML
    private Label lblLastnameField;
    @FXML
    private Label lblFirstnameField;

    @FXML
    private Label lblEmailField;
    @FXML
    private TextField EmailField;

    @FXML
    private TextField FirstnameField;

    @FXML
    private TextField LastnameField;



    @FXML
    private Button editbutton;

    @FXML
    private ImageView imageView;


    private User user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        VBoxupdateprofile.setVisible(true);
        VBoxupdatepwd.setVisible(false);

        lblFirstnameField.setVisible(false);
        lblLastnameField.setVisible(false);
        lblEmailField.setVisible(false);
        lblPhoneField.setVisible(false);

        //sider
        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });
/*-------------------------*/
        User currentUser = UserSession.getCurrentUser();

        if (currentUser != null) {


            // Set the user's information
            FirstnameField1.setText(currentUser.getFirstname());
            LastnameField1.setText(currentUser.getLastname());
            PhoneField1.setText(currentUser.getPhone());
            EmailField1.setText(currentUser.getEmail());
            if (currentUser.getImage() != null) {
                imageView1.setImage(convertBase64ToImage(currentUser.getImage()));
                imageView1.setVisible(true);
            }



            // Set the user's information for editing
            FirstnameField.setText(currentUser.getFirstname());
            LastnameField.setText(currentUser.getLastname());
            PhoneField.setText(currentUser.getPhone());
            EmailField.setText(currentUser.getEmail());
            if (currentUser.getImage() != null) {
                imageView.setImage(convertBase64ToImage(currentUser.getImage()));
                imageView.setVisible(true);
            }


        }


    }

    public void setUser(User user) {
        this.user = user;
    }








    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    private void logout(ActionEvent actionEvent) {

        // Get the current stage
        Stage stage = (Stage) logout.getScene().getWindow();

        try {
            // Create a new FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/LoginForm.fxml"));

            // Load the LoginForm.fxml filea
            Parent root = loader.load();

            // Create a new Scene with the loaded LoginForm.fxml file
            Scene scene = new Scene(root);

            // Set the newly created Scene as the scene for the current stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void deconnecterButtonOnClick(ActionEvent event){
        // Get the current stage
        Stage stage = (Stage) logout.getScene().getWindow();

        try {
            // Create a new FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/pidevjava/LoginForm.fxml"));

            // Load the LoginForm.fxml filea
            Parent root = loader.load();

            // Create a new Scene with the loaded LoginForm.fxml file
            Scene scene = new Scene(root);

            // Set the newly created Scene as the scene for the current stage
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void modifierMdpOnClick(ActionEvent event) {
        VBoxupdateprofile.setVisible(false);
        VBoxupdatepwd.setVisible(true);

    }
    @FXML
    private void profileButtonOnClick(ActionEvent event){
        VBoxupdateprofile.setVisible(true);
        VBoxupdatepwd.setVisible(false);
    }

    @FXML
    public void downloadpdf(ActionEvent actionEvent) {
        generatePDF();

    }

    public void generatePDF() {
        try {
            // Create a new PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream for the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set the font and font size
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            // Begin the text block
            contentStream.beginText();

            // Add the content of the VBoxprofile pane to the PDF
            contentStream.newLineAtOffset(100, 700); // Set the starting position
            contentStream.showText("My Profile");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("First Name: " + FirstnameField1.getText());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Last Name: " + LastnameField1.getText());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Phone: " + PhoneField1.getText());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Email: " + EmailField1.getText());

            // End the text block
            contentStream.endText();

            // Retrieve the image from the database as a base64 encoded string
            String imageBase64String = currentUser.getImage();

            // Convert the base64 string to a byte array
            byte[] imageBytes = Base64.getDecoder().decode(imageBase64String);

            // Create the PDImageXObject from the byte array
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "image");

            // Scale the image to make it smaller
            float scale = 0.2f; // Scale factor
            float imageWidth = pdImage.getWidth() * scale;
            float imageHeight = pdImage.getHeight() * scale;
            float imageX = (page.getMediaBox().getWidth() - imageWidth) / 2;
            float imageY = page.getMediaBox().getHeight() - imageHeight - 50;
            contentStream.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);

            // Draw a line between the fields
            float lineY = imageY - 50;
            contentStream.moveTo(100, lineY);
            contentStream.lineTo(page.getMediaBox().getWidth() - 100, lineY);
            contentStream.stroke();

            // Close the content stream
            contentStream.close();

            // Save the document to a file
            document.save(new File("profile.pdf"));

            // Close the document
            document.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "PDF generated successfully.", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to generate PDF.", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }




    @FXML
    void updateUser(ActionEvent event) throws IOException {
        User currentUser = UserSession.getCurrentUser();

        if (currentUser != null) {
            // Update the user's information
            currentUser.setFirstname(FirstnameField.getText());
            currentUser.setLastname(LastnameField.getText());
            currentUser.setPhone(PhoneField.getText());
            currentUser.setEmail(EmailField.getText());
            Image image = imageView.getImage();
            File imageFile = new File("temp_image.png"); // Provide a temporary file name or adjust as needed

            // Save the image to a temporary file
            try {
                if (image!=null){
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", imageFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //    showAlert(Alert.AlertType.ERROR, "Error", "Failed to save the image.");
                return;
            }
            // Convert the image file to Base64 encoding
            String base64Image = convertImageToBase64(imageFile);
            // Update the user's information in the database
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET firstname = ?, lastname = ?, phone = ?, email = ?,image=? WHERE id = ?");
                preparedStatement.setString(1, currentUser.getFirstname());
                preparedStatement.setString(2, currentUser.getLastname());
                preparedStatement.setString(3, currentUser.getPhone());
                preparedStatement.setString(4, currentUser.getEmail());
                preparedStatement.setString(5, base64Image);
                preparedStatement.setInt(6, currentUser.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
            // Update the user's image
            currentUser.setImage(base64Image);

            // Update the displayed user's information
            FirstnameField1.setText(currentUser.getFirstname());
            LastnameField1.setText(currentUser.getLastname());
            PhoneField1.setText(currentUser.getPhone());
            EmailField1.setText(currentUser.getEmail());
            imageView1.setImage(convertBase64ToImage(currentUser.getImage()));
        }
    }


    /********************************image uplode*************************************/
    @FXML
    public void chooseAndUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Call method to handle file selection
            handleSelectedFile(selectedFile);
        }
    }


    private void handleSelectedFile(File file) {
        System.out.println("handleSelectedFile called");

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
        System.out.println("convertBase64ToImage called");


        try (FileInputStream imageInFile = new FileInputStream(file)) {
            byte[] imageData = new byte[(int) file.length()];
            imageInFile.read(imageData);
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            System.out.println(base64Image); // Print the base64 string
            return base64Image;
        }
    }



}

