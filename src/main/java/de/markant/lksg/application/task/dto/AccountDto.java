package de.markant.lksg.application.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for Account information.
 * This class is used to encapsulate the data required for account creation.
 */

@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    /** The account number. */
    private String accountNr;

    /** The current balance of the account. */
    private Double balance;
}
