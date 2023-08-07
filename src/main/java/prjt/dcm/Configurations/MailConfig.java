package prjt.dcm.Configurations;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class MailConfig {
    public static JavaMailSenderImpl getMailConfig() {
        JavaMailSenderImpl emailConfig = new JavaMailSenderImpl();
        Properties p = emailConfig.getJavaMailProperties();
        p.put("mail.transport.protocol", "smtp");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.starttls.required", "true");
        p.put("mail.debug", "true");
        p.put("mail.smtp.ssl.enable", true);
        emailConfig.setHost("smtp.gmail.com");
        emailConfig.setPort(465);
        emailConfig.setUsername("hiba.tanane@etud.iga.ac.ma");
        emailConfig.setPassword("yesknezkbpgytssg");
        return emailConfig;
    }
}

