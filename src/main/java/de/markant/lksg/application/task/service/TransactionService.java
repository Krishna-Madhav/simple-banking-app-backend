package de.markant.lksg.application.task.service;

import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Get all transactions
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    // Get all transactions for a specific account by accountNumber
    public List<Transaction> getTransactionsByAccountNr(String accountNumber){
        return transactionRepository.findByAccount_AccountNr(accountNumber);
    }
}
