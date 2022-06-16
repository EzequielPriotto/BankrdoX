package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.LoanType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LoanDTO {
    private long id;
    private List<Integer> payments = new ArrayList<>();
    private LoanType name;
    private int maxAmount;
    private String descriptionLoan;
    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.payments = loan.getPayments();
        this.maxAmount = loan.getMaxAmount();
        this.name = loan.getName();
        this.descriptionLoan = loan.getDescriptionLoan();
    }
}