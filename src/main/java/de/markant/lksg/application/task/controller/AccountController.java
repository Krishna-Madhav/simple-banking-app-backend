package de.markant.lksg.application.task.controller;

import de.markant.lksg.application.task.constants.Constants;
import de.markant.lksg.application.task.dto.TransferDto;
import de.markant.lksg.application.task.exception.ResourceNotFoundException;
import de.markant.lksg.application.task.model.Account;
import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.service.AccountService;
import de.markant.lksg.application.task.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing accounts.
 * Provides endpoints for account operations such as create, retrieve, update, delete accounts and fetch transaction details.
 */
@RestController
@RequestMapping(Constants.BASE_PATH)

public class AccountController {

    @Autowired
    AccountService accountService; // Injecting AccountService to handle account operations

    /**
     * Retrieves all accounts.
     * @return List of all accounts.
     */
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }


    /**
     * Retrieves an account by its account number.
     * @param accountNumber The account number to look for.
     * @return ResponseEntity containing the account or 404 if not found.
     */
    @GetMapping(Constants.ACCOUNT_PATH)
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
    Account account = accountService.findAccountByNr(accountNumber);
    if (account == null) {
        return ResponseEntity.notFound().build(); // Return 404 if account is not found
    }
    return ResponseEntity.ok(account);
    }

    /**
     * Creates a new account.
     * @param accountDto DTO containing account details.
     * @return ResponseEntity containing the created account.
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto) {
       
        Account newAccount = accountService.createAccount(accountDto.getAccountNr(), accountDto.getBalance());
        accountService.createInitialTransaction(newAccount);

        return ResponseEntity.ok(newAccount);
    }

    /**
     * Deposits an amount into an account.
     * @param accountNumber The account number to deposit into.
     * @param amount The amount to deposit.
     * @return ResponseEntity with a success message and new balance.
     */

    @PostMapping(Constants.ACCOUNT_PATH + Constants.DEPOSIT_PATH)
    public ResponseEntity<Map<String, Object>> deposit(@PathVariable String accountNumber, @RequestParam Double amount) {
   
    accountService.deposit(accountNumber, amount);
    
    Map<String, Object> response = new HashMap<>();
    response.put("message", String.format(Constants.DEPOSIT_SUCCESS, amount));
    response.put("newBalance", accountService.findAccountByNr(accountNumber).getBalance());
    
    return ResponseEntity.ok(response);
}

    /**
     * Withdraws an amount from an account.
     * @param accountNumber The account number to withdraw from.
     * @param amount The amount to withdraw.
     * @return ResponseEntity with a success message and new balance.
     */
    @PostMapping(Constants.ACCOUNT_PATH + Constants.WITHDRAW_PATH)
    public ResponseEntity<Map<String, Object>> withdraw(@PathVariable String accountNumber, @RequestParam Double amount) {
        accountService.withdraw(accountNumber, amount);

        Map<String, Object> response = new HashMap<>();
        response.put("message", String.format(Constants.WITHDRAW_SUCCESS, amount));
        response.put("newBalance", accountService.findAccountByNr(accountNumber).getBalance());

        return ResponseEntity.ok(response);
    }

    /**
     * Transfers an amount from one account to another.
     * @param transferDto DTO containing transfer details.
     * @return ResponseEntity with a success message.
     */
    @PostMapping(Constants.TRANSFER_PATH)
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody TransferDto transferDto) {
        accountService.transfer(transferDto.getSourceAccountNumber(), transferDto.getTargetAccountNumber(), transferDto.getTransferAmount());

        Map<String, Object> response = new HashMap<>();
        response.put("message", String.format(Constants.TRANSFER_SUCCESS, transferDto.getTransferAmount()));
        response.put("sourceAccount", transferDto.getSourceAccountNumber());
        response.put("targetAccount", transferDto.getTargetAccountNumber());
        response.put("transferAmount", transferDto.getTransferAmount());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves transactions for a specific account.
     * @param accountNumber The account number to retrieve transactions for.
     * @return ResponseEntity containing a list of transactions or 404 if not found.
     */
    @GetMapping(Constants.ACCOUNT_PATH + Constants.TRANSACTIONS_PATH)
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@PathVariable String accountNumber) {
        List<Transaction> transactions = accountService.getTransactionsForAccount(accountNumber);

        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }

    /**
     * Deletes an account by its account number.
     * @param accountNumber The account number to delete.
     * @return ResponseEntity with status indicating success or failure.
     */
    @DeleteMapping(Constants.ACCOUNT_PATH)
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
    try {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build(); // Return 204 No Content status
    } catch (ResourceNotFoundException ex) {
        return ResponseEntity.notFound().build(); // Return 404 Not Found if account doesn't exist
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Handle other exceptions
    }
}

}
