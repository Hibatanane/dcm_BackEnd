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
        String nom = roleRepository.findRoleByNom(roleDTO.getNom().toLowerCase());
        if (nom != null) {
            return new ApiResponse("Le rôle existe déjà", 409);
        }
        Role role = creerOuModifierRole(roleDTO);
        roleRepository.save(role);
        return new ApiResponse("Role ajouté", 201);
    }

    public Role creerOuModifierRole(RoleDTO roleDTO) {

        Role role = new Role();
        role.setNom(roleDTO.getNom().toLowerCase());
        if (roleDTO.getStatut().equals("true")) {
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
