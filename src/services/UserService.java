package services;

import entities.User;

import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public void registerUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public void updateUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void deleteUser(String userId) {
        users.remove(userId);
    }

    public ConcurrentHashMap<String, User> getUsers() {
        return users;
    }

    public int getUserCount() {
        return users.size();
    }

    public void validateUser(String userId) {
        User user = users.get(userId);
        if(user == null) {
            throw new IllegalArgumentException("User does not exist");
        }
    }

}
