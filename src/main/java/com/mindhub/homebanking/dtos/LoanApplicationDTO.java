package com.mindhub.homebanking.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoanApplicationDTO {
    private long idLoan;
    private int amount;
    private int payments;
    private String numberAccount;
}
