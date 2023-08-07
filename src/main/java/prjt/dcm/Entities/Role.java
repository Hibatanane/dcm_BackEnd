package prjt.dcm.Entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRole;

    private String nom;
    //Relation : Dans un rôle, il y a n users
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> userR;
}
