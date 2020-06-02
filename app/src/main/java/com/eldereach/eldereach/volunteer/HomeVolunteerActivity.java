package com.eldereach.eldereach.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.eldereach.eldereach.LogInActivity;
import com.eldereach.eldereach.R;
import com.eldereach.eldereach.volunteer.foodaid.FoodAidVolunteerActivity;
import com.eldereach.eldereach.volunteer.transport.TransportVolunteerActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeVolunteerActivity extends AppCompatActivity {
    ImageButton btnLogOut;
    ImageButton buttonFoodAid;
    ImageButton buttonTransport;
    ImageButton buttonVisit;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        initialiseComponents();
    }

    private void initialiseComponents() {
        btnLogOut = findViewById(R.id.buttonLogOutVolunteer);
        buttonFoodAid = findViewById(R.id.buttonFoodAidVolunteer);
        buttonTransport = findViewById(R.id.buttonTransportVolunteer);
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(HomeVolunteerActivity.this, LogInActivity.class));
            }
        });

        buttonFoodAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeVolunteerActivity.this, FoodAidVolunteerActivity.class));
            }
        });

        buttonTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeVolunteerActivity.this, TransportVolunteerActivity.class));
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