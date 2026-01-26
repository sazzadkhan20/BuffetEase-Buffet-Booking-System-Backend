package com.BuffetEase.dtos;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class BuffetBookingCreateDTO {

    @NotBlank
    @Min(1)
    private int userId;

    @NotBlank
    @Min(1)
    private int buffetScheduleId;

    @NotBlank
    @Min(value = 1, message = "numberOfGuests must be greater than 0")
    private int numberOfGuests;

    @NotBlank
    @Min(1)
    private int paymentId;

    public BuffetBookingCreateDTO() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBuffetScheduleId() {
        return buffetScheduleId;
    }

    public void setBuffetScheduleId(int buffetScheduleId) {
        this.buffetScheduleId = buffetScheduleId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
}
