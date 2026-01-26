package com.BuffetEase.services;

import com.BuffetEase.dtos.PaymentDTO;
import com.BuffetEase.entities.BookingPaymentEntity;
import com.BuffetEase.interfaces.IBookingPaymentRepository;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class BookingPaymentService {
    private final IBookingPaymentRepository repository;

    public BookingPaymentService(IBookingPaymentRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> createPayment(PaymentDTO dto) {

        Map<String, Object> data =
                repository.getPaymentInitData(dto.getBookingId());

        if (data == null) {
            throw new IllegalArgumentException("Invalid booking id or booking not payable");
        }

        String transactionId = "TXN_" + UUID.randomUUID().toString().substring(0, 10);

        BookingPaymentEntity payment = new BookingPaymentEntity();
        payment.setBookingId(dto.getBookingId());
        payment.setAmount(new BigDecimal(data.get("total_price").toString()));
        payment.setPaymentMethod("SSL_COMMERZ");
        payment.setPaymentStatus("PENDING");
        payment.setTransactionId(transactionId);

        repository.create(payment);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("store_id", "testbox");
        params.add("store_passwd", "qwerty");
        params.add("total_amount", payment.getAmount().toString());
        params.add("currency", "BDT");
        params.add("tran_id", transactionId);
        params.add("success_url", "http://localhost:8080/payment/success");
        params.add("fail_url", "http://localhost:8080/payment/fail");
        params.add("cus_name", data.get("name").toString());
        params.add("cus_email", data.get("email").toString());
        params.add("cus_phone", data.get("phone").toString());
        params.add("cus_add1", "Dhaka");
        params.add("cus_city", "Dhaka");
        params.add("cus_country", "Bangladesh");

        params.add("shipping_method", "NO");
        params.add("product_name", "Buffet Booking");
        params.add("product_category", "Service");
        params.add("product_profile", "general");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new RestTemplate().postForObject(
                "https://sandbox.sslcommerz.com/gwprocess/v4/api.php",
                new HttpEntity<>(params, headers),
                Map.class
        );
    }

    public void markSuccess(String txn) {
        repository.updateStatus(txn, "PAID");
    }

    public void markFailed(String txn) {
        repository.updateStatus(txn, "FAILED");
    }

    public List<Map<String, Object>> getAll() {
        return repository.findAll();
    }

    public Map<String, Object> getByPaymentId(int id) {
        return repository.findByPaymentId(id);
    }

    public List<Map<String, Object>> getByBookingId(int bookingId) {
        return repository.findByBookingId(bookingId);
    }
}
