package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.LoanType;
import lombok.Getter;

@Getter
public class ClientLoanDTO {
    private long id;
    private long idLoan;

    private LoanType name;
    private double amount, payment;
    private int payments;


    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoans) {
        this.idLoan = clientLoans.getLoan().getId();
        this.id= clientLoans.getId();
        this.name = clientLoans.getLoan().getName();
        this.amount = clientLoans.getAmount();
        this.payments = clientLoans.getPayments();
        this.payment = clientLoans.getPayment();
    }

}
