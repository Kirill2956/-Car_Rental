package com.carrental.service;

import com.carrental.model.Car;
import com.carrental.model.Rental;
import com.carrental.model.User;
import com.carrental.repository.InMemoryCarRepository;
import com.carrental.repository.InMemoryRentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RentalService} class.
 */
class RentalServiceTest {

    private RentalService rentalService;
    private InMemoryCarRepository carRepository;
    private InMemoryRentalRepository rentalRepository;

    private Car car1;
    private User user1;

    private static final double CAR1_DAILY_RATE = 30.0;
    private static final double CHILD_SEAT_COST = 10.0;
    private static final double GPS_COST = 5.0;
    private static final int INITIAL_RENTAL_DAYS = 3; // Corresponds to 4 actual rental days including start and end
    private static final int MODIFIED_RENTAL_DAYS = 3; // Corresponds to 4 actual rental days after modification

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        // Arrange
        carRepository = new InMemoryCarRepository();
        rentalRepository = new InMemoryRentalRepository();
        rentalService = new RentalService(carRepository, rentalRepository);

        car1 = new Car("Toyota", "Corolla", 2020, CAR1_DAILY_RATE);
        carRepository.save(car1);

        user1 = new User("John Doe", "john.doe@example.com", "1234567890", "DL12345");
    }

    /**
     * Tests successful car booking with additional options.
     */
    @Test
    void testBookCarSuccess() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(INITIAL_RENTAL_DAYS);
        Map<String, Double> options = new HashMap<>();
        options.put("Child Seat", CHILD_SEAT_COST);

        // Act
        Optional<Rental> rental = rentalService.bookCar(user1.getId(), car1.getId(), startDate, endDate, options);

        // Assert
        assertTrue(rental.isPresent());
        assertEquals(Rental.RentalStatus.PENDING, rental.get().getStatus());
        assertFalse(carRepository.findById(car1.getId()).get().isAvailable());
        assertEquals(CAR1_DAILY_RATE * (INITIAL_RENTAL_DAYS + 1) + CHILD_SEAT_COST, rental.get().getTotalCost());
        assertTrue(rental.get().getAdditionalOptions().containsKey("Child Seat"));
    }

    /**
     * Tests booking a car that is currently unavailable.
     */
    @Test
    void testBookCarCarUnavailable() {
        // Arrange
        car1.setAvailable(false);
        carRepository.save(car1);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(INITIAL_RENTAL_DAYS);

        // Act
        Optional<Rental> rental = rentalService.bookCar(user1.getId(), car1.getId(), startDate, endDate, Collections.emptyMap());

        // Assert
        assertFalse(rental.isPresent());
    }

    /**
     * Tests booking a car that does not exist in the repository.
     */
    @Test
    void testBookCarCarNotFound() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(INITIAL_RENTAL_DAYS);

        // Act
        Optional<Rental> rental = rentalService.bookCar(user1.getId(), UUID.randomUUID(), startDate, endDate, Collections.emptyMap());

        // Assert
        assertFalse(rental.isPresent());
    }

    /**
     * Tests booking a car for a period that overlaps with an existing booking.
     */
    @Test
    void testBookCarOverlappingBooking() {
        // Arrange
        LocalDateTime startDate1 = LocalDateTime.now();
        LocalDateTime endDate1 = startDate1.plusDays(5);
        rentalService.bookCar(user1.getId(), car1.getId(), startDate1, endDate1, Collections.emptyMap());

        LocalDateTime startDate2 = startDate1.plusDays(2);
        LocalDateTime endDate2 = startDate1.plusDays(7);

        // Act
        Optional<Rental> rental = rentalService.bookCar(user1.getId(), car1.getId(), startDate2, endDate2, Collections.emptyMap());

        // Assert
        assertFalse(rental.isPresent());
    }

    /**
     * Tests successful modification of an existing rental.
     */
    @Test
    void testModifyRentalSuccess() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(INITIAL_RENTAL_DAYS);
        Optional<Rental> initialRental = rentalService.bookCar(user1.getId(), car1.getId(), startDate, endDate, Collections.emptyMap());

        assertTrue(initialRental.isPresent());
        Rental rentalToModify = initialRental.get();

        LocalDateTime newStartDate = startDate.plusDays(1);
        LocalDateTime newEndDate = newStartDate.plusDays(MODIFIED_RENTAL_DAYS);
        Map<String, Double> newOptions = new HashMap<>();
        newOptions.put("GPS", GPS_COST);

        // Act
        Optional<Rental> modifiedRental = rentalService.modifyRental(rentalToModify.getId(), newStartDate, newEndDate, newOptions);

        // Assert
        assertTrue(modifiedRental.isPresent());
        assertEquals(newStartDate, modifiedRental.get().getStartDate());
        assertEquals(newEndDate, modifiedRental.get().getEndDate());
        assertTrue(modifiedRental.get().getAdditionalOptions().containsKey("GPS"));
        assertEquals(CAR1_DAILY_RATE * (MODIFIED_RENTAL_DAYS + 1) + GPS_COST, modifiedRental.get().getTotalCost());
    }

    /**
     * Tests modifying a rental that does not exist.
     */
    @Test
    void testModifyRentalNotFound() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(INITIAL_RENTAL_DAYS);
        Map<String, Double> options = new HashMap<>();

        // Act
        Optional<Rental> modifiedRental = rentalService.modifyRental(UUID.randomUUID(), startDate, endDate, options);
        
        // Assert
        assertFalse(modifiedRental.isPresent());
    }

    /**
     * Tests modifying a rental that is not in a modifiable status (e.g., ACTIVE).
     */
    @Test
    void testModifyRentalInvalidStatus() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(INITIAL_RENTAL_DAYS);
        Optional<Rental> initialRental = rentalService.bookCar(user1.getId(), car1.getId(), startDate, endDate, Collections.emptyMap());

        assertTrue(initialRental.isPresent());
        Rental rentalToModify = initialRental.get();
        rentalToModify.setStatus(Rental.RentalStatus.ACTIVE);
        rentalRepository.save(rentalToModify);

        LocalDateTime newStartDate = startDate.plusDays(1);
        LocalDateTime newEndDate = endDate.plusDays(2);

        // Act
        Optional<Rental> modifiedRental = rentalService.modifyRental(rentalToModify.getId(), newStartDate, newEndDate, Collections.emptyMap());

        // Assert
        assertFalse(modifiedRental.isPresent());
    }

    /**
     * Tests successful cancellation of an existing rental.
     */
    @Test
    void testCancelRentalSuccess() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(INITIAL_RENTAL_DAYS);
        Optional<Rental> rental = rentalService.bookCar(user1.getId(), car1.getId(), startDate, endDate, Collections.emptyMap());

        assertTrue(rental.isPresent());

        // Act
        boolean isCancelled = rentalService.cancelRental(rental.get().getId());

        // Assert
        assertTrue(isCancelled);
        Optional<Rental> cancelledRental = rentalService.getRentalById(rental.get().getId());
        assertTrue(cancelledRental.isPresent());
        assertEquals(Rental.RentalStatus.CANCELLED, cancelledRental.get().getStatus());
        assertTrue(carRepository.findById(car1.getId()).get().isAvailable());
    }

    /**
     * Tests cancellation of a rental that does not exist.
     */
    @Test
    void testCancelRentalNotFound() {
        // Arrange

        // Act & Assert
        assertFalse(rentalService.cancelRental(UUID.randomUUID()));
    }
} 