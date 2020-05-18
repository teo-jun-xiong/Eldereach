package com.eldereach.eldereach;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TransportClientActivity extends AppCompatActivity {
    Button btnHelp;
    EditText transportOtherPurpose;
    Spinner dropdown;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_client_transport);

        initialiseComponents();
    }

    private void initialiseComponents() {
        btnHelp = findViewById(R.id.btnTransportHelp);
        transportOtherPurpose = findViewById(R.id.transportClientOther);

        String[] dropdownOptions = new String[]{
                "Medical appointment at hospital",
                "Community centre event",
                "Charity organisation event",
                "Others"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_client_transport);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();

                Toast.makeText(adapterView.getContext(), selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }
}
