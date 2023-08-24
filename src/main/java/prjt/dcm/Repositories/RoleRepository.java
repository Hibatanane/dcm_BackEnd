package prjt.dcm.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import prjt.dcm.Entities.Role;

public interface RoleRepository extends JpaRepository <Role,Long>
{
    public Role findRoleByNom(String nom);
    public Role findByIdRole(long id);
}
