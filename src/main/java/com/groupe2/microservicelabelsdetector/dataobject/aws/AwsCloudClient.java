package com.groupe2.microservicelabelsdetector.dataobject.aws;

import com.groupe2.microservicelabelsdetector.dataobject.ICloudClient;
import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

public final class AwsCloudClient implements ICloudClient {
    private static AwsCloudClient instance = null;
    private AwsCredentialsProvider credentialsProvider;
    private RekognitionClient rekognitionClient = new RekognitionClient() {
        @Override
        public String serviceName() {
            return null;
        }

        @Override
        public void close() {

        }
    };

    private Region region;

    private AwsCloudClient() {
        Dotenv dotenv = Dotenv.load();
        credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(dotenv.get("ACCESS_KEY"), dotenv.get("SECRET_KEY")));
        region = Region.EU_WEST_2;
    }

    public static AwsCloudClient getInstance() {
        if (instance == null) {
            instance = new AwsCloudClient();
        }
        return instance;
    }

    public AwsCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public Region getRegion() {
        return region;
    }

    RekognitionClient getRekognitionClient() {
        return rekognitionClient;
    }
}
