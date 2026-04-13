package com.binoj.finflow.service;

import com.binoj.finflow.entity.User;

public interface  UserService {
    String register(User user);
    String verifyOtp(String mobile, String otp);

    String login(String mobile);
}
