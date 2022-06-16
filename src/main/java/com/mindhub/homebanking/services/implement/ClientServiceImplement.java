package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public ClientDTO getClientDTO(long id) {
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @Override
    public List<ClientDTO> getClientsDTO() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public Client getClient(String credential) {
        if (credential.contains("@")){
            return clientRepository.findByEmail(credential);
        }
        else{
            return clientRepository.findByUserName(credential);
        }
    }

    @Override
    public Client getClientToken(String token) {
        return clientRepository.findByToken(token);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public boolean existClient(long id) {
        return clientRepository.existsById(id);
    }
}
