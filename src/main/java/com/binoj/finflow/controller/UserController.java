package com.binoj.finflow.controller;

import com.binoj.finflow.entity.User;
import com.binoj.finflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String mobile,
                            @RequestParam String otp) {
        return userService.verifyOtp(mobile, otp);
    }
}
