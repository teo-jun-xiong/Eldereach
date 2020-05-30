package com.eldereach.eldereach.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.eldereach.eldereach.LogInActivity;
import com.eldereach.eldereach.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeVolunteerActivity extends AppCompatActivity {
    ImageButton btnLogOut;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        initialiseComponents();
    }

    private void initialiseComponents() {
        btnLogOut = findViewById(R.id.buttonLogOutVolunteer);
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(HomeVolunteerActivity.this, LogInActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);

        // Prevents user from logging out and then pressing the "back" button to get back to the home screen
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(HomeVolunteerActivity.this, LogInActivity.class));
        }
    }
}