package com.groupe2.microservicelabelsdetector.domain.responsebody;

public class Label {
    private String name;
    private float confidence;

    public Label(){}

    public Label(String name, float confidence) {
        this.name = name;
        this.confidence = confidence;
    }

    public String getName() {
        return name;
    }

    public float getConfidence() {
        return confidence;
    }
}
