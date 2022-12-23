package com.groupe2.microservicelabelsdetector.controller;

import com.groupe2.microservicelabelsdetector.labelsdetector.LabelObj;
import com.groupe2.microservicelabelsdetector.labelsdetector.aws.AwsLabelDetectorHelper;
import com.groupe2.microservicelabelsdetector.domain.requestbody.UrlToImage;
import com.groupe2.microservicelabelsdetector.domain.responsebody.Label;
import com.groupe2.microservicelabelsdetector.domain.responsebody.LabelsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class LabelDetectorController {

    @PostMapping("/detect/image")
    public LabelsResponse postDetectImage(@RequestBody UrlToImage image) {
        AwsLabelDetectorHelper awsLabelDetectorHelper = new AwsLabelDetectorHelper();

        System.out.println("URL : " + image.getUrl() + " maxLabels : " + image.getMaxLabels() + " minConfidence : " + image.getMinConfidence());
        List<LabelObj> labels = awsLabelDetectorHelper.getLabelsFromImage(image.getUrl(), image.getMaxLabels(), image.getMinConfidence());

        List<Label> nlabels = labels.stream().map(label -> new Label(label.getName(), label.getConfidence())).toList();

        return new LabelsResponse(nlabels);
    }

    @ExceptionHandler()
    public ResponseEntity<String> handleDataObjectNotFoundException(
            IllegalArgumentException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

}
