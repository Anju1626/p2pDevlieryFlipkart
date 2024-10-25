package factory;

import entities.Item;

import java.util.UUID;

public class ItemFactory {
    public static Item createItem(String name, int qty) {
        String itemId = generateItemId();
        return new Item(itemId, name, qty);
    }

    private static String generateItemId() {
        return "Item-" + UUID.randomUUID();
    }
}
