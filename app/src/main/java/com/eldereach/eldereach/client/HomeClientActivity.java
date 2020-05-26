package com.eldereach.eldereach.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.eldereach.eldereach.LogInActivity;
import com.eldereach.eldereach.R;
import com.eldereach.eldereach.client.foodaid.FoodAidClientActivity;
import com.eldereach.eldereach.client.transport.TransportClientActivity;
import com.eldereach.eldereach.client.visit.VisitClientActivity;
import com.google.firebase.auth.FirebaseAuth;

/** Home screen for clients */
public class HomeClientActivity extends AppCompatActivity {
    Button buttonLogOut;
    ImageButton buttonTransport;
    ImageButton buttonFoodAid;
    ImageButton buttonVisit;
    ImageButton buttonHotline;
    Button buttonRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        initialiseComponents();
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);

        // Prevents user from logging out and then pressing the "back" button to get back to the home screen
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(HomeClientActivity.this, LogInActivity.class));
        }
    }

    private void initialiseComponents() {
        buttonLogOut = findViewById(R.id.buttonLogOutClient);
        buttonTransport = findViewById(R.id.buttonTransportClient);
        buttonFoodAid = findViewById(R.id.buttonFoodAidClient);
        buttonVisit = findViewById(R.id.buttonVisitClient);
        buttonHotline = findViewById(R.id.buttonHotlinesClient);
        buttonRequests = findViewById(R.id.buttonRequestsClient);

        // Signs out of the app
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeClientActivity.this, LogInActivity.class));

            }
        });

        buttonFoodAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeClientActivity.this, FoodAidClientActivity.class));
            }
        });

        buttonTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeClientActivity.this, TransportClientActivity.class));
            }
        });

        buttonVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeClientActivity.this, VisitClientActivity.class));
            }
        });

        buttonRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeClientActivity.this, RequestsClientActivity.class));
            }
        });
    }
}