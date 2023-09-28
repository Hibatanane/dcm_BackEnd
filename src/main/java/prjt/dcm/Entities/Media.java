package prjt.dcm.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

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
    private LocalDate date;
    private String description;
    private long taille;
    private String chemin;
    private String typeMedia;
    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL)
    private List<MotCle> motCles = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "idUser")
    private User userM;
    public Media(Media media)
    {
this();
this.nom=media.getNom();
this.statut=media.getStatut();
this.version=media.getVersion();
this.extension=media.getExtension();
this.date=media.getDate();
this.description=media.getDescription();
this.taille=media.getTaille();
this.typeMedia=media.getTypeMedia();
this.userM=media.getUserM();
    }
}
