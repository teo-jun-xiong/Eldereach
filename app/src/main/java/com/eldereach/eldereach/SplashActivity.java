package com.eldereach.eldereach;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import static android.view.animation.AnimationUtils.loadAnimation;

public class SplashActivity extends AppCompatActivity {
    ImageView image;
    Handler handler;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        image = findViewById(R.id.splash);
        handler = new Handler();
        firebaseAuth = FirebaseAuth.getInstance();

        Animation fade = loadAnimation(getApplicationContext(),R.anim.fade_out);
        image.startAnimation(fade);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is logged in from previous session
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SplashActivity.this, UserActivity.class));
                }

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);

    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }
}
