package com.carrental.repository;

import com.carrental.model.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Car entities.
 * Provides standard CRUD operations and specific query methods for cars.
 */
public interface CarRepository {
    /**
     * Saves a car entity. If the car already exists, it updates it.
     * @param car The car to save.
     */
    void save(Car car);
    /**
     * Finds a car by its unique identifier.
     * @param id The UUID of the car to find.
     * @return An Optional containing the found car, or empty if not found.
     */
    Optional<Car> findById(UUID id);
    /**
     * Retrieves all car entities.
     * @return A list of all cars.
     */
    List<Car> findAll();
    /**
     * Deletes a car by its unique identifier.
     * @param id The UUID of the car to delete.
     */
    void delete(UUID id);
} 