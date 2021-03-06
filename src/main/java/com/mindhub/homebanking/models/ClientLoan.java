package com.mindhub.homebanking.models;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Unmodifiable;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Unmodifiable
    private long id;
    private int payments;
    private double amount;
    private double payment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    @Unmodifiable
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    @Unmodifiable
    private Loan loan;

    public ClientLoan(){}

    public ClientLoan(int payments, double amount, Client client, Loan loan,double payment) {
        this.payments = payments;
        this.amount = amount;
        this.client = client;
        this.loan = loan;
        this.payment = payment;
    }

}
