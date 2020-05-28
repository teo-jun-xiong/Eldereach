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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.widget.AdapterView.*;

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

                visitRequest.put("isAccepted", false);

                // Visit - Email - Visit date - Request made date time
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy hh:mm", Locale.ENGLISH);
                String documentName = "V_" + firebaseAuth.getCurrentUser().getEmail() + "_" + dateTime + "_" + simpleDateFormat.format(new Date());
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
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy hh:mm", Locale.ENGLISH);
                        buttonDateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(VisitClientActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(VisitClientActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean isDateAfterCurrentDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy hh:mm", Locale.ENGLISH);
        Date currentDate = new Date();
        Date comparingDate = null;

        try {
            comparingDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert comparingDate != null;
        return comparingDate.after(currentDate);
    }

    private boolean isDateTime(String dateTimeDest) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy hh:mm", Locale.ENGLISH);
        try {
            simpleDateFormat.parse(dateTimeDest);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }
}
