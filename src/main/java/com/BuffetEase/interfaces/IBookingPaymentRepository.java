package com.BuffetEase.interfaces;

import com.BuffetEase.entities.BookingPaymentEntity;

import java.util.List;
import java.util.Map;

public interface IBookingPaymentRepository {
    void create(BookingPaymentEntity payment);

    void updateStatus(String transactionId, String status);

    //BookingPaymentEntity findByTransactionId(String transactionId);

    List<Map<String, Object>> findAll();

    Map<String, Object> findByPaymentId(int id);

    List<Map<String, Object>> findByBookingId(int bookingId);

    Map<String, Object> getPaymentInitData(int bookingId);
}
