package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utils.*;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;
import static java.util.Arrays.asList;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEnconder;



	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);




	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return (args) ->{

			Client cliente1 =  new Client("Melba", "Morel", "melba@mindhub.com",passwordEnconder.encode("bokaShoTeAmo"),"Melbita");
			Client cliente2 = new Client("Edu", "Mendoza", "eduMendo@mindhub.com",passwordEnconder.encode("robert123"),"ElEdu");
			Client clienteAdmin = new Client("Ezequiel", "Priotto", "Eze_admin@bank.com",passwordEnconder.encode("admin123"), "Zeke");

			cliente1.setEnabled(true);
			cliente2.setEnabled(true);
			clienteAdmin.setEnabled(true);

			clientRepository.save(cliente1);
			clientRepository.save(cliente2);
			clientRepository.save(clienteAdmin);

			Account cuenta1 = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now(),4900.00, GenerateRandomNumberCVU(), AccountType.DOLAR);
			Account cuenta2 = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now().minusDays(1),7390.00, GenerateRandomNumberCVU(), AccountType.DOLAR );
			Account cuenta3 = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now().minusDays(5),1489.00, GenerateRandomNumberCVU(), AccountType.DOLAR );

			cliente1.addAccount(cuenta1);
			cliente1.addAccount(cuenta2);
			cliente1.addAccount(cuenta3);

			Account cuenta5 = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now().plusDays(2),1640.00, GenerateRandomNumberCVU(), AccountType.DOLAR );
			cliente2.addAccount(cuenta5);

			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);
			accountRepository.save(cuenta3);
			accountRepository.save(cuenta5);


			Transaction transaction1 = new Transaction(4380,"Games" , LocalDateTime.now().plusMinutes(42).plusHours(10),cuenta1,DEBIT);
			Transaction transaction2 = new Transaction(980,"Food" , LocalDateTime.now(),cuenta1,DEBIT);
			Transaction transaction3 = new Transaction(1600,"Transfer" , LocalDateTime.now().plusDays(2).plusMinutes(2).plusHours(4),cuenta1,CREDIT);
			Transaction transaction4 = new Transaction(3500,"Services" , LocalDateTime.now().plusDays(12).plusMinutes(23).plusHours(1),cuenta1,DEBIT);

			Transaction transaction5 = new Transaction(6700,"Transfer" , LocalDateTime.now().plusMinutes(23).plusHours(1),cuenta2,CREDIT);
			Transaction transaction6 = new Transaction(223,"Transfer" , LocalDateTime.now().plusMinutes(17).plusHours(8),cuenta2,CREDIT);

			Transaction transaction7 = new Transaction(420, "Outfit", LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta3,DEBIT);
			Transaction transaction8 = new Transaction(8120, "Outfit", LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta3,DEBIT);
			Transaction transaction9 = new Transaction(5403, "Transfer", LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta3,CREDIT);
			Transaction transaction10 = new Transaction(42.60, "Transfer", LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta2,CREDIT);
			Transaction transaction11 = new Transaction(10560, "Services", LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta1,DEBIT);
			Transaction transaction12 = new Transaction(19530, "Others", LocalDateTime.now().minusDays(23).plusMinutes(12), cuenta1,DEBIT);



			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			transactionRepository.save(transaction12);


			List<Integer> payments = asList(12,24,36,48,60);
			Loan loan1 = new Loan(payments, LoanType.HIPOTECARIO, 500000);
			loanRepository.save(loan1);

			payments = asList(6,12,24);
			Loan loan2 = new Loan(payments, LoanType.PERSONAL, 100000);
			loanRepository.save(loan2);

			payments = asList(6,12,24,36);
			Loan loan3 = new Loan(payments, LoanType.AUTOMOTRIZ, 300000);
			loanRepository.save(loan3);

			ClientLoan cloan1 = new ClientLoan(60,400000,  cliente1, loan1);
			ClientLoan cloan2 = new ClientLoan(12,50000,  cliente1, loan2);

			ClientLoan cloan3 = new ClientLoan(24,100000,  cliente1, loan2);
			ClientLoan cloan4 = new ClientLoan(36,200000,  cliente2, loan3);


			clientLoanRepository.save(cloan1);
			clientLoanRepository.save(cloan2);
			clientLoanRepository.save(cloan3);
			clientLoanRepository.save(cloan4);


			Card card1 = new Card(cliente1,CardType.DEBIT, CardColor.GOLD, GenerateRandomNumberCard(10, 0,CardType.DEBIT),682,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
			Card card2 = new Card(cliente1,CardType.CREDIT, CardColor.TITANIUM,  GenerateRandomNumberCard(10, 0,CardType.CREDIT),543,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
			Card card3 = new Card(cliente1,CardType.CREDIT, CardColor.SILVER,  GenerateRandomNumberCard(10, 0,CardType.CREDIT),763,LocalDateTime.now(),LocalDateTime.now().plusYears(5));
			Card card4 = new Card(cliente1,CardType.CREDIT, CardColor.SILVER,  GenerateRandomNumberCard(10, 0,CardType.CREDIT),540,LocalDateTime.now(),LocalDateTime.now().plusYears(5));

			cliente1.addCard(card1);
			cliente1.addCard(card2);
			cliente1.addCard(card3);
			cliente1.addCard(card4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);


			System.out.println("PROGRAMA INICIADO :D");
		};
	}



}
