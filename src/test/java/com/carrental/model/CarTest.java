package com.carrental.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Car} model class.
 */
class CarTest {

    private static final String BRAND_TOYOTA = "Toyota";
    private static final String MODEL_CAMRY = "Camry";
    private static final int YEAR_2023 = 2023;
    private static final double DAILY_RATE_50 = 50.0;
    private static final double DAILY_RATE_120 = 120.0;
    private static final String BRAND_HONDA = "Honda";
    private static final String MODEL_CIVIC = "Civic";
    private static final double DAILY_RATE_45 = 45.0;
    private static final String BRAND_BMW = "BMW";
    private static final String MODEL_X5 = "X5";
    private static final double DAILY_RATE_100 = 100.0;

    /**
     * Tests the successful creation of a Car object.
     */
    @Test
    void testCarCreation() {
        // Arrange

        // Act
        Car car = new Car(BRAND_TOYOTA, MODEL_CAMRY, YEAR_2023, DAILY_RATE_50);
        
        // Assert
        assertNotNull(car.getId());
        assertEquals(BRAND_TOYOTA, car.getBrand());
        assertEquals(MODEL_CAMRY, car.getModel());
        assertEquals(YEAR_2023, car.getYear());
        assertEquals(DAILY_RATE_50, car.getDailyRate());
        assertTrue(car.isAvailable());
    }

    /**
     * Tests the availability status of a Car object.
     */
    @Test
    void testCarAvailability() {
        // Arrange
        Car car = new Car(BRAND_HONDA, MODEL_CIVIC, YEAR_2023, DAILY_RATE_45);
        
        // Act & Assert (initial state)
        assertTrue(car.isAvailable());

        // Act (change state)
        car.setAvailable(false);

        // Assert (changed state)
        assertFalse(car.isAvailable());
    }

    /**
     * Tests the update of a Car's daily rental rate.
     */
    @Test
    void testCarRateUpdate() {
        // Arrange
        Car car = new Car(BRAND_BMW, MODEL_X5, YEAR_2023, DAILY_RATE_100);
        
        // Act & Assert (initial state)
        assertEquals(DAILY_RATE_100, car.getDailyRate());
        
        // Act (change state)
        car.setDailyRate(DAILY_RATE_120);
        
        // Assert (changed state)
        assertEquals(DAILY_RATE_120, car.getDailyRate());
    }
} 