package com.carrental.repository;

import com.carrental.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the {@link CarRepository} interface.
 * This repository stores {@link Car} objects in a {@link ConcurrentHashMap}.
 */
public class InMemoryCarRepository implements CarRepository {

    private final Map<UUID, Car> cars = new ConcurrentHashMap<>();

    /**
     * Saves a car entity to the in-memory store.
     * If a car with the same ID already exists, it will be updated.
     * @param car The car to save.
     */
    @Override
    public void save(Car car) {
        cars.put(car.getId(), car);
    }

    /**
     * Finds a car by its unique identifier in the in-memory store.
     * @param id The UUID of the car to find.
     * @return An Optional containing the found car, or empty if not found.
     */
    @Override
    public Optional<Car> findById(UUID id) {
        return Optional.ofNullable(cars.get(id));
    }

    /**
     * Retrieves all car entities from the in-memory store.
     * @return A list of all cars.
     */
    @Override
    public List<Car> findAll() {
        return new java.util.ArrayList<>(cars.values());
    }

    /**
     * Deletes a car by its unique identifier from the in-memory store.
     * @param id The UUID of the car to delete.
     */
    @Override
    public void delete(UUID id) {
        cars.remove(id);
    }

    /**
     * Finds all cars that are currently available for rental.
     * @return A list of available cars.
     */
    public List<Car> findAvailableCars() {
        return cars.values().stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());
    }
} 