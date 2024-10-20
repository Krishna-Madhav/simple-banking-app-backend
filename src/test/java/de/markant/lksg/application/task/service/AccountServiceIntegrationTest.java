package de.markant.lksg.application.task.service;

import de.markant.lksg.application.task.exception.ResourceNotFoundException;
import de.markant.lksg.application.task.exception.TransactionException;
import de.markant.lksg.application.task.model.Account;
import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.model.TransactionType;
import de.markant.lksg.application.task.repository.AccountRepository;
import de.markant.lksg.application.task.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Rollback after each test
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        // Clean the database before each test
        accountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    public void testCreateAccount() {
        String accountNumber = "123456";
        double initialBalance = 100.0;

        Account account = accountService.createAccount(accountNumber, initialBalance);

        assertThat(account.getAccountNr()).isEqualTo(accountNumber);
        assertThat(account.getBalance()).isEqualTo(initialBalance);
        assertThat(account.getTransactions()).isEmpty();
    }

    @Test
    public void testCreateAccount_AccountAlreadyExists() {
        String accountNumber = "123456";
        double initialBalance = 100.0;

        accountService.createAccount(accountNumber, initialBalance);

        TransactionException exception = assertThrows(TransactionException.class, () -> {
            accountService.createAccount(accountNumber, initialBalance);
        });

        assertThat(exception.getMessage()).contains("Account number " + accountNumber + " already exists.");
    }

    @Test
    public void testDeposit() {
        String accountNumber = "123456";
        double initialBalance = 100.0;
        accountService.createAccount(accountNumber, initialBalance);

        double depositAmount = 50.0;
        accountService.deposit(accountNumber, depositAmount);

        Account account = accountService.findAccountByNr(accountNumber);
        assertThat(account.getBalance()).isEqualTo(initialBalance + depositAmount);

        List<Transaction> transactions = accountService.getTransactionsForAccount(accountNumber);
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transactions.get(0).getTransactionAmount()).isEqualTo(depositAmount);
    }

    @Test
    public void testWithdraw() {
        String accountNumber = "123456";
        double initialBalance = 100.0;
        accountService.createAccount(accountNumber, initialBalance);

        double withdrawAmount = 30.0;
        accountService.withdraw(accountNumber, withdrawAmount);

        Account account = accountService.findAccountByNr(accountNumber);
        assertThat(account.getBalance()).isEqualTo(initialBalance - withdrawAmount);

        List<Transaction> transactions = accountService.getTransactionsForAccount(accountNumber);
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getTransactionType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(transactions.get(0).getTransactionAmount()).isEqualTo(withdrawAmount);
    }

    @Test
    public void testWithdraw_InsufficientBalance() {
        String accountNumber = "123456";
        double initialBalance = 50.0;
        accountService.createAccount(accountNumber, initialBalance);

        TransactionException exception = assertThrows(TransactionException.class, () -> {
            accountService.withdraw(accountNumber, 100.0);
        });

        assertThat(exception.getMessage()).contains("Insufficient balance");
    }

    @Test
    public void testTransfer() {
        String sourceAccountNumber = "123456";
        String targetAccountNumber = "654321";
        accountService.createAccount(sourceAccountNumber, 200.0);
        accountService.createAccount(targetAccountNumber, 100.0);

        double transferAmount = 50.0;
        accountService.transfer(sourceAccountNumber, targetAccountNumber, transferAmount);

        Account sourceAccount = accountService.findAccountByNr(sourceAccountNumber);
        Account targetAccount = accountService.findAccountByNr(targetAccountNumber);

        assertThat(sourceAccount.getBalance()).isEqualTo(200.0 - transferAmount);
        assertThat(targetAccount.getBalance()).isEqualTo(100.0 + transferAmount);

        List<Transaction> sourceTransactions = accountService.getTransactionsForAccount(sourceAccountNumber);
        List<Transaction> targetTransactions = accountService.getTransactionsForAccount(targetAccountNumber);

        assertThat(sourceTransactions).hasSize(1);
        assertThat(sourceTransactions.get(0).getTransactionType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(sourceTransactions.get(0).getTransactionAmount()).isEqualTo(transferAmount);

        assertThat(targetTransactions).hasSize(1);
        assertThat(targetTransactions.get(0).getTransactionType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(targetTransactions.get(0).getTransactionAmount()).isEqualTo(transferAmount);
    }

    @Test
    public void testDeleteAccount() {
        String accountNumber = "123456";
        accountService.createAccount(accountNumber, 100.0);

        accountService.deleteAccount(accountNumber);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.findAccountByNr(accountNumber);
        });

        assertThat(exception.getMessage()).contains("Account number " + accountNumber + " not found!");
    }

    @Test
    public void testDeleteAccount_AccountNotFound() {
        String accountNumber = "123456";

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.deleteAccount(accountNumber);
        });

        assertThat(exception.getMessage()).contains("Account number " + accountNumber + " not found!");
    }
}
