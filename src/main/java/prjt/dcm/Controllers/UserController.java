package prjt.dcm.Controllers;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prjt.dcm.Dao.UserDao;
import prjt.dcm.Entities.User;
import prjt.dcm.Services.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/connexion")
    public String login(@RequestParam String email, @RequestParam String mdp) {
        String rep = userService.login(email, mdp);
        System.out.println("email : " + email + " mdp : " + mdp);
        if (rep.equals("introuvable") || rep.equals("incorrect")) {
            return "401";
        }
        return "200";
    }

    @PostMapping("/mdpOublie")
    public String envoyerMdp(@RequestBody String email) throws MessagingException {
        return userService.recupererMdp(email);
    }
    @PostMapping("/reinitialiserMdp")
    public String reinitialiserMdp(@RequestParam String email,@RequestParam String mdp)
    {
        return userService.changerMdp(email,mdp);
    }


    @GetMapping("/update/{id}")
    public User getUser(@PathVariable("id") long id) {
        return userService.getUser(id);
    }
}
