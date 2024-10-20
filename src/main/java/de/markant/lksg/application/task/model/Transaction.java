package de.markant.lksg.application.task.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity class representing a financial transaction.
 * This class maps to the 'transactions' table in the database and
 * contains details about individual transactions related to accounts.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne // Defines a many-to-one relationship with Account
    @JsonIgnore
    private Account account;

    @Enumerated(EnumType.STRING) // Specifies that the transaction type will be stored as a string in the database
    private TransactionType transactionType;

    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMMM dd, yyyy, HH:mm") // Specifies the timestamp format
    private LocalDateTime timeStamp;

    private Double oldBalance;
    private Double newBalance;
    
    private Double transactionAmount;

    private String targetAccountNr;

    /**
     * Custom builder for creating a Transaction instance.
     * @param account The account associated with the transaction.
     * @param transactionType The type of the transaction (e.g., DEPOSIT, WITHDRAWAL).
     * @param oldBalance The balance before the transaction.
     * @param newBalance The balance after the transaction.
     * @param transactionAmount The amount of the transaction.
     * @param targetAccountNr The target account number if the transaction involves another account.
     * @param timeStamp The date and time of the transaction. If null, the current time will be used.
     */
    @Builder(builderMethodName = "transactionBuilder")
    public Transaction(Account account, TransactionType transactionType, Double oldBalance, Double newBalance, Double transactionAmount, String targetAccountNr, LocalDateTime timeStamp) {
        this.account = account;
        this.transactionType = transactionType;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.transactionAmount = transactionAmount;
        this.targetAccountNr = targetAccountNr;
        this.timeStamp = (timeStamp == null) ? LocalDateTime.now() : timeStamp;
    }
}