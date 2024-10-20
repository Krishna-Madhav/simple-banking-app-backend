package de.markant.lksg.application.task.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


/**
 * Entity class representing a bank account.
 * This class maps to the 'accounts' table in the database and
 * contains information about the account, including its transactions.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(unique = true, nullable = false)
    private String accountNr;

    private Double balance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account") // Defines a one-to-many relationship with Transaction
    private List<Transaction> transactions;

    /**
     * Custom builder for creating an Account instance with account number and balance.
     * @param accountNr The unique account number.
     * @param balance The initial balance of the account.
     */
    @Builder
    public Account(String accountNr, Double balance) {
        this.accountNr = accountNr;
        this.balance = balance;
    }
}