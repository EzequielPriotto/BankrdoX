package com.mindhub.homebanking.Utils;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import net.bytebuddy.utility.RandomString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.CardType.CREDIT;

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

    }
