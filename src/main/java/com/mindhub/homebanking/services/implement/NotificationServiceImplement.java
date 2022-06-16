package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Notification;
import com.mindhub.homebanking.repositories.NotificationRepository;
import com.mindhub.homebanking.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImplement implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;


    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllByClient(Client client) {
        return notificationRepository.findByClient(client);
    }
}
