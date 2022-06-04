package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.CardType.CREDIT;
import static java.util.stream.Collectors.toList;

@Entity  // crea la tabla en base a la entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();


    private String firstName, lastName, email;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> loans = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

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


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        loans.add(clientLoan);
    }

    public List<Loan> getLoansClient() {return loans.stream().map(loan -> loan.getLoan()).collect(toList());}

    public Set<ClientLoan> getLoans() {return loans;}

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }


    public void addCard(Card card) {
        card.setClient(this);
        cards.add(card);
    }

    public Set<Card> getCards() {
        return cards;
    }
    public List<Card> getCardsDebit() {
        return cards.stream().filter(card -> card.getCardType() == CardType.DEBIT).collect(Collectors.toList());
    }
    public List<Card> getCardsCredit() {
        return cards.stream().filter(card -> card.getCardType() == CREDIT).collect(Collectors.toList());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String verificationCode) {
        this.token = verificationCode;
    }

    public void deleteToken() {
        this.token = "";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}

