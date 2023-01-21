package com.example.parkingappfrags_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Using SharedPreferences to see and check whether the app has been installed for the first time or not
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String FirstTime = preferences.getString("FirstTimeInstall", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Checking if the app has been opened before, if true, switch to MainActivity directly
                if (FirstTime.equals("Yes")) {

                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_bottom_top, R.anim.slide_down_top_bottom);
                    finish();
                }

                // If the app has been opened for the first time, switch to Only_Once_Activity
                else {
                    Intent intent = new Intent(SplashScreen.this, Only_Once_Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_bottom_top, R.anim.slide_down_top_bottom);
                    finish();
                }
            }
        },2500); // The Splash Screen will last for 2500 ms or 2.5 sec.
    }
}