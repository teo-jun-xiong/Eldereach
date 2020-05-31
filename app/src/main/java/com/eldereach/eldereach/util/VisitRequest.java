package com.eldereach.eldereach.util;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class VisitRequest {
    private String email;
    private String dateTime;
    private String dateRequest;
    private String service;
    private String special;
    private String name;
    private String address;
    private long status;
    private String serviceProviderName;
    private String serviceProviderPhone;

    public VisitRequest(DocumentSnapshot documentSnapshot) {
        Map<String, Object> map = documentSnapshot.getData();

        assert map != null;
        email = (String) map.get("email");
        dateTime = (String) map.get("dateTime");
        dateRequest = (String) map.get("dateRequest");
        service = (String) map.get("service");
        special = (String) map.get("special");
        status = (long) map.get("status");
        name = (String) map.get("name");
        address = (String) map.get("address");
        serviceProviderName = (String) map.get("serviceProviderName");
        serviceProviderPhone = (String) map.get("serviceProviderPhone");
    }

    public String getSpecial() {
        return special;
    }

    public String getStatus() {
        if (status == 0) {
            return "Pending";
        } else if (status == 1) {
            return "Accepted";
        } else {
            return "Completed";
        }
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

    public String getName() {
        return name;
    }

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
}
