package com.picpaychallengesimulation.picpaychallengesimulation.services;

import com.picpaychallengesimulation.picpaychallengesimulation.domain.user.User;
import com.picpaychallengesimulation.picpaychallengesimulation.domain.user.UserType;
import com.picpaychallengesimulation.picpaychallengesimulation.dtos.UserDTO;
import com.picpaychallengesimulation.picpaychallengesimulation.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public void validateTransaction(User sender, BigDecimal amountTransaction) throws Exception {
        if(sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("The Merchant type user is not authorized to carry out this transaction.");
        }

        if(sender.getBalance().compareTo(amountTransaction) < 0) {
            throw new Exception("This user does not have enough balance.");
        }
    }

    public User findUserById(Long id) throws Exception {
        return this.userRepository.findUserById(id).orElseThrow(() -> new Exception("User not found"));
    }

    public User createUser (UserDTO data) {
        User newUser = new User(data);
        this.saveUser(newUser);

        return newUser;
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }
}
