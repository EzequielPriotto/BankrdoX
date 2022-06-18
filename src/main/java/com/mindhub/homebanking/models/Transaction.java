package com.mindhub.homebanking.models;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Unmodifiable;
import javax.persistence.*;
import java.time.LocalDateTime;
import static com.mindhub.homebanking.Utils.Utils.GenerateToken;
@Getter
@Setter
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Unmodifiable
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    @Unmodifiable
    private Account account;

    private double amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;
    @Unmodifiable
    private String idEncrypted;

    private String cryptoOrUsd,category;
    private double oldBalance, newBalance;

    public Transaction(){}


    public Transaction(double amount,String category, String description, LocalDateTime date, Account account, TransactionType type, double oldBalance, double newBalance, String cryptoOrUsd) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
        this.account = account;
        this.type = type;
        this.idEncrypted = GenerateToken(15);
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.cryptoOrUsd = cryptoOrUsd;
    }
    public Transaction(double amount,String category, String description, Account account, TransactionType type, double oldBalance, double newBalance, String cryptoOrUsd) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = LocalDateTime.now();
        this.account = account;
        this.type = type;
        this.idEncrypted = GenerateToken(15);
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.cryptoOrUsd = cryptoOrUsd;
    }

}
