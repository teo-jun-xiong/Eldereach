package com.eldereach.eldereach.client.foodaid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.eldereach.eldereach.client.HomeClientActivity;
import com.eldereach.eldereach.util.MultiSelectSpinner;
import com.eldereach.eldereach.R;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FoodAidClientActivity extends FragmentActivity implements MultiSelectSpinner.OnMultipleItemsSelectedListener {
    Spinner dropdownDietary;
    MultiSelectSpinner dropdownMeal;
    EditText textAllergy;
    Button buttonDateTime;
    Button buttonSubmit;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_food_aid);

        initialiseComponents();
    }

    private void initialiseComponents() {
        dropdownDietary = findViewById(R.id.dropdownDietaryFoodAidClient);
        dropdownMeal = findViewById(R.id.dropdownMealFoodAidClient);
        textAllergy = findViewById(R.id.textAllergyFoodAidClient);
        buttonDateTime = findViewById(R.id.buttonDateTimeDeliveryFoodAidClient);
        buttonSubmit = findViewById(R.id.buttonSubmitRequestFoodAidClient);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        buttonDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(buttonDateTime);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> foodAidRequest = new HashMap<>();

                foodAidRequest.put("email", firebaseAuth.getCurrentUser().getEmail());
                foodAidRequest.put("dietary", dropdownDietary.getSelectedItem().toString());
                foodAidRequest.put("allergy", textAllergy.getText().toString());

                List<String> selectedMeals = dropdownMeal.getSelectedStrings();

                if (selectedMeals.size() == 0) {
                    dropdownMeal.requestFocus();
                    Toast.makeText(FoodAidClientActivity.this, "Please select the types of food or meals you require.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    foodAidRequest.put("meals", selectedMeals);
                }

                String dateTime = buttonDateTime.getText().toString();

                if (isDateTime(dateTime) && isDateAfterCurrentDate(dateTime)) {
                    foodAidRequest.put("dateTimeHome", dateTime);
                } else {
                    Toast.makeText(FoodAidClientActivity.this, "The date and time of the delivery is earlier than the current date.",Toast.LENGTH_SHORT).show();
                    return;
                }

                foodAidRequest.put("isAccepted", false);
                // Food Aid - Email - Delivery date time - Request made date time
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy hh:mm", Locale.ENGLISH);
                String documentName = "F_" + firebaseAuth.getCurrentUser().getEmail() + "_" + dateTime + "_" + simpleDateFormat.format(new Date());
                final DocumentReference docRef = db.collection("foodAidRequests").document(documentName); // set(transportRequest);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(FoodAidClientActivity.this, "This transport request already exists. Please check under 'My Requests'.", Toast.LENGTH_SHORT).show();
                            } else {
                                docRef.set(foodAidRequest);
                                Toast.makeText(FoodAidClientActivity.this, "Food aid request submitted.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(FoodAidClientActivity.this, HomeClientActivity.class));
                            }
                        }
                    }
                });
            }
        });

        String[] dropdownDietaryOptions = new String[]{
                "Halal",
                "Vegetarian",
                "Halal vegetarian",
                "None"
        };

        ArrayAdapter<String> dietaryAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dropdownDietaryOptions);
        dropdownDietary.setAdapter(dietaryAdapter);

        String[] array = {"Cooked breakfast", "Cooked lunch", "Cooked dinner", "Fruits", "Assorted canned food"};
        dropdownMeal.setItems(array);
        dropdownMeal.hasNoneOption(false);
        dropdownMeal.setSelection(new int[]{0});
        dropdownMeal.setListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
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

                new TimePickerDialog(FoodAidClientActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(FoodAidClientActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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

    @Override
    public void selectedIndices(List<Integer> indices) {
    }

    @Override
    public void selectedStrings(List<String> strings) {
    }
}
