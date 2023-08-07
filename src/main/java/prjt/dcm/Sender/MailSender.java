package prjt.dcm.Sender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import prjt.dcm.Configurations.MailConfig;

import java.util.Calendar;
import java.util.Date;

public class MailSender {
    public static void htmlEmailMessenger(String from, String toMail, String subject, String body) throws MessagingException {
        JavaMailSender sender = MailConfig.getMailConfig();
        //Pour composer le mail
        MimeMessage message = sender.createMimeMessage();
        //Facilite la composition du mail
        MimeMessageHelper htmlMessage = new MimeMessageHelper(message, true);
        htmlMessage.setTo(toMail);
        htmlMessage.setFrom(from);
        htmlMessage.setSubject(subject);
        htmlMessage.setText(body, true);
        // Ajouter l'image en tant que pièce jointe
        ClassPathResource logoImageResource = new ClassPathResource("static/assets/Logo.png");
        htmlMessage.addInline("logoImage", logoImageResource);

        sender.send(message);
    }
}
