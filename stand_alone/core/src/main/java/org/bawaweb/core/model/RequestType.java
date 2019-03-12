package org.bawaweb.core.model;

/**
 * Created by medhoran on 12/6/13.
 */
public enum RequestType {

    GET('G'), ADD('A');

    private Character requestType;

    private RequestType(Character s) {
        requestType = s;
    }

    public Character getRequestType() {
        return requestType;
    }

}
