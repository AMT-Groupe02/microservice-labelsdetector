package com.groupe2.microservicelabelsdetector.dataobject;

public class DataObjectNotFoundException extends RuntimeException {
    public DataObjectNotFoundException() {
        super("File not found");
    }
}
