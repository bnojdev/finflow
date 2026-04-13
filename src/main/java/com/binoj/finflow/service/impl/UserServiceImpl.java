package com.binoj.finflow.service.impl;

import com.binoj.finflow.entity.Account;
import com.binoj.finflow.entity.User;
import com.binoj.finflow.entity.UserStatus;
import com.binoj.finflow.repository.AccountRepository;
import com.binoj.finflow.repository.UserRepository;
import com.binoj.finflow.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public String register(User user) {

        String otp = generateOtp();

        user.setOtp(otp);
        user.setStatus(UserStatus.PENDING);

        userRepository.save(user);


        System.out.println("Generated OTP: " + otp);

        return "User registered. OTP sent (check console)";
    }

    public String verifyOtp(String mobile, String otp) {

        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setOtp(null);
        userRepository.save(user);

        Account account = new Account();
        account.setUserId(user.getId());
        account.setBalance(new BigDecimal("1000.00"));

        accountRepository.save(account);

        return "User verified and account created";
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
