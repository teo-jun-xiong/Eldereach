package com.eldereach.eldereach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class MainActivity extends AppCompatActivity {
    public EditText email, password;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Checks if the user is logged in
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        } else {
            email = findViewById(R.id.ETemail);
            password = findViewById(R.id.ETpassword);
            btnSignUp = findViewById(R.id.btnSignUp);
            signIn = findViewById(R.id.TVSignIn);

            // User signs up for an account
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String emailID = email.getText().toString();
                    String paswd = password.getText().toString();
                    if (emailID.isEmpty()) {
                        email.setError("Provide your Email first!");
                        email.requestFocus();
                    } else if (paswd.isEmpty()) {
                        password.setError("Set your password");
                        password.requestFocus();
                    } else {
                        firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this.getApplicationContext(),
                                            "Sign up unsuccessful: " + Objects.requireNonNull(task.getException()).getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Map<String, Object> user_Info = new HashMap<>();
                                    user_Info.put("email", emailID);
                                    user_Info.put("is_Client", true);
                                    user_Info.put("is_Solo", false);
                                    user_Info.put("phone", 12345678);
                                    user_Info.put("registered_Organization", "NA");
                                    user_Info.put("volunteer_Description", "NA");

                                    db.collection("users").document("email").set(user_Info);
                                    startActivity(new Intent(MainActivity.this, UserActivity.class));
                                }
                            }
                        });
                    }
                }
            });

            // User signs in to existing account
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent I = new Intent(MainActivity.this, ActivityLogin.class);
                    startActivity(I);
                }
            });
        }
    }
}