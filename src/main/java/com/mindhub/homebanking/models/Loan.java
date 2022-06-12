package com.mindhub.homebanking.models;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Unmodifiable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
@Getter
@Setter
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Unmodifiable
    private long id;


    @ElementCollection
    @Column(name="payments")
    private List<Integer> payments = new ArrayList<>();

    private LoanType name;
    private int maxAmount;
    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> loans;

    public Loan(){}

    public Loan(List<Integer> payments, LoanType name, int maxAmount) {
        this.payments = payments;
        this.name = name;
        this.maxAmount = maxAmount;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        loans.add(clientLoan);
    }

}
