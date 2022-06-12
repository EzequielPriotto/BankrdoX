package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    Loan getLoanById(long id);
    List<Loan> getLoans();

    void saveClientLoan(ClientLoan clientLoan);
}
