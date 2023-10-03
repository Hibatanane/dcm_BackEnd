package prjt.dcm.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.minio.*;
import io.minio.errors.*;
import jakarta.mail.MessagingException;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import prjt.dcm.Dto.ApiResponse;
import prjt.dcm.Dto.DimensionDTO;
import prjt.dcm.Dto.MediaDTO;
import prjt.dcm.Entities.Media;
import prjt.dcm.Entities.MotCle;
import prjt.dcm.Entities.User;
import prjt.dcm.Helpers.UUIDGenerator;
import prjt.dcm.Repositories.MediaRepository;
import prjt.dcm.Repositories.MotCleRepository;
import prjt.dcm.Repositories.UserRepository;
import prjt.dcm.Sender.MailSender;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private MinioService minioService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MotCleRepository motCleRepository;
    private final MinioClient minioClient;
    @Autowired
    private DocumentToImage documentToImage;

    public MediaService(MinioClient minioClient) {
        this.minioClient = minioClient;

    }


    public String addImageOrVideo(MultipartFile file,
                                  String description,
                                  String version,
                                  String statut,
                                  List<String> motsCles,
                                  String bucketName,
                                  String email) throws Exception {

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        UUID uuid = UUIDGenerator.generateUniqueUUID();
        String uuidString = uuid.toString();
        Media media = new Media();
        User user = userRepository.findUserByEmail("hiba.tanane@gmail.com");
        String chemin = file.getOriginalFilename();
        int index = chemin.lastIndexOf(".");
        String extension = chemin.substring(index + 1).toLowerCase();
        String nomImage = chemin.substring(0, index);
        chemin = uuidString + chemin;
        media.setChemin(chemin);
        media.setNom(nomImage);
        media.setExtension(extension);
        media.setDate(LocalDate.now());
        media.setDescription(description);
        media.setStatut(statut);
        media.setVersion(version);
        media.setTaille(file.getSize());
        media.setUserM(user);
        media.setTypeMedia(bucketName);
        mediaRepository.save(media);

        for (String motCle : motsCles) {
            MotCle m = new MotCle();
            m.setMedia(media);
            m.setMotCle(motCle);
            motCleRepository.save(m);
        }
        try {
            if (bucketName.equals("video") && !extension.equals("mp4")) {
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "drbs6sxu8",
                        "api_key", "914154124864651",
                        "api_secret", "Ndmz9iTnqSpqkXFzzP2F9Nw0-yA",
                        "secure", true));
                Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "resource_type", "video",
                        "format", "mp4"
                ));
                String mp4Url = (String) result.get("secure_url");
                System.out.println("mp4 URL : " + mp4Url);
                URL url = new URL(mp4Url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                Resource videoResource = new UrlResource(url);

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(chemin)
                                .stream(videoResource.getInputStream(), videoResource.contentLength(), -1)
                                .build()
                );

                return "inserer";
            }
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(chemin)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(putObjectArgs);
            return "inserer";

        } catch (IOException e) {
            media.setStatut("Erreur de publication");
            mediaRepository.save(media);
            e.printStackTrace();
            return "non inserer";

        }

    }

    public String addPictos(MultipartFile file,
                            String description,
                            String nom,
                            List<String> motsCles,
                            String email) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String bucketName = "pictos";
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        UUID uuid = UUIDGenerator.generateUniqueUUID();
        String uuidString = uuid.toString();
        Media media = new Media();
        User user = userRepository.findUserByEmail("hiba.tanane@gmail.com");
        String chemin = file.getOriginalFilename();
        int index = chemin.lastIndexOf(".");
        String extension = chemin.substring(index + 1).toLowerCase();
        chemin = uuidString + chemin;
        media.setChemin(chemin);
        media.setNom(nom);
        media.setExtension(extension);
        media.setDate(LocalDate.now());
        media.setDescription(description);
        media.setTaille(file.getSize());
        media.setUserM(user);
        media.setTypeMedia("pictos");
        mediaRepository.save(media);
        for (String motCle : motsCles) {
            MotCle m = new MotCle();
            m.setMedia(media);
            m.setMotCle(motCle);
            motCleRepository.save(m);
        }
        try {
            InputStream inputStream = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(chemin)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(putObjectArgs);
            return "inserer";

        } catch (IOException e) {
            media.setStatut("Erreur de publication");
            mediaRepository.save(media);
            e.printStackTrace();
            return "non inserer";

        }
    }

    public String addDocument(MultipartFile file,
                              String nom,
                              String description,
                              String version,
                              String statut,
                              List<String> motsCles,
                              String email) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String bucketName = "document";
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        UUID uuid = UUIDGenerator.generateUniqueUUID();
        String uuidString = uuid.toString();
        Media media = new Media();
        //User user = userRepository.findUserByEmail(email);
        User user = userRepository.findUserByEmail("hiba.tanane@gmail.com");
        String chemin = file.getOriginalFilename();
        int index = chemin.lastIndexOf(".");
        String extension = chemin.substring(index + 1).toLowerCase();
        chemin = uuidString + chemin;
        media.setChemin(chemin);
        media.setNom(nom);
        media.setExtension(extension);
        media.setDate(LocalDate.now());
        media.setDescription(description);
        media.setTaille(file.getSize());
        media.setUserM(user);
        media.setVersion(version);
        media.setStatut(statut);
        media.setTypeMedia("document");
        mediaRepository.save(media);
        for (String motCle : motsCles) {
            MotCle m = new MotCle();
            m.setMedia(media);
            m.setMotCle(motCle);
            motCleRepository.save(m);
        }
        try {
            InputStream inputStream = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(chemin)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(putObjectArgs);
            return "inserer";

        } catch (IOException e) {
            media.setStatut("Erreur de publication");
            mediaRepository.save(media);
            e.printStackTrace();
            return "non inserer";

        }
    }

    public DimensionDTO dimensionDocx(String mediaUrl) {
        DimensionDTO dimension = new DimensionDTO(0, 0);
        try {
            URL url = new URL(mediaUrl);
            InputStream inputStream = url.openStream();
            XWPFDocument document = new XWPFDocument(inputStream);
            BigInteger widthBigInteger = (BigInteger) document.getDocument().getBody().getSectPr().getPgSz().getW();
            BigInteger heightBigInteger = (BigInteger) document.getDocument().getBody().getSectPr().getPgSz().getH();
            double cm = 0.035 / 20;
            int width = (int) (widthBigInteger.intValue() * cm);
            int height = (int) (heightBigInteger.intValue() * cm);
            System.out.println("Largeur" + width);
            System.out.println("Longueur" + height);
            dimension.setLargeur(width);
            dimension.setLongueur(height);
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("erreur");

        } catch (IOException e) {
            System.out.println("erreur");
        }
        return dimension;
    }

    public DimensionDTO dimensionPpt(String mediaUrl) {
        DimensionDTO dimension = new DimensionDTO(0, 0);
        try {
            URL url = new URL(mediaUrl);
            InputStream inputStream = url.openStream();
            XMLSlideShow ppt = new XMLSlideShow(inputStream);
            XSLFSlide page = ppt.getSlides().get(0);
            int width = (int) ppt.getPageSize().getWidth();
            int height = (int) ppt.getPageSize().getHeight();
            System.out.println("PPT LARGEUR" + width);
            System.out.println("PPT Longueur" + height);
            dimension.setLargeur(width);
            dimension.setLongueur(height);
        } catch (FileNotFoundException e) {
            System.out.println("erreur");
        } catch (IOException e) {
            System.out.println("erreur");

        }
        return dimension;
    }

    public DimensionDTO dimensionImage(String mediaUrl) {
        DimensionDTO dimension = new DimensionDTO(0, 0);
        try {
            BufferedImage image = ImageIO.read(new URL(mediaUrl));
            if (image != null) {
                dimension.setLargeur(image.getWidth());
                dimension.setLongueur(image.getHeight());
                System.out.println("Width : " + image.getWidth());
            }

        } catch (MalformedURLException e) {
            System.out.println("erreur");
        } catch (IOException e) {
            System.out.println("erreur");

        }
        return dimension;
    }

    public List<MediaDTO> getMedias(Long idUser, String typeMedia, String bucketName) throws IOException {
        List<Media> mediaList = mediaRepository.getAllMedias(idUser, typeMedia);
        List<MediaDTO> mediaDTOs = new ArrayList<>();
        for (Media media : mediaList) {

            MediaDTO mediaDTO = new MediaDTO();
            mediaDTO.setIdMedia(media.getIdMedia());
            mediaDTO.setNom(media.getNom());
            mediaDTO.setStatut(media.getStatut());
            mediaDTO.setVersion(media.getVersion());
            mediaDTO.setExtension(media.getExtension());
            mediaDTO.setDate(media.getDate());
            mediaDTO.setChemin(media.getChemin());
            mediaDTO.setDescription(media.getDescription());
            mediaDTO.setTaille(media.getTaille());
            mediaDTO.setTypeMedia(media.getTypeMedia());
            mediaDTO.setUrl(minioService.getMediaFromUrl(media.getTypeMedia(), media.getChemin()));
            if (bucketName.equals("document")) {
                if (media.getExtension().equals("pdf") && media.getTaille() > 0) {
                    mediaDTO.setImageConvertie(documentToImage.convertPdfToImage(minioService.getMediaFromUrl("document", media.getChemin())));
                } else if (media.getExtension().equals("pptx") && media.getTaille() > 0) {
                    System.out.println(minioService.getMediaFromUrl("document", media.getChemin()));
                    mediaDTO.setImageConvertie(documentToImage.convertPptxToImage(minioService.getMediaFromUrl("document", media.getChemin())));

                } else {
                    mediaDTO.setImageConvertie(null);
                }

            }
            List<String> motsCles = motCleRepository.findMotCleByMediaId(media.getIdMedia());
            List<String> motsClesRetrouves = new ArrayList<>();
            for (String motCle : motsCles) {

                motsClesRetrouves.add(motCle);
            }
            mediaDTO.setMotsCles(motsClesRetrouves);
            mediaDTOs.add(mediaDTO);
        }
        return mediaDTOs;
    }

    public void supprimerMedia(List<Long> idMedias) {
        for (Long id : idMedias) {
            Media media = mediaRepository.findByIdMedia(id);
            minioService.supprimerObject(media.getTypeMedia(), media.getChemin());
            mediaRepository.deleteByIdMedia(id);
        }
    }

    public void supprimerTousMedias(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        mediaRepository.deleteAllByTypeMedia(bucketName);
        minioService.supprimerBucket(bucketName);
    }

    public ApiResponse duppliquer(List<Long> idMedias) throws IOException {
        for (Long idMedia : idMedias) {
            Media media = mediaRepository.findByIdMedia(idMedia);
            Media mediaADupliquer = new Media(media);
            mediaADupliquer.setChemin(mediaADupliquer.getIdMedia() + media.getChemin());
            for (MotCle motCle : media.getMotCles()) {
                MotCle mc = new MotCle();
                mc.setMotCle(motCle.getMotCle());
                mc.setMedia(mediaADupliquer);
            }
            try {
                minioClient.copyObject(
                        CopyObjectArgs.builder()
                                .source(CopySource.builder()
                                        .bucket(media.getTypeMedia())
                                        .object(media.getChemin())
                                        .build())
                                .bucket(media.getTypeMedia())
                                .object(mediaADupliquer.getChemin())
                                .build()
                );
                mediaRepository.save(mediaADupliquer);
            } catch (Exception e) {
                e.printStackTrace();
                return new ApiResponse("erreur", 500);
            }
        }
        return new ApiResponse("dupliquer", 201);
    }

    public ApiResponse envoyerEmail(String from, String to, String subject, String body, List<String> url) {
        try {
            MailSender.envoyerMail(from, to, subject, body, url);
            return new ApiResponse("envoyer", 200);
        } catch (MessagingException e) {
            return new ApiResponse("erreur", 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MediaDTO getMediaById(Long idMedia) {

        Media media = mediaRepository.findByIdMedia(idMedia);
        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setIdMedia(media.getIdMedia());
        mediaDTO.setNom(media.getNom());
        mediaDTO.setStatut(media.getStatut());
        mediaDTO.setVersion(media.getVersion());
        mediaDTO.setExtension(media.getExtension());
        mediaDTO.setDate(media.getDate());
        mediaDTO.setChemin(media.getChemin());
        mediaDTO.setDescription(media.getDescription());
        mediaDTO.setTaille(media.getTaille());
        mediaDTO.setTypeMedia(media.getTypeMedia());
        mediaDTO.setUrl(minioService.getMediaFromUrl(media.getTypeMedia(), media.getChemin()));
        List<String> motsCles = motCleRepository.findMotCleByMediaId(media.getIdMedia());
        List<String> motsClesRetrouves = new ArrayList<>();
        for (String motCle : motsCles) {

            motsClesRetrouves.add(motCle);
        }
        mediaDTO.setMotsCles(motsClesRetrouves);
        return mediaDTO;
    }

    public ApiResponse modifierMedia(long idMedia,
                                     String nom,
                                     String description,
                                     String statut,
                                     String version,
                                     List<String> motsCles) {
        Media media = mediaRepository.findByIdMedia(idMedia);
        media.setVersion(version);
        media.setNom(nom);
        media.setStatut(statut);
        media.setDescription(description);
        media.getMotCles().clear();
        for (String motCleStr : motsCles) {
            MotCle motCle = new MotCle();
            motCle.setMotCle(motCleStr);
            motCle.setMedia(media);
            media.getMotCles().add(motCle);
        }
        mediaRepository.save(media);
        return new ApiResponse("envoyé", 200);
    }
}
