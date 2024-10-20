package de.markant.lksg.application.task.controller;

import de.markant.lksg.application.task.dto.AccountDto;
import de.markant.lksg.application.task.model.Account;
import de.markant.lksg.application.task.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        // Clean up repository
        accountRepository.deleteAll();
    }

    @Test
    void testCreateAccountIntegration() throws Exception {
        AccountDto accountDto = new AccountDto("12345", 1000.0);

        MvcResult result = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNr\": \"12345\", \"balance\": 1000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNr", is("12345")))
                .andExpect(jsonPath("$.balance", is(1000.0)))
                .andReturn();

        // Optionally verify repository state
        Optional<Account> accountOpt = accountRepository.findAccountByAccountNr("12345");
        assertEquals(true, accountOpt.isPresent());
        assertEquals(1000.0, accountOpt.get().getBalance());
    }

    @Test
    void testDepositIntegration() throws Exception {
        Account account = new Account("12345", 1000.0);
        accountRepository.save(account);

        mockMvc.perform(post("/accounts/12345/deposit")
                        .param("amount", "500")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Amount 500.0 deposited successfully!")))
                .andExpect(jsonPath("$.newBalance", is(1500.0)));

        // Optionally verify repository state
        Account updatedAccount = accountRepository.findAccountByAccountNr("12345").orElse(null);
        assertEquals(1500.0, updatedAccount.getBalance());
    }

    @Test
    void testDeleteAccountIntegration() throws Exception {
        Account account = new Account("12345", 1000.0);
        accountRepository.save(account);

        mockMvc.perform(delete("/accounts/12345"))
                .andExpect(status().isNoContent());

        // Verify account is deleted
        Optional<Account> deletedAccount = accountRepository.findAccountByAccountNr("12345");
        assertEquals(false, deletedAccount.isPresent());
    }
}
