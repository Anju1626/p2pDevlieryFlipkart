package entities;

public class User {
    private final String userId;
    private final String name;
    private final String contact;

    public User(String userId, String name, String contact) {
        this.userId = userId;
        this.name = name;
        this.contact = contact;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}
