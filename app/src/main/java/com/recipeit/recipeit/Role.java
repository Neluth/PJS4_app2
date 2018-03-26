package com.recipeit.recipeit;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elena on 13/03/2018.
 */

public class Role {

    public Role(){
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("0","ROLE_USER");
        return result;
    }
}