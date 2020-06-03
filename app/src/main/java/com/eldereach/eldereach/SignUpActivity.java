package com.eldereach.eldereach;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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

    Button buttonClient;
    Button buttonVolunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);

        //initialiseComponents();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layoutSignUp, new SignUpFragment());
        fragmentTransaction.commit();
    }

    void loadClientSignUpFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layoutSignUp, new ClientSignUpFragment());
        fragmentTransaction.commit();
    }

    void loadVolunteerSignUpFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.layoutSignUp, new VolunteerSignUpFragment());
        fragmentTransaction.commit();
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
