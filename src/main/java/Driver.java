import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.util.List;

public class Driver {

    private static ProfileCredentialsProvider credentialsProvider;

    private static String bucketName = "atspocimages";

    public static void main(String[] args) {
        credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
            System.out.println("rashid " + credentialsProvider.getCredentials().getAWSAccessKeyId());
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


        listFilesInBucket(s3client);

//        putFilesInBucket(s3client);

//        deleteFilesInBucket(s3client);
//        deleteMutipleFilesInBucket(s3client);

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

}
