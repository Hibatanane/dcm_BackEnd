package prjt.dcm.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;
    private String nom;
    private String prenom;
    private String mdp;
    private String email;
    private  String fonction;
    //Relation User-Role : un user a un seul role
    @ManyToOne
    @JoinColumn(name = "idRole")
    private Role role;

    // Relation User-Media :
    @OneToMany(mappedBy = "userM", cascade = CascadeType.ALL)
    private List<Media> medias;


}
