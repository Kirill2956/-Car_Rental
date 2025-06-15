package com.carrental.service;

import com.carrental.model.Car;
import com.carrental.model.Rental;
import com.carrental.model.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.RentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing car rental operations.
 * This class handles the business logic related to booking, modifying, and canceling car rentals.
 */
public class RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);
    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    /**
     * Constructs a new RentalService with the given repositories.
     * @param carRepository The repository for managing car data.
     * @param rentalRepository The repository for managing rental data.
     */
    public RentalService(CarRepository carRepository, RentalRepository rentalRepository) {
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
    }

    /**
     * Books a car for a user for a specified period with additional options.
     * @param userId The unique identifier of the user making the booking.
     * @param carId The unique identifier of the car to be booked.
     * @param startDate The start date and time of the rental.
     * @param endDate The end date and time of the rental.
     * @param additionalOptions A map of additional options (e.g., "Child Seat" -> cost).
     * @return An Optional containing the newly created Rental if successful, or empty if the car is unavailable or already booked.
     */
    public Optional<Rental> bookCar(UUID userId, UUID carId, LocalDateTime startDate, LocalDateTime endDate, Map<String, Double> additionalOptions) {
        Optional<Car> carOptional = carRepository.findById(carId);
        if (carOptional.isEmpty() || !carOptional.get().isAvailable()) {
            logger.warn("Attempted to book unavailable or non-existent car with ID: {}", carId);
            return Optional.empty(); // Car not found or not available
        }

        Car car = carOptional.get();
        // Basic check for overlapping rentals (can be expanded for more robust checks)
        boolean isCarBooked = rentalRepository.findAll().stream()
                .filter(r -> r.getCarId().equals(carId) && (r.getStatus() == Rental.RentalStatus.ACTIVE || r.getStatus() == Rental.RentalStatus.PENDING))
                .anyMatch(r -> !(endDate.isBefore(r.getStartDate()) || startDate.isAfter(r.getEndDate())));

        if (isCarBooked) {
            logger.warn("Attempted to book car with ID: {} for an overlapping period.", carId);
            return Optional.empty(); // Car already booked for the specified period
        }

        Rental rental = new Rental(userId, carId, startDate, endDate);
        rental.setAdditionalOptions(additionalOptions);
        rental.setTotalCost(calculateTotalCost(car, startDate, endDate, additionalOptions));
        rentalRepository.save(rental);
        car.setAvailable(false); // Mark car as unavailable
        carRepository.save(car);
        logger.info("Car rental booked successfully: {}", rental.getId());
        return Optional.of(rental);
    }

    /**
     * Modifies an existing car rental booking.
     * A rental can only be modified if its current status is PENDING.
     * @param rentalId The unique identifier of the rental to modify.
     * @param newStartDate The new start date and time for the rental.
     * @param newEndDate The new end date and time for the rental.
     * @param newAdditionalOptions The new map of additional options for the rental.
     * @return An Optional containing the updated Rental if successful, or empty if the rental is not found, not modifiable, or new dates conflict.
     */
    public Optional<Rental> modifyRental(UUID rentalId, LocalDateTime newStartDate, LocalDateTime newEndDate, Map<String, Double> newAdditionalOptions) {
        Optional<Rental> rentalOptional = rentalRepository.findById(rentalId);
        if (rentalOptional.isEmpty() || rentalOptional.get().getStatus() != Rental.RentalStatus.PENDING) {
            logger.warn("Attempted to modify non-existent or unmodifiable rental with ID: {}", rentalId);
            return Optional.empty(); // Rental not found or cannot be modified in current status
        }

        Rental rental = rentalOptional.get();
        Optional<Car> carOptional = carRepository.findById(rental.getCarId());
        if (carOptional.isEmpty()) {
            logger.error("Car associated with rental ID: {} not found. Data inconsistency detected.", rentalId);
            return Optional.empty(); // Car associated with rental not found (shouldn't happen in a consistent system)
        }

        Car car = carOptional.get();

        // Check for new date conflicts, assuming the car becomes available temporarily during modification
        boolean isCarBooked = rentalRepository.findAll().stream()
                .filter(r -> r.getCarId().equals(car.getId()) && !r.getId().equals(rentalId) && (r.getStatus() == Rental.RentalStatus.ACTIVE || r.getStatus() == Rental.RentalStatus.PENDING))
                .anyMatch(r -> !(newEndDate.isBefore(r.getStartDate()) || newStartDate.isAfter(r.getEndDate())));

        if (isCarBooked) {
            logger.warn("Attempted to modify rental with ID: {} to an overlapping period.", rentalId);
            return Optional.empty(); // Car already booked for the new specified period
        }

        rental.setStartDate(newStartDate);
        rental.setEndDate(newEndDate);
        rental.setAdditionalOptions(newAdditionalOptions);
        rental.setTotalCost(calculateTotalCost(car, newStartDate, newEndDate, newAdditionalOptions));
        rentalRepository.save(rental);
        logger.info("Car rental modified successfully: {}", rental.getId());
        return Optional.of(rental);
    }

    /**
     * Cancels an existing car rental booking.
     * @param rentalId The unique identifier of the rental to cancel.
     * @return true if the rental was successfully cancelled, false otherwise.
     */
    public boolean cancelRental(UUID rentalId) {
        Optional<Rental> rentalOptional = rentalRepository.findById(rentalId);
        if (rentalOptional.isEmpty()) {
            logger.warn("Attempted to cancel non-existent rental with ID: {}", rentalId);
            return false; // Rental not found
        }

        Rental rental = rentalOptional.get();
        rental.setStatus(Rental.RentalStatus.CANCELLED);
        rentalRepository.save(rental);

        Optional<Car> carOptional = carRepository.findById(rental.getCarId());
        carOptional.ifPresent(car -> {
            car.setAvailable(true); // Mark car as available again
            carRepository.save(car);
            logger.info("Car {} marked as available after rental {} cancellation.", car.getId(), rental.getId());
        });
        logger.info("Car rental cancelled successfully: {}", rental.getId());
        return true;
    }

    /**
     * Retrieves a list of all existing car rental bookings.
     * @return A list of all rentals.
     */
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Retrieves a specific rental booking by its unique identifier.
     * @param id The UUID of the rental to retrieve.
     * @return An Optional containing the found Rental, or empty if not found.
     */
    public Optional<Rental> getRentalById(UUID id) {
        return rentalRepository.findById(id);
    }

    /**
     * Calculates the total cost of a rental based on the car's daily rate and additional options.
     * @param car The car being rented.
     * @param startDate The start date and time of the rental.
     * @param endDate The end date and time of the rental.
     * @param additionalOptions A map of additional options and their costs.
     * @return The calculated total cost for the rental period.
     */
    private double calculateTotalCost(Car car, LocalDateTime startDate, LocalDateTime endDate, Map<String, Double> additionalOptions) {
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to include start and end day
        double cost = days * car.getDailyRate();
        for (Double optionCost : additionalOptions.values()) {
            cost += optionCost;
        }
        return cost;
    }
} 