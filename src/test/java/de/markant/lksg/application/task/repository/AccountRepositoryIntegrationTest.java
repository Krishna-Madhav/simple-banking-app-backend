package de.markant.lksg.application.task.repository;

import de.markant.lksg.application.task.model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional // Rollback after each test
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        // Cleans the repository before each test
        accountRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        // Cleanup after each test
        accountRepository.deleteAll();
    }

    @Test
    public void testFindAccountByAccountNr() {

        String accountNumber = "123456";
        Account account = new Account();
        account.setAccountNr(accountNumber);
        account.setBalance(100.0);
        accountRepository.save(account);

        Optional<Account> foundAccount = accountRepository.findAccountByAccountNr(accountNumber);

        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getAccountNr()).isEqualTo(accountNumber);
    }

    @Test
    public void testFindAccountByAccountNr_NotFound() {

        String accountNumber = "non-existent-account";

        Optional<Account> foundAccount = accountRepository.findAccountByAccountNr(accountNumber);

        assertThat(foundAccount).isNotPresent();
    }
}
