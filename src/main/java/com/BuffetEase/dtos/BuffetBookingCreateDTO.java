package com.BuffetEase.dtos;

import jakarta.validation.constraints.*;

public class BuffetBookingCreateDTO {

    @NotNull(message = "Buffet schedule ID is required")
    @Positive(message = "Buffet schedule ID must be a positive number")
    private Integer buffetScheduleId;

    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 50, message = "Number of guests cannot exceed 50")
    private Integer numberOfGuests = 1;

    public BuffetBookingCreateDTO() {
        this.numberOfGuests = 1;
    }

    public BuffetBookingCreateDTO(Integer buffetScheduleId, Integer numberOfGuests) {
        this.buffetScheduleId = buffetScheduleId;
        this.numberOfGuests = (numberOfGuests != null && numberOfGuests > 0) ? numberOfGuests : 1;
    }

    public BuffetBookingCreateDTO(Integer buffetScheduleId) {
        this.buffetScheduleId = buffetScheduleId;
        this.numberOfGuests = 1;
    }

    public Integer getBuffetScheduleId() {
        return buffetScheduleId;
    }

    public void setBuffetScheduleId(Integer buffetScheduleId) {
        this.buffetScheduleId = buffetScheduleId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
    public void setNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests == null || numberOfGuests <= 0) {
            this.numberOfGuests = 1;
        } else {
            this.numberOfGuests = numberOfGuests;
        }
    }
    @Override
    public String toString() {
        return "BuffetBookingCreateDTO{" +
                "buffetScheduleId=" + buffetScheduleId +
                ", numberOfGuests=" + numberOfGuests +
                '}';
    }
}