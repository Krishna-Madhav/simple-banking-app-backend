package de.markant.lksg.application.task.exception;

/**
 * Custom exception to indicate that a requested resource was not found.
 * This exception extends RuntimeException and is used to signal
 * situations where a specific resource could not be located.
 */
public class ResourceNotFoundException extends RuntimeException{

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * @param message The detail message, which is saved for later retrieval.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
