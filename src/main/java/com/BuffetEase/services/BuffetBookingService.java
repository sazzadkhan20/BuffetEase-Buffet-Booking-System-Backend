package com.BuffetEase.services;

import com.BuffetEase.dtos.BuffetBookingCreateDTO;
import com.BuffetEase.dtos.BuffetBookingUpdateDTO;
import com.BuffetEase.dtos.BuffetScheduleDTO;
import com.BuffetEase.entities.BuffetBookingEntity;
import com.BuffetEase.exceptions.PackageNotFoundException;
import com.BuffetEase.interfaces.IBuffetBookingRepository;
import com.BuffetEase.repositories.BuffetBookingRepository;
import com.BuffetEase.repositories.BuffetPackageRepository;
import com.BuffetEase.repositories.BuffetScheduleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class BuffetBookingService {

    private IBuffetBookingRepository repo;
    private final BuffetScheduleRepository scheduleRepo;
    private final BuffetPackageRepository packageRepo;

    public BuffetBookingService(IBuffetBookingRepository repo,
                                BuffetScheduleRepository scheduleRepo,
                                BuffetPackageRepository packageRepo) {
        this.repo = repo;
        this.scheduleRepo = scheduleRepo;
        this.packageRepo = packageRepo;

    }

    public void createBooking(int id,BuffetBookingCreateDTO dto) {
        int packageId = scheduleRepo.checkAvailability(dto.getBuffetScheduleId(),dto.getNumberOfGuests());
        if (packageId == 0)
            throw new PackageNotFoundException("Buffet schedule closed or not found");
        else if(packageRepo.checkAvailability(packageId).equals(BigDecimal.ZERO))
            throw new PackageNotFoundException("Buffet package not available or date passed");
        BuffetBookingEntity booking = new BuffetBookingEntity();
        booking.setUserId(id);
        booking.setBookingStatus("PENDING");
        booking.setBuffetScheduleId(dto.getBuffetScheduleId());
        BigDecimal pricePerPerson = packageRepo.checkAvailability(packageId);
        BigDecimal totalPrice = pricePerPerson
                .multiply(BigDecimal.valueOf(dto.getNumberOfGuests()));
        booking.setTotalPrice(totalPrice);
        booking.setNumberOfGuests(dto.getNumberOfGuests());
        repo.createBooking(booking);
        scheduleRepo.update(dto.getBuffetScheduleId(), dto.getNumberOfGuests());
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
