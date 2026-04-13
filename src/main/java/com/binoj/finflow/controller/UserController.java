package com.binoj.finflow.controller;

import com.binoj.finflow.entity.User;
import com.binoj.finflow.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody User user) {
        log.info("Registration request for user with mobile {}", user.getMobile());
        return userService.register(user);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String mobile,
                            @RequestParam String otp) {
        log.info("OTP verification request for mobile {}", mobile);
        return userService.verifyOtp(mobile, otp);
    }

    @PostMapping("/login")
    public String login(@RequestParam String mobile) {
        log.info("Login request for mobile {}", mobile);
        return userService.login(mobile);
    }
}
