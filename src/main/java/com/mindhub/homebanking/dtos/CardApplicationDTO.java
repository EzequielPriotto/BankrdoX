package com.mindhub.homebanking.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CardApplicationDTO {
    private String number,cardHolder, category, description,expiry;
    private int cvv;
    private double amount;

}
