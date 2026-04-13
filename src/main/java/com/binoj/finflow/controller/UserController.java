package com.binoj.finflow.controller;

import com.binoj.finflow.dto.ApiResponse;
import com.binoj.finflow.entity.User;
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

    public UserController(UserService userService) {
        this.userService = userService;
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
    public String verifyOtp(@RequestParam String mobile,
                            @RequestParam String otp) {
        log.info("OTP verification request for mobile {}", mobile);
        return userService.verifyOtp(mobile, otp);
    }

    @PostMapping("/login")
    public  ResponseEntity<ApiResponse<?>> login(@RequestParam String mobile) {
        log.info("Login request for mobile {}", mobile);
        String token = userService.login(mobile);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Login successful", token)
        );
    }
}
