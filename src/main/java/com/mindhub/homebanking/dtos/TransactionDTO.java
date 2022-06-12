package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionDTO {
    private long id;


    private double amount;
    private String description;
    private LocalDateTime date;

    private TransactionType type;

    private String link;

    private String accountNumber;

    private String idEncrypted;


    public TransactionDTO (Transaction transaction){
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.link = "http://localhost:8080/rest/transactions/" + this.id;
        this.accountNumber = transaction.getAccount().getNumber();
        this.idEncrypted = transaction.getIdEncrypted();
    }

}
