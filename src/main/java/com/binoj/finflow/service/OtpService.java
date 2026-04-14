package com.binoj.finflow.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OtpService {

    @Retry(name = "otpRetry", fallbackMethod = "fallbackOtp")
    public void sendOtp(String mobile, String otp) {

        log.info("Sending OTP to mobile {}", mobile);

        boolean isSent = simulateExternalOtpService(mobile, otp);

        if (!isSent) {
            log.warn("OTP sending failed, triggering retry...");
            throw new RuntimeException("OTP delivery failed");
        }

        log.info("OTP sent successfully to {}", mobile);
    }

    public void fallbackOtp(String mobile, String otp, Exception ex) {

        log.error("OTP sending FAILED after retries for mobile {}. Reason: {}",
                mobile, ex.getMessage());

        throw new RuntimeException("Unable to send OTP. Please try again later.");
    }


    private boolean simulateExternalOtpService(String mobile, String otp) {
        return Math.random() > 0.5;
    }
}