package prjt.dcm.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO
{
    private long idMedia;
    private String nom;
    private String statut;
    private String version;
    private String extension;
    private LocalDate date;
    private String description;
    private long taille;
    private String url;
    private List<String> motsCles;
    private String chemin;
    private String typeMedia;
    private String imageConvertie;

}
