package com.groupe2.microservicelabelsdetector.domain.responsebody;

import java.util.List;

public class LabelsResponse {
    private List<Label> labels;

    public LabelsResponse() {
    }

    public LabelsResponse(List<Label> labels) {
        this.labels = labels;
    }

    public List<Label> getLabels() {
        return labels;
    }

}
