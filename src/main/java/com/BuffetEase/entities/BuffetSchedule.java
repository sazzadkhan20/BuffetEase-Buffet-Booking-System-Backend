package com.BuffetEase.entities;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class BuffetSchedule {

    @Min(1)
    private int id;

    @Min(1)
    private int buffetPackageId;

    @NotNull
    private LocalDate buffetDate;

    @Min(0)
    private int availableCapacity;

    @NotNull
    private LocalTime bookingCutoffTime;

    private boolean isOpen;

    public BuffetSchedule() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuffetPackageId() {
        return buffetPackageId;
    }

    public void setBuffetPackageId(int buffetPackageId) {
        this.buffetPackageId = buffetPackageId;
    }

    public LocalDate getBuffetDate() {
        return buffetDate;
    }

    public void setBuffetDate(LocalDate buffetDate) {
        this.buffetDate = buffetDate;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public LocalTime getBookingCutoffTime() {
        return bookingCutoffTime;
    }

    public void setBookingCutoffTime(LocalTime bookingCutoffTime) {
        this.bookingCutoffTime = bookingCutoffTime;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
