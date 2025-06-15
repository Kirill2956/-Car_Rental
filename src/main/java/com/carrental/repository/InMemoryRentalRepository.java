package com.carrental.repository;

import com.carrental.model.Rental;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the {@link RentalRepository} interface.
 * This repository stores {@link Rental} objects in a {@link ConcurrentHashMap}.
 */
public class InMemoryRentalRepository implements RentalRepository {

    private final Map<UUID, Rental> rentals = new ConcurrentHashMap<>();

    /**
     * Saves a rental entity to the in-memory store.
     * If a rental with the same ID already exists, it will be updated.
     * @param rental The rental to save.
     */
    @Override
    public void save(Rental rental) {
        rentals.put(rental.getId(), rental);
    }

    /**
     * Finds a rental by its unique identifier in the in-memory store.
     * @param id The UUID of the rental to find.
     * @return An Optional containing the found rental, or empty if not found.
     */
    @Override
    public Optional<Rental> findById(UUID id) {
        return Optional.ofNullable(rentals.get(id));
    }

    /**
     * Retrieves all rental entities from the in-memory store.
     * @return A list of all rentals.
     */
    @Override
    public List<Rental> findAll() {
        return new java.util.ArrayList<>(rentals.values());
    }

    /**
     * Deletes a rental by its unique identifier from the in-memory store.
     * @param id The UUID of the rental to delete.
     */
    @Override
    public void delete(UUID id) {
        rentals.remove(id);
    }
} 