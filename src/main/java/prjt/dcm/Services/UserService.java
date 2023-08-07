package prjt.dcm.Services;

import jakarta.mail.MessagingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prjt.dcm.Entities.User;
import prjt.dcm.Helpers.HTML;
import prjt.dcm.Repositories.UserRepository;
import prjt.dcm.Sender.MailSender;

import java.io.IOException;
import java.net.MalformedURLException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public String login(String email, String mdp) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {

            return "introuvable";
        } else {
            if (!user.getMdp().equals(mdp))
                return "incorrect";
        }
        return "succes";
    }

    public String recupererMdp(String email) throws MessagingException {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            return "404";
        try {
            MailSender.htmlEmailMessenger("hiba.tanane@etud.iga.ac.ma", email, "Réinitialisation du mot de passe", HTML.htmlEmailTemplate());
            System.out.println("envoyer ");
            return "200";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "500";
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public String changerMdp(String email, String mdp) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            user.setMdp(mdp);
            userRepository.save(user);
            return "200";
        }
        return "404";
    }

    public User getUser(long id) {
        return userRepository.findUserByIdUser(id);
    }
}
