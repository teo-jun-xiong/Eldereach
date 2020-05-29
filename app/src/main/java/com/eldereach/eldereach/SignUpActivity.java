package com.eldereach.eldereach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eldereach.eldereach.client.HomeClientActivity;
import com.eldereach.eldereach.volunteer.HomeVolunteerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.view.View.*;
import static android.widget.CompoundButton.OnCheckedChangeListener;

public class SignUpActivity extends AppCompatActivity {
    Button buttonSignUp;
    EditText textEmail;
    EditText textPassword;
    EditText textPhone;
    TextView textVolunteerSection;
    EditText textRegisteredOrganizations;
    EditText textExperience;
    EditText textName;
    EditText textAddress;
    CheckBox checkboxIsVolunteer;
    CheckBox checkboxIsRegistered;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);

        initialiseComponents();
    }

    private void initialiseComponents() {
        buttonSignUp = findViewById(R.id.buttonSignUpSignUp);
        textEmail = findViewById(R.id.textEmailSignUp);
        textName = findViewById(R.id.textNameSignUp);
        textPassword = findViewById(R.id.textPasswordSignUp);
        textPhone = findViewById(R.id.textPhoneSignUp);
        textAddress = findViewById(R.id.textAddressSignup);
        textVolunteerSection = findViewById(R.id.textVolunteerSectionSignUp);
        textRegisteredOrganizations = findViewById(R.id.textRegisteredOrganizationsSignUp);
        textExperience = findViewById(R.id.textExperienceSignUp);
        checkboxIsVolunteer = findViewById(R.id.checkboxIsVolunteerSignUp);
        checkboxIsRegistered = findViewById(R.id.checkboxIsRegisteredSignUp);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textVolunteerSection.setText("This section is for clients only.");
        textRegisteredOrganizations.setVisibility(INVISIBLE);
        textExperience.setVisibility(INVISIBLE);
        checkboxIsRegistered.setVisibility(INVISIBLE);

        buttonSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick (View view){
                String emailID = textEmail.getText().toString();
                String pass = textPassword.getText().toString();
                String name = textName.getText().toString();

                if (emailID.isEmpty()) {
                    textEmail.setError("Please indicate your email.");
                    textEmail.requestFocus();
                } else if (pass.isEmpty()) {
                    textPassword.setError("Please set a password for your Eldereach account.");
                    textPassword.requestFocus();
                } else if (name.isEmpty()) {
                    textName.setError("Please indicate your name.");
                    textName.requestFocus();
                } else if (!checkboxIsVolunteer.isChecked() && textAddress.getText().toString().isEmpty()) {
                    textAddress.setError("Please indicate your home address.");
                    textAddress.requestFocus();
                } else {
                    createAccount();
                }
            }
        });

        checkboxIsVolunteer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    textVolunteerSection.setText("This section is for volunteers only.");
                    textExperience.setVisibility(VISIBLE);
                    checkboxIsRegistered.setVisibility(VISIBLE);
                    textAddress.setVisibility(INVISIBLE);
                } else {
                    textVolunteerSection.setText("This section is for clients only.");
                    textAddress.setVisibility(VISIBLE   );
                    textExperience.setVisibility(INVISIBLE);
                    textRegisteredOrganizations.setVisibility(INVISIBLE);
                    checkboxIsRegistered.setVisibility(INVISIBLE);
                }
            }
        });

        checkboxIsRegistered.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    textRegisteredOrganizations.setVisibility(VISIBLE);
                } else {
                    textRegisteredOrganizations.setVisibility(INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    @SuppressWarnings("unchecked")
    private void createAccount() {
        final String emailID = textEmail.getText().toString();
        String pass = textPassword.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(emailID, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this.getApplicationContext(),
                            "Sign up unsuccessful: " + Objects.requireNonNull(task.getException()).getMessage(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> user_Info = new HashMap<>();
                    user_Info.put("email", emailID);
                    user_Info.put("name", textName.getText().toString());
                    user_Info.put("isVolunteer", checkboxIsVolunteer.isChecked());
                    user_Info.put("isRegistered", checkboxIsRegistered.isChecked());
                    user_Info.put("address", textAddress.getText().toString());
                    user_Info.put("phone", textPhone.getText().toString());
                    user_Info.put("registeredOrganization", textRegisteredOrganizations.getText().toString());
                    user_Info.put("volunteerExperience", textExperience.getText().toString());

                    db.collection("users").document(emailID).set(user_Info);

                    if (checkboxIsVolunteer.isChecked()) {
                        startActivity(new Intent(SignUpActivity.this, HomeVolunteerActivity.class));
                    } else {
                        startActivity(new Intent(SignUpActivity.this, HomeClientActivity.class));
                    }
                }
            }
        });
    }
}
