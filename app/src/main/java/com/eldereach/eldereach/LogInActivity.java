package com.eldereach.eldereach;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static com.google.firebase.auth.FirebaseAuth.*;

/**
 * Log in page for both clients and volunteers.
 */
public class LogInActivity extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    Button btnLogIn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialiseComponents();

        // Log in button attempts to sign in using the email and password provided
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userEmail = loginEmail.getText().toString();
                String userPaswd = loginPassword.getText().toString();
                if (userEmail.isEmpty()) {
                    loginEmail.setError("Provide your Email first!");
                    loginEmail.requestFocus();
                } else if (userPaswd.isEmpty()) {
                    loginPassword.setError("Enter Password!");
                    loginPassword.requestFocus();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(LogInActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "Not successful", Toast.LENGTH_SHORT).show();
                            } else {
                                DocumentReference docRef = db.collection("users").document(userEmail);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            assert document != null;
                                            Object isVolunteer = Objects.requireNonNull(document.getData()).get("isVolunteer");

                                            if (isVolunteer instanceof Boolean) {
                                                if (((boolean) isVolunteer)) {
                                                    startActivity(new Intent(LogInActivity.this, HomeVolunteerActivity.class));
                                                } else {
                                                    startActivity(new Intent(LogInActivity.this, HomeClientActivity.class));
                                                }
                                            } else {
                                                Toast.makeText(LogInActivity.this, "Not successful", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(LogInActivity.this, "Not successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });

    }

    private void initialiseComponents() {
        firebaseAuth = getInstance();
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginpaswd);
        btnLogIn = findViewById(R.id.btnLogIn);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LogInActivity.this, MainActivity.class));
        finish();
    }
}
