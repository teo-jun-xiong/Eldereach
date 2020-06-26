package com.eldereach.eldereach.util;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FoodAidRequest implements EldereachRequest{
    private String id;
    private String email;
    private String dateTime;
    private String dateRequest;
    private String dietary;
    private String allergies;
    private String address;
    private String name;
    private String serviceProviderName;
    private String serviceProviderPhone;
    private String serviceProviderEmail;
    private long status;
    private ArrayList<String> meals;

    @SuppressWarnings("unchecked")
    public FoodAidRequest(DocumentSnapshot documentSnapshot) {
        Map<String, Object> map = documentSnapshot.getData();

        assert map != null;
        id = (String) map.get("id");
        email = (String) map.get("email");
        dateTime = (String) map.get("dateTime");
        dietary = (String) map.get("dietary");
        allergies = (String) map.get("allergy");
        name = (String) map.get("name");
        dateRequest = (String) map.get("dateRequest");
        address = (String) map.get("address");
        serviceProviderName = (String) map.get("serviceProviderName");
        serviceProviderPhone = (String) map.get("serviceProviderPhone");
        serviceProviderEmail = (String) map.get("serivceProviderEmail");
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

    @Override
    public String getStatus() {
        if (status == 0) {
            return "Pending";
        } else if (status == 1) {
            return "Accepted";
        } else {
            return "Completed";
        }
    }

    public String getMeals() {
        String mealString = "";

        for (int i = 0; i < meals.size() - 1; i++) {
            mealString += meals.get(i) + ", ";
        }

        mealString += meals.get(meals.size() - 1);
        return mealString;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDateRequest() {
        return dateRequest;
    }

    public String getAddress() {
        return address;
    }

    public String getServiceProviderName() {
        if (serviceProviderName.equals("")) {
            return "-";
        }

        return serviceProviderName;
    }

    public String getServiceProviderPhone() {
        if (serviceProviderPhone.equals("")) {
            return "-";
        }

        return serviceProviderPhone;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getServiceProviderEmail() {
        return serviceProviderEmail;
    }

    @Override
    public String getRequestType() {
        return "Food aid";
    }
}
