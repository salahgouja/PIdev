package sample.pidevjava.controller;



import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.*;
import java.util.Properties;

public class JavaMailUtil {

    public static void sendMail(String recepient) throws Exception{
        // session object
        System.out.println("prepring.....");
        Properties props = new Properties();
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");

        String user="khdhia28@gmail.com";
        String password="dhaw0304@";

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        Message message =prepareMessage(session,user,recepient);
        Transport.send (message);
        System.out.println("sended.....");

    }
    private static Message prepareMessage(Session session , String user ,String recepient){
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject(" NOTIFICATION APPOINTEMENTT !!! ");
            message.setText("test");
            return message;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
