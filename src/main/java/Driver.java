import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Driver {

    private static ProfileCredentialsProvider credentialsProvider;

    final static Logger logger = Logger.getLogger(Driver.class);
    private static String bucketName = "atspocimages";

    public static void main(String[] args) throws IOException {
        credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
            System.out.println("rashid " + credentialsProvider.getCredentials().getAWSAccessKeyId());
            logger.info("rashid " + credentialsProvider.getCredentials().getAWSAccessKeyId());
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (~/.aws/credentials), and is in valid format.", e);
        }


        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_WEST_2)
                .build();


//        listFilesInBucket(s3client);

//        putFilesInBucket(s3client);

//        deleteFilesInBucket(s3client);
//        deleteMutipleFilesInBucket(s3client);

        downloadFilesFromBucket(s3client);

    }

    private static void deleteFilesInBucket(AmazonS3 s3client) {
        s3client.deleteObject(bucketName,"hello.txt");
    }

    private static void putFilesInBucket(AmazonS3 s3client) {
        s3client.putObject(bucketName, "hello.txt", new File("./images/hello.txt"));
    }

    private static void listFilesInBucket(AmazonS3 s3client) {
        S3Objects.inBucket(s3client, bucketName).forEach((S3ObjectSummary objectSummary) -> {
            System.out.println(objectSummary.getKey());
        });
    }

    private static void deleteMutipleFilesInBucket(AmazonS3 s3client) {

        String objkeyArr[] = {
                "hello.txt",
                "VPCDiagram.jpg"
        };
        DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucketName)
                .withKeys(objkeyArr);
        s3client.deleteObjects(delObjReq);
    }

    private static void downloadFilesFromBucket(AmazonS3 s3client) throws IOException {


        S3Objects.inBucket(s3client, bucketName).forEach((S3ObjectSummary objectSummary) -> {
            System.out.println(objectSummary.getKey());
            S3Object s3object = s3client.getObject(bucketName, objectSummary.getKey());
            S3ObjectInputStream inputStream = s3object.getObjectContent();
            try {
                FileUtils.copyInputStreamToFile(inputStream, new File("./download/" + objectSummary.getKey()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
