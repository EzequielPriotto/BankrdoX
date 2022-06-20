package com.mindhub.homebanking.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResumeApplicationDTO {
    String accountNumber;
    LocalDateTime dateFrom, dateTo;
}
