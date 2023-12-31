package prjt.dcm.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import prjt.dcm.Entities.MotCle;

import java.util.List;

public interface MotCleRepository extends JpaRepository<MotCle,Long>
{
    @Query("Select m.motCle from MotCle m WHERE m.media.idMedia=:idMedia")
    List<String> findMotCleByMediaId(@Param("idMedia")long idMedia);
    @Modifying
    @Transactional
    @Query("Delete  from MotCle m Where m.media.idMedia=:idMedia")
    void deleteAllByIdMedia(@Param("idMedia")long idMedia);
}
