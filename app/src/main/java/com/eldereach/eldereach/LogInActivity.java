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

import static com.google.firebase.auth.FirebaseAuth.*;

public class LogInActivity extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    Button btnLogIn;
    FirebaseAuth firebaseAuth;
    AuthStateListener authStateListener;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialiseComponents();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = loginEmail.getText().toString();
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
                                Toast.makeText(LogInActivity.this, "Not sucessful", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(LogInActivity.this, UserActivity.class));
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

        authStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(LogInActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(LogInActivity.this, UserActivity.class);
                    startActivity(I);
                } else {
                    Toast.makeText(LogInActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
