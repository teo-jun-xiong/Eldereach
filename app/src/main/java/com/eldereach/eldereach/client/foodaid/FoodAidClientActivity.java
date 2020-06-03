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

import com.eldereach.eldereach.R;
import com.eldereach.eldereach.client.HomeClientActivity;
import com.eldereach.eldereach.util.EldereachDateTime;
import com.eldereach.eldereach.util.MultiSelectSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.eldereach.eldereach.util.MultiSelectSpinner.*;

public class FoodAidClientActivity extends FragmentActivity implements OnMultipleItemsSelectedListener {
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

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    private void initialiseComponents() {
        dropdownDietary = findViewById(R.id.dropdownDietaryFoodAidClient);
        dropdownMeal = findViewById(R.id.dropdownMealFoodAidClient);
        textAllergy = findViewById(R.id.textAllergyFoodAidClient);
        buttonDateTime = findViewById(R.id.buttonDateTimeDeliveryFoodAidClient);
        buttonSubmit = findViewById(R.id.buttonSubmitRequestFoodAidClient);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Sets button to show date and time picker
        buttonDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(buttonDateTime);
            }
        });

        // Initialize multiple selection spinner
        String[] dropdownDietaryOptions = new String[]{
                "Halal",
                "Vegetarian",
                "Halal vegetarian",
                "None"
        };

        ArrayAdapter<String> dietaryAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, dropdownDietaryOptions);
        dropdownDietary.setAdapter(dietaryAdapter);

        String[] array = {
                "Cooked breakfast",
                "Cooked lunch",
                "Cooked dinner",
                "Fruits",
                "Assorted canned food"
        };

        dropdownMeal.setItems(array);
        dropdownMeal.hasNoneOption(false);
        dropdownMeal.setSelection(new int[]{0});
        dropdownMeal.setListener(this);

        // Gathers data from components and adds it to the database
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, Object> foodAidRequest = new HashMap<>();

                String email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
                String currentDate = EldereachDateTime.getCurrentDate();
                foodAidRequest.put("dateRequest", currentDate);
                foodAidRequest.put("status", 0);
                foodAidRequest.put("email", email);
                foodAidRequest.put("dietary", dropdownDietary.getSelectedItem().toString());
                foodAidRequest.put("allergy", textAllergy.getText().toString());

                // Obtains data from the multiple selection spinner
                List<String> selectedMeals = dropdownMeal.getSelectedStrings();

                // At least one option in the spinner has to be selected
                if (selectedMeals.size() == 0) {
                    dropdownMeal.requestFocus();
                    Toast.makeText(
                            FoodAidClientActivity.this,
                            "Please select the types of food or meals you require.",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    foodAidRequest.put("meals", selectedMeals);
                }

                // Obtains the date and time of delivery from EditText
                String dateTime = buttonDateTime.getText().toString();

                if (EldereachDateTime.isDateTime(dateTime) && EldereachDateTime.isDateAfterCurrentDate(dateTime)) {
                    foodAidRequest.put("dateTime", dateTime);
                } else {
                    if (EldereachDateTime.isDateAfterCurrentDate(dateTime)) {
                        Toast.makeText(
                                FoodAidClientActivity.this,
                                "Not date time",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(
                                FoodAidClientActivity.this,
                                "The date and time of the delivery is earlier than the current date.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Final array to store user information to be included in the database
                final String[] userInfo = {"", ""};

                assert email != null;
                db.collection("users").document(email)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> map = documentSnapshot.getData();

                        assert map != null;
                        userInfo[0] = (String) map.get("name");
                        userInfo[1] = (String) map.get("address");
                        foodAidRequest.put("name", userInfo[0]);
                        foodAidRequest.put("address", userInfo[1]);
                    }
                });

                // Food Aid - Email - Delivery date time - Request made date time
                String documentName = "F_" + firebaseAuth.getCurrentUser().getEmail() + "_" + dateTime + "_" + currentDate;
                foodAidRequest.put("id", documentName);
                foodAidRequest.put("serviceProviderName", "");
                foodAidRequest.put("serviceProviderPhone", "");
                foodAidRequest.put("serviceProviderEmail", "");
                final DocumentReference docRef = db.collection("foodAidRequests").document(documentName);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            // If the request already exists (i.e. created at the same time and delivery at the same time)
                            // TODO improve handling of duplicate requests - should the id not include time of creation?
                            assert document != null;
                            if (document.exists()) {
                                Toast.makeText(
                                        FoodAidClientActivity.this,
                                        "This food aid request already exists. Please check under 'My Requests'.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                docRef.set(foodAidRequest);
                                Toast.makeText(
                                        FoodAidClientActivity.this,
                                        "Food aid request submitted.",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(FoodAidClientActivity.this, HomeClientActivity.class));
                            }
                        }
                    }
                });
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
                        buttonDateTime.setText(EldereachDateTime.simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(
                        FoodAidClientActivity.this,
                        timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false).show();
            }
        };

        new DatePickerDialog(
                FoodAidClientActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void selectedIndices(List<Integer> indices) {
    }

    @Override
    public void selectedStrings(List<String> strings) {
    }
}
