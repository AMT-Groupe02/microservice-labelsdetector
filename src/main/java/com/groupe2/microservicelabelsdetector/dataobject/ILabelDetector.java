package com.groupe2.microserverdataobject.dataobject;

import java.util.List;

public interface ILabelDetector {
    List<LabelObj> getLabelsFromImage(String url, int maxLabels, float minConfidence);
}
