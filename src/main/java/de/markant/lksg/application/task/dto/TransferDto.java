package de.markant.lksg.application.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private Double transferAmount;
}
