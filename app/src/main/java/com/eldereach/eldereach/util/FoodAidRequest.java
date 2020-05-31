package com.eldereach.eldereach.util;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FoodAidRequest {
    private String email;
    private String dateTime;
    private String dietary;
    private String allergies;
    private long status;
    private ArrayList<String> meals;

    @SuppressWarnings("unchecked")
    public FoodAidRequest(DocumentSnapshot documentSnapshot) {
        Map<String, Object> map = documentSnapshot.getData();

        assert map != null;
        email = (String) map.get("email");
        dateTime = (String) map.get("dateTime");
        dietary = (String) map.get("dietary");
        allergies = (String) map.get("allergy");
        status = (long) map.get("status");
        meals = (ArrayList<String>) map.get("meals");
    }

    public String getEmail() {
        return email;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDietary() {
        return dietary;
    }

    public String getAllergies() {
        return allergies;
    }

    public long getStatus() {
        return status;
    }

    public String getMeals() {
        String mealString = "";

        for (int i = 0; i < meals.size() - 1; i++) {
            mealString += meals.get(i) + ", ";
        }

        mealString += meals.get(meals.size() - 1);
        return mealString;
    }
}
