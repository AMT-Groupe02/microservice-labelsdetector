package com.groupe2.microservicelabelsdetector;

import com.groupe2.microservicelabelsdetector.dataobject.ILabelDetector;
import com.groupe2.microservicelabelsdetector.dataobject.LabelObj;
import com.groupe2.microservicelabelsdetector.dataobject.aws.AwsLabelDetectorHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AwsLabelDetectorHelperTest {
    // it should be a link that never expires, but we don't have enough time, so it will be a random image that we found on Google.
    static private final String URL = "https://images.unsplash.com/photo-1546636889-ba9fdd63583e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=715&q=80";
    static private ILabelDetector labelDetectorHelper;

    @BeforeAll
    static void setUp() {
        labelDetectorHelper = new AwsLabelDetectorHelper();
    }

    @Test
    void getLabelsFromImage_wrongMaxLabel_Error() {
        //given
        final int MAX_LABELS = -2;
        final float MIN_CONFIDENCE = 80;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.getLabelsFromImage(URL, MAX_LABELS, MIN_CONFIDENCE));
    }

    @Test
    void getLabelsFromImage_wrongMinConfidenceGreater_Error() {
        //given
        final int MAX_LABELS = 4;
        final float MIN_CONFIDENCE = 101;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.getLabelsFromImage(URL, MAX_LABELS, MIN_CONFIDENCE));
    }

    @Test
    void getLabelsFromImage_wrongMinConfidenceSmaller_Error() {
        //given
        final int MAX_LABELS = 4;
        final float MIN_CONFIDENCE = -1;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> labelDetectorHelper.getLabelsFromImage(URL, MAX_LABELS, MIN_CONFIDENCE));
    }

    @Test
    void Analyze_ParametersDefaultValues_ContentFromAwsRekognitionWithoutFilter() {
        //given
        List<LabelObj> labels = labelDetectorHelper.getLabelsFromImage(URL);
        //when
        //then
        assertTrue(labels.size() > 0);
    }

    @Test
    void Analyze_MaxLabelsEqual20_ContentFromAwsRekognitionFilterApplied() {
        //given
        final int MAX_LABELS = 20;
        List<LabelObj> labels = labelDetectorHelper.getLabelsFromImage(URL, MAX_LABELS);
        //when
        //then
        assertTrue(labels.size() > 0);
        assertTrue(labels.size() <= 20);

    }

    @Test
    void Analyze_MinConfidenceLevelEqual70_ContentFromAwsRekognitionFilterApplied() {
        //given
        final float MIN_CONFIDENCE = 70;
        List<LabelObj> labels = labelDetectorHelper.getLabelsFromImage(URL, MIN_CONFIDENCE);
        //when
        //then
        assertTrue(labels.size() > 0);

        for (LabelObj label : labels) {
            assertTrue(label.getConfidence() >= MIN_CONFIDENCE);
        }
    }

    @Test
    void Analyse_MaxLabel30AndConfidenceLevel50_ContentFromAwsRekognitionFilterApplied() {
        //given
        final int MAX_LABELS = 30;
        final float MIN_CONFIDENCE = 50;
        List<LabelObj> labels = labelDetectorHelper.getLabelsFromImage(URL, MAX_LABELS, MIN_CONFIDENCE);
        //when
        //then
        assertTrue(labels.size() > 0);
        assertTrue(labels.size() <= 30);
        for (LabelObj label : labels) {
            assertTrue(label.getConfidence() >= MIN_CONFIDENCE);
        }
    }
}
