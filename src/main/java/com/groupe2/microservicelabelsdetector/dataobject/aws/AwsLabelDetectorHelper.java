package com.groupe2.microserverdataobject.dataobject.aws;

import ch.amt.dataobject.ILabelDetector;
import ch.amt.dataobject.LabelObj;
import ch.amt.dataobject.URLRequestErrorException;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import javax.imageio.ImageIO;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class AwsLabelDetectorHelper implements ILabelDetector {
    private static final AwsCloudClient awsClient = AwsCloudClient.getInstance();

    static byte[] downloadFile(String url) throws IOException {
        URL url2 = new URL(url);
        try(InputStream in = new BufferedInputStream(url2.openStream())){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            return out.toByteArray();
        }
    }

    @Override
    public List<LabelObj> getLabelsFromImage(String url, int maxLabels, float minConfidence) {
        try{
            byte[] image = downloadFile(url);


            if (maxLabels < 0 || minConfidence < 0 || minConfidence > 100) {
                throw new IllegalArgumentException("maxLabels and minConfidence must be greater or equal to 0");
            }

            try(RekognitionClient rekClient = RekognitionClient.builder().credentialsProvider(awsClient.getCredentialsProvider()).region(awsClient.getRegion()).build()) {

                SdkBytes sourceBytes = SdkBytes.fromByteArray(image);

                Image souImage = Image.builder()
                        .bytes(sourceBytes)
                        .build();

                DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                        .image(souImage)
                        .maxLabels(10)
                        .build();

                DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
                List<Label> labels = labelsResponse.labels();

                return awsLabelsToLabelObjs(labels);
            }

        }catch (IOException e){
            throw new URLRequestErrorException();
        }
    }


    public List<LabelObj> getLabelsFromImage(String bucketName, String imageKey, int maxLabels, float minConfidence) {

        if (maxLabels < 0 || minConfidence < 0 || minConfidence > 100) {
            throw new IllegalArgumentException("maxLabels and minConfidence must be greater or equal to 0");
        }

        try(RekognitionClient rekClient = RekognitionClient.builder().credentialsProvider(awsClient.getCredentialsProvider()).region(awsClient.getRegion()).build()) {

            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder().image(
                    Image.builder().s3Object(
                            S3Object.builder().name(imageKey).bucket(bucketName).build()).build())
                    .maxLabels(maxLabels)
                    .minConfidence(minConfidence)
                    .build();

            DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
            List<Label> labels = labelsResponse.labels();

            return awsLabelsToLabelObjs(labels);

        }
    }

    private static List<LabelObj> awsLabelsToLabelObjs(List<Label> labels) {
        List<LabelObj> labelObjs = new LinkedList<>();
        for (Label label : labels) {
            labelObjs.add(awsLabelToLabelObj(label));
        }
        return labelObjs;
    }

    private static LabelObj awsLabelToLabelObj(Label label) {
        List<LabelObj.InstanceObj> instances = new LinkedList<>();
        if (label.instances() != null) {
            for (Instance instance : label.instances()) {
                instances.add(new LabelObj.InstanceObj(instance.confidence(), instance.boundingBox() != null ?
                        new LabelObj.InstanceObj.BoundingBoxObj(
                                instance.boundingBox().height(),
                                instance.boundingBox().left(),
                                instance.boundingBox().top(),
                                instance.boundingBox().width()) : null));
            }
        }

        List<String> parents = new LinkedList<>();
        if (label.parents() != null) {
            for (Parent parent : label.parents()) {
                parents.add(parent.name());
            }
        }
        return new LabelObj(label.name(), label.confidence(), instances, parents);
    }
}
