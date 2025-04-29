package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jpmc.midascore.foundation.Balance;

@RestController
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam long userId) {
        UserRecord user = userRepository.findById(userId);
        float balance = (user != null) ? user.getBalance() : 0;
        return new Balance(balance);
    }
}