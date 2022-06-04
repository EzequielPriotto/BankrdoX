package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;


    private double amount;
    private String description;
    private LocalDateTime date;

    private TransactionType type;


    public Transaction(){}


    public Transaction(double amount, String description, LocalDateTime date, Account account, TransactionType type) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
        this.type = type;
    }
    public Transaction(double amount, String description, Account account, TransactionType type) {
        this.amount = amount;
        this.description = description;
        this.date = LocalDateTime.now();
        this.account = account;
        this.type = type;
    }



    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
    public Account getAccountOwner() {
        return this.account;
    }

    public void setAccountOwner(Account account) {
        this.account = account;
    }
}
