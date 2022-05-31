package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.configurations.WebAuthentication;
import com.mindhub.homebanking.dtos.ClientDTO;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utils.GenerateRandomNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map(client -> new com.mindhub.homebanking.dtos.ClientDTO(client)).collect(toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){

       return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }


    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password, @RequestParam String userName) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || userName.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientRepository.findByEmail(email) != null ) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByUserName(userName) != null){
            return new ResponseEntity<>("Username already in use", HttpStatus.FORBIDDEN);
        }
        if(password.length() < 5){
            return new ResponseEntity<>("Short password", HttpStatus.FORBIDDEN);
        }
        if(userName.length() > 15){
            return new ResponseEntity<>("Username too long", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password), userName);
        Account account = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now(),0);
        client.addAccount(account);

        clientRepository.save(client);
        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @PatchMapping("/clients/current")
    public ResponseEntity<Object> changeCurrentClient(Authentication authentication, @RequestParam String avatar, @RequestParam String userName) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (avatar.isEmpty() || userName.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(!avatar.contains(".png")){
            return new ResponseEntity<>("File not supported", HttpStatus.FORBIDDEN);
        }
        client.setAvatar(avatar);
        client.setUserName(userName);
        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);


    }


}
