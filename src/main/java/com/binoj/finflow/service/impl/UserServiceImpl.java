package com.binoj.finflow.service.impl;

import com.binoj.finflow.entity.Account;
import com.binoj.finflow.entity.User;
import com.binoj.finflow.entity.UserStatus;
import com.binoj.finflow.exception.ResourceNotFoundException;
import com.binoj.finflow.repository.AccountRepository;
import com.binoj.finflow.repository.UserRepository;
import com.binoj.finflow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.binoj.finflow.security.JwtUtil;

import java.math.BigDecimal;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;


    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.jwtUtil = jwtUtil;
    }

    public String register(User user) {
        log.info("Registering user with mobile: {}", user.getMobile());

        String otp = generateOtp();
        log.info("Generated OTP for user: {}", user.getMobile());

        user.setOtp(otp);
        user.setStatus(UserStatus.PENDING);

        userRepository.save(user);
        log.info("User registered successfully: {}", user.getMobile());

        log.info("Generated OTP: {}", otp);

        return "User registered. OTP sent (check console)";
    }

    public String verifyOtp(String mobile, String otp) {
        log.info("Verifying OTP for mobile: {}", mobile);

        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("User found for mobile: {}", mobile);

        if (!user.getOtp().equals(otp)) {
            log.warn("Invalid OTP for mobile: {}", mobile);
            throw new RuntimeException("Invalid OTP");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setOtp(null);
        userRepository.save(user);
        log.info("OTP verified for mobile: {}", mobile);

        Account account = new Account();
        account.setUserId(user.getId());
        account.setBalance(new BigDecimal("1000.00"));

        accountRepository.save(account);
        log.info("Account created for user: {}", user.getId());

        return "User verified and account created";
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public String login(String mobile) {
        log.info("Login attempt for mobile: {}", mobile);

        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("User found for login: {}", mobile);

        if (user.getStatus() != UserStatus.ACTIVE) {
            log.warn("Login failed: user not active for mobile: {}", mobile);
            throw new RuntimeException("User not active");
        }

        String token = jwtUtil.generateToken(mobile);
        log.info("Login successful for mobile: {}", mobile);
        return token;
    }
}
