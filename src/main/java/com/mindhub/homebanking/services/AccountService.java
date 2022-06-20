package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {

    Account getAccountByNumber(String number);
    AccountDTO getAccountDtoById(long id);
    AccountDTO getAccountDTO(Long id);
    List<Account> getAccountsByClient(Client client);
    List<AccountDTO> getAccountsDTO();

    List<Account> getAccounts();

    void saveAccount(Account account);

}
