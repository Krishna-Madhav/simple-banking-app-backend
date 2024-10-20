package de.markant.lksg.application.task.service;

import de.markant.lksg.application.task.exception.ResourceNotFoundException;
import de.markant.lksg.application.task.exception.TransactionException;
import de.markant.lksg.application.task.model.Account;
import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.model.TransactionType;
import de.markant.lksg.application.task.repository.AccountRepository;
import de.markant.lksg.application.task.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * The AccountService class is responsible for handling operations related to bank accounts
 * like  deposits, withdrawals, and transfers between accounts. This class interacts with the AccountRepository and
 * TransactionRepository to persist data and perform CRUD operations on Account and Transaction entities.
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    /**
     * Retrieves all accounts from the repository.
     *
     * @return A list of all Account entities.
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Finds a specific account by its account number.
     *
     * @param accountNumber The account number to search for.
     * @return The Account entity if found.
     * @throws ResourceNotFoundException If the account is not found.
     */
    public Account findAccountByNr(String accountNumber) {
        return accountRepository.findAccountByAccountNr(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account number " + accountNumber + " not found!"));
    }

    /**
     * Fetches all transactions for a specific account.
     *
     * @param accountNumber The account number for which transactions are to be fetched.
     * @return A list of Transaction entities associated with the account.
     */
    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        return transactionService.getTransactionsByAccountNr(accountNumber);
    }

    /**
     * Creates a new account with the given account number and initial balance.
     *
     * @param accountNumber   The unique account number for the new account.
     * @param initialBalance  The initial balance for the new account.
     * @return The newly created Account entity.
     * @throws TransactionException If the account number already exists.
     */
    public Account createAccount(String accountNumber, Double initialBalance) {
        if (accountRepository.findAccountByAccountNr(accountNumber).isPresent()) {
            throw new TransactionException("Account number " + accountNumber + " already exists.");
        }

        Account newAccount = Account.builder()
                .accountNr(accountNumber)
                .balance(initialBalance)
                .build();
        newAccount.setTransactions(new ArrayList<>());

        return accountRepository.save(newAccount);
    }

    /**
     * Creates an initial transaction for the newly created account.
     *
     * @param account The account for which the initial transaction is to be created.
     */
    public void createInitialTransaction(Account account) {
        transactionRepository.save(
                Transaction.transactionBuilder()
                        .account(account)
                        .transactionType(TransactionType.ACCOUNT_CREATION)
                        .oldBalance(0.0)
                        .newBalance(account.getBalance())
                        .transactionAmount(account.getBalance())
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Deposits a specified amount into an account.
     *
     * @param accountNumber The account number where the amount is to be deposited.
     * @param amount       The amount to be deposited (must be positive).
     * @throws TransactionException If the amount is negative.
     */
    public void deposit(String accountNumber, Double amount) {
        Account account = findAccountByNr(accountNumber);
        if (amount < 0) {
            throw new TransactionException("Deposit amount should be positive");
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
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Withdraws a specified amount from an account.
     *
     * @param accountNumber The account number from which the amount is to be withdrawn.
     * @param amount       The amount to be withdrawn (must be positive and less than or equal to the balance).
     * @throws TransactionException If the amount is negative or exceeds the account balance.
     */
    public void withdraw(String accountNumber, Double amount) {
        Account account = findAccountByNr(accountNumber);

        if (amount < 0) {
            throw new TransactionException("Invalid amount");
        }
        if (amount > account.getBalance()) {
            throw new TransactionException("Insufficient balance");
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
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Transfers a specified amount from one account to another.
     *
     * @param sourceAccountNumber The account number from which to transfer the amount.
     * @param targetAccountNumber The account number to which the amount is to be transferred.
     * @param transferAmount      The amount to be transferred (must be positive and less than or equal to the balance).
     * @throws TransactionException If the transfer amount is invalid or exceeds the source account's balance.
     */
    public void transfer(String sourceAccountNumber, String targetAccountNumber, Double transferAmount) {
        Account sourceAccount = findAccountByNr(sourceAccountNumber);
        Account targetAccount = findAccountByNr(targetAccountNumber);

        if (transferAmount > sourceAccount.getBalance()) {
            throw new TransactionException("Insufficient balance for transfer!");
        }
        if (transferAmount <= 0) {
            throw new TransactionException("Invalid transfer amount.");
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
                        .timeStamp(LocalDateTime.now())
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

    /**
     * Deletes an account by its account number.
     *
     * @param accountNumber The account number of the account to be deleted.
     * @throws ResourceNotFoundException If the account is not found.
     */
    public void deleteAccount(String accountNumber) {
    Account account = findAccountByNr(accountNumber); // Find the account first
    if (account == null) {
        throw new ResourceNotFoundException("Account number " + accountNumber + " not found!"); // Exception handling
    }
    accountRepository.delete(account); // Then delete the account
}

}
