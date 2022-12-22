package com.groupe2.microservicelabelsdetector.dataobject.aws;

import com.groupe2.microservicelabelsdetector.dataobject.ILabelDetector;
import com.groupe2.microservicelabelsdetector.dataobject.LabelObj;
import com.groupe2.microservicelabelsdetector.dataobject.URLRequestErrorException;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class AwsLabelDetectorHelper implements ILabelDetector {
    private static final AwsCloudClient awsClient = AwsCloudClient.getInstance();

    private static byte[] downloadFile(String url) throws IOException {
        URL url2 = new URL(url);
        try (InputStream in = new BufferedInputStream(url2.openStream())) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            return out.toByteArray();
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
                instances.add(new LabelObj.InstanceObj(instance.confidence(), instance.boundingBox() != null ? new LabelObj.InstanceObj.BoundingBoxObj(instance.boundingBox().height(), instance.boundingBox().left(), instance.boundingBox().top(), instance.boundingBox().width()) : null));
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

    @Override
    public List<LabelObj> getLabelsFromImage(String url, int maxLabels, float minConfidence) {
        if (maxLabels < 0 || minConfidence < 0 || minConfidence > 100) {
            throw new IllegalArgumentException("maxLabels and minConfidence must be greater or equal to 0 and minConfidence must be lower than 100");
        }

        return getLabelsFromImage(url, DetectLabelsRequest.builder().maxLabels(maxLabels).minConfidence(minConfidence));
    }

    @Override
    public List<LabelObj> getLabelsFromImage(String url, int maxLabels){
        if (maxLabels < 0) {
            throw new IllegalArgumentException("maxLabels and minConfidence must be greater or equal to 0 and minConfidence must be lower than 100");
        }

        return getLabelsFromImage(url, DetectLabelsRequest.builder().maxLabels(maxLabels));
    }
    @Override
    public List<LabelObj> getLabelsFromImage(String url, float minConfidence){
        if (minConfidence < 0 || minConfidence > 100) {
            throw new IllegalArgumentException("maxLabels and minConfidence must be greater or equal to 0 and minConfidence must be lower than 100");
        }

        return getLabelsFromImage(url, DetectLabelsRequest.builder().minConfidence(minConfidence));
    }

    @Override
    public List<LabelObj> getLabelsFromImage(String url) {
        return getLabelsFromImage(url, DetectLabelsRequest.builder());
    }

    private List<LabelObj> getLabelsFromImage(String url, DetectLabelsRequest.Builder builder) {
        try {
            byte[] image = downloadFile(url);

            try (RekognitionClient rekClient = RekognitionClient.builder().credentialsProvider(awsClient.getCredentialsProvider()).region(awsClient.getRegion()).build()) {

                SdkBytes sourceBytes = SdkBytes.fromByteArray(image);

                Image souImage = Image.builder().bytes(sourceBytes).build();

                DetectLabelsRequest detectLabelsRequest = builder.image(souImage).build();

                DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
                List<Label> labels = labelsResponse.labels();

                return awsLabelsToLabelObjs(labels);
            }

        } catch (IOException e) {
            throw new URLRequestErrorException();
        }
    }
}
