package com.recipeit.recipeit.models;

import com.google.firebase.database.Exclude;

/**
 * Created by leach on 03/04/2018.
 */

public class Comment {
    public String comment;
    public String rating;
    public boolean accepted;
    public User user;

    public Comment(){}
}
