package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.PasswordView;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    ClientService clientService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputName-> {
            Client client = clientService.getClient(inputName);

            if (client != null) {
                if (client.isEnabled()){
                    if(client.getEmail().contains("_admin@bank.com")){
                        return new User(client.getEmail(), client.getPassword(),AuthorityUtils.createAuthorityList("ADMIN"));
                    }
                    else {
                        return new User(client.getEmail(), client.getPassword(),AuthorityUtils.createAuthorityList("CLIENT"));
                    }
                }
                else {
                    throw new UsernameNotFoundException("Client no activated: " + inputName);
                }
            }
            else {
                throw new UsernameNotFoundException("Unknown client: " + inputName);
            }

        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
