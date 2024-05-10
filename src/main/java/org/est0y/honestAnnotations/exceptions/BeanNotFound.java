package org.est0y.honestAnnotations.exceptions;

public class BeanNotFound extends RuntimeException {
    public BeanNotFound(String message,Exception e) {
        super(message,e);
    }
}
