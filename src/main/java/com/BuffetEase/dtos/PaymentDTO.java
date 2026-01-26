package com.BuffetEase.dtos;

import jakarta.validation.constraints.*;

public class PaymentDTO {

    @NotNull
    @Min(1)
    @Positive
    private int bookingId;

    public PaymentDTO() {}

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
}
