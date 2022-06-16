package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationDTO {
    private long id;
    private String user, message, url;
    private LocalDateTime date;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.user = notification.getUser();
        this.message = notification.getMessage();
        this.url = notification.getUrl();
        this.date = notification.getDate();
    }
}
