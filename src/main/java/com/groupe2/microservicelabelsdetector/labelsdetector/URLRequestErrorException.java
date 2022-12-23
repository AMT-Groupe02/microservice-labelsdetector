package com.groupe2.microservicelabelsdetector.labelsdetector;

public class URLRequestErrorException extends RuntimeException {
    public URLRequestErrorException() {
        super("Could not access the URL");
    }
}
