// src/main/java/com/restaurant/exception/ResourceNotFoundException.java
package com.restaurant.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}