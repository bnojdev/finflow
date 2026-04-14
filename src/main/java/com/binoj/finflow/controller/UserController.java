package com.binoj.finflow.controller;

import com.binoj.finflow.dto.ApiResponse;
import com.binoj.finflow.entity.User;
import com.binoj.finflow.security.TokenBlacklistService;
import com.binoj.finflow.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;

    public UserController(UserService userService, TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody User user) {
        log.info("Registration request for user with mobile {}", user.getMobile());
        String msg = userService.register(user);
        return new ResponseEntity<>(
                new ApiResponse<>(201, msg, null),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifyOtp(@RequestParam String mobile,
                            @RequestParam String otp) {
        log.info("OTP verification request for mobile {}", mobile);
        String msg = userService.verifyOtp(mobile, otp);
        return ResponseEntity.ok(
                new ApiResponse<>(200, msg, null)
        );
    }

    @PostMapping("/login")
    public  ResponseEntity<ApiResponse<?>> login(@RequestParam String mobile) {
        log.info("Login request for mobile {}", mobile);
        String token = userService.login(mobile);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Login successful", token)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            log.info("Token blacklisted for logout");
            return ResponseEntity.ok(new ApiResponse<>(200, "Logged out successfully", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Invalid token", null));
        }
    }
}
