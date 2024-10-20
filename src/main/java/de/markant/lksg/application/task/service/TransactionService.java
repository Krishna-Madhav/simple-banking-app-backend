package de.markant.lksg.application.task.service;

import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The TransactionService handles transaction operations like fetching all transactions
 * and fetching transaction for specific account.
 * It interacts with the TransactionRepository for data access.
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Retrieves all transactions.
     * @return List of all Transaction entities.
     */
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    /**
     * Fetches all transactions for a specific account identified by its account number.
     * @param accountNumber The account number for which to fetch transactions.
     * @return List of Transaction entities associated with the specified account.
     */

    public List<Transaction> getTransactionsByAccountNr(String accountNumber){
        return transactionRepository.findByAccount_AccountNr(accountNumber);
    }
}
