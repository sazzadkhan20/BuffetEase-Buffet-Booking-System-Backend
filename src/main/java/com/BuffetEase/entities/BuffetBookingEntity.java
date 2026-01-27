package com.BuffetEase.entities;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BuffetBookingEntity {
    private int id;

    @NotNull
    @Min(1)
    private int userId;

    @NotNull
    @Min(1)
    private int buffetScheduleId;

    @NotNull
    @Min(1)
    private int numberOfGuests;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal totalPrice;

    @NotNull
    private String bookingStatus;

    private LocalDateTime bookingTime;

    public BuffetBookingEntity() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBuffetScheduleId() { return buffetScheduleId; }
    public void setBuffetScheduleId(int buffetScheduleId) {
        this.buffetScheduleId = buffetScheduleId;
    }

    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
}
