package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class ClientDTO {
    private long id;
    private String firstName, lastName, email;
    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO> loans;
    private Set<CardDTO> cards;
    private String password, userName,avatar;
    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.password = client.getPassword();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.loans = client.getLoans().stream().map(loan-> new ClientLoanDTO(loan)).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(card-> new CardDTO(card)).collect(Collectors.toSet());
        this.userName = client.getUserName();
        this.avatar = client.getAvatar();
    }


}
