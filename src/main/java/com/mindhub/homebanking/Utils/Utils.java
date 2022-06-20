package com.mindhub.homebanking.Utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.*;
import net.bytebuddy.utility.RandomString;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.CardType.CREDIT;

public class Utils {

    private static List<String> numbersCreated = new ArrayList<>();
    private static List<String> numbersCardsCreated = new ArrayList<>();
    private static List<String> tokensCreated = new ArrayList<>();

    private static List<String> numbersCVUCreated = new ArrayList<>();

    public static String GenerateRandomNumber(int max, int min) {
        int number;
        String numberFinal = "";

        do {
            for (int i = 0; i < 8; i++) {
                number = (int) ((Math.random() * (max - min)) + min);
                numberFinal += number;
            }
        }
        while (numbersCreated.contains(numberFinal));
        numbersCreated.add(numberFinal);
        return numberFinal;
    }

    public static String GenerateRandomNumberCard(int max, int min, CardType cardType) {
        int number;

        String numberCard;
        do {
            numberCard = "";
            String part1 = cardType == CREDIT ? "45" : "52";
            String part2 = "";
            String part3 = "";
            String part4 = "";

            for (int i = 0; i < 16; i++) {
                number = (int) ((Math.random() * (max - min)) + min);
                switch (i) {
                    case 2:
                    case 3:
                        part1 += Integer.toString(number);
                        break;
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        part2 += Integer.toString(number);
                        break;
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        part3 += Integer.toString(number);
                        break;
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                        part4 += Integer.toString(number);
                        break;


                }
            }

            numberCard = part1 + " " + part2 + " " + part3 + " " + part4;
        }
        while (numbersCardsCreated.contains(numberCard));
        numbersCardsCreated.add(numberCard);
        return numberCard;

    }

    public static String GenerateRandomNumberCVU() {
        int number;
        String numberCVU;
        do {
            numberCVU = "";
            for (int i = 0; i < 22; i++) {
                number = (int) ((Math.random() * (9 - 0)) + 0);
                if (i < 5) {
                    numberCVU += "0";
                } else {
                    numberCVU += number;
                }
            }

        }
        while (numbersCVUCreated.contains(numberCVU));
        numbersCVUCreated.add(numberCVU);
        return numberCVU;
    }

    public static String GenerateRandomNumberAddress() {
        String number;
        String numberCVU;
        do {
            numberCVU = "0x";
            number = RandomString.make(20);
            numberCVU += number;
//        0x1553603181e87240549707cf4ad92f78e42c70fb
        }
        while (numbersCVUCreated.contains(numberCVU));
        numbersCVUCreated.add(numberCVU);
        return numberCVU;
    }

    public static String GenerateToken(int tokenL) {
        String token = "";
        do {
            token = RandomString.make(tokenL);
        } while (tokensCreated.contains(token));
        tokensCreated.add(token);

        return token;
    }

    public static void DeleteToken(String tokenD) {
        tokensCreated = tokensCreated.stream().filter(token -> token != tokenD).collect(Collectors.toList());
    }


    public static int SelectLimit(CardColor cardColor) {
        if (cardColor == CardColor.SILVER)
            return 100000;
        if (cardColor == CardColor.GOLD)
            return 300000;
        if (cardColor == CardColor.TITANIUM)
            return 500000;
        return 0;
    }

