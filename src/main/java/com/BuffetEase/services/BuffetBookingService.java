package com.BuffetEase.services;

import com.BuffetEase.dtos.BuffetBookingCreateDTO;
import com.BuffetEase.dtos.BuffetBookingUpdateDTO;
import com.BuffetEase.entities.BuffetBookingEntity;
import com.BuffetEase.interfaces.IBuffetBookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class BuffetBookingService {

    private IBuffetBookingRepository repo;

    public BuffetBookingService(IBuffetBookingRepository repo) {
        this.repo = repo;
    }

    public void createBooking(BuffetBookingCreateDTO dto) {

        if (!repo.userExistsAndActive(dto.getUserId()))
            throw new RuntimeException("User not found or inactive");

        if (!repo.buffetScheduleExistsAndOpen(dto.getBuffetScheduleId()))
            throw new RuntimeException("Buffet schedule closed or not found");

        if (!repo.buffetScheduleDateValid(dto.getBuffetScheduleId()))
            throw new RuntimeException("Buffet date already passed");

        if (!repo.buffetPackageActiveForSchedule(dto.getBuffetScheduleId()))
            throw new RuntimeException("Buffet package is inactive");

        if (!repo.paymentVerified(dto.getPaymentId()))
            throw new RuntimeException("Payment not verified");

        if (dto.getNumberOfGuests() > repo.getAvailableCapacity(dto.getBuffetScheduleId()))
            throw new RuntimeException("Not enough available seats");

        double totalPrice = repo.calculateTotalPrice(dto.getBuffetScheduleId(), dto.getNumberOfGuests());

        BuffetBookingEntity booking = new BuffetBookingEntity();
        booking.setUserId(dto.getUserId());
        booking.setBuffetScheduleId(dto.getBuffetScheduleId());
        booking.setNumberOfGuests(dto.getNumberOfGuests());
        booking.setTotalPrice(java.math.BigDecimal.valueOf(totalPrice));
        booking.setBookingStatus("CONFIRMED");
        booking.setBookingTime(LocalDateTime.now());

        repo.createBooking(booking);
        repo.decreaseAvailableCapacity(dto.getBuffetScheduleId(), dto.getNumberOfGuests());
    }

    public void cancelBooking(int bookingId) {
        if (!repo.bookingExists(bookingId))
            throw new RuntimeException("Booking not found");
        BuffetBookingEntity b = repo.findById(bookingId);

        if (!"CONFIRMED".equals(b.getBookingStatus()))
            throw new RuntimeException("Only confirmed bookings can be cancelled");

        if (LocalDateTime.now().isAfter(repo.getBookingCutoffTime(b.getBuffetScheduleId())))
            throw new RuntimeException("Booking cutoff time passed");

        repo.updateBookingStatus(bookingId, "CANCELLED");
        repo.increaseAvailableCapacity(b.getBuffetScheduleId(), b.getNumberOfGuests());
    }

    public void modifyGuests(int bookingId, BuffetBookingUpdateDTO dto) {
        if (!repo.bookingExists(bookingId))
            throw new RuntimeException("Booking not found");
        BuffetBookingEntity b = repo.findById(bookingId);
        int diff = dto.getNewGuestCount() - b.getNumberOfGuests();

        if (diff > repo.getAvailableCapacity(b.getBuffetScheduleId()))
            throw new RuntimeException("Insufficient capacity");

        double newPrice = repo.calculateTotalPrice(b.getBuffetScheduleId(), dto.getNewGuestCount());

        repo.updateGuestCount(bookingId, dto.getNewGuestCount(), newPrice);
        repo.decreaseAvailableCapacity(b.getBuffetScheduleId(), diff);
    }

    public List<Map<String, Object>> userBookings(int userId) {
        return repo.findBookingsByUserId(userId);
    }

    public List<Map<String, Object>> adminBookings(String status, Integer userId) {
        return repo.adminFilterBookings(status, userId);
    }

}
