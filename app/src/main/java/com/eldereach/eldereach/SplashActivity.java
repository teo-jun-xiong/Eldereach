package com.eldereach.eldereach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    ImageView image;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        image = findViewById(R.id.splashImageView);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if user is logged in from previous session
        if (firebaseAuth.getCurrentUser() != null) {
            DocumentReference docRef = db.collection("users").document(Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail()));

            // Obtain document with the title
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        Object isVolunteer = Objects.requireNonNull(document.getData()).get("isVolunteer");

                        if (isVolunteer instanceof Boolean) {
                            if (((boolean) isVolunteer)) {
                                startActivity(new Intent(SplashActivity.this, HomeVolunteerActivity.class));
                            } else {
                                startActivity(new Intent(SplashActivity.this, HomeClientActivity.class));
                            }
                        }
                    }
                }
            });
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }
}
