package com.BuffetEase.dtos;

import jakarta.validation.constraints.Min;

public class BuffetBookingUpdateDTO {
    @Min(value = 1, message = "numberOfGuests must be greater than 0")
    private int newGuestCount;

    public BuffetBookingUpdateDTO(){}

    public int getNewGuestCount() {
        return newGuestCount;
    }

    public void setNewGuestCount(int newGuestCount) {
        this.newGuestCount = newGuestCount;
    }
}
