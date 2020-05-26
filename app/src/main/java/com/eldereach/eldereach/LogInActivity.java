package com.eldereach.eldereach;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eldereach.eldereach.client.HomeClientActivity;
import com.eldereach.eldereach.volunteer.HomeVolunteerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * Log in page for both clients and volunteers.
 */
public class LogInActivity extends AppCompatActivity {
    EditText textEmail;
    EditText textPassword;
    Button buttonLogIn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialiseComponents();
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

    @SuppressWarnings("unchecked")
    private void initialiseComponents() {
        firebaseAuth = FirebaseAuth.getInstance();
        textEmail = findViewById(R.id.textEmailLogIn);
        textPassword = findViewById(R.id.textPasswordLogIn);
        buttonLogIn = findViewById(R.id.buttonLogInLogIn);
        db = FirebaseFirestore.getInstance();

        // Log in button attempts to sign in using the email and password provided
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = textEmail.getText().toString();
                String password = textPassword.getText().toString();
                if (email.isEmpty()) {
                    textEmail.setError("Provide your Email first!");
                    textEmail.requestFocus();
                } else if (password.isEmpty()) {
                    textPassword.setError("Enter Password!");
                    textPassword.requestFocus();
                } else {

                    // Logs in to the server
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "Not successful", Toast.LENGTH_SHORT).show();
                            } else {

                                // If log in is successful, retrieve user's data from the database
                                DocumentReference docRef = db.collection("users").document(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail()));
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();

                                            assert document != null;
                                            Object isVolunteer = Objects.requireNonNull(document.getData()).get("isVolunteer");

                                            // Directs user to either activity depending on whether they are clients or volunteers
                                            if (isVolunteer instanceof Boolean) {
                                                if (((boolean) isVolunteer)) {
                                                    startActivity(new Intent(LogInActivity.this, HomeVolunteerActivity.class));
                                                } else {
                                                    startActivity(new Intent(LogInActivity.this, HomeClientActivity.class));
                                                }
                                            } else {
                                                Toast.makeText(LogInActivity.this, "User data corrupted.", Toast.LENGTH_SHORT).show();
                                            }
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
}
