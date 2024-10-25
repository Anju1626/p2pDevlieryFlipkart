package factory;

import entities.Driver;

import java.util.UUID;

public class DriverFactory {

    public static Driver createDriver(String name, String contact) {
        String driverId = generateDriverId();
        return new Driver(driverId, name, contact, true);
    }

    private static String generateDriverId() {
        return "Driver-" + UUID.randomUUID();
    }
}
