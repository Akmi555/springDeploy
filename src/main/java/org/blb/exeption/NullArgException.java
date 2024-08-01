package org.blb.exeption;

public class NullArgException extends RuntimeException{
    public NullArgException(String field){
        super("{\"fieldName\" : \""+field+"\", \"message\" : \"must not be null\"}");
    }
}
