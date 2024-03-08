package sample.pidevjava.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.pidevjava.model.User;
import sample.pidevjava.model.UserRole;
import sample.pidevjava.validator.UserValidator;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import javax.imageio.ImageIO;

public class DashboardAdmin implements Initializable {
    @FXML
    private JFXButton profile;
    @FXML
    private ImageView imageView;
    private Stage primaryStage;
    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;
    @FXML
    private JFXButton logout;
    @FXML
    private TextField searchField;
    @FXML
    private Label lblLastnameField;
    @FXML
    private Label lblPasswordConfirmField;
    @FXML
    private Label lblPasswordField;
    @FXML
    private Label lblFirstnameField;
    @FXML
    private Label lblPhoneField;
    @FXML
    private Label lblEmailField;
    @FXML
    private TextField EmailField;

    @FXML
    private TextField FirstnameField;

    @FXML
    private TextField LastnameField;

    @FXML
    private TextField PasswordConfirmField;

    @FXML
    private TextField PasswordField;

    @FXML
    private TextField PhoneField;

    @FXML
    private ChoiceBox<UserRole> RoleFieldchoise;

    @FXML
    private ListView<User> mylistview;


    private User user;

    public void setUser(User user) {
        this.user = user;
    }


    public class UserCellFactory implements Callback<ListView<User>, ListCell<User>> {
        @Override
        public ListCell<User> call(ListView<User> param) {
            return new ListCell<User>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);
                    if (empty || user == null) {
                        setText(null);
                    } else {
                        setText(user.getFirstname() + "        " + user.getLastname()+"        "+user.getEmail()+"        "+user.getRole());
                    }
                }
            };
        }
    }

    @FXML
    void addUser(ActionEvent event) throws IOException {
        String firstname = FirstnameField.getText();
        String lastname = LastnameField.getText();
        String phone = PhoneField.getText();
        String email = EmailField.getText();
        String pass = PasswordField.getText();
        String newpasswordconfirm = PasswordConfirmField.getText();
        String role = String.valueOf(RoleFieldchoise.getValue());

        boolean isValid = true;

        if (!UserValidator.isValidName(firstname)) {
            FirstnameField.requestFocus();
            isValid = false;
            System.out.println("Invalid first name");
        }

        if (!UserValidator.isValidName(lastname)) {
            LastnameField.requestFocus();
            isValid = false;
            System.out.println("Invalid last name");
        }

        if (!UserValidator.isValidEmail(email)) {
            lblEmailField.setVisible(true);
            EmailField.requestFocus();
            isValid = false;
            System.out.println("Invalid email");
        } else {
            lblEmailField.setVisible(false);
        }

        if (!UserValidator.isValidPhone(phone)) {
            lblPhoneField.setVisible(true);
            PhoneField.requestFocus();
            isValid = false;
            System.out.println("Invalid phone number");
        } else {
            lblPhoneField.setVisible(false);
        }

        if (!isValid) {
            System.out.println("Validation failed, user not added");
            return;
        }


        if (!pass.equals(newpasswordconfirm)) {
            lblPasswordField.setVisible(true);
            lblPasswordConfirmField.setVisible(true);
            PasswordField.requestFocus();
            return;
        } else {
            lblPasswordField.setVisible(false);
            lblPasswordConfirmField.setVisible(false);
        }

        // Hash the password
        String hashedPass = HashPasswordController.hashPassword(pass);

        Image image = imageView.getImage();
        File imageFile = new File("temp_image.png"); // Provide a temporary file name or adjust as needed

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





        User user = new User(firstname, lastname, email, phone, hashedPass, role,base64Image);
        UserController userController = new UserController();

        userController.add(user);



        // Refresh the ListView with the updated list of users
        mylistview.getItems().clear();
        mylistview.getItems().addAll(userController.getAllUsers());

    }

    /********************************image uplode*************************************/
    @FXML
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



                        /* ------------------------------------- */
    @FXML
    void removeUser(ActionEvent event) {
        User selectedUser = (User) mylistview.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            UserController userController = new UserController();
            userController.delete(selectedUser);

            // Refresh the ListView with the updated list of users
            mylistview.getItems().clear();
            mylistview.getItems().addAll(userController.getAllUsers());
        }

    }

    @FXML
    void updateUser(ActionEvent event) throws IOException {
        System.out.println("Entered updateUser method");
        User selectedUser = (User) mylistview.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            String firstname = FirstnameField.getText();
            String lastname = LastnameField.getText();
            String phone = PhoneField.getText();
            String email = EmailField.getText();
            String pass = PasswordField.getText();
            String role = String.valueOf(RoleFieldchoise.getValue());
            Image image = imageView.getImage();

            File imageFile = new File("temp_image.png");

            // Save the image to a temporary file
            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", imageFile);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Convert the image file to Base64 encoding

            String base64Image = convertImageToBase64(imageFile);
            //System.out.println("Base64 image: " + base64Image);

            selectedUser.setFirstname(firstname);
            selectedUser.setLastname(lastname);
            selectedUser.setPhone(phone);
            selectedUser.setEmail(email);
            selectedUser.setPassword(pass);
            selectedUser.setRole(role);
            selectedUser.setImage(base64Image);

            UserController userController = new UserController();
            //System.out.println("+++++++++++");

            userController.update(selectedUser);

            // Refresh the ListView with the updated list of users
            mylistview.getItems().clear();
            mylistview.getItems().addAll(userController.getAllUsers());
        }

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize the ChoiceBox with the available roles
        RoleFieldchoise.getItems().addAll(UserRole.values());
        // Initialize the ListView with the list of users
        UserController userController = new UserController();
        mylistview.getItems().addAll(userController.getAllUsers());
        mylistview.setCellFactory(new UserCellFactory());
        lblFirstnameField.setVisible(false);
        lblLastnameField.setVisible(false);
        lblEmailField.setVisible(false);
        lblPasswordConfirmField.setVisible(false);
        lblPasswordField.setVisible(false);
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

 /*       if (user != null) {
            FirstnameLabel.setText(user.getFirstname());
            FastnameLabel.setText(user.getLastname());
            PhoneLabel.setText(user.getPhone());
            EmailLabel.setText(user.getEmail());
        }
*/


    }

    @FXML
    public void handleSearch() {
        String searchQuery = searchField.getText().trim();
        if (searchQuery.isEmpty()) {
            UserController userController = new UserController();
            mylistview.getItems().clear();
            mylistview.getItems().addAll(userController.getAllUsers());
        } else {
            UserController userController = new UserController();
            ObservableList<User> searchResults = userController.searchUsers(searchQuery);
            mylistview.getItems().clear();
            mylistview.getItems().addAll(searchResults);
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
