package com.groupe2.microserverdataobject.dataobject;

public class DataObjectNotFoundException extends RuntimeException {
    public DataObjectNotFoundException() {
        super("File not found");
    }
}
