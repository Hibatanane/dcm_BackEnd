package prjt.dcm.Services;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import io.minio.errors.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Transactional
public class MinioService {
    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String getMediaFromUrl(String bucketName, String chemin) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(chemin)
                            .method(Method.GET
                            )
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new prjt.dcm.Exceptions.MinioException("Oops ! il y a eu un problème au niveau du serveur ");
        }
    }
    public void supprimerObject(String bucketName,String chemin)
    {
        try{
            minioClient.removeObject(RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(chemin)
                    .build());
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        }

    }
    public void supprimerBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
       if(minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build()))
        {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(item.objectName()).build());
            }
            minioClient.removeBucket(
                    RemoveBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        }


    }
}
