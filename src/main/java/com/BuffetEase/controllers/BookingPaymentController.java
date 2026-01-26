package com.BuffetEase.controllers;

import com.BuffetEase.dtos.PaymentDTO;
import com.BuffetEase.services.BookingPaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class BookingPaymentController {

    private final BookingPaymentService service;

    public BookingPaymentController(BookingPaymentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @Valid @RequestBody PaymentDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(service.createPayment(dto));
    }

    @PostMapping("/success")
    public void success(@RequestParam("tran_id") String tranId) {
        service.markSuccess(tranId);
    }

    @PostMapping("/fail")
    public void fail(@RequestParam("tran_id") String tranId) {
        service.markFailed(tranId);
    }

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getByPaymentId(@PathVariable int id) {
        return service.getByPaymentId(id);
    }

    @GetMapping("/booking/{bookingId}")
    public List<Map<String, Object>> getByBookingId(@PathVariable int bookingId) {
        return service.getByBookingId(bookingId);
    }
}
