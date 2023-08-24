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
    private boolean statut;
    //Relation : Dans un rôle, il y a n users
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> userR;
}

