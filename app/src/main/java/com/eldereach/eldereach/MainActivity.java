package com.eldereach.eldereach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button buttonSignUp;
    Button buttonLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseComponents();
        setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    private void setListeners() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
            }
        });
    }

    private void initialiseComponents() {
        buttonSignUp = findViewById(R.id.button_sign_up);
        buttonLogIn = findViewById(R.id.button_log_in);
    }
}