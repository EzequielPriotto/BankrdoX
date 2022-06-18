package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {


    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    LoanService loanService;
    @Autowired
    TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoansDto(){
        return  loanService.getLoansDTO();
    }
    @GetMapping("/clients/current/loans")
    public List<ClientLoanDTO> getClientLoansDto(Authentication authentication){
        return  loanService.getClientLoansDTOCurrent(clientService.getClient(authentication.getName()));
    }

    @Transactional
    @PostMapping("/clients/current/loans")
    public ResponseEntity<Object> makeLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

        Loan loan = loanService.getLoanById(loanApplicationDTO.getIdLoan());
        Client client = clientService.getClient(authentication.getName());
        Account account = accountService.getAccountByNumber(loanApplicationDTO.getNumberAccount());
        List<Long> ids = client.getLoans().stream().map(clientLoan -> clientLoan.getLoan().getId() ).collect(Collectors.toList());

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

        if (ids.contains(loanApplicationDTO.getIdLoan()))
            return new ResponseEntity<>("The client have this type of loan", HttpStatus.FORBIDDEN);

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getPayments(),loanApplicationDTO.getAmount() * loan.getFee(),client,loan,(loanApplicationDTO.getAmount() * loan.getFee()) / loanApplicationDTO.getPayments());
        Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), "Transfer", "Loan of " + loanApplicationDTO.getAmount() + " usd",account,TransactionType.CREDIT,account.getBalance(), account.getBalance() + loanApplicationDTO.getAmount(), "USD");
        account.addBalance(loanApplicationDTO.getAmount());


        transactionService.saveTransaction(transaction);
        loanService.saveClientLoan(clientLoan);
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }






}
