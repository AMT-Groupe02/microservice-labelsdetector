package com.groupe2.microserverdataobject.dataobject.aws;

import com.groupe2.microserverdataobject.dataobject.ICloudClient;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
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
        credentialsProvider = EnvironmentVariableCredentialsProvider.create();
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
