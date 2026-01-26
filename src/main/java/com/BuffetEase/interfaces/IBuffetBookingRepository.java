package com.BuffetEase.interfaces;
import com.BuffetEase.entities.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IBuffetBookingRepository {

    int createBooking(BuffetBookingEntity booking);

    BuffetBookingEntity findById(int bookingId);

    List<Map<String, Object>> findBookingsByUserId(int userId);

    boolean bookingExists(int bookingId);

    void updateBookingStatus(int bookingId, String status);

    void updateGuestCount(int bookingId, int guestCount, double newTotalPrice);

    void increaseAvailableCapacity(int scheduleId, int seats);

    void decreaseAvailableCapacity(int scheduleId, int seats);

    int getAvailableCapacity(int scheduleId);

    LocalDateTime getBookingCutoffTime(int scheduleId);

    boolean buffetScheduleExistsAndOpen(int scheduleId);

    boolean buffetScheduleDateValid(int scheduleId);

    boolean buffetPackageActiveForSchedule(int scheduleId);

    boolean userExistsAndActive(int userId);

    boolean paymentVerified(int paymentId);

    double calculateTotalPrice(int scheduleId, int guestCount);

    List<Map<String, Object>> adminFilterBookings(String status, Integer userId);
}
