package com.jobtracker.exception;

/**
 * CustomExceptions
 * Day 2: Custom exception classes for better error handling
 */
public class CustomExceptions {

    /**
     * ResourceNotFoundException
     * Thrown when a requested resource (like a job application) is not found
     */
    public static class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException(String message) {
            super(message);
        }

        public ResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * BadRequestException
     * Thrown when request data is invalid
     */
    public static class BadRequestException extends RuntimeException {

        public BadRequestException(String message) {
            super(message);
        }

        public BadRequestException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * UnauthorizedException
     * Thrown when user is not authorized to access a resource
     */
    public static class UnauthorizedException extends RuntimeException {

        public UnauthorizedException(String message) {
            super(message);
        }

        public UnauthorizedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}