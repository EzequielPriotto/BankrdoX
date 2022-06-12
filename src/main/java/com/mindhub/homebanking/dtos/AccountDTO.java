package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class AccountDTO {

    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;


    private Set<TransactionDTO> transactions = new HashSet<>();

    private String cvu;

    private AccountType accountType;

    public AccountDTO (Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.cvu = account.getCvu();
        this.accountType = account.getAccountType();
    }

}
