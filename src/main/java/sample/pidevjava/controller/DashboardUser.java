package sample.pidevjava.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import sample.pidevjava.model.User;
import sample.pidevjava.model.UserRole;
import sample.pidevjava.validator.UserValidator;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class DashboardUser implements Initializable {

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
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private Button editbutton;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<User> mylistview;

    @FXML
    private Button uploadButton;


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
    void addUser(ActionEvent event) {
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

        User user = new User(firstname, lastname, email, phone, hashedPass, role);
        UserController userController = new UserController();

            userController.add(user);



        // Refresh the ListView with the updated list of users
        mylistview.getItems().clear();
        mylistview.getItems().addAll(userController.getAllUsers());

    }


    @FXML
    void chooseAndUploadImage(ActionEvent event) {
        // Use a FileChooser to allow the user to choose an image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Display the selected image in the ImageView
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);

            // Save the image file to a folder on the server
            // You can use the Files.copy() method to copy the file to a folder on the server
        }

    }

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
    void updeteUser(ActionEvent event) {
        User selectedUser = (User) mylistview.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String firstname = FirstnameField.getText();
            String lastname = LastnameField.getText();
            String phone = PhoneField.getText();
            String email = EmailField.getText();
            String pass = PasswordField.getText();
            String role = String.valueOf(RoleFieldchoise.getValue());

            selectedUser.setFirstname(firstname);
            selectedUser.setLastname(lastname);
            selectedUser.setPhone(phone);
            selectedUser.setEmail(email);
            selectedUser.setPassword(pass);
            selectedUser.setRole(role);

            UserController userController = new UserController();

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




}
