package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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

    public long getId() {
        return id;
    }


    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public LoanType getName() {
        return name;
    }

    public void setName(LoanType name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }


    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        loans.add(clientLoan);
    }

    public List<Client> getClient() {
        return loans.stream().map(loan -> loan.getClient()).collect(toList());
    }



}
