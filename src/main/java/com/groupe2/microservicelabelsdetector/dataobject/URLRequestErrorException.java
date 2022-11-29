package com.groupe2.microserverdataobject.dataobject;

public class URLRequestErrorException extends RuntimeException {
    public URLRequestErrorException() {
        super("Could not access the URL");
    }
}
