package com.recipeit.recipeit.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felii on 29/03/2018.
 */

@IgnoreExtraProperties
public class Recettes {
    public boolean accepted;
    public long createdAt;
    public int difficulty;
    public String history;
    public String slug;
    public String userid;
    public String title;
    public String thumbnail;
    public List<String> steps;
    public List<Ingredients> ingredients;
    public String origin;
    public String type;
    public Time time;

    public Recettes(){}


    public Recettes(
            long createdAt,
            int difficulty,
            String slug,
            String title,
            String userId,
            String thumbnail){
        this.accepted = true;
        this.createdAt = createdAt;
        this.difficulty = difficulty;
        this.slug = slug;
        this.title = title;
        this.userid = userId;
        this.thumbnail = thumbnail;
    }
}
