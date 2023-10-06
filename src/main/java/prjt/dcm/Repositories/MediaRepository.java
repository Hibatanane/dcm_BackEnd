package prjt.dcm.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prjt.dcm.Dto.MediaDTO;
import prjt.dcm.Entities.Media;
import prjt.dcm.Entities.MotCle;

import java.time.LocalDate;
import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    @Query("SELECT DISTINCT m FROM Media m JOIN m.motCles mc WHERE mc.motCle IN :motsCles")
    List<Media> findByMotsCles(@Param("motsCles") List<String> motsCles);

    @Query("Select m from Media m WHERE m.userM.idUser=:idUser AND m.typeMedia=:type_media")
    List<Media> getAllMedias(@Param("idUser") long idUser, @Param("type_media") String type_media);

    Media findByIdMedia(Long id);

    void deleteByIdMedia(Long id);

    void deleteAllByTypeMedia(String typeMedia);


}
