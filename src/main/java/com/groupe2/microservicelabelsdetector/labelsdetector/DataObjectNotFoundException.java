package com.groupe2.microservicelabelsdetector.labelsdetector;

public class DataObjectNotFoundException extends RuntimeException {
    public DataObjectNotFoundException() {
        super("File not found");
    }
}
