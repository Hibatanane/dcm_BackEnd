package prjt.dcm.Controllers;

import lombok.RequiredArgsConstructor;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import prjt.dcm.Dto.AuthenticationResponse;
import prjt.dcm.Dto.RegisterDTO;
import prjt.dcm.Dto.UserDTO;
import prjt.dcm.Entities.User;
import prjt.dcm.Security.JwtAuthenticationFilter;
import prjt.dcm.Services.UserService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());



    public Authentication authentication(Authentication authentication) {
        return authentication;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        AuthenticationResponse rep = userService.authenticate(userDTO);
        System.out.println("rep = " + rep);
        if (rep != null) {
            System.out.println("génération token");

            return ResponseEntity.ok(rep);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Données incorrectes");
    }

    @PostMapping("/mdpOublie")
    public String envoyerMdp(@RequestBody String email) throws MessagingException {
        return userService.recupererMdp(email);
    }

    @PostMapping("/reinitialiserMdp")
    public String reinitialiserMdp(@RequestParam String email, @RequestParam String mdp) {
        return userService.changerMdp(email, mdp);
    }


    @GetMapping("/update/{id}")
    public User getUser(@PathVariable("id") long id) {
        return userService.getUser(id);
    }
    @GetMapping("/sayHello")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> sayHello() {
        logger.info("Méthode sayhello executée");
        return ResponseEntity.ok("Say Hello est bien affiché");
    }


}
