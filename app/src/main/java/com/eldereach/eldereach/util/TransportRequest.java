package com.eldereach.eldereach.util;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class TransportRequest {
    private String email;
    private String location;
    private String purpose;
    private boolean returnNeeded;
    private long status;
    private String dateTimeHome;
    private String dateTimeDest;

    public TransportRequest(DocumentSnapshot documentSnapshot) {
        Map<String, Object> map = documentSnapshot.getData();

        assert map != null;
        email = (String) map.get("email");
        location = (String) map.get("location");
        purpose = (String) map.get("purpose");
        returnNeeded = (boolean) map.get("returnNeeded");
        status = (long) map.get("status");
        dateTimeHome = (String) map.get("dateTimeHome");
        dateTimeDest = (String) map.get("dateTimeDest");
    }

    // Used to sort
    public String getDateTime() {
        return dateTimeHome;
    }

    public String getDestDate() {
        return dateTimeDest;
    }

    public String getLocation() {
        return location;
    }

    public boolean getReturnNeeded() {
        return returnNeeded;
    }

    @NonNull
    @Override
    public String toString() {
        return email + " " + dateTimeHome + " " + location + dateTimeDest;
    }

    public long getStatus() {
        return status;
    }
}
