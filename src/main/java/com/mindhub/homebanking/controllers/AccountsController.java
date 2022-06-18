package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.Utils.*;

@RestController
@RequestMapping("/api")
public class AccountsController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccountsDTO();
    }

    @GetMapping("accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountDtoById(id);
    }

    @PostMapping(path = "/clients/current/accounts/")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType accountType){
        Client client = clientService.getClient(authentication.getName());
        List<Account> accounts = accountService.getAccountsByClient(client);
        List<Account> accountsActives = accounts.stream().filter(account -> account.isActive()).collect(Collectors.toList());
                if(accountsActives.size() != 3){
                    String cvu = accountType == AccountType.DOLAR? GenerateRandomNumberCVU(): GenerateRandomNumberAddress();
                    Account account = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now(), 0.0, cvu,accountType);
                    client.addAccount(account);
                    accountService.saveAccount(account);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
                else{
                  return new ResponseEntity<>("Max limit account created", HttpStatus.FORBIDDEN);
                }
    }

    @GetMapping("/clients/current/accounts/")
    public List<AccountDTO> getCurrentAccounts() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("/clients/current/accounts/{id}")
    public AccountDTO getCurrentAccount(@PathVariable Long id){
        return accountService.getAccountDtoById(id);
    }

    @Transactional
    @PostMapping("/clients/current/accounts/disable/")
    public ResponseEntity<Object> activeAccount(Authentication authentication, @RequestParam String accountNumber) {

        Client client = clientService.getClient(authentication.getName());
        Account account = accountService.getAccountByNumber(accountNumber);

        if (accountNumber.isEmpty())
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        if (client == null)
            return new ResponseEntity<>("Client not authorized", HttpStatus.FORBIDDEN);

        if (account == null)
            return new ResponseEntity<>("Account not exist", HttpStatus.FORBIDDEN);

        if (!client.getAccounts().contains(account))
            return new ResponseEntity<>("Account no is of this client", HttpStatus.FORBIDDEN);

        boolean isActive = account.isActive();
        account.setActive(!isActive);
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }




    }
