package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utils.GenerateRandomNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountsController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientController clientController;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @RequestMapping("accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){

        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @RequestMapping(path = "/clients/current/accounts/", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        List<Account> accounts = accountRepository.findByClient(client);

                if(accounts.size() != 3){
                    Account account = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now(), 0.0);
                    client.addAccount(account);
                    accountRepository.save(account);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
                else{
                  return new ResponseEntity<>("Max limit account created", HttpStatus.FORBIDDEN);
                }
    }

    @RequestMapping("/clients/current/accounts/")
    public List<AccountDTO> getCurrentAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @RequestMapping("/clients/current/accounts/{id}")
    public AccountDTO getCurrentAccount(@PathVariable Long id){

        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }
}
