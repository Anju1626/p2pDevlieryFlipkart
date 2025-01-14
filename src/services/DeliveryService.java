package services;

import entities.Driver;
import entities.Item;
import entities.Order;
import enums.OrderStatus;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;

public class DeliveryService {
    private final UserService userService;
    private final DriverService driverService;
    private final ConcurrentHashMap<String, Order> orders = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Item> items = new ConcurrentHashMap<>();
    private final BlockingDeque<Order> pendingOrders = new java.util.concurrent.LinkedBlockingDeque<>();
    private final Object assignmentLock = new Object();

    public DeliveryService(UserService userService, DriverService driverService) {
        this.userService = userService;
        this.driverService = driverService;
        assignmentThread.start();
    }

    private final Thread assignmentThread = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Checking for pending orders...");
            try {
                Order order = pendingOrders.take();
                if(order.getStatus() != OrderStatus.CANCELLED) {
                    System.out.println("Unassigned order found: " + order.getOrderId());
                    assignDriverToOrder(order);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    });

    private void assignDriverToOrder(Order order) {
        synchronized (assignmentLock) {
            Optional<Driver> availableDriver = driverService.findAvailableDriver();
            if (availableDriver.isPresent()) {
                System.out.println("Assigning driver: " + availableDriver.get().getName() + " to order: " + order.getOrderId());
                Driver driver = availableDriver.get();
                driverService.setDriverAvailable(driver.getDriverId(), false);
                order.assignDriver(driver.getDriverId());
            } else {
                pendingOrders.offer(order);
            }
        }
    }

    public void registerItem(Item item) {
        items.put(item.getItemId(), item);
    }

    public Order createOrder(String userId, String itemId) {
        validateItem(itemId);
        userService.validateUser(userId);

        String orderId = generateOrderId();
        Order order = new Order(orderId, userId, itemId);
        orders.put(orderId, order);
        pendingOrders.offer(order);
        return order;
    }

    public void cancelOrder(String orderId) {
        Order order =validateOrder(orderId);

        synchronized (order) {
            if(order.getStatus() == OrderStatus.PICKED_UP) {
                throw new IllegalStateException("Order cannot be cancelled as it has already picked up");
            }

            order.updateStatus(OrderStatus.CANCELLED);

            String driverId = order.getAssignedDriverId();
            if(driverId != null) {
                Driver driver = driverService.getDriver(driverId);
                driverService.setDriverAvailable(driver.getDriverId(), true);
            }
        }
    }

    public void pickupOrder(String driverId, String orderId) {
        Driver driver = driverService.getDriver(driverId);
        Order order = validateOrder(orderId);

        synchronized (order) {
            if(order.getStatus() != OrderStatus.ASSIGNED ||
            !order.getAssignedDriverId().equals(driverId)) {
                throw new IllegalStateException("Order is not assigned to this driver or cannot be picked up");
            }
            if(order.getStatus() == OrderStatus.CANCELLED) {
                throw new IllegalStateException("Order cannot be picked up as it has been cancelled");
            }
            order.updateStatus(OrderStatus.PICKED_UP);
        }
    }

    public void markOrderDelivered(String driverId, String orderId) {
        Order order = validateOrder(orderId);

        synchronized (order) {
            if(order.getStatus() != OrderStatus.PICKED_UP) {
                throw new IllegalStateException("Order cannot be marked as delivered as it has not been picked up");
            }
            order.updateStatus(OrderStatus.DELIVERED);
            Driver driver = driverService.getDriver(driverId);
            driverService.setDriverAvailable(driver.getDriverId(), true);
        }
    }

    public OrderStatus getOrderStatus(String orderId) {
        Order order = validateOrder(orderId);
        return order.getStatus();
    }
    private String generateOrderId() {
        return "Order-" + UUID.randomUUID();
    }

    private Order validateOrder(String orderId) {
        Order order = orders.get(orderId);
        if(order == null) {
            throw new IllegalArgumentException("Order does not exist");
        }
        return order;
    }
    private void validateItem(String itemId) {
        Item item = items.get(itemId);
        if(item == null) {
            throw new IllegalArgumentException("Item does not exist");
        }
    }
}
