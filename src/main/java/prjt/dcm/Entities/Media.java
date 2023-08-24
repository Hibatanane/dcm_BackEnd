package prjt.dcm.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMedia;
    private String nom;
    private String statut;
    private String version;
    private String extension;
    private Date date;
    private String description;
    private int taille;
    private String chemin;
    @ManyToOne
    @JoinColumn(name = "idUser")
    private User userM;
}
