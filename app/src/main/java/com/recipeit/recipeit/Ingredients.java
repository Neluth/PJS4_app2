package com.recipeit.recipeit;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by felii on 29/03/2018.
 */

public class Ingredients {

    public Ingredients(){}

    @Exclude
    public Map<String, Object> toMap(int index, String quantity) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ing",index);
        result.put("quantity",quantity);
        return result;
    }

}
