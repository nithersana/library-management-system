package com.library.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete domain class representing a System Administrator.
 */
public class Admin extends User {
    private final Map<String, User> managedUsers;

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.managedUsers = new HashMap<>();
    }

    /**
     * Provisions a new user account (Member or Librarian).
     */
    public void createAccount(User user) {
        if (user != null) {
            managedUsers.put(user.getUserId(), user);
            System.out.println("[ADMIN] Created account for " + user.getName() + " [Role: " + user.getRole() + ", ID: " + user.getUserId() + "]");
        }
    }

    /**
     * Deletes a user account by user ID.
     */
    public boolean deleteAccount(String targetUserId) {
        if (managedUsers.containsKey(targetUserId)) {
            User removed = managedUsers.remove(targetUserId);
            System.out.println("[ADMIN] Successfully deleted account for user: " + removed.getName());
            return true;
        }
        System.err.println("[ADMIN ERROR] Account ID not found: " + targetUserId);
        return false;
    }

    public User getUser(String userId) {
        return managedUsers.get(userId);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
