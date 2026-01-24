package com.BuffetEase.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalTime;

public class BuffetPackageDTO {

    private int id;

    @NotBlank
    @Size(max = 50)
    private String packageName;

    private String description;

    @NotNull
    @DecimalMin("0.1")
    private BigDecimal pricePerPerson;

    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

    @NotNull
    @Min(1)
    private int maxCapacity;

    @NotNull
    private boolean isActive;

    public BuffetPackageDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(BigDecimal pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "BuffetPackageDTO{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", description='" + description + '\'' +
                ", pricePerPerson=" + pricePerPerson +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", maxCapacity=" + maxCapacity +
                ", isActive=" + isActive +
                '}';
    }
}
