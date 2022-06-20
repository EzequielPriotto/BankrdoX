package com.mindhub.homebanking.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Unmodifiable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Entity  // crea la tabla en base a la entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Unmodifiable
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    @Unmodifiable
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    @Column(unique = true)
    @Unmodifiable
    private String number;


    @Unmodifiable
    private LocalDateTime creationDate;

    private double balance;

    @Unmodifiable
    private String cvu;
    @Unmodifiable
    private AccountType accountType;

    private boolean active;
    public Account(){}

    public Account(String number, LocalDateTime creationDate, double balance, String cvu, AccountType accountType ){
        this.number =  number;
        this.creationDate = creationDate;
        this.balance = accountType.equals(AccountType.CRYPTO) ? balance / 20532 : balance;
        this.cvu = cvu;
        this.accountType = accountType;
        this.active = true;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }
    public void addBalance(double amountAdd){
        this.balance += amountAdd;
    }
    public void restBalance(double amountAdd){
        this.balance -= amountAdd;
    }

}

