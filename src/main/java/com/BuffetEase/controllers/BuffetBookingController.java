package com.BuffetEase.controllers;

import com.BuffetEase.dtos.BuffetBookingCreateDTO;
import com.BuffetEase.dtos.BuffetBookingUpdateDTO;
import com.BuffetEase.services.BuffetBookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BuffetBookingController {

    private BuffetBookingService service;

    public BuffetBookingController(BuffetBookingService service) {
        this.service = service;
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> create(@Valid @RequestBody BuffetBookingCreateDTO dto, BindingResult br) {
        if (br.hasErrors())
            return ResponseEntity.badRequest().body(
                    br.getFieldErrors().stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .toList()
            );

        service.createBooking(dto);
        return ResponseEntity.ok("Booking confirmed");
    }

    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable int id) {
        service.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled");
    }

    @PutMapping("/bookings/{id}/modify")
    public ResponseEntity<?> modify(@PathVariable int id,
                                    @Valid @RequestBody BuffetBookingUpdateDTO dto,
                                    BindingResult br) {
        if (br.hasErrors())
            return ResponseEntity.badRequest().body(
                    br.getFieldErrors().stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .toList()
            );

        service.modifyGuests(id, dto);
        return ResponseEntity.ok("Booking updated");
    }

    @GetMapping("/{userId}/bookings")
    public List<Map<String, Object>> userBookings(@PathVariable int userId) {
        return service.userBookings(userId);
    }

    @GetMapping("/bookings")
    public List<Map<String, Object>> adminBookings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer userId) {
        return service.adminBookings(status, userId);
    }
}
