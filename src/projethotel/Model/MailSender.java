package projethotel.Model;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender{

    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "inscriptionhotelo@gmail.com";
    static final String FROMNAME = "HOTELO";

    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "AKIA3LGUUOHWMKDTEP6E";

    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "BJOklYi23Tmi2bdgHL5vDVSFzrQmFvaxbvy5MABI01ZZ";

    // Amazon SES SMTP host name. This example uses the USA Ouest (Oregon) region.
    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "email-smtp.eu-central-1.amazonaws.com";

    // The port you will connect to on the Amazon SES SMTP endpoint. 
    static final int PORT = 587;

    public static void SendMailEmploye(String TO, String password) throws MessagingException, UnsupportedEncodingException{
        String SUBJECT = "Inscription HOTELO - Nouvel Employé";
        String BODY = String.join(
            System.getProperty("line.separator"),
            "<h1>Bienvenue en tant que nouveau membre HOTELO",
            "<p style= 'font-size: 15px'>Veuillez utiliser le mot de passe temporaire ci-joint que vous devrez changer lors de votre première connexion.\n",
            "A bientôt sur HOTELO.\n\n", 
            password
        );
        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY, "text/html;charset=utf-8");

        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        } finally {
            // Close and terminate the connection.
            transport.close();
        }
    }
    
     public static void SendMailClient(String TO, String password) throws MessagingException, UnsupportedEncodingException{
        String SUBJECT = "Inscription HOTELO - Nouveau Client";
        String BODY = String.join(
            System.getProperty("line.separator"),
            "<h1>Bienvenue en tant que nouveau client HOTELO",
            "<p style= 'font-size: 15px'>Veuillez utiliser le mot de passe temporaire ci-joint que vous devrez changer lors de votre première connexion.\n",
            "A bientôt sur HOTELO.\n\n", 
            password
        );
        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY, "text/html;charset=utf-8");

        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        } finally {
            // Close and terminate the connection.
            transport.close();
        }
    }
     
     public static void SendMailChangement(String TO) throws MessagingException, UnsupportedEncodingException{
        String SUBJECT = "HOTELO - Changement de Mail";
        String BODY = String.join(
            System.getProperty("line.separator"),
            "<h1>Cher client Hotelo",
            "<p style= 'font-size: 15px'>Votre ancien mail a changé pour celui-ci.\n",
            "A bientôt sur HOTELO.\n\n"
        );
        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY, "text/html;charset=utf-8");

        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        } finally {
            // Close and terminate the connection.
            transport.close();
        }
    }
}
