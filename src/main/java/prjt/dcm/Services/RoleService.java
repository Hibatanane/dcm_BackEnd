package prjt.dcm.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prjt.dcm.Dto.ApiResponse;
import prjt.dcm.Dto.RoleDTO;
import prjt.dcm.Entities.Role;
import prjt.dcm.Repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

public RoleDTO recupererRole(long idRole)
{

    Role role = roleRepository.findByIdRole(idRole);
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setIdRole(role.getIdRole());
    roleDTO.setNom(role.getNom());
    roleDTO.setStatut(role.getStatut());
    roleDTO.setDescription(role.getDescription());
    roleDTO.setImporter(role.isImporter());
    roleDTO.setModifier(role.isModifier());
    roleDTO.setDupliquer(role.isDupliquer());
    roleDTO.setSupprimer(role.isSupprimer());
    roleDTO.setTelecharger(role.isTelecharger());
    roleDTO.setCopier(role.isCopier());
    roleDTO.setPartager(role.isPartager());
    roleDTO.setAjouterUser(role.isAjouterUser());
    roleDTO.setModifierUser(role.isModifierUser());
    roleDTO.setSupprimerUser(role.isSupprimerUser());
    roleDTO.setAjouterRole(role.isAjouterRole());
    roleDTO.setModifierRole(role.isModifierRole());
    roleDTO.setSupprimerRole(role.isSupprimerRole());
    return roleDTO;

}

    public List<RoleDTO> recupererRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> rolesDTO = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setIdRole(role.getIdRole());
            roleDTO.setNom(role.getNom());
            roleDTO.setStatut(role.getStatut());
            roleDTO.setDescription(role.getDescription());
            roleDTO.setImporter(role.isImporter());
            roleDTO.setModifier(role.isModifier());
            roleDTO.setDupliquer(role.isDupliquer());
            roleDTO.setSupprimer(role.isSupprimer());
            roleDTO.setTelecharger(role.isTelecharger());
            roleDTO.setCopier(role.isCopier());
            roleDTO.setPartager(role.isPartager());
            roleDTO.setAjouterUser(role.isAjouterUser());
            roleDTO.setModifierUser(role.isModifierUser());
            roleDTO.setSupprimerUser(role.isSupprimerUser());
            roleDTO.setAjouterRole(role.isAjouterRole());
            roleDTO.setModifierRole(role.isModifierRole());
            roleDTO.setSupprimerRole(role.isSupprimerRole());
            rolesDTO.add(roleDTO);
        }
        return rolesDTO;
    }

    @Transactional
    public void supprimerRole(List<Long> idRole) {
        for (Long id : idRole) {
            roleRepository.deleteByIdRole(id);
        }
    }

    public ApiResponse modifierRole() {

        return new ApiResponse("modifié", 200);

    }


    public ApiResponse ajouterRole(RoleDTO roleDTO) {
        Role test = roleRepository.findRoleByNom(roleDTO.getNom().toLowerCase());
       if(test!=null)
       {
           if (test.getNom() != null) {
               return new ApiResponse("Le rôle existe déjà", 409);
           }
       }
        Role role = creerOuModifierRole(roleDTO,new Role());
        roleRepository.save(role);
        return new ApiResponse("Role ajouté", 201);
    }

    public ApiResponse modifierRole(RoleDTO roleDTO) {
        Role role = roleRepository.findRoleByNom(roleDTO.getNom());
        if(role!=null)
{
    if (role.getNom() != null && role.getIdRole() != roleDTO.getIdRole()) {
        return new ApiResponse("Le rôle existe déjà.", 409);
    } else {
        role = creerOuModifierRole(roleDTO,role);
        roleRepository.save(role);
        return new ApiResponse("Rôle modifié", 200);
    }
}
        role = roleRepository.findByIdRole(roleDTO.getIdRole());
        role = creerOuModifierRole(roleDTO,role);
        roleRepository.save(role);
        return new ApiResponse("Rôle modifié", 200);

    }
    public Role creerOuModifierRole(RoleDTO roleDTO,Role role)
    {
        role.setNom(roleDTO.getNom().toLowerCase());
        if (roleDTO.getStatut().equals("true"))
        {
            role.setStatut("Activé");

        } else {
            role.setStatut("Désactivé");

        }
        role.setDescription(roleDTO.getDescription());
        role.setImporter(roleDTO.isImporter());
        role.setModifier(roleDTO.isModifier());
        role.setDupliquer(roleDTO.isDupliquer());
        role.setSupprimer(roleDTO.isSupprimer());
        role.setTelecharger(roleDTO.isTelecharger());
        role.setCopier(roleDTO.isCopier());
        role.setPartager(roleDTO.isPartager());
        role.setAjouterUser(roleDTO.isAjouterUser());
        role.setModifierUser(roleDTO.isModifierUser());
        role.setSupprimerUser(roleDTO.isSupprimerUser());
        role.setAjouterRole(roleDTO.isAjouterRole());
        role.setModifierRole(roleDTO.isModifierRole());
        role.setSupprimerRole(roleDTO.isSupprimerRole());
        return role;
    }

}
