package com.carrental.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a car rental booking in the system.
 */
public class Rental {
    private final UUID id;
    private final UUID userId;
    private final UUID carId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double totalCost;
    private RentalStatus status;
    private Map<String, Double> additionalOptions;

    /**
     * Constructs a new Rental instance.
     * @param userId The unique identifier of the user making the rental.
     * @param carId The unique identifier of the car being rented.
     * @param startDate The start date and time of the rental period.
     * @param endDate The end date and time of the rental period.
     */
    public Rental(UUID userId, UUID carId, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = RentalStatus.PENDING;
        this.additionalOptions = new HashMap<>();
    }

    /**
     * Enumeration for the possible statuses of a car rental.
     */
    public enum RentalStatus {
        PENDING,
        ACTIVE,
        COMPLETED,
        CANCELLED
    }

    /**
     * Returns the unique identifier of the rental.
     * @return The UUID of the rental.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the unique identifier of the user who made the rental.
     * @return The UUID of the user.
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Returns the unique identifier of the car rented.
     * @return The UUID of the car.
     */
    public UUID getCarId() {
        return carId;
    }

    /**
     * Returns the start date and time of the rental.
     * @return The start date and time.
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date and time of the rental.
     * @param startDate The new start date and time.
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date and time of the rental.
     * @return The end date and time.
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date and time of the rental.
     * @param endDate The new end date and time.
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the total cost of the rental.
     * @return The total cost.
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Sets the total cost of the rental.
     * @param totalCost The new total cost.
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Returns the current status of the rental.
     * @return The current rental status.
     */
    public RentalStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the rental.
     * @param status The new rental status.
     */
    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    /**
     * Returns a map of additional options chosen for the rental and their respective costs.
     * @return A map of additional options.
     */
    public Map<String, Double> getAdditionalOptions() {
        return additionalOptions;
    }

    /**
     * Sets the map of additional options for the rental.
     * @param additionalOptions A map of additional options and their costs.
     */
    public void setAdditionalOptions(Map<String, Double> additionalOptions) {
        this.additionalOptions = additionalOptions;
    }
} 