    public static void MakePDF(HttpServletResponse response, List<Transaction> transactions, String from, String to) throws IOException, DocumentException {

        Font catFont = new Font(Font.STRIKETHRU, 20, Font.BOLD);
        Font smallBold = new Font(Font.STRIKETHRU, 12);
        Font tableFont = new Font(Font.STRIKETHRU, 10, Font.NORMAL);
        Font headerFont = new Font(Font.STRIKETHRU, 12, Font.BOLD);

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        PdfPTable table = new PdfPTable(6);
        float[] columnWidths = new float[]{20f, 15f, 15f, 20f, 15f, 15f,};


        table.setWidths(columnWidths);
        table.setWidthPercentage(110);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        List<String> headers = List.of("ID", "DATE", "CATEGORY", "DESCRIPTION", "ACCOUNT", "AMOUNT");

        headers.forEach(header -> {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new Color(234, 88, 25));
            cell.setPadding(8);

            table.addCell(cell);
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        AtomicReference<Double> totalExpense = new AtomicReference<>((double) 0);
        AtomicReference<Double> totalIncoming = new AtomicReference<>((double) 0);

        transactions.forEach(transaction -> {

            PdfPCell idCell = new PdfPCell(new Phrase(transaction.getIdEncrypted(), tableFont));
            idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            idCell.setVerticalAlignment(Element.ALIGN_CENTER);
            idCell.setBackgroundColor(new Color(231, 198, 184));
            idCell.setPadding(8);
            table.addCell(idCell);


            String currentDateTime = dateFormatter.format(transaction.getDate());
            PdfPCell dateCell = new PdfPCell(new Phrase(currentDateTime, tableFont));
            dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dateCell.setVerticalAlignment(Element.ALIGN_CENTER);
            dateCell.setBackgroundColor(new Color(231, 198, 184));
            dateCell.setPadding(8);
            table.addCell(dateCell);


            PdfPCell categoryCell = new PdfPCell(new Phrase(transaction.getCategory(), tableFont));
            categoryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            categoryCell.setVerticalAlignment(Element.ALIGN_CENTER);
            categoryCell.setBackgroundColor(new Color(231, 198, 184));
            categoryCell.setPadding(8);
            table.addCell(categoryCell);


            PdfPCell descriptionCell = new PdfPCell(new Phrase(transaction.getDescription(), tableFont));
            descriptionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            descriptionCell.setVerticalAlignment(Element.ALIGN_CENTER);
            descriptionCell.setBackgroundColor(new Color(231, 198, 184));
            descriptionCell.setPadding(8);
            table.addCell(descriptionCell);


            PdfPCell accountCell = new PdfPCell(new Phrase(transaction.getAccount().getNumber(), tableFont));
            accountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            accountCell.setVerticalAlignment(Element.ALIGN_CENTER);
            accountCell.setBackgroundColor(new Color(231, 198, 184));
            accountCell.setPadding(8);
            table.addCell(accountCell);

            String amount = transaction.getType() == TransactionType.DEBIT ? "-" + String.valueOf(transaction.getAmount()) : "+" + String.valueOf(transaction.getAmount());

            amount = transaction.getCryptoOrUsd().equals("USD") ? amount + "usd" : amount + "btc";

            PdfPCell amountCell = new PdfPCell(new Phrase(amount, tableFont));
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCell.setVerticalAlignment(Element.ALIGN_CENTER);

            if (transaction.getType() == TransactionType.DEBIT) {
                totalExpense.updateAndGet(v -> (double) (v + transaction.getAmount()));
                amountCell.setBackgroundColor(new Color(191, 49, 49));
            } else {
                totalIncoming.updateAndGet(v -> (double) (v + transaction.getAmount()));
                amountCell.setBackgroundColor(new Color(84, 173, 84));
            }
            accountCell.setPadding(8);
            table.addCell(amountCell);

        });


        PdfPCell spaceCell = new PdfPCell(new Phrase(""));
        spaceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        spaceCell.setBackgroundColor(new Color(231, 198, 184));
        spaceCell.setPadding(8);
        spaceCell.setColspan(3);
        table.addCell(spaceCell);

        headerFont.setStyle(Font.ITALIC);

        PdfPCell totalExpenseCell = new PdfPCell(new Phrase("TOTAL EXPENSE",headerFont ));
        totalExpenseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalExpenseCell.setBackgroundColor(new Color(231, 198, 184));
        totalExpenseCell.setPadding(8);
        totalExpenseCell.setColspan(2);
        table.addCell(totalExpenseCell);

        PdfPCell amountExpenseCell = new PdfPCell(new Phrase(String.valueOf(totalExpense) + "usd", headerFont));
        amountExpenseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        amountExpenseCell.setBackgroundColor(new Color(231, 198, 184));
        amountExpenseCell.setPadding(8);
        table.addCell(amountExpenseCell);


        table.addCell(spaceCell);


        PdfPCell totalIncomingCell = new PdfPCell(new Phrase("TOTAL INCOME",headerFont ));
        totalIncomingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalIncomingCell.setBackgroundColor(new Color(231, 198, 184));
        totalIncomingCell.setPadding(8);
        totalIncomingCell.setColspan(2);
        table.addCell(totalIncomingCell);

        PdfPCell amountIncomingCell = new PdfPCell(new Phrase(String.valueOf(totalIncoming) + "usd", headerFont));
        amountIncomingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        amountIncomingCell.setBackgroundColor(new Color(231, 198, 184));
        amountIncomingCell.setPadding(8);
        table.addCell(amountIncomingCell);


        Client client = transactions.stream().findFirst().orElse(null).getAccount().getClient();


        Paragraph title = new Paragraph("ACCOUNT TRANSACTIONS RESUME", catFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);

        Paragraph nameParr = new Paragraph("Client full name:  " + client.getFullName(), smallBold);
        nameParr.setSpacingAfter(5);

        Paragraph dateParr = new Paragraph("from: " + from + " to " + to, smallBold);
        dateParr.setSpacingAfter(20);




        document.add(title);
        document.add(nameParr);
        document.add(dateParr);
        document.add(table);
        document.close();

    }


}
