package customUtils;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.entities.EmailObject;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


/**
 * References :
 * [1] sendgrid/sendgrid-java
 *  sendgrid/sendgrid-java (2020). Available at: https://github.com/sendgrid/sendgrid-java#installation (Accessed: 1 October 2020).
 */
public class EmailUtil {

    public static void sendEmail(EmailObject email) throws MessagingException, UnirestException {


        System.out.println(Unirest.post("https://api.pepipost.com/v5/mail/send")
                .header("api_key", "7643511c0216843e5c2ee340a035d49a")
                .header("content-type", "application/json")
                .body("{\"from\":{\"email\":\"chaudhariyogesh20@pepisandbox.com\",\"name\":\"chaudhariyogesh20\"},\"subject\":\""+email.getSubject()+"\",\"content\":[{\"type\":\"html\",\"value\":\""+email.getMatter()+"\"}],\"personalizations\":[{\"to\":[{\"email\":\"chaudhari.yogesh20@gmail.com\",\"name\":\"Lionel Messi\"}]}]}")
                .asString());

        String body = "{\"from\":{\"email\":\"chaudhariyogesh20@pepisandbox.com\",\"name\":\"chaudhariyogesh20\"},\"subject\":\""+email.getSubject()+"\",\"content\":[{\"type\":\"html\",\"value\":\""+email.getMatter()+"\"}],\"personalizations\":[{\"to\":[{\"email\":\""+email.getToEmail()+"\",\"name\":\""+email.getToName()+"\"}]}]}";
        System.out.println(body);
        return;

//        String host="smtp.pepipost.com";
//        final String user="chaudhariyogesh20";
//        final String fromEmail = "chaudhariyogesh20@pepisandbox.com";
//        final String password="chaudhariyogesh20_7643511c0216843e5c2ee340a035d49a";
//
//        String to="chaudhari.yogesh20@gmail.com";
//
//
//
//
//        // sets SMTP server properties
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", host);
//        properties.put("mail.smtp.port", 25);
//        properties.put("mail.smtp.auth", "true");
//        //properties.put("mail.smtp.starttls.enable", "true");
//// *** BEGIN CHANGE
//        properties.put("mail.smtp.user", user);
//
//        // creates a new session, no Authenticator (will connect() later)
//        Session session = Session.getDefaultInstance(properties);
//// *** END CHANGE
//
//        // creates a new e-mail message
//        Message msg = new MimeMessage(session);
//
//        msg.setFrom(new InternetAddress(fromEmail));
//        InternetAddress[] toAddresses = { new InternetAddress(to) };
//        msg.setRecipients(Message.RecipientType.TO, toAddresses);
//        msg.setSubject("Test Mail");
//        msg.setSentDate(new Date());
//        // set plain text message
//        msg.setText("Test Dummy Content");
//
//// *** BEGIN CHANGE
//        // sends the e-mail
//        Transport t = session.getTransport("smtp");
//        t.connect(user, password);
//        t.sendMessage(msg, msg.getAllRecipients());
//        t.close();
//// *** END CHANGE

    }

}
