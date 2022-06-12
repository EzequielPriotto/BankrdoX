package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    AccountRepository accountRepository;


    @Override
    public Account getAccountByNumber(String number) {
       if (number.contains("VIN")){
           return accountRepository.findByNumber(number);
       }
       else {
           return accountRepository.findByCvu(number);
       }
    }

    @Override
    public AccountDTO getAccountDtoById(long id) {
        return new AccountDTO(accountRepository.getById(id));
    }

    @Override
    public AccountDTO getAccountDTO(Long id) {
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @Override
    public List<Account> getAccountsByClient(Client client) {
        return accountRepository.findByClient(client);
    }

    @Override
    public List<AccountDTO> getAccountsDTO() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

}
