package com.eldereach.eldereach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.widget.CompoundButton.*;

public class SignUpActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText phone;
    CheckBox isVolunteer;
    CheckBox isRegistered;
    EditText registeredOrganization;
    EditText volunteerExperience;
    Button buttonSignUp;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initialiseComponents();
        setListeners();
    }

    private void initialiseComponents() {
        email = findViewById(R.id.ETemail);
        password = findViewById(R.id.ETpassword);
        phone = findViewById(R.id.ETphone);
        isVolunteer = findViewById(R.id.checkboxVolunteer);
        isRegistered = findViewById(R.id.checkboxOrganization);
        registeredOrganization = findViewById(R.id.ETorg);
        volunteerExperience = findViewById(R.id.ETexp);
        buttonSignUp = findViewById(R.id.btnSignUp);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void setListeners() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                String emailID = email.getText().toString();
                String pass = password.getText().toString();
                if (emailID.isEmpty()) {
                    email.setError("Provide your email first!");
                    email.requestFocus();
                } else if (pass.isEmpty()) {
                    password.setError("Set your password");
                    password.requestFocus();
                } else {
                    createAccount();
                }
            }
        });


        isVolunteer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    registeredOrganization.setEnabled(true);
                    volunteerExperience.setEnabled(true);
                } else {
                    registeredOrganization.setEnabled(false);
                    volunteerExperience.setEnabled(false);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void createAccount() {
        final String emailID = email.getText().toString();
        String pass = password.getText().toString();

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
                    user_Info.put("is_Client", !isVolunteer.isChecked());
                    user_Info.put("is_Solo", !isRegistered.isChecked());
                    user_Info.put("phone", phone.getText().toString());
                    user_Info.put("registered_Organization", registeredOrganization.getText().toString());
                    user_Info.put("volunteer_Description", volunteerExperience.getText().toString());

                    db.collection("users").document(emailID).set(user_Info);
                    startActivity(new Intent(SignUpActivity.this, UserActivity.class));
                }
            }
        });
    }
}
