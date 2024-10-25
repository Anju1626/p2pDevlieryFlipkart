package services;

import entities.Driver;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DriverService {
    private final ConcurrentHashMap<String, Driver> drivers = new ConcurrentHashMap<>();

    public void registerDriver(Driver driver) {
        drivers.put(driver.getDriverId(), driver);
    }

    public Driver validateDriver(String driverId) {
        Driver driver = drivers.get(driverId);
        if(driver == null) {
            throw new IllegalArgumentException("Driver does not exist");
        }
        return driver;
    }

    public boolean isDriverAvailable(String driverId) {
        Driver driver = validateDriver(driverId);
        return driver.isAvailable();
    }

    public void setDriverAvailable(String driverId, boolean available) {
        Driver driver = validateDriver(driverId);
        driver.setAvailable(available);
    }

    public Driver createDriver(String name, String contact) {
        String driverId = generateDriverId();
        return new Driver(driverId, name, contact, true);
    }

    private String generateDriverId() {
        return String.valueOf(System.currentTimeMillis());
    }

    public Driver getDriver(String driverId) {
        return validateDriver(driverId);
    }

    public ConcurrentHashMap<String, Driver> getDrivers() {
        return drivers;
    }

    public Optional<Driver> findAvailableDriver() {
        return drivers.values().stream().filter(Driver::isAvailable).findFirst();
    }
}
