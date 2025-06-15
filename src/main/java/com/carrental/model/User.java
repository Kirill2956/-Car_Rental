package com.carrental.model;

import java.util.UUID;

/**
 * Represents a user in the car rental system.
 */
public class User {
    private final UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private String licenseNumber;

    /**
     * Constructs a new User instance.
     * @param name The full name of the user.
     * @param email The email address of the user.
     * @param phoneNumber The phone number of the user.
     * @param licenseNumber The driver's license number of the user.
     */
    public User(String name, String email, String phoneNumber, String licenseNumber) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
    }

    /**
     * Returns the unique identifier of the user.
     * @return The UUID of the user.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the name of the user.
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     * @param name The new name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email address of the user.
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * @param email The new email address of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the phone number of the user.
     * @return The phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     * @param phoneNumber The new phone number of the user.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the driver's license number of the user.
     * @return The driver's license number of the user.
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * Sets the driver's license number of the user.
     * @param licenseNumber The new driver's license number of the user.
     */
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
} 