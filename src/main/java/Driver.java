import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.Bucket;
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



        S3Objects.inBucket(s3client, bucketName).forEach((S3ObjectSummary objectSummary) -> {
            // TODO: Consume `objectSummary` the way you need
            System.out.println(objectSummary.getKey());
        });
        s3client.putObject(bucketName, "hello.txt", new File("./images/hello.txt"));

//        s3client.deleteObject(bucketName,"hello.txt"); //How to delete an object

    }


}
