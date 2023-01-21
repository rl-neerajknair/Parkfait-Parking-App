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

public class InfoDevActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dev);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_info_dev);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_back_arrow, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoDevActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Buttons and links to Social Media Pages
        Button github_page_btn = findViewById(R.id.socials_github);
        Button linkedin_page_btn = findViewById(R.id.socials_linkedin);
        Button instagram_page_btn = findViewById(R.id.socials_instagram);

        github_page_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/RunelightRL")
                );
                startActivity(i);
            }
        });

        linkedin_page_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.linkedin.com/in/neeraj-k-nair-173b2a96/")
                );
                startActivity(i);
            }
        });

        instagram_page_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/rune_clairvoy.nt/")
                );
                startActivity(i);
            }
        });

    }
}