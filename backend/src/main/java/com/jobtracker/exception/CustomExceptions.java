package com.jobtracker.exception;

public class CustomExceptions {

    /**
     * ResourceNotFoundException
     * Thrown when a requested resource is not found (404)
     */
    public static class ResourceNotFoundException extends RuntimeException {
        private String resourceName;
        private String fieldName;
        private Object fieldValue;

        public ResourceNotFoundException(String message) {
            super(message);
        }

        public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
            super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
            this.resourceName = resourceName;
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        public String getResourceName() {
            return resourceName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Object getFieldValue() {
            return fieldValue;
        }
    }

    /**
     * BadRequestException
     * Thrown when request data is invalid (400)
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
     * Thrown when user is not authorized (403)
     */
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }

        public UnauthorizedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * DuplicateResourceException
     * Thrown when trying to create a resource that already exists (409)
     */
    public static class DuplicateResourceException extends RuntimeException {
        private String resourceName;
        private String fieldName;
        private Object fieldValue;

        public DuplicateResourceException(String message) {
            super(message);
        }

        public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
            super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
            this.resourceName = resourceName;
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }
    }

    /**
     * InvalidTokenException
     * Thrown when JWT token is invalid or expired (401)
     */
    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }

        public InvalidTokenException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}