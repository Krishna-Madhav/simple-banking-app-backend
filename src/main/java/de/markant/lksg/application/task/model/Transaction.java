package de.markant.lksg.application.task.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JsonIgnore
    private Account account;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private LocalDateTime timeStamp;

    private Double oldBalance;
    private Double newBalance;
    
    private Double transactionAmount;

    private String targetAccountNr; 

    @Builder(builderMethodName = "transactionBuilder")
    public Transaction(Account account, TransactionType transactionType, Double oldBalance, Double newBalance, Double transactionAmount, String targetAccountNr) {
        this.account = account;
        this.transactionType = transactionType;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.transactionAmount = transactionAmount;
        this.targetAccountNr = targetAccountNr;
        this.timeStamp = LocalDateTime.now();
    }
}