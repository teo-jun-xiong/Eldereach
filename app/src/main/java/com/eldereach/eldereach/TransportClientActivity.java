package com.eldereach.eldereach;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static android.widget.CompoundButton.INVISIBLE;
import static android.widget.CompoundButton.OnCheckedChangeListener;
import static android.widget.CompoundButton.VISIBLE;

public class TransportClientActivity extends FragmentActivity implements OnMapReadyCallback {
    Button buttonHelp;
    EditText textOthers;
    TextView textOthersPrompt;
    Spinner dropdown;
    TextView textDestPickup;
    TextView textDestPrompt;
    EditText textDestDate;
    EditText textDestTime;
    CheckBox checkboxReturn;
    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView search;

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
        buttonHelp = findViewById(R.id.buttonHelpTransportClient);
        textOthers = findViewById(R.id.textOthersTransportClient);
        textOthersPrompt = findViewById(R.id.textOthersPromptTransportClient);
        dropdown = findViewById(R.id.dropdownTransportClient);
        checkboxReturn = findViewById(R.id.checkboxReturnTransportClient);
        textDestPickup = findViewById(R.id.textDestPickupTransportClient);
        textDestPrompt = findViewById(R.id.textDestPromptTransportClient);
        textDestDate = findViewById(R.id.textDestDateTransportClient);
        textDestTime = findViewById(R.id.textDestTimeTransportClient);
        search = findViewById(R.id.searchTransportClient);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTransportClient);

        textDestPickup.setVisibility(INVISIBLE);
        textDestPrompt.setVisibility(INVISIBLE);
        textDestDate.setVisibility(INVISIBLE);
        textDestTime.setVisibility(INVISIBLE);
        textOthersPrompt.setVisibility(View.INVISIBLE);
        textOthers.setVisibility(View.INVISIBLE);

        // if a return trip is required, make the details visible
        checkboxReturn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    textDestPickup.setVisibility(VISIBLE);
                    textDestPrompt.setVisibility(VISIBLE);
                    textDestDate.setVisibility(VISIBLE);
                    textDestTime.setVisibility(VISIBLE);
                } else {
                    textDestPickup.setVisibility(INVISIBLE);
                    textDestPrompt.setVisibility(INVISIBLE);
                    textDestDate.setVisibility(INVISIBLE);
                    textDestTime.setVisibility(INVISIBLE);
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
                List<Address> addressList = null;

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
                        Toast.makeText(TransportClientActivity.this, "Please narrow down the search terms.", Toast.LENGTH_SHORT).show();
                    } else {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
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
}
