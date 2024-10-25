import entities.Driver;
import entities.Item;
import entities.Order;
import entities.User;
import factory.DriverFactory;
import factory.ItemFactory;
import factory.UserFactory;
import services.DeliveryService;
import services.DriverService;
import services.UserService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        UserService userService = new UserService();
        DriverService driverService = new DriverService();

        DeliveryService deliveryService = new DeliveryService(userService, driverService);

        User user = UserFactory.createUser("John Doe", "1234567890");
        Driver driver = DriverFactory.createDriver("Driver John", "1234567890");
        Item item = ItemFactory.createItem("Cake Item", 1);

        userService.registerUser(user);
        driverService.registerDriver(driver);
        deliveryService.registerItem(item);

        Order order = deliveryService.createOrder(user.getUserId(), item.getItemId());
        System.out.println("Order created: " + order.getOrderId());

        Thread.sleep(1000);

        System.out.println("Order status: " + order.getStatus());

        Thread.sleep(1000);

        deliveryService.pickupOrder(driver.getDriverId(), order.getOrderId());
        System.out.println("Order picked up: " + order.getOrderId());

        Thread.sleep(1000);

        try {
            deliveryService.cancelOrder(order.getOrderId());
            System.out.println("Order cancelled: " + order.getOrderId());
        } catch (Exception e) {
            System.out.println("Order cannot be cancelled: " + order.getOrderId() + " " +e.getLocalizedMessage());
        }

        Thread.sleep(1000);

        deliveryService.markOrderDelivered(driver.getDriverId(), order.getOrderId());
        System.out.println("Order delivered: " + order.getOrderId());


        System.out.println("------ All processing completed ------");
    }
}