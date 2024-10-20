package de.markant.lksg.application.task.service;

import de.markant.lksg.application.task.model.Transaction;
import de.markant.lksg.application.task.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTransactions() {

        List<Transaction> mockTransactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        mockTransactions.add(transaction1);
        mockTransactions.add(transaction2);

        when(transactionRepository.findAll()).thenReturn(mockTransactions);


        List<Transaction> result = transactionService.getAllTransactions();

        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetTransactionsByAccountNr() {

        String accountNumber = "123456789";
        List<Transaction> mockTransactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        mockTransactions.add(transaction1);
        mockTransactions.add(transaction2);

        when(transactionRepository.findByAccount_AccountNr(accountNumber)).thenReturn(mockTransactions);

        List<Transaction> result = transactionService.getTransactionsByAccountNr(accountNumber);

        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findByAccount_AccountNr(accountNumber);
    }
}
