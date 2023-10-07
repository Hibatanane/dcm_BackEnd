package prjt.dcm.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prjt.dcm.Dto.RoleDTO;
import prjt.dcm.Entities.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository <Role,Long>
{
    @Query("Select r from Role r where r.nom =:nom ")
     Role  findRoleByNom(@Param("nom") String nom);
     Role findByIdRole(long id);

    void deleteByIdRole(long id);
}
