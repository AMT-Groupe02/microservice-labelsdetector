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

    // TODO ici il y a quelque chose qui ne va pas : cette requête ne modifie pas l'état du microservice donc ça devriat etre un GET
    // je pense qu'une partie de la confusion viens du fait que cet endpoint a été pensé comme une "fonction" et non comme une "Resource"
    // qui est sensé être la manière de concevoir les REST API. Je vous invite a consulter le lien dur les REST API Good practice fournis
    // et/ou venir en discuter avec moi si c'est pas clair.
    @PostMapping("/detect/image")
    public LabelsResponse postDetectImage(@RequestBody UrlToImage image) {
        AwsLabelDetectorHelper awsLabelDetectorHelper = new AwsLabelDetectorHelper();

        System.out.println("URL : " + image.getUrl() + " maxLabels : " + image.getMaxLabels() + " minConfidence : " + image.getMinConfidence());
        List<LabelObj> labels ;

        // (pas pénalisé) vous pouvez utiliser des paramètres par défault avec @RequestParam voir https://www.baeldung.com/spring-request-param
        if (image.getMaxLabels() != null && image.getMinConfidence() != null){
            labels = awsLabelDetectorHelper.getLabelsFromImage(image.getUrl(), image.getMaxLabels(), image.getMinConfidence());
        }else if(image.getMaxLabels() != null ){
            labels = awsLabelDetectorHelper.getLabelsFromImage(image.getUrl(), image.getMaxLabels());
        }else if (image.getMinConfidence() != null){
            labels = awsLabelDetectorHelper.getLabelsFromImage(image.getUrl(), image.getMinConfidence());
        }else{
            labels = awsLabelDetectorHelper.getLabelsFromImage(image.getUrl());
        }

        List<Label> nlabels = labels.stream().map(label -> new Label(label.getName(), label.getConfidence())).toList();

        // (pas pénalisé) mais vous devriez retourner des ResponseEntity et non des objets c'est plus flexible et plus standard
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
