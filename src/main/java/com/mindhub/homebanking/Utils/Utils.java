package com.mindhub.homebanking.Utils;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Transaction;
import net.bytebuddy.utility.RandomString;
import org.springframework.format.annotation.DateTimeFormat;
import org.w3c.dom.css.RGBColor;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.CardType.CREDIT;
import static java.awt.Color.*;

public class Utils {

    private static List<String> numbersCreated = new ArrayList<>();
    private static List<String> numbersCardsCreated = new ArrayList<>();
    private static List<String> tokensCreated = new ArrayList<>();

    private static List<String> numbersCVUCreated = new ArrayList<>();

    public static String GenerateRandomNumber(int max, int min){
            int number;
            String numberFinal = "";

        do {
               for (int i = 0; i <8; i++){
                   number = (int) ((Math.random() * (max - min)) + min);
                   numberFinal += number;
               }
            }
            while(numbersCreated.contains(numberFinal));
            numbersCreated.add(numberFinal);
            return numberFinal;
    }

    public static String GenerateRandomNumberCard(int max, int min, CardType cardType){
        int number;

            String numberCard;
            do {
                numberCard = "";
                String part1 = cardType == CREDIT? "45" : "52";
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

    public static String GenerateRandomNumberCVU(){
        int number;
        String numberCVU;
        do {
            numberCVU = "";
            for (int i = 0; i < 22; i++) {
                number = (int) ((Math.random() * (9 - 0)) + 0);
                if(i < 5){
                    numberCVU += "0" ;
                }
                else {
                    numberCVU += number ;
                }
            }

        }
        while (numbersCVUCreated.contains(numberCVU));
        numbersCVUCreated.add(numberCVU);
        return numberCVU;
    }

    public static String GenerateRandomNumberAddress(){
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

    public static String GenerateToken(int tokenL){
        String token = "";
        do {
            token = RandomString.make(tokenL);
        }while (tokensCreated.contains(token));
        tokensCreated.add(token);

        return token;
    }

    public static void DeleteToken(String tokenD) {
        tokensCreated =  tokensCreated.stream().filter(token -> token != tokenD).collect(Collectors.toList());
    }


    public static int SelectLimit(CardColor cardColor){
        if(cardColor == CardColor.SILVER)
            return 100000;
        if(cardColor == CardColor.GOLD)
            return 300000;
        if(cardColor == CardColor.TITANIUM)
            return 500000;
        return 0;
    }

    public static void MakePDF(HttpServletResponse response, List<Transaction> transactions, String from, String to ) throws IOException, DocumentException {

        Font catFont = new Font(Font.STRIKETHRU, 20,Font.BOLD);
        Font redFont = new Font(Font.TIMES_ROMAN, 12,Font.NORMAL, RED);
        Font subFont = new Font(Font.TIMES_ROMAN, 16,Font.BOLD);
        Font smallBold = new Font(Font.STRIKETHRU, 12,Font.ITALIC);

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        PdfPTable table = new PdfPTable(6);
        float[] columnWidths = new float[]{20f, 15f, 15f, 20f, 15f, 15f,};


        table.setWidths(columnWidths);
        table.setWidthPercentage(110);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        List<String> headers = List.of("ID","DATE","CATEGORY","DESCRIPTION","ACCOUNT","AMOUNT");

        headers.forEach(header->{
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new Color(234, 88, 25));
            cell.setPadding(8);

            table.addCell(cell);
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        transactions.forEach(transaction -> {

            PdfPCell idCell = new PdfPCell(new Phrase(transaction.getIdEncrypted()));
            idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            idCell.setBackgroundColor(new Color(231, 198, 184));
            idCell.setPadding(8);
            table.addCell(idCell);


            String currentDateTime = dateFormatter.format(transaction.getDate());
            PdfPCell dateCell = new PdfPCell(new Phrase(currentDateTime));
            dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dateCell.setBackgroundColor(new Color(231, 198, 184));
            dateCell.setPadding(8);
            table.addCell(dateCell);


            PdfPCell categoryCell = new PdfPCell(new Phrase(transaction.getCategory()));
            categoryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            categoryCell.setBackgroundColor(new Color(231, 198, 184));
            categoryCell.setPadding(8);
            table.addCell(categoryCell);


            PdfPCell descriptionCell = new PdfPCell(new Phrase(transaction.getDescription()));
            descriptionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            descriptionCell.setBackgroundColor(new Color(231, 198, 184));
            descriptionCell.setPadding(8);
            table.addCell(descriptionCell);


            PdfPCell accountCell = new PdfPCell(new Phrase(transaction.getAccount().getNumber()));
            accountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            accountCell.setBackgroundColor(new Color(231, 198, 184));
            accountCell.setPadding(8);
            table.addCell(accountCell);


            PdfPCell amountCell = new PdfPCell(new Phrase(String.valueOf(transaction.getAmount())));
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCell.setBackgroundColor(new Color(231, 198, 184));
            accountCell.setPadding(8);
            table.addCell(amountCell);

        });


        Paragraph title = new Paragraph("ACCOUNT TRANSACTIONS RESUME", catFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(5);

        Paragraph subPara = new Paragraph("from: " + from + " to " + to, smallBold);
        subPara.setAlignment(Element.ALIGN_CENTER);
        subPara.setSpacingAfter(20);

        document.add(title);
        document.add(subPara);
        document.add(table);
        document.close();

    }


    }
