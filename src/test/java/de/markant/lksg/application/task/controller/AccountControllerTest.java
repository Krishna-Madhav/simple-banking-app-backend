package de.markant.lksg.application.task.controller;

import de.markant.lksg.application.task.dto.AccountDto;
import de.markant.lksg.application.task.dto.TransferDto;
import de.markant.lksg.application.task.exception.ResourceNotFoundException;
import de.markant.lksg.application.task.model.Account;
import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccounts() {
        Account account = new Account();
        account.setAccountNr("12345");
        account.setBalance(1000.0);

        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account));

        List<Account> accounts = accountController.getAllAccounts();
        assertEquals(1, accounts.size());
        assertEquals("12345", accounts.get(0).getAccountNr());
    }

    @Test
    void testGetAccountByNumberFound() {
        Account account = new Account();
        account.setAccountNr("12345");
        account.setBalance(1000.0);

        when(accountService.findAccountByNr("12345")).thenReturn(account);

        ResponseEntity<Account> response = accountController.getAccountByNumber("12345");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }



    @Test
    void testCreateAccount() {
        AccountDto accountDto = new AccountDto("12345", 1000.0);
        Account account = new Account();
        account.setAccountNr("12345");
        account.setBalance(1000.0);

        when(accountService.createAccount(any(String.class), any(Double.class))).thenReturn(account);

        ResponseEntity<Account> response = accountController.createAccount(accountDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testDeposit() {
        String accountNumber = "12345";
        double amount = 1000.0;

        // Mocking the findAccountByNr method
        when(accountService.findAccountByNr(accountNumber)).thenReturn(new Account(accountNumber, 1000.0));

        doNothing().when(accountService).deposit(any(), any());

        // Calling the deposit method in the controller
        ResponseEntity<Map<String, Object>> response = accountController.deposit(accountNumber, amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
    }


    @Test
    void testWithdraw() {
        String accountNumber = "12345";
        double amount = 500.0;

        // Mock the account retrieval
        Account account = new Account(accountNumber, 1000.0);
        when(accountService.findAccountByNr(accountNumber)).thenReturn(account);

        doNothing().when(accountService).withdraw(accountNumber, amount);

        ResponseEntity<Map<String, Object>> response = accountController.withdraw(accountNumber, amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
    }


    @Test
    void testTransfer() {
        TransferDto transferDto = new TransferDto("12345", "67890", 500.0);

        doNothing().when(accountService).transfer(any(), any(), any());

        // Calling the transfer method in the controller
        ResponseEntity<Map<String, Object>> response = accountController.transfer(transferDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    void testTransferInvalidAmount() {
        TransferDto transferDto = new TransferDto("12345", "67890", -500.0);

        // Mocking the transfer service to throw exception for invalid amount
        doThrow(new IllegalArgumentException("Invalid transfer amount"))
                .when(accountService).transfer(anyString(), anyString(), eq(-500.0));

        ResponseEntity<Map<String, Object>> response = accountController.transfer(transferDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetTransactionsForAccount() {
        String accountNumber = "12345";
        when(accountService.getTransactionsForAccount(accountNumber)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Transaction>> response = accountController.getTransactionsForAccount(accountNumber);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteAccount() {
        String accountNumber = "12345";
        doNothing().when(accountService).deleteAccount(accountNumber);

        ResponseEntity<Void> response = accountController.deleteAccount(accountNumber);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteAccountNotFound() {
        String accountNumber = "12345";
        doThrow(new ResourceNotFoundException("Account not found")).when(accountService).deleteAccount(accountNumber);

        ResponseEntity<Void> response = accountController.deleteAccount(accountNumber);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
