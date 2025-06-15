package com.carrental.model;

import java.util.UUID;

/**
 * Represents a car available for rental in the system.
 */
public class Car {
    private final UUID id;
    private String brand;
    private String model;
    private int year;
    private double dailyRate;
    private boolean available;

    /**
     * Constructs a new Car instance.
     * @param brand The brand of the car (e.g., "Toyota").
     * @param model The model of the car (e.g., "Camry").
     * @param year The manufacturing year of the car.
     * @param dailyRate The daily rental rate for the car.
     */
    public Car(String brand, String model, int year, double dailyRate) {
        this.id = UUID.randomUUID();
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
        this.available = true;
    }

    /**
     * Returns the unique identifier of the car.
     * @return The UUID of the car.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the brand of the car.
     * @return The brand of the car.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the car.
     * @param brand The new brand of the car.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the model of the car.
     * @return The model of the car.
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of the car.
     * @param model The new model of the car.
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Returns the manufacturing year of the car.
     * @return The manufacturing year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the manufacturing year of the car.
     * @param year The new manufacturing year.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Returns the daily rental rate of the car.
     * @return The daily rental rate.
     */
    public double getDailyRate() {
        return dailyRate;
    }

    /**
     * Sets the daily rental rate of the car.
     * @param dailyRate The new daily rental rate.
     */
    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    /**
     * Checks if the car is currently available for rental.
     * @return true if the car is available, false otherwise.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets the availability status of the car.
     * @param available true to mark the car as available, false as unavailable.
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
} 