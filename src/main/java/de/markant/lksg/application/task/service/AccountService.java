package de.markant.lksg.application.task.service;

import de.markant.lksg.application.task.exception.ResourceNotFoundException;
import de.markant.lksg.application.task.model.Account;
import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.model.TransactionType;
import de.markant.lksg.application.task.repository.AccountRepository;
import de.markant.lksg.application.task.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    // All accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Find specific account by accountNumber
    public Account findAccountByNr(String accountNumber) {
        return accountRepository.findAccountByAccountNr(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account number " + accountNumber + " not found!"));
    }

    // Fetch transactions for a specific account
    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        return transactionService.getTransactionsByAccountNr(accountNumber);
    }

    public Account createAccount(String accountNumber, Double initialBalance) {
        if (accountRepository.findAccountByAccountNr(accountNumber).isPresent()) {
            throw new RuntimeException("Account number " + accountNumber + " already exists.");
        }

        Account newAccount = Account.builder()
                .accountNr(accountNumber)
                .balance(initialBalance)
                .build();
        newAccount.setTransactions(new ArrayList<>());

        return accountRepository.save(newAccount);
    }

    public void createInitialTransaction(Account account) {
        transactionRepository.save(
                Transaction.transactionBuilder()
                        .account(account)
                        .transactionType(TransactionType.ACCOUNT_CREATION)
                        .oldBalance(0.0)
                        .newBalance(account.getBalance())
                        .transactionAmount(account.getBalance())
                        .build()
        );
    }

    public void deposit(String accountNumber, Double amount) {
        Account account = findAccountByNr(accountNumber);
        if (amount < 0) {
            throw new RuntimeException("Deposit amount should be positive");
        }

        Double oldBalance = account.getBalance();
        account.setBalance(oldBalance + amount);

        accountRepository.save(account);

        transactionRepository.save(
                Transaction.transactionBuilder()
                        .account(account)
                        .transactionType(TransactionType.DEPOSIT)
                        .oldBalance(oldBalance)
                        .newBalance(account.getBalance())
                        .transactionAmount(amount)
                        .build()
        );
    }

    public void withdraw(String accountNumber, Double amount) {
        Account account = findAccountByNr(accountNumber);

        if (amount < 0) {
            throw new RuntimeException("Invalid amount");
        }
        if (amount > account.getBalance()) {
            throw new RuntimeException("Insufficient balance");
        }

        Double oldBalance = account.getBalance();
        account.setBalance(oldBalance - amount);

        accountRepository.save(account);

        transactionRepository.save(
                Transaction.transactionBuilder()
                        .account(account)
                        .oldBalance(oldBalance)
                        .newBalance(account.getBalance())
                        .transactionAmount(amount)
                        .transactionType(TransactionType.WITHDRAWAL)
                        .build()
        );
    }

    public void transfer(String sourceAccountNumber, String targetAccountNumber, Double transferAmount) {
        Account sourceAccount = findAccountByNr(sourceAccountNumber);
        Account targetAccount = findAccountByNr(targetAccountNumber);

        if (transferAmount > sourceAccount.getBalance()) {
            throw new RuntimeException("Insufficient balance for transfer!");
        }
        if (transferAmount <= 0) {
            throw new RuntimeException("Invalid transfer amount.");
        }

        Double sourceOldBalance = sourceAccount.getBalance();
        Double targetOldBalance = targetAccount.getBalance();

        sourceAccount.setBalance(sourceOldBalance - transferAmount);
        targetAccount.setBalance(targetOldBalance + transferAmount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        transactionRepository.save(
                Transaction.transactionBuilder()
                        .account(sourceAccount)
                        .transactionType(TransactionType.TRANSFER)
                        .oldBalance(sourceOldBalance)
                        .newBalance(sourceAccount.getBalance())
                        .transactionAmount(transferAmount)
                        .targetAccountNr(targetAccountNumber)
                        .build()
        );

        transactionRepository.save(
                Transaction.transactionBuilder()
                        .account(targetAccount)
                        .oldBalance(targetOldBalance)
                        .transactionType(TransactionType.TRANSFER)
                        .newBalance(targetAccount.getBalance())
                        .transactionAmount(transferAmount)
                        .targetAccountNr(sourceAccountNumber)
                        .build()
        );
    }

    // **method to delete an account**
    public void deleteAccount(String accountNumber) {
        Account account = findAccountByNr(accountNumber); // Find the account first
        accountRepository.delete(account); // Then delete the account
    }
}
