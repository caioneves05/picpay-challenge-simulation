package com.picpaychallengesimulation.picpaychallengesimulation.services;

import com.picpaychallengesimulation.picpaychallengesimulation.domain.transaction.Transaction;
import com.picpaychallengesimulation.picpaychallengesimulation.domain.user.User;
import com.picpaychallengesimulation.picpaychallengesimulation.dtos.TransactionDTO;
import com.picpaychallengesimulation.picpaychallengesimulation.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${url.authorization}")
    private String urlAuthorization;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {

        User sender = this.userService.findUserById(transaction.senderId());

        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.amount());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.amount());

        if(!isAuthorized) {
            throw new Exception("Transaction is not authorized");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.amount());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestemp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.amount()));
        receiver.setBalance(receiver.getBalance().add(transaction.amount()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender,"Transaction created successfully.");
        this.notificationService.sendNotification(receiver,"Transaction received successfully.");

        return newTransaction;
        
    }

    public boolean authorizeTransaction(User sender, BigDecimal amount) {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(urlAuthorization, Map.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
            return Boolean.TRUE.equals(authorizationResponse.getBody().get("authorization"));
        }
        return false;
    }
}
