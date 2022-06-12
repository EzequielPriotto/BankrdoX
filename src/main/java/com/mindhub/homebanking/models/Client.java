package com.mindhub.homebanking.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Unmodifiable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.CardType.CREDIT;
@Setter
@Getter
@Entity  // crea la tabla en base a la entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Unmodifiable
    private long id;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();


    private String firstName, lastName, email;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> loans = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    @Unmodifiable
    private String password, userName;
    private String avatar = "./assets/avatares/avatar1.png";


    private String token;
    private boolean enabled;

    public Client(){}
    public Client(String firstName, String lastName, String email, String password, String userName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userName = userName;
    }


    public String getFullName(){
        return this.firstName + " " + this.getLastName();
    }


    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }
    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        loans.add(clientLoan);
    }
    public void addCard(Card card) {
        card.setClient(this);
        cards.add(card);
    }


    public List<Card> getCardsDebit() {
        return cards.stream().filter(card -> card.getCardType() == CardType.DEBIT).collect(Collectors.toList());
    }
    public List<Card> getCardsCredit() {
        return cards.stream().filter(card -> card.getCardType() == CREDIT).collect(Collectors.toList());
    }



    public void deleteToken() {
        this.token = "";
    }
    public boolean isEnabled() {
        return this.enabled;
    }



}

