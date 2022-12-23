package com.groupe2.microservicelabelsdetector.domain.requestbody;

public class UrlToImage {
    private String url;
    private Integer maxLabels;
    private Float minConfidence;

    public UrlToImage() {
    }

    public UrlToImage( String url, Integer maxLabels, Float minConfidence) {
        this.url = url;
        this.maxLabels = maxLabels;
        this.minConfidence = minConfidence;
    }

    public Integer getMaxLabels() {
        return maxLabels;
    }

    public Float getMinConfidence() {
        return minConfidence;
    }

    public String getUrl() {
        return url;
    }
}
