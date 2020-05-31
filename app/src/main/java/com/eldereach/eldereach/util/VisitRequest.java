package com.eldereach.eldereach.util;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class VisitRequest {
    private String email;
    private String dateTime;
    private String service;
    private String special;
    private long status;

    public VisitRequest(DocumentSnapshot documentSnapshot) {
        Map<String, Object> map = documentSnapshot.getData();

        assert map != null;
        email = (String) map.get("email");
        dateTime = (String) map.get("dateTime");
        service = (String) map.get("service");
        special = (String) map.get("special");
        status = (long) map.get("status");
    }

    public String getSpecial() {
        return special;
    }

    public long getStatus() {
        return status;
    }

    public String getService() {
        return service;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getEmail() {
        return email;
    }
}
