package com.groupe2.microservicelabelsdetector.labelsdetector;

import java.util.List;

public interface ILabelDetector {
    List<LabelObj> getLabelsFromImage(String url, int maxLabels, float minConfidence);
    List<LabelObj> getLabelsFromImage(String url, int maxLabels);
    List<LabelObj> getLabelsFromImage(String url, float minConfidence);
    List<LabelObj> getLabelsFromImage(String url);
}
