package com.groupe2.microservicelabelsdetector;

import com.groupe2.microservicelabelsdetector.dataobject.IDataObject;
import com.groupe2.microservicelabelsdetector.dataobject.ILabelDetector;
import com.groupe2.microservicelabelsdetector.dataobject.LabelObj;
import com.groupe2.microservicelabelsdetector.dataobject.aws.AwsLabelDetectorHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AwsLabelDetectorHelperTest {
    static private final String BUCKET_NAME = "amt.team02.diduno.education";
    static private final String IMAGE_KEY = "test";

    // it should be a link that never expires, but I don't have enough time, so it will be a random image that I found on Google.
    static private final String URL = "https://images.unsplash.com/photo-1546636889-ba9fdd63583e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=715&q=80";
    static private ILabelDetector labelDetectorHelper;

    @BeforeAll
    static void setUp() {
        labelDetectorHelper = new AwsLabelDetectorHelper();
    }

    @Test
    void getLabelsFromUrl() {

        List<LabelObj> labels = labelDetectorHelper.getLabelsFromImage(URL, 2, 2);

        assertTrue(labels.size() > 0);
    }

    @Test
    void getLabelsFromImage_wrongMaxLabel_Error() {
        final int MAX_LABELS = -2;
        final float MIN_CONFIDENCE = 80;

        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.getLabelsFromImage(URL, MAX_LABELS, MIN_CONFIDENCE));
    }

    @Test
    void getLabelsFromImage_wrongMinConfidenceGreater_Error() {
        final int MAX_LABELS = 4;
        final float MIN_CONFIDENCE = 101;

        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.getLabelsFromImage(URL, MAX_LABELS, MIN_CONFIDENCE));
    }

    @Test
    void getLabelsFromImage_wrongMinConfidenceSmaller_Error() {
        final int MAX_LABELS = 4;
        final float MIN_CONFIDENCE = -1;

        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.getLabelsFromImage(URL, MAX_LABELS, MIN_CONFIDENCE));
    }
}
