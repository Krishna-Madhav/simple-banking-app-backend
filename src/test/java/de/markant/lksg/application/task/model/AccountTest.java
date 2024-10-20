package de.markant.lksg.application.task.model;

import static org.junit.jupiter.api.Assertions.*;

import de.markant.lksg.application.task.model.Account;
import de.markant.lksg.application.task.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        // Initialize the account object before each test
        account = Account.builder()
                .accountNr("123456789")
                .balance(1000.00)
                .build();
    }

    @Test
    void testAccountCreation() {
        // Test if the account is created successfully
        assertNotNull(account);
        assertEquals("123456789", account.getAccountNr());
        assertEquals(1000.00, account.getBalance());
        
    }

    @Test
    void testAccountBalanceUpdate() {
        // Test if the account balance can be updated
        account.setBalance(2000.00);
        assertEquals(2000.00, account.getBalance());
    }



    @Test
    void testUniqueAccountNumber() {
        // Test if the account number is unique
        Account account1 = Account.builder()
                .accountNr("987654321")
                .balance(500.00)
                .build();

        assertEquals("987654321", account1.getAccountNr());
    }

    @Test
    void testAccountBuilder() {
        // Test the builder pattern
        Account accountFromBuilder = Account.builder()
                .accountNr("111222333")
                .balance(1500.00)
                .build();

        assertEquals("111222333", accountFromBuilder.getAccountNr());
        assertEquals(1500.00, accountFromBuilder.getBalance());
        
    
    }
}
