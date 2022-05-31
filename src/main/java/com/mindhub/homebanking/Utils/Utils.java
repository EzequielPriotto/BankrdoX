package com.mindhub.homebanking.Utils;
import com.mindhub.homebanking.models.CardType;
import java.util.ArrayList;
import java.util.List;
import static com.mindhub.homebanking.models.CardType.CREDIT;

public class Utils {

    private static List<String> numbersCreated = new ArrayList<>();
    private static List<String> numbersCardsCreated = new ArrayList<>();


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

}
