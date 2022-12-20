package com.groupe2.microservicelabelsdetector.dataobject.aws;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AwsCloudClientTest {

    @Test
    void isSingleton(){
        AwsCloudClient client1 = AwsCloudClient.getInstance();
        AwsCloudClient client2 = AwsCloudClient.getInstance();
        assertEquals(client1, client2);
    }

    @Test
    void regionIsEUWEST1(){
        AwsCloudClient client = AwsCloudClient.getInstance();
        assertEquals(Region.EU_WEST_2, client.getRegion());
    }

    @Test
    void getCredientialsProvider(){
        AwsCloudClient client = AwsCloudClient.getInstance();
        assertNotNull(client.getCredentialsProvider());
    }
}