package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    ClientDTO getClientDTO(long id);
    List<ClientDTO> getClientsDTO();
    Client getClient(String credential);
    Client getClientToken(String token);
    void saveClient(Client client);

}
