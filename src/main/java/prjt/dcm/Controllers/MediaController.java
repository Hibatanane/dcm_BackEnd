package prjt.dcm.Controllers;

import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prjt.dcm.Dto.ApiResponse;
import prjt.dcm.Dto.DimensionDTO;
import prjt.dcm.Dto.MediaDTO;
import prjt.dcm.Entities.User;
import prjt.dcm.Repositories.MediaRepository;
import prjt.dcm.Repositories.MotCleRepository;
import prjt.dcm.Repositories.UserRepository;
import prjt.dcm.Services.MediaService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequestMapping("/minio")
@RestController
public class MediaController {
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MotCleRepository motCleRepository;
    @Autowired
    private MediaService mediaService;
    private final MinioClient minioClient;
    private final String bucketNameImage = "image";
    private final String bucketNameVideo = "video";
    private final String bucketNameDocument = "document";
    private final String bucketNamePictos = "pictos";

    public MediaController(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostMapping("/insererImage")
    public String insererImage(@RequestParam("file") MultipartFile file,
                               @RequestParam("description") String description,
                               @RequestParam("version") String version,
                               @RequestParam("statut") String statut,
                               @RequestParam("motsCles") List<String> motsCles,
                               String email
    ) throws Exception {

        return mediaService.addImageOrVideo(file, description, version, statut, motsCles, "image", email);
    }

    @PostMapping("/insererVideo")
    public String insererVideo(@RequestParam("file") MultipartFile file,
                               @RequestParam("description") String description,
                               @RequestParam("version") String version,
                               @RequestParam("statut") String statut,
                               @RequestParam("motsCles") List<String> motsCles,
                               String email
    ) throws Exception {

        return mediaService.addImageOrVideo(file, description, version, statut, motsCles, "video", email);
    }

    @PostMapping("/insererPictos")
    public String insererPictos(@RequestParam("file") MultipartFile file,
                                @RequestParam("description") String description,
                                @RequestParam("nom") String nom,
                                @RequestParam("motsCles") List<String> motsCles,
                                String email
    ) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return mediaService.addPictos(file, description, nom, motsCles, email);
    }

    @PostMapping("/insererDocument")
    public String insererDocument(@RequestParam("file") MultipartFile file,
                                  @RequestParam("description") String description,
                                  @RequestParam("nom") String nom,
                                  @RequestParam("motsCles") List<String> motsCles,
                                  @RequestParam("version") String version,
                                  @RequestParam("statut") String statut,
                                  String email) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return mediaService.addDocument(file, nom, description, version, statut, motsCles, email);
    }


    @GetMapping("/recupererMedias")
    public ResponseEntity<List<MediaDTO>> recupererMedias(//@RequestParam("email")
                                                          String email,
                                                          @RequestParam("media") String media) throws IOException {
        String bucketName;
        if (media.equals("image"))
            bucketName = bucketNameImage;
        else if (media.equals("video"))
            bucketName = bucketNameVideo;
        else if (media.equals("document"))
            bucketName = bucketNameDocument;
        else
            bucketName = bucketNamePictos;
        User user = userRepository.findUserByEmail("hiba.tanane21@gmail.com");
        List<MediaDTO> mediaDtos = mediaService.getMedias(user.getIdUser(), media, bucketName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(mediaDtos, headers, HttpStatus.OK);
    }

    @GetMapping("/{idMedia}")
    public ResponseEntity<MediaDTO> recupererMedia(@PathVariable Long idMedia) {
        return new ResponseEntity<>(mediaService.getMediaById(idMedia), HttpStatus.OK);
    }

    @PostMapping("/supprimerMedias")
    public ResponseEntity<Void> deleteMediaById(@RequestBody List<Long> idMedias) {
        mediaService.supprimerMedia(idMedias);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/supprimerTousMedias")
    public ResponseEntity<Void> deleteAllMedias(@RequestBody String media) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        mediaService.supprimerTousMedias(media);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/dupliquerMedia")
    public ResponseEntity<ApiResponse> dupliquerMedia(@RequestBody List<Long> idMedias) throws IOException {
        ApiResponse response = mediaService.duppliquer(idMedias);
        if ("dupliquer".equals(response.getMessage())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/envoyerEmail")
    public ResponseEntity<ApiResponse> envoyerEmail(//@RequestParam("from") String from,
                                                    @RequestParam("to") String to,
                                                    @RequestParam("subject") String subject,
                                                    @RequestParam("body") String body,
                                                    @RequestParam("url") List<String> url) {
        ApiResponse response = mediaService.envoyerEmail("hiba.tanane21@gmail.com", to, subject, body, url);
        if ("envoyer".equals(response.getMessage()))
            return ResponseEntity.status(HttpStatus.OK).body(response);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/dimensionDocx")
    public ResponseEntity<DimensionDTO> getDimensionDocx(@RequestParam("url") String url) {
        System.out.println("Execution de methode dimension doc");
        DimensionDTO dimensionDTO = mediaService.dimensionDocx(url);
        return ResponseEntity.status(HttpStatus.OK).body(dimensionDTO);
    }

    @GetMapping("/dimensionPpt")
    public ResponseEntity<DimensionDTO> getDimensionPpt(@RequestParam("url") String url) {
        System.out.println("Execution de methode dimension ppt");
        DimensionDTO dimensionDTO = mediaService.dimensionPpt(url);
        return ResponseEntity.status(HttpStatus.OK).body(dimensionDTO);
    }

    @GetMapping("/dimensionImage")
    public ResponseEntity<DimensionDTO> getDimensionImage(@RequestParam("url") String url) {
        System.out.println("Execution de methode dimension image");
        DimensionDTO dimensionDTO = mediaService.dimensionImage(url);
        return ResponseEntity.status(HttpStatus.OK).body(dimensionDTO);
    }

    @PostMapping("/modifierMedia")
    public ResponseEntity<ApiResponse> modifierMedia(@RequestParam("idMedia") long idMedia,
                                                     @RequestParam("nom") String nom,
                                                     @RequestParam("description") String description,
                                                     @RequestParam("statut") String statut,
                                                     @RequestParam("version") String version,
                                                     @RequestParam("motsCles") List<String> motsCles) {
        return ResponseEntity.status(HttpStatus.OK).body(mediaService.modifierMedia(idMedia, nom, description, statut, version, motsCles));

    }


}