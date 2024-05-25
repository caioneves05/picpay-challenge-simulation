package com.picpaychallengesimulation.picpaychallengesimulation.services;

import com.picpaychallengesimulation.picpaychallengesimulation.domain.user.User;
import com.picpaychallengesimulation.picpaychallengesimulation.dtos.TransactionDTO;
import com.picpaychallengesimulation.picpaychallengesimulation.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    public void createTransaction(TransactionDTO transaction) throws Exception {

        User sender = this.userService.findUserById(transaction.senderId());

        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.amount());
        
    }
}
