package com.eldereach.eldereach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeClientActivity extends AppCompatActivity {
    Button btnLogOut;
    ImageButton btnTransport;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        btnLogOut = findViewById(R.id.btnClientLogOut);
        btnTransport = findViewById(R.id.btnClientTransport);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(HomeClientActivity.this, LogInActivity.class);
                startActivity(I);

            }
        });

        btnTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeClientActivity.this, "Not successful", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(HomeClientActivity.this, TransportClientActivity.class));
            }
        });
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
}