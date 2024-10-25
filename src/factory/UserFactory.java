package factory;

import entities.User;

import java.util.UUID;

public class UserFactory {
    public static User createUser(String name, String contact) {
        String userId = generateUserId();
        return new User(userId, name, contact);
    }

    private static String generateUserId() {
        return "User-" + UUID.randomUUID();
    }
}
