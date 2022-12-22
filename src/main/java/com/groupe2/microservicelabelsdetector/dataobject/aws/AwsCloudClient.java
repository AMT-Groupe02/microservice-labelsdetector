package com.groupe2.microservicelabelsdetector.dataobject.aws;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

public final class AwsCloudClient {
    private static AwsCloudClient instance = null;
    private final AwsCredentialsProvider credentialsProvider;

    private final Region region;

    private AwsCloudClient() {
        Dotenv dotenv = Dotenv.load();
        credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(dotenv.get("ACCESS_KEY"), dotenv.get("SECRET_KEY")));
        // TODO ajouter la posibiliter de changer de region
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

}
