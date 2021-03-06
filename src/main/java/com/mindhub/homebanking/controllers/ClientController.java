package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utils.*;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;


    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClientsDTO();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
       return clientService.getClientDTO(id);
    }


    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return new ClientDTO(clientService.getClient(authentication.getName()));
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password, @RequestParam String userName, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || userName.isEmpty())
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);


        if (userName.contains(" ") && userName.contains("@"))
            return new ResponseEntity<>("Character invalid", HttpStatus.FORBIDDEN);


        if (clientService.getClient(email) != null )
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);


        if (clientService.getClient(userName) != null)
            return new ResponseEntity<>("Username already in use", HttpStatus.FORBIDDEN);


        if(password.length() < 5)
            return new ResponseEntity<>("Short password", HttpStatus.FORBIDDEN);


        if(userName.length() > 15)
            return new ResponseEntity<>("Username too long", HttpStatus.FORBIDDEN);


        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password), userName);
        Account account = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now(),0, GenerateRandomNumberCVU(), AccountType.DOLAR);

        client.addAccount(account);

        String randomCode = GenerateToken(64);
        client.setToken(randomCode);
        client.setEnabled(false);

        clientService.saveClient(client);
        accountService.saveAccount(account);

        sendVerificationEmail(client);

        return new ResponseEntity<>("User created, email verification is required",HttpStatus.CREATED);
    }




    @GetMapping("/verifyToken/{token}")
    public ResponseEntity<?> verifyToken(@PathVariable String token) throws MessagingException, UnsupportedEncodingException {
        Client client = clientService.getClientToken(token);
        if(client == null)
            return new ResponseEntity<>("Token incorrect", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }


    @PostMapping("/clients/current/sendToken")
    public ResponseEntity<Object> sendTokenTemp(Authentication authentication) throws MessagingException, UnsupportedEncodingException {

        Client client = clientService.getClient(authentication.getName());

        if(client == null)
            return new ResponseEntity<>("You not have a authorization", HttpStatus.FORBIDDEN);

        String toAddress = client.getEmail();
        String fromAddress = "bankrdox@gmail.com";
        String senderName = "BankrdoX";
        String subject = "Identity Verification Code";
        String content = "<h2 style=\"color:black;\">Hi [[name]]!</h2>"
                + "<p style=\"color:black;\"> Do not share this code with anyone! </p>"
                +"<img src=\"https://i.imgur.com/DjW6seD.png\" alt=\"ImgRegister\" width=\"450\" height=\"302\"/> <br>"
                + "<h3 style=\"color:#ff5e14;\">[[code]]</h3>"
                + "<div style=\"display:flex;gap: 0.4rem;\"> <p style=\"color:black;\"> Thank you, </p> <p style=\"color:rgb(232, 91, 26);font-weight: bold;\"> BankrdoX teams. </p> </div> "
                ;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", client.getFullName());

        String code = GenerateCode(0,9);
        content = content.replace("[[code]]", code);

        helper.setText(content, true);
        mailSender.send(message);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }


    @Transactional
    @PostMapping("/activateAccount/{token}")
    public ResponseEntity<Object> activateAccount(HttpServletRequest request, @PathVariable String token){

        Client client = clientService.getClientToken(token);

        if(client == null)
            return new ResponseEntity<>("Token invalid", HttpStatus.FORBIDDEN);

        client.setEnabled(true);
        client.deleteToken();
        DeleteToken(token);
        clientService.saveClient(client);
        return new ResponseEntity<>( HttpStatus.ACCEPTED);
    }


    @PatchMapping("/clients/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam String token, @RequestParam String email, @RequestParam String newPassword) throws MessagingException, UnsupportedEncodingException {

        Client client = clientService.getClientToken(token);

        if(client == null)
            return new ResponseEntity<>("Token incorrect", HttpStatus.FORBIDDEN);

        if (!client.getEmail().equals(email))
            return new ResponseEntity<>("Email incorrect", HttpStatus.FORBIDDEN);

        if(newPassword.length() < 5)
            return new ResponseEntity<>("Password short", HttpStatus.FORBIDDEN);

        client.setPassword(passwordEncoder.encode(newPassword));
        client.deleteToken();
        clientService.saveClient(client);
        return new ResponseEntity<>( HttpStatus.OK);
    }


    @PostMapping("/clients/changePassword/sendToken")
    public ResponseEntity<Object> sendTokenRecoverPassword(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {

        if (email.isEmpty())
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);

        Client client = clientService.getClient(email);

        if (client == null)
            return new ResponseEntity<>("Email invalid", HttpStatus.FORBIDDEN);


        String toAddress = client.getEmail();
        String fromAddress = "bankrdox@gmail.com";
        String senderName = "BankrdoX";
        String subject = "Recover your password";
        String content = "<h2 style=\"color:black;\">Hi [[name]]!</h2>"
                + "<p style=\"color:black;\"> Please click the link below to recover your password: </p>"
                +"<img src=\"https://i.imgur.com/DjW6seD.png\" alt=\"ImgRegister\" width=\"450\" height=\"302\"/> <br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\" style=\"color:#ff5e14;\">RECOVER YOUR PASSWORD</a></h3>"
                + "<div style=\"display:flex;gap: 0.4rem;\"> <p style=\"color:black;\"> Thank you, </p> <p style=\"color:rgb(232, 91, 26);font-weight: bold;\"> BankrdoX teams. </p> </div> "
                ;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", client.getFullName());

        client.setToken(GenerateToken(64));

        String verifyURL = "https://bankrdox.herokuapp.com/web/recoverPassword.html?token=" + client.getToken();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("/clients/current")
    public ResponseEntity<Object> changeCurrentClient(Authentication authentication, @RequestParam String avatar, @RequestParam String userName) {
        Client client = clientService.getClient(authentication.getName());

        if (avatar.isEmpty() || userName.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (userName.contains(" ")) {
            return new ResponseEntity<>("Character invalid", HttpStatus.FORBIDDEN);
        }
        if(!avatar.contains(".png")){
            return new ResponseEntity<>("File not supported", HttpStatus.FORBIDDEN);
        }

        client.setAvatar(avatar);
        client.setUserName(userName);
        notificationService.getAllByClient(client).stream().forEach(notification -> {
            if (notification.getClient() == client){
                notification.setUrl(avatar);
            }
        });
        clientService.saveClient(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Transactional
    @PostMapping("/clients/current/disable")
    public ResponseEntity<Object> disableCurrentClient(Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        Client client = clientService.getClient(authentication.getName());

        if (client == null)
            return new ResponseEntity<>("You no are client",HttpStatus.FORBIDDEN);

        String randomCode = GenerateToken(64);
        client.setToken(randomCode);
        client.setEnabled(false);

        clientService.saveClient(client);
        sendDisableEmail(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    private void sendVerificationEmail(Client client)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = client.getEmail();
        String fromAddress = "bankrdox@gmail.com";
        String senderName = "BankrdoX";
        String subject = "Please verify your registration";
        String content = "<h2 style=\"color:black;\">Hi [[name]]!</h2>"
                + "<p style=\"color:black;\"> Please click the link below to verify your registration: </p>"
                +"<img src=\"https://i.imgur.com/DjW6seD.png\" alt=\"ImgRegister\" width=\"450\" height=\"302\"/> <br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\" style=\"color:#ff5e14;\">VERIFY YOUR ACCOUNT</a></h3>"
                + "<div style=\"display:flex;gap: 0.4rem;\"> <p style=\"color:black;\"> Thank you, </p> <p style=\"color:rgb(232, 91, 26);font-weight: bold;\"> BankrdoX teams. </p> </div> "
                ;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", client.getFullName());

        String verifyURL = "https://bankrdox.herokuapp.com/web/activateClient.html?token=" + client.getToken();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
    }

    private void sendDisableEmail(Client client)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = client.getEmail();
        String fromAddress = "bankrdox@gmail.com";
        String senderName = "BankrdoX";
        String subject = "Disable account";
        String content = "<h2 style=\"color:black;\">Hi [[name]]!</h2>"
                + "<p style=\"color:black;\"> You have recently terminated your BankrdoX account. We hope you will be back soon. If you wish, please click on the following link: </p>"
                +"<img src=\"https://i.imgur.com/DjW6seD.png\" alt=\"ImgRegister\" width=\"450\" height=\"302\"/> <br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\" style=\"color:#ff5e14;\">REACTIVE YOUR ACCOUNT</a></h3>"
                + "<div style=\"display:flex;gap: 0.4rem;\"> <p style=\"color:black;\"> Thank you, </p> <p style=\"color:rgb(232, 91, 26);font-weight: bold;\"> BankrdoX teams. </p> </div> "
                ;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", client.getFullName());

        String verifyURL = "https://bankrdox.herokuapp.com/web/activateClient.html?token=" + client.getToken();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
    }
}
