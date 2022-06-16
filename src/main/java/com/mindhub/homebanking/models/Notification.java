package com.mindhub.homebanking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Unmodifiable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity  // crea la tabla en base a la entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Unmodifiable
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    @Unmodifiable
    private Client client;

    private String user, message, url;

    private LocalDateTime date;

    public Notification(){}


    public Notification( String user, String message, String url) {
        this.user = user;
        this.message = message;
        this.url = url;
        this.date = LocalDateTime.now();
    }
}
