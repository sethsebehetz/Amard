package com.etz.gh.amard.model;

import java.io.Serializable;

/**
 *
 * @author seth.sebeh
 */
public class AmardResponse implements Serializable{

    private static final long serialVersionUID = 73237322121194342L;
    int httpCode;
    String message;

    public AmardResponse(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
