package com.carrental.repository;

import com.carrental.model.Rental;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Rental entities.
 * Provides standard CRUD operations for rental bookings.
 */
public interface RentalRepository {
    /**
     * Saves a rental entity. If the rental already exists, it updates it.
     * @param rental The rental to save.
     */
    void save(Rental rental);
    /**
     * Finds a rental by its unique identifier.
     * @param id The UUID of the rental to find.
     * @return An Optional containing the found rental, or empty if not found.
     */
    Optional<Rental> findById(UUID id);
    /**
     * Retrieves all rental entities.
     * @return A list of all rentals.
     */
    List<Rental> findAll();
    /**
     * Deletes a rental by its unique identifier.
     * @param id The UUID of the rental to delete.
     */
    void delete(UUID id);
} 