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


    public Recettes(int difficulty, String slug, String title, String userId, String thumbnail, String origin,
                    String type, String history){
        this.accepted = true;
        this.difficulty = difficulty;
        this.slug = slug;
        this.title = title;
        this.userid = userId;
        this.thumbnail = thumbnail;
        this.origin = origin;
        this.type = type;
        this.history = history;
    }
}
