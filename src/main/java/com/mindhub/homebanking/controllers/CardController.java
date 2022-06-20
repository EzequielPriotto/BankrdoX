package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


import static com.mindhub.homebanking.Utils.Utils.GenerateRandomNumberCard;
import static com.mindhub.homebanking.Utils.Utils.SelectLimit;
import static com.mindhub.homebanking.models.CardType.CREDIT;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/cards/")
    public List<CardDTO> getCards() {
        return cardService.getCardsDTO();
    }

    @GetMapping("/clients/current/cards/")
    public List<CardDTO> getCurrentCards(Authentication authentication){
        Client client = clientService.getClient(authentication.getName());
        return client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping(path = "/clients/current/cards/")
    public ResponseEntity<Object> createCurrentCards(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication) {

        try{
            Client client = clientService.getClient(authentication.getName());
            String number = GenerateRandomNumberCard(0,9, cardType);
            String cardHolder = client.getFullName();
            int cvv =(int) ((Math.random() * (99 - 999)) + 999);

            if(cardType == CREDIT && client.getCardsCredit().size() >= 3){
                return new ResponseEntity<>("Max limit credit card created", HttpStatus.FORBIDDEN);
            }
            if(cardType == CardType.DEBIT && client.getCardsDebit().size() >= 3){
                return new ResponseEntity<>("Max limit debit card created", HttpStatus.FORBIDDEN);
            }
            int limitCard = SelectLimit(cardColor);
            Card card = new Card(cardHolder,cardType,cardColor,number,cvv,limitCard);
            client.addCard(card);
            cardService.saveCard(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>("Error in creation process",HttpStatus.FORBIDDEN);
        }


    }


    @Transactional
    @PostMapping("/clients/current/cards/disable/")
    public ResponseEntity<Object> activeCard(Authentication authentication, @RequestParam String cardNumber) {

        Client client = clientService.getClient(authentication.getName());
        Card card = cardService.getCardByNumber(cardNumber);
        if (cardNumber.isEmpty())
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        if (client == null)
            return new ResponseEntity<>("Client not authorized", HttpStatus.FORBIDDEN);

        if (card == null)
            return new ResponseEntity<>("Card not exist", HttpStatus.FORBIDDEN);

        if (!client.getAccounts().contains(card))
            return new ResponseEntity<>("Card no is of this client", HttpStatus.FORBIDDEN);

        if (card.getExpense() > 0)
            return new ResponseEntity<>("Card have expenses", HttpStatus.FORBIDDEN);

        boolean isActive = card.isActive();
        card.setActive(!isActive);
        cardService.saveCard(card);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}
