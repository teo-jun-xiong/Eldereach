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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static android.widget.CompoundButton.*;

public class TransportClientActivity extends FragmentActivity implements OnMapReadyCallback {
    Button btnHelp;
    EditText purpose;
    TextView others;
    Spinner dropdown;
    TextView destinationPrompt;
    TextView returnPrompt;
    EditText returnDate;
    EditText returnTime;
    CheckBox returnRequired;
    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_transport);

        initialiseComponents();
    }

    private void initialiseComponents() {
        btnHelp = findViewById(R.id.btnTransportHelp);
        purpose = findViewById(R.id.purposeClientTransport);
        others = findViewById(R.id.othersClientTransport);
        dropdown = findViewById(R.id.dropdownClient);
        returnRequired = findViewById(R.id.checkboxReturn);
        destinationPrompt = findViewById(R.id.destinationPrompt);
        returnPrompt = findViewById(R.id.dateTimeTextBack);
        returnDate = findViewById(R.id.dateClientTransportBack);
        returnTime = findViewById(R.id.timeClientTransportBack);

        destinationPrompt.setVisibility(INVISIBLE);
        returnPrompt.setVisibility(INVISIBLE);
        returnDate.setVisibility(INVISIBLE);
        returnTime.setVisibility(INVISIBLE);

        others.setVisibility(View.INVISIBLE);
        purpose.setVisibility(View.INVISIBLE);

        returnRequired.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    destinationPrompt.setVisibility(VISIBLE);
                    returnPrompt.setVisibility(VISIBLE);
                    returnDate.setVisibility(VISIBLE);
                    returnTime.setVisibility(VISIBLE);
                } else {
                    destinationPrompt.setVisibility(INVISIBLE);
                    returnPrompt.setVisibility(INVISIBLE);
                    returnDate.setVisibility(INVISIBLE);
                    returnTime.setVisibility(INVISIBLE);
                }
            }
        });

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

                if (!selectedItem.equals("Others")) {
                    others.setVisibility(View.INVISIBLE);
                    purpose.setVisibility(View.INVISIBLE);
                } else {
                    others.setVisibility(View.VISIBLE);
                    purpose.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        search = findViewById(R.id.searchClientTransport);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapClientTransport);

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
}
