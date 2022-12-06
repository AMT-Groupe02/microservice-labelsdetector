package com.groupe2.microservicelabelsdetector.domain.requestbody;

public class UrlToImage {
    private String url;
    private int maxLabels;
    private float minConfidence;

    public UrlToImage() {
    }

    public UrlToImage( String url, int maxLabels, float minConfidence) {
        this.url = url;
        this.maxLabels = maxLabels;
        this.minConfidence = minConfidence;
    }

    public int getMaxLabels() {
        return maxLabels;
    }

    public float getMinConfidence() {
        return minConfidence;
    }

    public String getUrl() {
        return url;
    }
}
