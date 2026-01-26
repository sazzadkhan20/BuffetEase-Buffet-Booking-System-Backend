package com.BuffetEase.utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OtpStore {

    public static class OtpData {
        public String otp;
        public LocalDateTime expiry;

        public OtpData(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }

    private static final Map<String, OtpData> otpMap = new ConcurrentHashMap<>();

    public static void storeOtp(String email, String otp) {
        otpMap.put(email, new OtpData(
                otp,
                LocalDateTime.now().plusMinutes(5)
        ));
    }

    public static OtpData getOtp(String email) {
        return otpMap.get(email);
    }

    public static void removeOtp(String email) {
        otpMap.remove(email);
    }
}
