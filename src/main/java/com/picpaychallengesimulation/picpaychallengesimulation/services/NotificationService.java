package com.picpaychallengesimulation.picpaychallengesimulation.services;

import com.picpaychallengesimulation.picpaychallengesimulation.domain.user.User;
import com.picpaychallengesimulation.picpaychallengesimulation.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${url.notification}")
    private String urlNotification;

    public void sendNotification(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        ResponseEntity<String> notificationResponse = restTemplate.postForEntity(urlNotification, notificationRequest, String.class);

        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
            System.out.println("Error to send notification");
            throw  new Exception("Notification Service is not working.");
        }
    }
}
