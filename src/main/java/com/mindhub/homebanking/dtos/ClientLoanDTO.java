package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.LoanType;

public class ClientLoanDTO {
    private long id;
    private long idLoan;

    private LoanType name;
    private double amount;
    private int payments;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoans) {
        this.id = clientLoans.getLoan().getId();
        this.idLoan= clientLoans.getId();
        this.name = clientLoans.getLoan().getName();
        this.amount = clientLoans.getAmount();
        this.payments = clientLoans.getPayments();
    }

    public long getId() {
        return id;
    }

    public long getIdLoan() {
        return idLoan;
    }


    public LoanType getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }


}
