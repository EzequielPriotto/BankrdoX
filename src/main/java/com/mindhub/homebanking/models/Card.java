package com.mindhub.homebanking.models;

import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private int cvv;

    private LocalDateTime thruDate;
    private LocalDateTime fromDate;

    public Card(){}
    public Card(Client client, CardType cardType, CardColor cardColor, String number, int cvv,LocalDateTime fromDate , LocalDateTime thruDate) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.cardHolder = client.getFullName();
    }

    public Card(String cardHolder, CardType cardType, CardColor cardColor, String number, int cvv) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = LocalDateTime.now().plusYears(5);
        this.fromDate = LocalDateTime.now();
        this.cardHolder = cardHolder;
    }



    public long getId() {
        return id;
    }
    public String getCardHolder() {
        return cardHolder;
    }
    public CardType getCardType() {
        return cardType;
    }
    public CardColor getCardColor() {
        return cardColor;
    }

    public String getNumber() {
        return number;
    }
    public int getCvv() {
        return cvv;
    }
    public LocalDateTime getThruDate() {
        return thruDate;
    }
    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

}
