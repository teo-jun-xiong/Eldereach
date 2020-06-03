package com.eldereach.eldereach.util;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class TransportRequest implements EldereachRequest {
    private String id;
    private String email;
    private String dest;
    private String purpose;
    private String dateTimeHome;
    private String dateTimeDest;
    private String dateRequest;
    private String address;
    private String name;
    private String serviceProviderName;
    private String serviceProviderPhone;
    private String serviceProviderEmail;
    private long status;
    private boolean returnNeeded;

    public TransportRequest(DocumentSnapshot documentSnapshot) {
        Map<String, Object> map = documentSnapshot.getData();

        assert map != null;
        id = (String) map.get("id");
        email = (String) map.get("email");
        dest = (String) map.get("location");
        purpose = (String) map.get("purpose");
        returnNeeded = (boolean) map.get("returnNeeded");
        status = (long) map.get("status");
        dateTimeHome = (String) map.get("dateTimeHome");
        dateTimeDest = (String) map.get("dateTimeDest");
        dateRequest = (String) map.get("dateRequest");
        name = (String) map.get("name");
        address = (String) map.get("address");
        serviceProviderName = (String) map.get("serviceProviderName");
        serviceProviderPhone = (String) map.get("serviceProviderPhone");
        serviceProviderEmail = (String) map.get("serivceProviderEmail");
    }

    // Used to sort
    public String getDateTime() {
        return dateTimeHome;
    }

    public String getDestDate() {
        return dateTimeDest;
    }

    @Override
    public String getDateRequest() {
        return dateRequest;
    }

    public String getHomeAddress() {
        return address;
    }

    public String getDestAddress() {
        return dest;
    }

    public boolean getReturnNeeded() {
        return returnNeeded;
    }

    @NonNull
    @Override
    public String toString() {
        return email + " " + dateTimeHome + " " + dest + dateTimeDest;
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

    @Override
    public String getName() {
        return name;
    }

    public String getPurpose() {
        return purpose;
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

    public String getId() {
        return id;
    }

    @Override
    public String getServiceProviderEmail() {
        return serviceProviderEmail;
    }

    @Override
    public String getRequestType() {
        return "Transport";
    }
}
