package com.binoj.finflow.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpCacheService {

    private record OtpData(String otp, LocalDateTime expiry) {}

    private final Map<String, OtpData> cache = new ConcurrentHashMap<>();

    public void saveOtp(String mobile, String otp) {
        cache.put(mobile,
                new OtpData(otp, LocalDateTime.now().plusMinutes(5)));
    }

    public String getOtp(String mobile) {
        OtpData data = cache.get(mobile);

        if (data == null) return null;

        if (data.expiry().isBefore(LocalDateTime.now())) {
            cache.remove(mobile);
            return null;
        }

        return data.otp();
    }

    public void clearOtp(String mobile) {
        cache.remove(mobile);
    }
}