package com.example.parkingappfrags_v2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class InfoAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_app);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_info_app);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_back_arrow, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoAppActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Buttons and links to GPay and Github
        ImageButton github_link_btn = findViewById(R.id.app_github_link_btn);
        Button gpay_link_btn = findViewById(R.id.gpay_donate_btn);

        github_link_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/RunelightRL/Parkfait-Parking-App")
                );
                startActivity(i);
            }
        });

        gpay_link_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "neerajknair2-2@okicici")
                                .appendQueryParameter("pn", "Parkfait. App")
                                .appendQueryParameter("tn", "Donation")
                                .appendQueryParameter("cu", "INR")
                                .build();
                String uri_string = uri.toString();

                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(uri_string)
                );
                startActivity(i);
            }
        });
    }
}