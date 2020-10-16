package customUtils;


import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.entities.EmailObject;
import javax.mail.MessagingException;

/**
 * References :
 * [1] sendgrid/sendgrid-java
 *  sendgrid/sendgrid-java (2020). Available at: https://github.com/sendgrid/sendgrid-java#installation (Accessed: 1 October 2020).
 */
public class EmailUtil {

    public static void sendEmail(EmailObject email) throws MessagingException, UnirestException, UnirestException {

        Unirest.post("https://api.pepipost.com/v5/mail/send")
                .header("api_key", "7643511c0216843e5c2ee340a035d49a")
                .header("content-type", "application/json")
                .body("{\"from\":{\"email\":\"chaudhariyogesh20@pepisandbox.com\",\"name\":\"chaudhariyogesh20\"},\"subject\":\""+email.getSubject()+"\",\"content\":[{\"type\":\"html\",\"value\":\""+email.getMatter()+"\"}],\"personalizations\":[{\"to\":[{\"email\":\"chaudhari.yogesh20@gmail.com\",\"name\":\"Lionel Messi\"}]}]}")
                .asString();

    }

}
