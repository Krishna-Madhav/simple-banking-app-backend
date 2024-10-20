package de.markant.lksg.application.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 * This class is responsible for handling exceptions thrown by controllers
 * and returning appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException.
     * @param resourceNotFoundException The exception thrown when a requested resource is not found.
     * @return ResponseEntity with a 404 NOT FOUND status and error message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceNotFoundException.getMessage());
    }

    /**
     * Handles TransactionException.
     * @param transactionException The exception thrown during a transaction-related error.
     * @return ResponseEntity with a 400 BAD REQUEST status and error message.
     */
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<String> handleTransactionException(TransactionException transactionException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(transactionException.getMessage());
    }


}
