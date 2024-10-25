package entities;

import enums.OrderStatus;

public class Order {
    private final String orderId;
    private final String userId;
    private final String itemId;
    private volatile String assignedDriverId;
    private volatile OrderStatus status;
    private final long createdTime;

    public Order(String orderId, String userId, String itemId) {
        this.orderId = orderId;
        this.userId = userId;
        this.itemId = itemId;
        this.createdTime = System.currentTimeMillis();
        this.status = OrderStatus.CREATED;
    }

    public synchronized void assignDriver(String driverId) {
        this.assignedDriverId = driverId;
        this.status = OrderStatus.ASSIGNED;
    }

    public synchronized void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public synchronized String getAssignedDriverId() {
        return assignedDriverId;
    }

    public synchronized OrderStatus getStatus() {
        return status;
    }
}
