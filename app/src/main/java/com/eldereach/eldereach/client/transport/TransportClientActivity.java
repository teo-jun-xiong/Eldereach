package com.eldereach.eldereach.client.transport;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.client.HomeClientActivity;
import com.eldereach.eldereach.util.EldereachDateTime;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.widget.CompoundButton.INVISIBLE;
import static android.widget.CompoundButton.OnCheckedChangeListener;
import static android.widget.CompoundButton.VISIBLE;
import static com.eldereach.eldereach.util.EldereachDateTime.simpleDateFormat;

public class TransportClientActivity extends FragmentActivity implements OnMapReadyCallback {
    Spinner dropdown;
    Button buttonHelp;
    Button buttonDateTimeHome;
    Button buttonDateTimeDest;
    Button buttonSubmitRequest;
    EditText textOthers;
    TextView textOthersPrompt;
    TextView textDestPickup;
    TextView textDestPrompt;
    CheckBox checkboxReturn;
    SearchView search;

    GoogleMap map;
    SupportMapFragment mapFragment;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    String locationDest = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_transport);

        initialiseComponents();
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // On launch, centers on NUH
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.294088, 103.783049), 17));
    }

    private void initialiseComponents() {
        dropdown = findViewById(R.id.dropdownTransportClient);
        buttonHelp = findViewById(R.id.buttonHelpTransportClient);
        buttonDateTimeHome = findViewById(R.id.buttonDateTimeHomeTransportClient);
        buttonDateTimeDest = findViewById(R.id.buttonDateTimeDestPickerTransportClient);
        buttonSubmitRequest = findViewById(R.id.buttonSubmitRequestTransportClient);
        textOthers = findViewById(R.id.textOthersTransportClient);
        textOthersPrompt = findViewById(R.id.textOthersPromptTransportClient);
        textDestPickup = findViewById(R.id.textDestPickupTransportClient);
        textDestPrompt = findViewById(R.id.textDestPromptTransportClient);
        checkboxReturn = findViewById(R.id.checkboxReturnTransportClient);
        search = findViewById(R.id.searchTransportClient);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTransportClient);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        buttonDateTimeDest.setVisibility(INVISIBLE);
        textDestPickup.setVisibility(INVISIBLE);
        textDestPrompt.setVisibility(INVISIBLE);
        textOthersPrompt.setVisibility(View.INVISIBLE);
        textOthers.setVisibility(View.INVISIBLE);

        buttonDateTimeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(buttonDateTimeHome);
            }
        });

        buttonDateTimeDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(buttonDateTimeDest);
            }
        });

        buttonSubmitRequest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> transportRequest = new HashMap<>();

                String email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
                String currentDate = EldereachDateTime.getCurrentDate();
                transportRequest.put("email", email);
                transportRequest.put("returnNeeded", checkboxReturn.isChecked());
                transportRequest.put("dateRequest", currentDate);
                transportRequest.put("status", 0);

                // If the dropdown item selected is others, it must not be empty
                if (dropdown.getSelectedItem().toString().equals("Others")) {
                    if (textOthers.getText().toString().equals("")) {
                        Toast.makeText(
                                TransportClientActivity.this,
                                "Please do not leave the 'Others' purpose empty.",
                                Toast.LENGTH_SHORT).show();
                        textOthers.requestFocus();
                        return;
                    } else {
                        transportRequest.put("purpose", textOthers.getText().toString());
                    }
                } else {
                    transportRequest.put("purpose", dropdown.getSelectedItem().toString());
                }

                String dateTimeHome = buttonDateTimeHome.getText().toString();
                String dateTimeDest;

                if (EldereachDateTime.isDateTime(dateTimeHome)) {
                    if (EldereachDateTime.isDateAfterCurrentDate(dateTimeHome)) {
                        transportRequest.put("dateTimeHome", dateTimeHome);
                    } else {
                        Toast.makeText(
                                TransportClientActivity.this,
                                "The date and time of the pickup from home is earlier than the current date.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    Toast.makeText(
                            TransportClientActivity.this,
                            "Please enter a valid date and time for the pickup from home.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // If a return trip is required
                if (checkboxReturn.isChecked()) {
                    dateTimeDest = buttonDateTimeDest.getText().toString();

                    if (EldereachDateTime.isDateTime(dateTimeDest)) {
                        if (EldereachDateTime.isDateBefore(dateTimeHome, dateTimeDest)) {
                            transportRequest.put("dateTimeDest", dateTimeDest);
                        } else {
                            Toast.makeText(
                                    TransportClientActivity.this,
                                    "The date and time of the return trip is before the pickup from home.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(
                                TransportClientActivity.this,
                                "Please enter a valid date and time for the pickup from the destination.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    dateTimeDest = "";
                    transportRequest.put("dateTimeDest", dateTimeDest);
                }

                if (locationDest != null) {
                    transportRequest.put("location", locationDest);
                } else {
                    Toast.makeText(
                            TransportClientActivity.this,
                            "Please select the destination in the search bar.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Final array to store user information to be included in the database
                final String[] userInfo = {"", ""};
                db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> map = documentSnapshot.getData();
                        userInfo[0] = (String) map.get("name");
                        userInfo[1] = (String) map.get("address");
                        transportRequest.put("name", userInfo[0]);
                        transportRequest.put("address", userInfo[1]);
                    }
                });

                String documentName = "T_" + email + "_" + dateTimeHome + "_" + dateTimeDest + "_" + currentDate;
                transportRequest.put("id", documentName);
                transportRequest.put("serviceProviderName", "");
                transportRequest.put("serviceProviderPhone", "");
                transportRequest.put("serviceProviderEmail", "");
                final DocumentReference docRef = db.collection("transportRequests").document(documentName);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(
                                        TransportClientActivity.this,
                                        "This transport request already exists. Please check under 'My Requests'.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                docRef.set(transportRequest);
                                Toast.makeText(
                                        TransportClientActivity.this,
                                        "Transport request submitted.",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TransportClientActivity.this, HomeClientActivity.class));
                            }
                        }
                    }
                });
            }
        });

        // if a return trip is required, make the details visible
        checkboxReturn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    textDestPickup.setVisibility(VISIBLE);
                    textDestPrompt.setVisibility(VISIBLE);
                    buttonDateTimeDest.setVisibility(VISIBLE);
                } else {
                    textDestPickup.setVisibility(INVISIBLE);
                    textDestPrompt.setVisibility(INVISIBLE);
                    buttonDateTimeDest.setVisibility(INVISIBLE);
                }
            }
        });

        // Options for dropdown selection
        String[] dropdownOptions = new String[]{
                "Medical appointment at hospital",
                "Community centre event",
                "Charity organisation event",
                "Others"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dropdownOptions);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();

                // If the "Others" option is selected, make the details visible
                if (!selectedItem.equals("Others")) {
                    textOthersPrompt.setVisibility(View.INVISIBLE);
                    textOthers.setVisibility(View.INVISIBLE);
                } else {
                    textOthersPrompt.setVisibility(View.VISIBLE);
                    textOthers.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Searches for the location based on the user search query
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                map.clear();

                String location = search.getQuery().toString();
                List<Address> addressList = new ArrayList<>();

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(TransportClientActivity.this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // If there are no results, inform the user
                    assert addressList != null;
                    if (addressList.size() == 0) {
                        Toast.makeText(
                                TransportClientActivity.this,
                                "Please narrow down the search terms.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        locationDest = address.getAddressLine(0);
                        map.addMarker(new MarkerOptions().position(latLng).title(location));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);
    }

    private void showDateTimeDialog(final Button dateTimePicker) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        dateTimePicker.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(
                        TransportClientActivity.this,
                        timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(
                TransportClientActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
