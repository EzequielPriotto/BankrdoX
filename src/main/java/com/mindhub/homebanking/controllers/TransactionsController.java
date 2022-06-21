package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.CardApplicationDTO;
import com.mindhub.homebanking.dtos.ResumeApplicationDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.Utils.MakePDF;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@RestController
@RequestMapping("/api")
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionService.getTransactionsDTO();
    }

    @Transactional
    @PostMapping("clients/current/transactions")
    public ResponseEntity<Object> makeTransactions(
            @RequestParam double amount, @RequestParam String category, @RequestParam String description,
            @RequestParam String accountSNumber, @RequestParam String accountRNumber,
            Authentication authentication
    ) {

        Account accountS = accountService.getAccountByNumber(accountSNumber);
        Account accountR = accountService.getAccountByNumber(accountRNumber);
        Client client = clientService.getClient(authentication.getName());

        if (amount == 0 || description.isEmpty() || accountSNumber.isEmpty() || accountRNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (accountSNumber == accountRNumber) {
            return new ResponseEntity<>("The accounts are same", HttpStatus.FORBIDDEN);
        }
        if (accountS == null) {
            return new ResponseEntity<>("The account origin dont exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(accountS)) {
            return new ResponseEntity<>("The client no is the account owner", HttpStatus.FORBIDDEN);
        }
        if (accountR == null) {
            return new ResponseEntity<>("The account destiny dont exist", HttpStatus.FORBIDDEN);
        }
        if (accountS.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }
        if (amount < 0) {
            return new ResponseEntity<>("Incorrect value", HttpStatus.FORBIDDEN);
        }
        if (accountR.getAccountType() != accountS.getAccountType()) {
            return new ResponseEntity<>("Incompatible accountType", HttpStatus.FORBIDDEN);

        }

        Transaction transactionDebit = new Transaction(amount, category, description, accountS, DEBIT, accountS.getBalance(), accountS.getBalance() - amount, "USD");
        Transaction transactionCredit = new Transaction(amount, category, description, accountR, TransactionType.CREDIT, accountR.getBalance(), accountR.getBalance() + amount, "USD");

        accountS.restBalance(amount);
        accountR.addBalance(amount);

        if (accountR.getClient() == accountS.getClient()) {
            Notification notificationOwn = new Notification(accountS.getClient().getUserName(), "Swap $" + amount + " from " + accountSNumber + " to " + accountRNumber, accountS.getClient().getAvatar());
            accountS.getClient().addNotification(notificationOwn);
            clientService.saveClient(accountS.getClient());
            notificationService.saveNotification(notificationOwn);
        } else {

            Notification notificationDebit = new Notification(accountR.getClient().getUserName(), "received $" + amount + " from you", accountR.getClient().getAvatar());
            Notification notificationCredit = new Notification(accountS.getClient().getUserName(), "sent you $" + amount, accountS.getClient().getAvatar());

            accountS.getClient().addNotification(notificationDebit);
            accountR.getClient().addNotification(notificationCredit);


            clientService.saveClient(accountS.getClient());
            clientService.saveClient(accountR.getClient());

            notificationService.saveNotification(notificationCredit);
            notificationService.saveNotification(notificationDebit);
        }

        transactionService.saveTransaction(transactionCredit);
        transactionService.saveTransaction(transactionDebit);

        accountService.saveAccount(accountR);
        accountService.saveAccount(accountS);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Transactional
    @PostMapping("clients/current/transactions/verify")
    public ResponseEntity<Object> verifyTransactions(
            @RequestParam double amount, @RequestParam String description,
            @RequestParam String accountSNumber, @RequestParam String accountRNumber,
            Authentication authentication
    ) {

        Account accountS = accountService.getAccountByNumber(accountSNumber);
        Account accountR = accountService.getAccountByNumber(accountRNumber);
        Client client = clientService.getClient(authentication.getName());

        if (amount == 0)
            return new ResponseEntity<>("Amount no available", HttpStatus.FORBIDDEN);

        if (description.isEmpty())
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        if (accountSNumber.isEmpty())
            return new ResponseEntity<>("Missing account origin", HttpStatus.FORBIDDEN);

        if (accountRNumber.isEmpty())
            return new ResponseEntity<>("Missing account destiny", HttpStatus.FORBIDDEN);

        if (accountSNumber == accountRNumber)
            return new ResponseEntity<>("The accounts are same", HttpStatus.FORBIDDEN);

        if (accountS == null)
            return new ResponseEntity<>("The account origin dont exist", HttpStatus.FORBIDDEN);

        if (!client.getAccounts().contains(accountS))
            return new ResponseEntity<>("The client no is the account owner", HttpStatus.FORBIDDEN);

        if (accountR == null)
            return new ResponseEntity<>("The account destiny dont exist", HttpStatus.FORBIDDEN);

        if (accountS.getBalance() < amount)
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);

        if (amount < 0)
            return new ResponseEntity<>("Incorrect value", HttpStatus.FORBIDDEN);

        if (accountR.getAccountType() != accountS.getAccountType())
            return new ResponseEntity<>("Incompatible accountType", HttpStatus.FORBIDDEN);

        return new ResponseEntity<>("data correct", HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("clients/current/swap")
    public ResponseEntity<Object> makeSwap(@RequestParam String accountCryptoNumber, @RequestParam String accountDollarNumber, @RequestParam double amount, @RequestParam String toFrom, Authentication authentication) {

        Client client = clientService.getClient(authentication.getName());
        Account accountCrypto = accountService.getAccountByNumber(accountCryptoNumber);
        Account accountDollar = accountService.getAccountByNumber(accountDollarNumber);

        if (toFrom.isEmpty())
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        if (amount < 0 || amount == 0)
            return new ResponseEntity<>("Amount no valid", HttpStatus.FORBIDDEN);
        if (client == null)
            return new ResponseEntity<>("Not exist client", HttpStatus.FORBIDDEN);
        if (accountCrypto == null && accountCryptoNumber.isEmpty())
            return new ResponseEntity<>("No exist the crypto account", HttpStatus.FORBIDDEN);
        if (accountDollar == null && accountDollarNumber.isEmpty())
            return new ResponseEntity<>("No exist the dollar account", HttpStatus.FORBIDDEN);
        if (client.getId() != accountCrypto.getClient().getId() || client.getId() != accountDollar.getClient().getId())
            return new ResponseEntity<>("The client no are the owner of one account", HttpStatus.FORBIDDEN);
        if (accountCrypto.getAccountType() != AccountType.CRYPTO)
            return new ResponseEntity<>("The crypto account no are crypto type", HttpStatus.FORBIDDEN);
        if (accountDollar.getAccountType() != AccountType.DOLAR)
            return new ResponseEntity<>("The dollar account no are dollar type", HttpStatus.FORBIDDEN);


        Transaction transactionDebit = null;
        Transaction transactionCredit = null;
        if (toFrom.equals("CRYPTO-DOLLAR")) {
            if (amount > accountCrypto.getBalance())
                return new ResponseEntity<>("The crypto balance no are available", HttpStatus.FORBIDDEN);

            transactionDebit = new Transaction(amount - amount * 0.05, "Swap", "Swap from" + accountCrypto.getNumber() + " to" + accountDollar.getNumber(), accountCrypto, DEBIT, accountCrypto.getBalance(), accountCrypto.getBalance() - amount, "CRYPTO");
            accountCrypto.setBalance(accountCrypto.getBalance() - amount - amount * 0.05);
            double amountUSD = amount * 20532;
            transactionCredit = new Transaction(amountUSD - amountUSD * 0.05, "Swap", "Swap from" + accountCrypto.getNumber() + " to" + accountDollar.getNumber(), accountDollar, CREDIT, accountDollar.getBalance(), accountDollar.getBalance() + amountUSD, "USD");
            accountDollar.setBalance(accountDollar.getBalance() + amountUSD - amountUSD * 0.05);
        } else if (toFrom.equals("DOLLAR-CRYPTO")) {
            if (amount > accountDollar.getBalance())
                return new ResponseEntity<>("The dollars balance no are available", HttpStatus.FORBIDDEN);

            transactionDebit = new Transaction(amount - amount * 0.05, "Swap", "Swap from" + accountDollar.getNumber() + " to" + accountCrypto.getNumber(), accountDollar, DEBIT, accountDollar.getBalance(), accountDollar.getBalance() - amount, "USD");
            accountDollar.setBalance(accountDollar.getBalance() - amount - amount * 0.05);
            double amountBTC = amount / 20532;
            transactionCredit = new Transaction(amountBTC - amountBTC * 0.05, "Swap", "Swap from" + accountDollar.getNumber() + " to" + accountCrypto.getNumber(), accountCrypto, CREDIT, accountCrypto.getBalance(), accountCrypto.getBalance() + amountBTC, "CRYPTO");
            accountCrypto.setBalance(accountCrypto.getBalance() + amountBTC - amountBTC * 0.05);
        }

        transactionService.saveTransaction(transactionDebit);
        transactionService.saveTransaction(transactionCredit);
        accountService.saveAccount(accountCrypto);
        accountService.saveAccount(accountDollar);

        return new ResponseEntity<>("Swap make successful", HttpStatus.CREATED);

    }

    @CrossOrigin
    @PostMapping("/transactions/makePayment")
    public ResponseEntity<Object> makePayment(@RequestBody CardApplicationDTO cardApplicationDTO) {


        Card card = cardService.getCardByNumber(cardApplicationDTO.getNumber());

        if (card == null)
            return new ResponseEntity<>("The card not exist", HttpStatus.FORBIDDEN);

        Client client = clientService.getClientById(card.getClient().getId());

        if (client == null)
            return new ResponseEntity<>("The client not exist", HttpStatus.FORBIDDEN);
        if (!card.getCardHolder().equals(cardApplicationDTO.getCardHolder()))
            return new ResponseEntity<>("CardHolder no valid", HttpStatus.FORBIDDEN);
        if (card.getCvv() != cardApplicationDTO.getCvv())
            return new ResponseEntity<>("cvv no valid", HttpStatus.FORBIDDEN);


        if (card.getCardType() == CardType.DEBIT) {
            Comparator<Account> idComparatorAccount = Comparator.comparing(Account::getId);
            List<Account> accounts = client.getAccounts().stream().sorted(idComparatorAccount)
                    .filter(account -> account.getBalance() > cardApplicationDTO.getAmount() && account.isActive() && account.getAccountType() == AccountType.DOLAR)
                    .collect(Collectors.toList());

            if (accounts.size() < 1)
                return new ResponseEntity<>("The client haven´t accounts", HttpStatus.FORBIDDEN);

            Account accountDebit = accounts.stream().findFirst().orElse(null);


            Transaction transaction = new Transaction(cardApplicationDTO.getAmount(), cardApplicationDTO.getCategory(), cardApplicationDTO.getDescription(), accountDebit, DEBIT, accountDebit.getBalance(), accountDebit.getBalance() - cardApplicationDTO.getAmount(), "USD");
            accountDebit.setBalance(accountDebit.getBalance() - cardApplicationDTO.getAmount());
            Notification notification = new Notification(client.getUserName(), "Pay $" + cardApplicationDTO.getAmount(), client.getAvatar());

            transactionService.saveTransaction(transaction);
            accountService.saveAccount(accountDebit);
            notificationService.saveNotification(notification);
            return new ResponseEntity<>("Payment make successful", HttpStatus.CREATED);

        }

        if(card.getLimitCard() < card.getExpense() + cardApplicationDTO.getAmount())
            return new ResponseEntity<>("Your payment exceed the limit of your card", HttpStatus.FORBIDDEN);

        Account account = client.getAccounts().stream().findFirst().orElse(null);
        Transaction transaction = new Transaction(cardApplicationDTO.getAmount(), cardApplicationDTO.getCategory(), cardApplicationDTO.getDescription(), account, DEBIT, account.getBalance(), account.getBalance(), "USD");
        card.setExpense(card.getExpense() + cardApplicationDTO.getAmount());
        Notification notification = new Notification(client.getUserName(), "Pay $" + cardApplicationDTO.getAmount(), client.getAvatar());

        transactionService.saveTransaction(transaction);
        accountService.saveAccount(account);
        notificationService.saveNotification(notification);

        return new ResponseEntity<>("Payment make successful", HttpStatus.CREATED);
    }

    @PostMapping("/transactions/resume")
    public ResponseEntity<Object> makeResume(@RequestBody ResumeApplicationDTO resumeApplicationDTO, Authentication authentication,
                                             HttpServletResponse response
                                             ) throws DocumentException, IOException {

        Account account = null;
        Client client = clientService.getClient(authentication.getName());

        account = accountService.getAccountByNumber(resumeApplicationDTO.getAccountNumber());

        if (client == null)
            return new ResponseEntity<>("Client not authorized", HttpStatus.FORBIDDEN);

        if (resumeApplicationDTO.getAccountNumber().isEmpty())
           return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);

        if(!resumeApplicationDTO.getAccountNumber().equals("all") && account == null)
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm") ;
        String currentDateTime = dateFormatter.format(new Date());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=BankrdoX_Resume_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);

        if(resumeApplicationDTO.getAccountNumber().equals("all")){

            List<Transaction> transactionsResume = new ArrayList<>();
            List<Transaction> finalTransactionsResume = transactionsResume;



            client.getAccounts().forEach(account1 -> {
                finalTransactionsResume.addAll(account1.getTransactions());
           });

            if(finalTransactionsResume.size() < 1)
                return new ResponseEntity<>("The client hasn't transactions", HttpStatus.FORBIDDEN);


            Comparator<Transaction> idComparatorTransaction = Comparator.comparing(Transaction::getId);
           transactionsResume = finalTransactionsResume.stream().sorted(idComparatorTransaction).filter(transaction -> transaction.getDate().isAfter(resumeApplicationDTO.getDateFrom())  && transaction.getDate().isBefore(resumeApplicationDTO.getDateTo())).collect(Collectors.toList());

            MakePDF(response, transactionsResume, dateTimeFormatter.format(resumeApplicationDTO.getDateFrom()), dateTimeFormatter.format(resumeApplicationDTO.getDateTo()));
        }


        else{
            List<Transaction> transactionsResume = account.getTransactions().stream().filter(transaction -> transaction.getDate().isAfter(resumeApplicationDTO.getDateFrom())  && transaction.getDate().isBefore(resumeApplicationDTO.getDateTo())).collect(Collectors.toList());

            if(transactionsResume.size() < 1)
                return new ResponseEntity<>("The client hasn't transactions", HttpStatus.FORBIDDEN);

            MakePDF(response, transactionsResume, dateTimeFormatter.format(resumeApplicationDTO.getDateFrom()), dateTimeFormatter.format(resumeApplicationDTO.getDateTo()));
        }

        return new ResponseEntity<>("Payment make successful", HttpStatus.CREATED);
    }






}
