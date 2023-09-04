package prjt.dcm.Services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import prjt.dcm.Dto.AuthenticationResponse;
import prjt.dcm.Dto.UserDTO;
import prjt.dcm.Entities.Role;
import prjt.dcm.Entities.User;
import prjt.dcm.Helpers.HTML;
import prjt.dcm.Repositories.RoleRepository;
import prjt.dcm.Repositories.UserRepository;
import prjt.dcm.Security.JwtService;
import prjt.dcm.Sender.MailSender;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


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
            user.setMdp(passwordEncoder().encode(mdp));
            userRepository.save(user);
            return "200";
        }
        return "404";
    }

    public User getUser(long id) {
        return userRepository.findUserByIdUser(id);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Execution de methode loadUserByUserName ");

        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            System.out.println(user.getRole().getNom());
        }
        if (user == null) throw new UsernameNotFoundException(String.format("User %s not found", email));
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getMdp())
                .roles(user.getRole().getNom()).build();

        return userDetails;
    }

    public AuthenticationResponse register() {
        return null;
    }

    public AuthenticationResponse authenticate(UserDTO req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    req.getEmail(),
                    req.getMdp()
            ));

            var user = userRepository.findUserByEmail(req.getEmail());

            if (user != null) {
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
            } else {
                // Retourner null si l'utilisateur n'est pas trouvé (peut-être gérer cela différemment si nécessaire)
                return null;
            }
        } catch (AuthenticationException e) {
            // Retourner null en cas d'erreur d'authentification
            return null;
        }
    }

}
