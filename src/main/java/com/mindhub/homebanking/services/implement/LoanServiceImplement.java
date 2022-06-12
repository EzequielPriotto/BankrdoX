package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Override
    public Loan getLoanById(long id) {
        return loanRepository.getById(id);
    }

    @Override
    public List<Loan> getLoans() {
        return loanRepository.findAll();
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
