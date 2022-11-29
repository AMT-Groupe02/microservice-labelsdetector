package com.groupe2.microserverdataobject.dataobject.aws;

import ch.amt.dataobject.DataObjectNotFoundException;
import ch.amt.dataobject.IDataObject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

public class AwsDataObject implements IDataObject {
    private static final AwsCloudClient AWS_CLIENT = AwsCloudClient.getInstance();
    private String path;
    private String bucketName;

    public AwsDataObject(String path) {
        this.bucketName = path.substring(0, path.indexOf("/"));
        this.path = path.substring(path.indexOf("/") + 1);
    }

    /**
     * Upload data to a bucket from base64 encoded string
     * @param dataBase64
     */
    public void upload(String dataBase64) {
        byte[] bytes = java.util.Base64.getDecoder().decode(dataBase64);
        upload(bytes);
    }

    /**
     * Upload data to a bucket from byte array
     * @param dataBytes
     */
    public void upload(byte[] dataBytes) {
        if (!AwsBucketHelper.bucketExists(bucketName)) {
            AwsBucketHelper.createBucket(bucketName);
        }

        try (S3Client s3 = S3Client.builder().credentialsProvider(AWS_CLIENT.getCredentialsProvider()).region(AWS_CLIENT.getRegion()).build()) {

            InputStream fis = new ByteArrayInputStream(dataBytes);

            s3.putObject(PutObjectRequest.builder().bucket(bucketName).key(path)
                            .contentLength((long) dataBytes.length)
                            .build(),
                    RequestBody.fromInputStream(fis, dataBytes.length));

            S3Waiter waiter = s3.waiter();
            HeadObjectRequest requestWait = HeadObjectRequest.builder()
                    .bucket(bucketName).key(path).build();

            WaiterResponse<HeadObjectResponse> waiterResponse = waiter.waitUntilObjectExists(requestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);
        }
    }

    /**
     * Downloads the current object into a byte array
     * @return byte array
     */
    public byte[] download() throws IOException {
        if (!AwsBucketHelper.bucketExists(bucketName) || !exists()) {
            throw new RuntimeException("File does not exist");
        }

        try (S3Client s3 = S3Client.builder().credentialsProvider(AWS_CLIENT.getCredentialsProvider()).region(AWS_CLIENT.getRegion()).build()) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();
            return s3.getObject(getObjectRequest).readAllBytes();
        }
    }

    /**
     * Deletes the current object
     * @throws DataObjectNotFoundException if the file does not exist
     */
    public void delete() {
        if (!AwsBucketHelper.bucketExists(bucketName) || !exists()) {
            throw new DataObjectNotFoundException();
        }
        try (S3Client s3 = S3Client.builder().credentialsProvider(AWS_CLIENT.getCredentialsProvider()).region(AWS_CLIENT.getRegion()).build()) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();

            s3.deleteObject(deleteObjectRequest);
        }

    }

    /**
     * Creates a presigned URL for the file that lasts for 60 minutes
     * @return
     */
    public String getUrl() {
        final int EXPIRATION_TIME = 60;

        if (!AwsBucketHelper.bucketExists(bucketName) || !exists()) {
            throw new DataObjectNotFoundException();
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(EXPIRATION_TIME))
                .getObjectRequest(getObjectRequest)
                .build();

        S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(AWS_CLIENT.getCredentialsProvider())
                .region(AWS_CLIENT.getRegion())
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();
    }

    /**
     * Checks if the current object exists
     * @return true if the object exists, false otherwise
     */
    public boolean exists() {
        try (S3Client s3 = S3Client.builder().credentialsProvider(AWS_CLIENT.getCredentialsProvider()).region(AWS_CLIENT.getRegion()).build()) {

            GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(path).build();
            s3.getObject(request);

        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            throw e;
            // TODO créer une exception custom pour chaque type d'erreur
        }
        return true;
    }
}
