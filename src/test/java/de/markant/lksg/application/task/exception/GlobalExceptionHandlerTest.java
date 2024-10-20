package de.markant.lksg.application.task.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleResourceNotFoundException() {

        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleResourceNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testHandleTransactionException() {

        String message = "Transaction error occurred";
        TransactionException exception = new TransactionException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleTransactionException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
}
