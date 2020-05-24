package com.eldereach.eldereach;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class TransportRequest {
    private String email;
    private String location;
    private String purpose;
    private boolean returnNeeded;
    private boolean isAccepted;
    private String dateTimeHome;
    private String dateTimeDest;

    TransportRequest(DocumentSnapshot documentSnapshot) {
        Map<String, Object> map = documentSnapshot.getData();

        assert map != null;
        email = (String) map.get("email");
        location = (String) map.get("location");
        purpose = (String) map.get("purpose");
        returnNeeded = (boolean) map.get("returnNeeded");
        isAccepted = (boolean) map.get("isAccepted");
        dateTimeHome = (String) map.get("dateTimeHome");
        dateTimeDest = (String) map.get("dateTimeDest");
    }

    String getHomeDate() {
        return dateTimeHome;
    }

    String getDestDate() {
        return dateTimeDest;
    }

    String getLocation() {
        return location;
    }

    boolean getReturnNeeded() {
        return returnNeeded;
    }

    @NonNull
    @Override
    public String toString() {
        return email + " " + dateTimeHome + " " + location + dateTimeDest;
    }
}
