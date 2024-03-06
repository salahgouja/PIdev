package sample.pidevjava.controller;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class JavaMailUtil {

    public static void sendMail(String recipient, String messageContent, String attachmentFilePath) throws Exception {
        // Session object
        System.out.println("Preparing...");
        Properties props = new Properties();
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        String user = "salahgouja11@gmail.com";
        String password = "urxk fsxx ddkd fboy";

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

        Message message = prepareMessage(session, user, recipient, messageContent, attachmentFilePath);
        Transport.send(message);
        System.out.println("Sent successfully...");
    }

    private static Message prepareMessage(Session session, String user, String recipient, String messageContent, String attachmentFilePath) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("NOTIFICATION APPOINTMENT !!!");

            // Create the message body part for the email
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messageContent); // Set the message content dynamically

            // Create Multipart object and add the message body part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Attach the file if the attachmentFilePath is not null
            if (attachmentFilePath != null) {
                // Create the attachment body part for the PDF file
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.attachFile(attachmentFilePath); // Attach the PDF file dynamically

                // Add the attachment body part to the Multipart object
                multipart.addBodyPart(attachmentBodyPart);
            }

            // Set the content of the message to the Multipart object
            message.setContent(multipart);

            return message;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
