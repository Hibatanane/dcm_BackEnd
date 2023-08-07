package prjt.dcm.Controllers;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;

import java.util.List;

public class MinioController {
    public static void main(String args[]) {
        System.out.println("Test de Minio");
        MinioClient minioClient = demo();
        try {
            List<Bucket> bList = minioClient.listBuckets();
            System.out.println("Connection réussion , total buckets : " + bList.size());

            for(int i=0;i<bList.size();i++)
            {
                System.out.println(bList.get(i));
            }
        } catch (MinioException e) {
            System.out.println("connexion echouée");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MinioClient demo() {
        MinioClient minioClient = MinioClient.builder().endpoint("play.min.io").credentials("minioadmin", "minioadmin").build();
        return minioClient;
    }
}