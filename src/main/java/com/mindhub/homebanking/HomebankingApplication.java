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
			Client clientePsycho = new Client("Pysho", "Team", "psycho_admin@bank.com",passwordEnconder.encode("admin123"), "Psycho");
			cliente1.setEnabled(true);
			cliente2.setEnabled(true);
			clienteAdmin.setEnabled(true);
			clientePsycho.setEnabled(true);

			clientRepository.save(cliente1);
			clientRepository.save(cliente2);
			clientRepository.save(clienteAdmin);
			clientRepository.save(clientePsycho);

			Account cuenta1 = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now(),4900.00, GenerateRandomNumberCVU(), AccountType.DOLAR);
			Account cuenta2 = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now().minusDays(1),7390.00, GenerateRandomNumberCVU(), AccountType.DOLAR );
			Account cuenta3 = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now().minusDays(5),1489.00, GenerateRandomNumberCVU(), AccountType.DOLAR );
			Account cuenta4 = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now().minusDays(5),999999 * 999999 , GenerateRandomNumberCVU(), AccountType.DOLAR );

			cliente1.addAccount(cuenta1);
			cliente1.addAccount(cuenta2);
			cliente1.addAccount(cuenta3);
			clientePsycho.addAccount(cuenta4);

			Account cuenta5 = new Account("VIN-" + GenerateRandomNumber(9, 0), LocalDateTime.now().plusDays(2),1640.00, GenerateRandomNumberCVU(), AccountType.DOLAR );
			cliente2.addAccount(cuenta5);

			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);
			accountRepository.save(cuenta3);
			accountRepository.save(cuenta4);
			accountRepository.save(cuenta5);


			Transaction transaction1 = new Transaction(4380,"Games" ,"250RP RIOTGAME", LocalDateTime.now().plusMinutes(42).plusHours(10),cuenta1,DEBIT, cuenta1.getBalance(), cuenta1.getBalance() - 4380, "USD");
			Transaction transaction2 = new Transaction(980,"Food" ,"McCombo x2", LocalDateTime.now(),cuenta1,DEBIT, cuenta1.getBalance(), cuenta1.getBalance() - 980, "USD");
			Transaction transaction3 = new Transaction(1600,"Transfer" ,"Ty man", LocalDateTime.now().plusDays(2).plusMinutes(2).plusHours(4),cuenta1,CREDIT, cuenta1.getBalance(), cuenta1.getBalance() + 1600, "USD");
			Transaction transaction4 = new Transaction(3500,"Services" ,"Internet services ", LocalDateTime.now().plusDays(12).plusMinutes(23).plusHours(1),cuenta1,DEBIT,cuenta1.getBalance(), cuenta1.getBalance() + 3500, "USD");

			Transaction transaction5 = new Transaction(6700,"Transfer" ,"Charles website work" , LocalDateTime.now().plusMinutes(23).plusHours(1),cuenta2,CREDIT,cuenta2.getBalance(), cuenta2.getBalance() + 6700, "USD");
			Transaction transaction6 = new Transaction(223,"Transfer" , "Charles website work" ,LocalDateTime.now().plusMinutes(17).plusHours(8),cuenta2,CREDIT, cuenta2.getBalance(),cuenta2.getBalance() + 223, "USD");

			Transaction transaction7 = new Transaction(420, "Outfit","Mua store", LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta3,DEBIT, cuenta3.getBalance(),  cuenta3.getBalance() - 420, "USD");
			Transaction transaction8 = new Transaction(8120, "Outfit", "Gucci store",LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta3,DEBIT,cuenta1.getBalance(), cuenta1.getBalance() - 8120, "USD");
			Transaction transaction9 = new Transaction(5403, "Transfer", "Bancomopiña bank" ,LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta3,CREDIT, cuenta3.getBalance(),  cuenta3.getBalance() + 5403, "USD");
			Transaction transaction10 = new Transaction(42.60, "Transfer", "Tomorrow send 400 more <3",LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta2,CREDIT, cuenta2.getBalance(),  cuenta2.getBalance() + 42.60, "USD");
			Transaction transaction11 = new Transaction(10560, "Services", "Water services" ,LocalDateTime.now().plusMinutes(7).plusHours(20), cuenta1,DEBIT, cuenta1.getBalance(),  cuenta1.getBalance() - 10560, "USD");
			Transaction transaction12 = new Transaction(19530, "Others", "Netflix" ,LocalDateTime.now().minusDays(23).plusMinutes(12), cuenta1,DEBIT , cuenta1.getBalance(),  cuenta1.getBalance() - 19530, "USD");



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
			Loan loan1 = new Loan(payments, LoanType.MORTGAGE, 500000,"A \"fixed-rate\" mortgage comes with an interest rate that won't change for the life of your home loan. A \"conventional\" (conforming) mortgage is a loan that conforms to established guidelines for the size of the loan and your financial situation. ", 1.35 );
			loanRepository.save(loan1);

			payments = asList(6,12,24);
			Loan loan2 = new Loan(payments, LoanType.PERSONAL, 100000, "You can use a personal loan for any purchase or product.\n" +
					"This one-time funding can help cover vacations, home renovations, medical bills or consolidating debts.You could get a loan for your project within hours.", 1.20);
			loanRepository.save(loan2);

			payments = asList(6,12,24,36);
			Loan loan3 = new Loan(payments, LoanType.AUTOMOTIVE, 300000, "It is a loan granted for the purchase of a new or used vehicle, which is pledged in favor of the Bank as collateral, until its total cancellation. ", 1.45);
			loanRepository.save(loan3);

			ClientLoan cloan1 = new ClientLoan(60,400000,  cliente1, loan1, (400000*loan1.getFee()) / 60);
			ClientLoan cloan2 = new ClientLoan(12,50000,  cliente1, loan2,(50000*loan2.getFee()) / 12 );

			ClientLoan cloan3 = new ClientLoan(24,100000,  cliente1, loan3,(100000*loan3.getFee()) / 24);
			ClientLoan cloan4 = new ClientLoan(36,200000,  cliente2, loan3,(200000*loan3.getFee()) / 36);


			clientLoanRepository.save(cloan1);
			clientLoanRepository.save(cloan2);
			clientLoanRepository.save(cloan3);
			clientLoanRepository.save(cloan4);


			Card card1 = new Card(cliente1,CardType.DEBIT, CardColor.GOLD, GenerateRandomNumberCard(10, 0,CardType.DEBIT),682,LocalDateTime.now(),LocalDateTime.now().plusYears(5),300000);
			Card card2 = new Card(cliente1,CardType.CREDIT, CardColor.TITANIUM,  GenerateRandomNumberCard(10, 0,CardType.CREDIT),543,LocalDateTime.now(),LocalDateTime.now().plusYears(5),500000);
			Card card3 = new Card(cliente1,CardType.DEBIT, CardColor.SILVER,  GenerateRandomNumberCard(10, 0,CardType.CREDIT),763,LocalDateTime.now(),LocalDateTime.now().plusYears(5), 100000);
			card3.setExpense(51000);
			Card card4 = new Card(cliente1,CardType.CREDIT, CardColor.GOLD,  GenerateRandomNumberCard(10, 0,CardType.CREDIT),540,LocalDateTime.now(),LocalDateTime.now().plusYears(5), 100000);
			card4.setExpense(90000);
			Card card5 = new Card(clientePsycho,CardType.DEBIT, CardColor.SILVER, "1234123412341234" ,123,LocalDateTime.now(),LocalDateTime.now().plusYears(5), 500000);

			cliente1.addCard(card1);
			cliente1.addCard(card2);
			cliente1.addCard(card3);
			cliente1.addCard(card4);
			clientePsycho.addCard(card5);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);
			cardRepository.save(card5);

			clientRepository.save(clientePsycho);

			System.out.println("PROGRAMA INICIADO :D");
		};
	}



}
