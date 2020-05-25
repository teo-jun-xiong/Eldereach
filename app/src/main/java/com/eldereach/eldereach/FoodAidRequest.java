package com.eldereach.eldereach;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FoodAidRequest {
    private String email;
    private String dateTime;
    private String dietary;
    private String allergies;
    private boolean isAccepted;
    private ArrayList<String> meals;

    @SuppressWarnings("unchecked")
    FoodAidRequest(DocumentSnapshot documentSnapshot) {
        Map<String, Object> map = documentSnapshot.getData();

        assert map != null;
        email = (String) map.get("email");
        dateTime = (String) map.get("dateTimeHome");
        dietary = (String) map.get("dietary");
        allergies = (String) map.get("allergy");
        isAccepted = (boolean) map.get("isAccepted");
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

    public boolean isAccepted() {
        return isAccepted;
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
