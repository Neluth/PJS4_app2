package com.recipeit.recipeit.models;

import java.util.Date;

/**
 * Created by Elena on 13/03/2018.
 */

public class User {
    private boolean allergique;
    public String username;
    public String email;
    public Long createdAt;
    public String createdAtString;
    public String password;
    public String avatar;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, Date createdAt, String password) {
        this.allergique = false;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt.getTime();
        this.password = password;
        this.avatar = "default";
    }

}
