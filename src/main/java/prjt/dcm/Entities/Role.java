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
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRole;
    private String nom;
    private String statut;
    private String description;
    private boolean importer;
    private boolean modifier;
    private boolean dupliquer;
    private boolean supprimer;
    private boolean telecharger;
    private boolean copier;
    private boolean partager;
    private boolean ajouterUser;
    private boolean modifierUser;
    private boolean supprimerUser;
    private boolean ajouterRole;
    private boolean modifierRole;
    private boolean supprimerRole;
    //Relation : Dans un rôle, il y a n users
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<User> userR;


}

