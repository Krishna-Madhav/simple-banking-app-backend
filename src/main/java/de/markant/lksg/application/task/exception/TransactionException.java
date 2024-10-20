package de.markant.lksg.application.task.exception;


/**
 * Custom exception to indicate that a transaction-related error has occurred.
 * This exception extends RuntimeException and is used to signal
 * issues during transaction processing.
 */
public class TransactionException extends RuntimeException {

    /**
     * Constructs a new TransactionException with the specified detail message.
     * @param message The detail message, which is saved for later retrieval.
     *
     */
    public TransactionException(String message) {
        super(message);
    }
}
