package prjt.dcm.Sender;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.taskdefs.Java;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.zeroturnaround.zip.ZipUtil;
import prjt.dcm.Configurations.MailConfig;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public static void envoyerMail(String from, String to, String subject, String body, List<String> urls) throws MessagingException, MalformedURLException {
        JavaMailSender sender = MailConfig.getMailConfig();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper htmlMessage = new MimeMessageHelper(message, true);
        htmlMessage.setFrom(from);
        htmlMessage.setTo(to);
        htmlMessage.setSubject(subject);
        htmlMessage.setText(body);
        for (String url : urls) {
            URL u = new URL(url);
            try (InputStream inputStream = u.openStream()) {
                String fileName = getFileNameFromUrl(url);
                byte[] fileBytes = IOUtils.toByteArray(inputStream);
                htmlMessage.addAttachment(fileName, new ByteArrayResource(fileBytes));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        sender.send(message);
    }

    private static String getFileNameFromUrl(String url) {
        int begin = url.lastIndexOf("/");
        int end = url.indexOf("?");
        String fileName = url.substring(begin + 1, end);
        fileName = fileName.replace("%", "_");
        return fileName;
    }

}
