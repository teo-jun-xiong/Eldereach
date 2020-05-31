package com.eldereach.eldereach.client.visit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.client.HomeClientActivity;
import com.eldereach.eldereach.util.EldereachDateTime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.widget.AdapterView.OnItemSelectedListener;
import static com.eldereach.eldereach.util.EldereachDateTime.isDateAfterCurrentDate;
import static com.eldereach.eldereach.util.EldereachDateTime.isDateTime;
import static com.eldereach.eldereach.util.EldereachDateTime.simpleDateFormat;

public class VisitClientActivity extends FragmentActivity {
    Button buttonSubmit;
    Spinner dropdownService;
    EditText textSpecial;
    EditText textOther;
    TextView textOtherPrompt;
    Button buttonDateTime;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_visit);

        initialiseComponents();
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    private void initialiseComponents() {
        buttonSubmit = findViewById(R.id.buttonSubmitRequestVisitClient);
        buttonDateTime = findViewById(R.id.buttonDateTimeVisitClient);
        dropdownService = findViewById(R.id.dropdownServiceVisitClient);
        textSpecial = findViewById(R.id.textSpecialVisitClient);
        textOther = findViewById(R.id.textOtherVisitClient);
        textOtherPrompt = findViewById(R.id.textOthersPromptVisitClient);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textOtherPrompt.setVisibility(View.INVISIBLE);
        textOther.setVisibility(View.INVISIBLE);

        buttonDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(buttonDateTime);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> visitRequest = new HashMap<>();

                visitRequest.put("email", firebaseAuth.getCurrentUser().getEmail());
                visitRequest.put("service", dropdownService.getSelectedItem());
                visitRequest.put("special", textSpecial.getText().toString());

                String dateTime = buttonDateTime.getText().toString();
                if (isDateTime(dateTime) && isDateAfterCurrentDate(dateTime)) {
                    visitRequest.put("dateTime", dateTime);
                } else {
                    Toast.makeText(VisitClientActivity.this, "The date and time of the visit is earlier than the current date.",Toast.LENGTH_SHORT).show();
                    return;
                }

                String currentDate = EldereachDateTime.getCurrentDate();
                visitRequest.put("dateRequest", currentDate);
                visitRequest.put("status", 0);

                final String[] userInfo = {""};
                db.collection("users").document(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> map = documentSnapshot.getData();
                        userInfo[0] = (String) map.get("name");
                        userInfo[1] = (String) map.get("address");
                        visitRequest.put("name", userInfo[0]);
                        visitRequest.put("address", userInfo[1]);
                    }
                });

                // Visit - Email - Visit date - Request made date time
                String documentName = "V_" + firebaseAuth.getCurrentUser().getEmail() + "_" + dateTime + "_" + currentDate;
                final DocumentReference docRef = db.collection("visitRequests").document(documentName);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(VisitClientActivity.this, "This visit request already exists. Please check under 'My Requests'.", Toast.LENGTH_SHORT).show();
                            } else {
                                docRef.set(visitRequest);
                                Toast.makeText(VisitClientActivity.this, "Visit request submitted.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(VisitClientActivity.this, HomeClientActivity.class));
                            }
                        }
                    }
                });
            }
        });

        String[] dropdownOptions = new String[]{
                "In-person counselling",
                "Service cleaning",
                "Medical home-care",
                "Others"
        };

        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dropdownOptions);
        dropdownService.setAdapter(serviceAdapter);

        dropdownService.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();

                // If the "Others" option is selected, make the details visible
                if (!selectedItem.equals("Others")) {
                    textOtherPrompt.setVisibility(View.INVISIBLE);
                    textOther.setVisibility(View.INVISIBLE);
                } else {
                    textOtherPrompt.setVisibility(View.VISIBLE);
                    textOther.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void showDateTimeDialog(final Button buttonDateTime) {
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
                        buttonDateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(VisitClientActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(VisitClientActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
