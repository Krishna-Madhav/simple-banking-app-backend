package de.markant.lksg.application.task.repository;

import de.markant.lksg.application.task.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findAccountByAccountNr(String accountNumber);
}
