package com.recipeit.recipeit;

/**
 * Created by Elena on 13/03/2018.
 */

public class User {
    private boolean allergique;
    public String username;
    public String email;
    public String createdAt;
    public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String createdAt, String password) {
        this.allergique = false;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.password = password;
    }

}
