package de.markant.lksg.application.task.repository;

import de.markant.lksg.application.task.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Account entities.
 * This interface extends JpaRepository, providing methods for CRUD operations
 * and custom query methods for Account-related data.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    /**
     * Retrieves an account by its unique account number.
     *
     * @param accountNumber The unique account number to search for.
     * @return An Optional containing the Account if found, or empty if not.
     */
    Optional<Account> findAccountByAccountNr(String accountNumber);
}
