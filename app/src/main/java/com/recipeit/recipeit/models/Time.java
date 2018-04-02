package com.recipeit.recipeit.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by felii on 29/03/2018.
 */

public class Time {

    public String hour;
    public String minute;

    public Time(){
    }

    @Exclude
    public Map<String, Object> toMap(String hour, String minute) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("hour",hour);
        result.put("minute",minute);
        return result;
    }
}
