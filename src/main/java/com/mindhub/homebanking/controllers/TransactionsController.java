package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionService.getTransactionsDTO();
    }

    @Transactional
    @PostMapping("clients/current/transactions")
    public ResponseEntity<Object> makeTransactions(
            @RequestParam double amount,@RequestParam String description,
            @RequestParam String accountSNumber, @RequestParam String accountRNumber,
            Authentication authentication
    ) {

        Account accountS = accountService.getAccountByNumber(accountSNumber);
        Account accountR = accountService.getAccountByNumber(accountRNumber);
        Client client = clientService.getClient(authentication.getName());

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
        if (accountR.getAccountType() != accountS.getAccountType()){
            return new ResponseEntity<>("Incompatible accountType", HttpStatus.FORBIDDEN);

        }

        Transaction transactionDebit = new Transaction(amount ,description,accountS, TransactionType.DEBIT);
        Transaction transactionCredit = new Transaction(amount,description,accountR, TransactionType.CREDIT);

        accountS.restBalance(amount);
        accountR.addBalance(amount);

        transactionService.saveTransaction(transactionCredit);
        transactionService.saveTransaction(transactionDebit);

        accountService.saveAccount(accountR);
        accountService.saveAccount(accountS);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    @Transactional
    @PostMapping("clients/current/transactions/verify")
    public ResponseEntity<Object> verifyTransactions(
            @RequestParam double amount,@RequestParam String description,
            @RequestParam String accountSNumber, @RequestParam String accountRNumber,
            Authentication authentication
    ) {

        Account accountS = accountService.getAccountByNumber(accountSNumber);
        Account accountR = accountService.getAccountByNumber(accountRNumber);
        Client client = clientService.getClient(authentication.getName());

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
        if (accountR.getAccountType() != accountS.getAccountType()){
            return new ResponseEntity<>("Incompatible accountType", HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>("data correct",HttpStatus.CREATED);
    }

    }
