package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoanController {


    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Transactional
    @PostMapping("/clients/current/loans")
    public ResponseEntity<Object> makeLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

        Loan loan = loanRepository.findById(loanApplicationDTO.getIdLoan()).orElse(null);
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(loanApplicationDTO.getNumberAccount());

        if (Long.toString(loanApplicationDTO.getIdLoan()).isEmpty() || loanApplicationDTO.getAmount() == 0 ||
                loanApplicationDTO.getPayments() == 0 || loanApplicationDTO.getNumberAccount().isEmpty())
            return new ResponseEntity<>("Missing date", HttpStatus.FORBIDDEN);


        if(loan == null)
            return new ResponseEntity<>("Loan not exist", HttpStatus.FORBIDDEN);

        if(loanApplicationDTO.getAmount() > loan.getMaxAmount())
            return new ResponseEntity<>("Amount ordered in excess of the maximum allowed", HttpStatus.FORBIDDEN);

        if (account == null)
            return new ResponseEntity<>("Account not exist", HttpStatus.FORBIDDEN);


        if(client.getId() != account.getClient().getId())
            return new ResponseEntity<>("the customer does not own that account", HttpStatus.FORBIDDEN);

        if (loanApplicationDTO.getAmount() < 0)
            return new ResponseEntity<>("the amount is negative", HttpStatus.FORBIDDEN);



        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getPayments(),loanApplicationDTO.getAmount() * 1.20,client,loan);
        Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), "Deposit",account,TransactionType.CREDIT);
        account.addBalance(loanApplicationDTO.getAmount());

        transactionRepository.save(transaction);
        clientLoanRepository.save(clientLoan);
        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
