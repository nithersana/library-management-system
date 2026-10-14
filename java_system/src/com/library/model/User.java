package com.library.model;

/**
 * Abstract base class representing a generic user in the Library Management System.
 * Demonstrates proper encapsulation with strictly private fields.
 */
public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private boolean loggedIn;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.loggedIn = false;
    }

    /**
     * Authenticates the user credentials.
     */
    public boolean login(String enteredEmail, String enteredPassword) {
        if (this.email.equalsIgnoreCase(enteredEmail) && this.password.equals(enteredPassword)) {
            this.loggedIn = true;
            System.out.println("[AUTH] " + this.name + " (" + getRole() + ") logged in successfully.");
            return true;
        }
        System.err.println("[AUTH] Login failed for email: " + enteredEmail);
        return false;
    }

    /**
     * Logs the user out of the system.
     */
    public void logout() {
        this.loggedIn = false;
        System.out.println("[AUTH] " + this.name + " logged out.");
    }

    public abstract String getRole();

    // Accessors and Mutators
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %s, Name: %s, Email: %s]", getRole(), userId, name, email);
    }
}
