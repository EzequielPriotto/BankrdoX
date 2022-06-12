package com.mindhub.homebanking.models;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Unmodifiable;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Unmodifiable
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    @Unmodifiable
    private Client client;

    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private int cvv;

    private int limitCard;
    private int expense;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;

    public Card(){}
    public Card(Client client, CardType cardType, CardColor cardColor, String number, int cvv,LocalDateTime fromDate , LocalDateTime thruDate, int limitCard) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.cardHolder = client.getFullName();
        this.limitCard = limitCard;
        this.expense = 13000;
    }

    public Card(String cardHolder, CardType cardType, CardColor cardColor, String number, int cvv, int limitCard) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = LocalDateTime.now().plusYears(5);
        this.fromDate = LocalDateTime.now();
        this.cardHolder = cardHolder;
        this.limitCard = limitCard;
        this.expense = 0;
    }


}
