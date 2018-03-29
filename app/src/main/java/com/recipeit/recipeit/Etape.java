package com.recipeit.recipeit;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by felii on 29/03/2018.
 */

public class Etape {

    public Etape(){
    }

    @Exclude
    public Map<String, Object> toMap(List<String> steps) {
        HashMap<String, Object> result = new HashMap<>();
        for (int i=0; i<steps.size();i++){
            result.put(Integer.toString(i),steps.get(i).toString());
        }
        return result;
    }
}
