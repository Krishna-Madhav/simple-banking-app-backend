package de.markant.lksg.application.task.service;

import de.markant.lksg.application.task.exception.ResourceNotFoundException;
import de.markant.lksg.application.task.model.Account;
import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.repository.AccountRepository;
import de.markant.lksg.application.task.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccounts() {
        when(accountRepository.findAll()).thenReturn(Collections.singletonList(new Account("12345", 1000.0)));
        List<Account> accounts = accountService.getAllAccounts();
        assertFalse(accounts.isEmpty());
    }

    @Test
    void testFindAccountByNrFound() {
        when(accountRepository.findAccountByAccountNr("12345")).thenReturn(Optional.of(new Account("12345", 1000.0)));

        Account account = accountService.findAccountByNr("12345");
        assertNotNull(account);
        assertEquals("12345", account.getAccountNr());
    }

    @Test
    void testFindAccountByNrNotFound() {
        when(accountRepository.findAccountByAccountNr("12345")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.findAccountByNr("12345");
        });
        assertEquals("Account number 12345 not found!", exception.getMessage());
    }

    @Test
    void testCreateAccount() {
        Account account = new Account("12345", 1000.0);
        when(accountRepository.findAccountByAccountNr("12345")).thenReturn(Optional.empty());
        when(accountRepository.save(any())).thenReturn(account);

        Account createdAccount = accountService.createAccount("12345", 1000.0);
        assertNotNull(createdAccount);
        assertEquals("12345", createdAccount.getAccountNr());
    }

    @Test
    void testDeposit() {
        Account account = new Account("12345", 1000.0);
        when(accountRepository.findAccountByAccountNr("12345")).thenReturn(Optional.of(account));

        accountService.deposit("12345", 500.0);
        assertEquals(1500.0, account.getBalance());
    }

    @Test
    void testWithdraw() {
        Account account = new Account("12345", 1000.0);
        when(accountRepository.findAccountByAccountNr("12345")).thenReturn(Optional.of(account));

        accountService.withdraw("12345", 500.0);
        assertEquals(500.0, account.getBalance());
    }

    @Test
    void testTransfer() {
        Account sourceAccount = new Account("12345", 1000.0);
        Account targetAccount = new Account("67890", 500.0);
        when(accountRepository.findAccountByAccountNr("12345")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findAccountByAccountNr("67890")).thenReturn(Optional.of(targetAccount));

        accountService.transfer("12345", "67890", 300.0);
        assertEquals(700.0, sourceAccount.getBalance());
        assertEquals(800.0, targetAccount.getBalance());
    }


    @Test
    void testDeleteAccount() {
        Account account = new Account("12345", 1000.0);
        when(accountRepository.findAccountByAccountNr("12345")).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).delete(account);
        accountService.deleteAccount("12345");
        verify(accountRepository, times(1)).delete(account);
    }
}
