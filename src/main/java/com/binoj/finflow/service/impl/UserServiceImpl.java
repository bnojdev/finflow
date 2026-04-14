package com.binoj.finflow.service.impl;

import com.binoj.finflow.entity.Account;
import com.binoj.finflow.entity.User;
import com.binoj.finflow.entity.UserStatus;
import com.binoj.finflow.exception.BadRequestException;
import com.binoj.finflow.exception.ResourceNotFoundException;
import com.binoj.finflow.repository.AccountRepository;
import com.binoj.finflow.repository.UserRepository;
import com.binoj.finflow.security.JwtUtil;
import com.binoj.finflow.service.OtpCacheService;
import com.binoj.finflow.service.OtpService;
import com.binoj.finflow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;
    private final OtpCacheService otpCacheService;
    private final OtpService otpService;

    public UserServiceImpl(UserRepository userRepository,
                           AccountRepository accountRepository,
                           JwtUtil jwtUtil,
                           OtpCacheService otpCacheService,
                           OtpService otpService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.jwtUtil = jwtUtil;
        this.otpCacheService = otpCacheService;
        this.otpService = otpService;
    }

    @Override
    public String register(User user) {
        log.info("Registering user with mobile: {}", user.getMobile());

        userRepository.findByMobile(user.getMobile())
                .ifPresent(u -> {
                    throw new BadRequestException("User already exists with this mobile");
                });

        String otp = generateOtp();
        log.info("Generated OTP for mobile {}: {}", user.getMobile(), otp);

        otpCacheService.saveOtp(user.getMobile(), otp);

        otpService.sendOtp(user.getMobile(), otp);

        user.setStatus(UserStatus.PENDING);
        user.setOtp(null);

        userRepository.save(user);

        log.info("User registered successfully: {}", user.getMobile());

        return "User registered. OTP sent";
    }

    @Override
    public String verifyOtp(String mobile, String otp) {
        log.info("Verifying OTP for mobile: {}", mobile);

        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String cachedOtp = otpCacheService.getOtp(mobile);

        if (cachedOtp == null) {
            log.warn("OTP expired or not found for mobile: {}", mobile);
            throw new BadRequestException("OTP expired or not found");
        }

        if (!cachedOtp.equals(otp)) {
            log.warn("Invalid OTP for mobile: {}", mobile);
            throw new BadRequestException("Invalid OTP");
        }

        otpCacheService.clearOtp(mobile);

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        log.info("OTP verified successfully for mobile: {}", mobile);

        Account account = new Account();
        account.setUserId(user.getId());
        account.setBalance(new BigDecimal("1000.00"));

        accountRepository.save(account);

        log.info("Account created for user: {}", user.getId());

        return "User verified and account created";
    }

    @Override
    public String login(String mobile) {
        log.info("Login attempt for mobile: {}", mobile);

        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            log.warn("Login failed: user not active for mobile: {}", mobile);
            throw new BadRequestException("User not active");
        }

        String token = jwtUtil.generateToken(mobile);

        log.info("Login successful for mobile: {}", mobile);

        return token;
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}