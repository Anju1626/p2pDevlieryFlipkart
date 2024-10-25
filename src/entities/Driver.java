package entities;

public class Driver {
    private final String driverId;
    private final String name;
    private final String contact;
    private volatile boolean available;

    public Driver(String driverId, String name, String contact, boolean available) {
        this.driverId = driverId;
        this.name = name;
        this.contact = contact;
        this.available = available;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public synchronized boolean isAvailable() {
        return available;
    }

    public synchronized void setAvailable(boolean available) {
        this.available = available;
    }
}
