package de.markant.lksg.application.task.repository;

import de.markant.lksg.application.task.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Transaction entities.
 * This interface extends JpaRepository and provides methods for CRUD operations
 * and custom query methods related to transactions.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    /**
     * Retrieves a list of transactions associated with a specific account number.
     *
     * @param accountNr The unique account number for which to find transactions.
     * @return A list of Transaction objects associated with the specified account number.
     */
    List<Transaction> findByAccount_AccountNr(String accountNr);

}
