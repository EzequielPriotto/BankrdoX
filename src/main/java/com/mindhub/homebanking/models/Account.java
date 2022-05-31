package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity  // crea la tabla en base a la entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();
    @Column(unique = true)
    private String number;

    @Column(unique = true)
    private LocalDateTime creationDate;
    private double balance;


    public Account(){}

    public Account(String number, LocalDateTime creationDate, double balance){
        this.number =  number;
        this.creationDate = creationDate;
        this.balance = balance;

    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransaction(Transaction transaction) {
        transaction.setAccountOwner(this);
        transactions.add(transaction);
    }
}

