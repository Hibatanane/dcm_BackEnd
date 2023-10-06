package prjt.dcm.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
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
}
