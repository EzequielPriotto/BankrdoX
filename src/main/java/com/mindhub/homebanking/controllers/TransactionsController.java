package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionsController {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionRepository.findAll().stream().map(transaction -> new TransactionDTO(transaction)).collect(toList());
    }

    @Transactional
    @PostMapping("clients/current/transactions")
    public ResponseEntity<Object> makeTransactions(
            @RequestParam double amount,@RequestParam String description,
            @RequestParam String accountSNumber, @RequestParam String accountRNumber,
            Authentication authentication
    ) {

        Account accountS = accountRepository.findByNumber(accountSNumber);
        Account accountR = accountRepository.findByNumber(accountRNumber);
        Client client = clientRepository.findByEmail(authentication.getName());

        if (amount == 0 || description.isEmpty() || accountSNumber.isEmpty() || accountRNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(accountSNumber == accountRNumber){
            return new ResponseEntity<>("The accounts are same", HttpStatus.FORBIDDEN);
        }
        if(accountS == null){
            return new ResponseEntity<>("The account origin dont exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountS)){
            return new ResponseEntity<>("The client no is the account owner", HttpStatus.FORBIDDEN);
        }
        if(accountR == null){
            return new ResponseEntity<>("The account destiny dont exist", HttpStatus.FORBIDDEN);
        }
        if (accountS.getBalance() < amount ){
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }
        if (amount < 0){
            return new ResponseEntity<>("Incorrect value", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(amount * -1,description,accountS, TransactionType.DEBIT);
        Transaction transactionCredit = new Transaction(amount,description,accountR, TransactionType.CREDIT);

        accountS.restBalance(amount);
        accountR.addBalance(amount);

        transactionRepository.save(transactionCredit);
        transactionRepository.save(transactionDebit);

        accountRepository.save(accountR);
        accountRepository.save(accountS);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    }
