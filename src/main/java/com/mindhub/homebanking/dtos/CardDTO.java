package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CardDTO {

    private long id;
    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private int cvv;
    private int limitCard;
    private int expense;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;

    public CardDTO() {
    }
    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.limitCard = card.getLimitCard();
        this.expense = card.getExpense();
    }
}
