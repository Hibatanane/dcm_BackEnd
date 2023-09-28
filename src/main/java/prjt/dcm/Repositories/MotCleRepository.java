package prjt.dcm.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prjt.dcm.Entities.MotCle;

import java.util.List;

public interface MotCleRepository extends JpaRepository<MotCle,Long>
{
    @Query("Select m.motCle from MotCle m WHERE m.media.idMedia=:idMedia")
    List<String> findMotCleByMediaId(@Param("idMedia")long idMedia);

}
