package com.groupe2.microserverdataobject.dataobject.aws;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

public final class AwsBucketHelper {
    private static final AwsCloudClient awsClient = AwsCloudClient.getInstance();

    private AwsBucketHelper() {
    }

    public static void createBucket(String bucketName) {
        try (S3Client s3 = S3Client.builder().credentialsProvider(awsClient.getCredentialsProvider()).region(awsClient.getRegion()).build()) {

            S3Waiter waiter = s3.waiter();

            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3.createBucket(createBucketRequest);

            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            WaiterResponse<HeadBucketResponse> waiterResponse = waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println(bucketName + " is ready");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteBucket(String bucketName) {
        try (S3Client s3 = S3Client.builder().credentialsProvider(awsClient.getCredentialsProvider()).region(awsClient.getRegion()).build()) {

            S3Waiter waiter = s3.waiter();

            ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsV2Response listObjectsV2Response;

            do {
                listObjectsV2Response = s3.listObjectsV2(listObjectsV2Request);
                for (S3Object s3Object : listObjectsV2Response.contents()) {
                    DeleteObjectRequest request = DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Object.key())
                            .build();
                    s3.deleteObject(request);
                }
            } while (Boolean.TRUE.equals(listObjectsV2Response.isTruncated()));

            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3.deleteBucket(deleteBucketRequest);

            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            WaiterResponse<HeadBucketResponse> waiterResponse = waiter.waitUntilBucketNotExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println(bucketName + " is deleted");

        }
    }


    public static boolean bucketExists(String bucketName) {
        try (S3Client s3 = S3Client.builder().credentialsProvider(awsClient.getCredentialsProvider()).region(awsClient.getRegion()).build()) {

            ListBucketsResponse response = s3.listBuckets();

            for (Bucket bucket : response.buckets()) {
                if (bucket.name().equals(bucketName)) {
                    return true;
                }
            }

        }
        return false;
    }
}
