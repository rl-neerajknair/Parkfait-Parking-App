package com.example.parkingappfrags_v2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Only_Once_Activity extends AppCompatActivity {
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_once);

        SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String FirstTime = preferences.getString("FirstTimeInstall", "");

        // Checking if the app has been opened before, if true, switch to MainActivity directly
        if (FirstTime.equals("Yes")) {

            Intent intent = new Intent(Only_Once_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // SMS Permission Check
        if (checkPermission(Manifest.permission.SEND_SMS)){
        }
        else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        // Input Values - No. of Rows, No. of Columns, No. of Parking Lots, Price-per-hour Amount, UPI-ID
        TextInputEditText rows = findViewById(R.id.only_once_row);
        TextInputEditText cols = findViewById(R.id.only_once_column);
        TextInputEditText num = findViewById(R.id.only_once_numberofparkinglots);
        TextInputEditText price = findViewById(R.id.price_per_hour_amount_only_once);
        TextInputEditText upi_id = findViewById(R.id.upi_id_onlyonce);

        // Button Click - Accept Button (acceptBtn)
        Button acceptBtn = findViewById(R.id.only_once_accept_btn);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = preferences.edit();

                // Checking if all the EditText (or TextInputEditText) have been given input
                Integer maxnum = Integer.parseInt(rows.getText().toString().trim()) * Integer.parseInt(cols.getText().toString().trim());
                if (rows.getText().toString().trim().equals("") || cols.getText().toString().trim().equals("") ||
                        num.getText().toString().trim().equals("") || price.getText().toString().trim().equals("") ||
                        upi_id.getText().toString().trim().equals("")) {
                    Toast.makeText(Only_Once_Activity.this, "Fields cannot be left empty", Toast.LENGTH_SHORT).show();
                }

                // Checking if the No. of Parking Lots is greater than the product of No. of Rows and Columns
                else if (Integer.parseInt(num.getText().toString().trim()) > maxnum) {
                    Toast.makeText(Only_Once_Activity.this, "Number of parking lots cannot exceed dimension size", Toast.LENGTH_SHORT).show();
                }

                // Inserting to Database
                else {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(Only_Once_Activity.this);

                    myDB.addDimensions(Integer.valueOf(rows.getText().toString().trim()), Integer.valueOf(cols.getText().toString().trim()), Integer.valueOf(num.getText().toString().trim()));
                    myDB.addPrice(Integer.valueOf(price.getText().toString().trim()));
                    myDB.addUPI(upi_id.getText().toString().trim());

                    int count = Integer.parseInt(num.getText().toString().trim());
                    for (int i=1; i<=count; i++)
                    {
                        myDB.addFreeLot(count, 0);
                    }

                    // Setting the check-flag for checking if it is the first-time install, as 'Yes'
                    editor.putString("FirstTimeInstall", "Yes");
                    editor.apply();

                    // Switching to MainActivity
                    Intent intent = new Intent(Only_Once_Activity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    // Function to check if Permission is enabled
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(Only_Once_Activity.this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}