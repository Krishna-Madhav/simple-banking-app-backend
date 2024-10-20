package de.markant.lksg.application.task.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

class TransactionTest {

    private Account account; // Mock Account object
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        // Initializing a mock account for transaction tests
        account = Account.builder()
                .accountNr("123456789")
                .balance(1000.00)
                .transactions(new ArrayList<>())
                .build();

        // Creating a Transaction object
        transaction = Transaction.transactionBuilder()
                .account(account)
                .transactionType(TransactionType.DEPOSIT)
                .oldBalance(1000.00)
                .newBalance(1500.00)
                .transactionAmount(500.00)
                .targetAccountNr(null) // No target account for deposit
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @Test
    void testTransactionCreation() {
        // Testing if the transaction is created successfully
        assertNotNull(transaction);
        assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());
        assertEquals(1000.00, transaction.getOldBalance());
        assertEquals(1500.00, transaction.getNewBalance());
        assertEquals(500.00, transaction.getTransactionAmount());
        assertEquals(account, transaction.getAccount());
        assertNotNull(transaction.getTimeStamp());
    }


    @Test
    void testTransactionType() {
        // Testing if the transaction type can be set and retrieved correctly
        transaction.setTransactionType(TransactionType.TRANSFER);
        assertEquals(TransactionType.TRANSFER, transaction.getTransactionType());
    }

    @Test
    void testOldAndNewBalance() {
        // Checking if old and new balances are correctly set
        transaction.setOldBalance(1000.00);
        transaction.setNewBalance(2000.00);
        assertEquals(1000.00, transaction.getOldBalance());
        assertEquals(2000.00, transaction.getNewBalance());
    }
}
