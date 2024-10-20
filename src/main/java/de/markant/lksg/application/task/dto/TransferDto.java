package de.markant.lksg.application.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for transferring funds between accounts.
 * This class is used to encapsulate the data required for a transfer operation.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {
    /** The account number from which funds will be withdrawn. */
    private String sourceAccountNumber;

    /** The account number to which funds will be deposited. */
    private String targetAccountNumber;

    /** The amount of money to be transferred. */
    private Double transferAmount;
}
