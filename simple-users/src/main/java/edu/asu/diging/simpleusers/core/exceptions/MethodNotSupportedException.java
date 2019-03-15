package edu.asu.diging.simpleusers.core.exceptions;

import org.springframework.web.bind.annotation.RequestMethod;

public class MethodNotSupportedException extends Exception {

    private RequestMethod method;
    
    public MethodNotSupportedException(RequestMethod method) {
        this.method = method;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }
    
    
}
