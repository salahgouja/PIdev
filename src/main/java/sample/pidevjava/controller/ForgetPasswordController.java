package sample.pidevjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample.pidevjava.Main;
import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.User;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;


public class ForgetPasswordController {
    @FXML
    private Button sendVerificationCode;
    @FXML

    private TextField validcode;
    @FXML

    private BorderPane root5;
    @FXML
    private TextField emailField;

    @FXML
    private ImageView annuler;

    public void initialize() {

        addExpirationDateColumn();
        addVerificationCodeColumn();
    }
    private void addExpirationDateColumn() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("ALTER TABLE USER ADD COLUMN expiration_date TIMESTAMP");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }
    private void addVerificationCodeColumn() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "USER", "verification_code");

            if (!columns.next()) {
                PreparedStatement statement = connection.prepareStatement("ALTER TABLE USER ADD COLUMN verification_code VARCHAR(255)");
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }


    public void sendVerificationCode(ActionEvent event) throws IOException {
        String email = emailField.getText();
        if (emailExists(email)) {
            String code = generateRandomCode(7);

            storeVerificationCode(email, code);
            sendVerificationCodeEmail(email, code);
            // Inform the user that the code has been sent
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Code Sent");
            alert.setHeaderText(null);
            alert.setContentText("A verification code has been sent to your email address.");
            alert.showAndWait();
/*
            // Load the EnterCode.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EnterCode.fxml"));
            Parent root5= loader.load();
            // Set the EnterCodeController as the controller for the EnterCode.fxml file
            EnterCodeController enterCodeController = loader.getController();
            enterCodeController.setEmail(email);
            Scene scene = new Scene(root5);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

*/
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EnterCode.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage primarystage = (Stage) sendVerificationCode.getScene().getWindow();
            primarystage.setScene(scene);
            primarystage.setTitle("EnterCode");
            primarystage.centerOnScreen();
        } else {
            // Inform the user that the email does not exist
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email Not Found");
            alert.setHeaderText(null);
            alert.setContentText("The email address you entered does not exist.");
            alert.showAndWait();
        }


    }

    public void resetPassword(ActionEvent event) {
        // Check if the verification code is expired
        if (isVerificationCodeExpired(emailField.getText())) {
            // Inform the user that the verification code is expired
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Verification Code Expired");
            alert.setHeaderText(null);
            alert.setContentText("The verification code has expired. Please request a new code.");
            alert.showAndWait();
            return;
        }

        String email = emailField.getText();
        String code = validcode.getText();

        // Check if the verification code is valid
        if (!isVerificationCodeValid(email, code)) {
            // Inform the user that the verification code is incorrect
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect Verification Code");
            alert.setHeaderText(null);
            alert.setContentText("The verification code you entered is incorrect.");
            alert.showAndWait();
            return;
        }

        // Prompt the user to enter a new password
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reset Password");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your new password:");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String newPassword = HashPasswordController.hashPassword(result.get());
            // Reset the user's password
            resetUserPassword(email, newPassword);

            // Inform the user that the password has been reset
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Reset");
            alert.setHeaderText(null);
            alert.setContentText("Your password has been successfully reset.");
            alert.showAndWait();
        }
    }

    private void resetUserPassword(String email, String newPassword) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE USER SET password = ? WHERE email = ?");
            statement.setString(1, newPassword);
            statement.setString(2, email);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }
    private boolean isVerificationCodeValid(String email, String code) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT verification_code FROM USER WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedCode = resultSet.getString("verification_code");
                return storedCode.equals(code);
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return false; // Verification code is not valid if there is an error
    }



    private boolean emailExists(String email) {
        UserController userController = new UserController();
        User user = userController.getUserByEmail(email);
        return user != null;
    }


    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }


    private void storeVerificationCode(String email, String code) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE USER SET verification_code = ?, expiration_date = ? WHERE email = ?");
            statement.setString(1, code);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusSeconds(60))); // Set expiration date from now
            statement.setString(3, email);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void sendVerificationCodeEmail(String email, String code) throws RuntimeException {
        String host = "smtp.gmail.com";
        int port = 587;
        String username = "salahgouja11@gmail.com";
        String password = "urxk fsxx ddkd fboy";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        //props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Set the SSL/TLS protocol version


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Verification Code");
            message.setText("Your verification code is: " + code);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }



    private boolean isVerificationCodeExpired(String email) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT expiration_date FROM USER WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Timestamp expirationDate = resultSet.getTimestamp("expiration_date");
                return expirationDate.toLocalDateTime().isBefore(LocalDateTime.now());
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return true; // Assume verification code is expired if there is an error
    }


    public void back(ActionEvent actionEvent) {
    }
}