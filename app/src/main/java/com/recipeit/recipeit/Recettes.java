package com.recipeit.recipeit;

/**
 * Created by felii on 29/03/2018.
 */

public class Recettes {
    public boolean accepted;
    public String createdAt;
    public int difficulty;
    public String slug;
    public String userId;
    public String title;
    public String thumbnail;

    public Recettes(){}
    public Recettes(String createdAt, int difficulty, String slug, String title, String userId, String thumbnail){
        this.accepted = true;
        this.createdAt = createdAt;
        this.difficulty = difficulty;
        this.slug = slug;
        this.title = title;
        this.userId = userId;
        this.thumbnail = thumbnail;
    }
}
