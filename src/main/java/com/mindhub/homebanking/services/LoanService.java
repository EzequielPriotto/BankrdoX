package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    Loan getLoanById(long id);
    List<Loan> getLoans();
    List<LoanDTO> getLoansDTO();
    List<ClientLoanDTO> getClientLoansDTOCurrent(Client client);

    boolean existLoan(long id);
    void saveClientLoan(ClientLoan clientLoan);
}
