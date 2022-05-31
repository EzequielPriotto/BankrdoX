package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.Utils.GenerateRandomNumberCard;
import static com.mindhub.homebanking.models.CardType.CREDIT;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/cards/")
    public List<CardDTO> getCards() {
        return cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(toList());
    }

    @RequestMapping("/clients/current/cards/")
    public List<CardDTO> getCurrentCards(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

    @RequestMapping(path = "/clients/current/cards/",  method = RequestMethod.POST)
    public ResponseEntity<Object> createCurrentCards(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication) {

        try{
            Client client = clientRepository.findByEmail(authentication.getName());
            String number = GenerateRandomNumberCard(0,9, cardType);
            String cardHolder = client.getFullName();
            int cvv =(int) ((Math.random() * (99 - 999)) + 999);

            if(cardType == CREDIT && client.getCardsCredit().size() >= 3){
                return new ResponseEntity<>("Max limit credit card created", HttpStatus.FORBIDDEN);
            }
            if(cardType == CardType.DEBIT && client.getCardsDebit().size() >= 3){
                return new ResponseEntity<>("Max limit debit card created", HttpStatus.FORBIDDEN);
            }

            Card card = new Card(cardHolder,cardType,cardColor,number,cvv);
            client.addCard(card);
            cardRepository.save(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>("Error in creation process",HttpStatus.FORBIDDEN);
        }


    }


}
