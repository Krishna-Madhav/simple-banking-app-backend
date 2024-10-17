package de.markant.lksg.application.task.controller;

import de.markant.lksg.application.task.dto.TransferDto;
import de.markant.lksg.application.task.model.Account;
import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.service.AccountService;
import de.markant.lksg.application.task.dto.AccountDto; // Import the AccountDto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
        Account account = accountService.findAccountByNr(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto) {
        Account newAccount = accountService.createAccount(accountDto.getAccountNr(), accountDto.getBalance());
        accountService.createInitialTransaction(newAccount);

        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String accountNumber, @RequestParam Double amount) {
        accountService.deposit(accountNumber, amount);
        return ResponseEntity.ok("Amount " + amount + " deposited successfully!");
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable String accountNumber, @RequestParam Double amount) {
        accountService.withdraw(accountNumber, amount);
        return ResponseEntity.ok("Amount " + amount + " has been withdrawn successfully!");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferDto transferDto) { // Changed to @RequestBody
        accountService.transfer(transferDto.getSourceAccountNumber(), transferDto.getTargetAccountNumber(), transferDto.getTransferAmount());
        return ResponseEntity.ok("Amount " + transferDto.getTransferAmount() + " transferred successfully!");
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@PathVariable String accountNumber) {
        List<Transaction> transactions = accountService.getTransactionsForAccount(accountNumber);

        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }

    // **New method for deleting account**
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber); // Call the service to delete the account
        return ResponseEntity.noContent().build(); // Return 204 No Content status
    }
}
