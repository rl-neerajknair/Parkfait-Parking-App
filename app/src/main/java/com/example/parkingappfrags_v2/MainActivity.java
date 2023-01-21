package com.example.parkingappfrags_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // SMS Permission Check
        if (checkPermission(Manifest.permission.SEND_SMS)){
        }
        else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.burger_new);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                break;
            case R.id.nav_info_app:
                intent = new Intent(MainActivity.this, InfoAppActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_info_dev:
                intent = new Intent(MainActivity.this, InfoDevActivity.class);
                startActivity(intent);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isOpen()) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
            System.exit(0);
        }
    }

    // Function to Insert the record to database using a Bottom Sheet Dialog
    public void show_add_dialog(String str_lotno) {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_record_layout);

        // Lot No.
        TextView customer_lot_no = dialog.findViewById(R.id.input_customer_lot_no);
        customer_lot_no.setText(str_lotno);

        // Start-Time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String dialogTime = mdformat.format(calendar.getTime());

        // Leave-Time
        String str_leave_time = "Not Left Yet"; // Default value to be entered to Master-DB

        // Input Values - Customer Name , Phone No. , Vehicle No.
        TextInputEditText customer_name = dialog.findViewById(R.id.input_customer_name);
        TextInputEditText customer_phone_num = dialog.findViewById(R.id.input_customer_phno);
        TextInputEditText customer_vehc_num = dialog.findViewById(R.id.input_customer_vehcno);

        // Button Click - Insert Record Button (insert_record_btn)
        Button insert_record_btn = dialog.findViewById(R.id.insert_customer_record_btn);
        insert_record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = customer_phone_num.getText().toString().trim();
                String smsMessage = "Hello " + customer_name.getText().toString().trim() +
                        "\nWelcome to our Parking Lots. \nYour Parking Lot Number is : " + str_lotno +
                        " for your vehicle " + customer_vehc_num.getText().toString().trim() + ".";

                if (customer_name.getText().toString().trim().equals("") ||
                        customer_phone_num.getText().toString().trim().equals("") ||
                        customer_vehc_num.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Fields cannot be left empty!", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkPermission(Manifest.permission.SEND_SMS)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
                        Toast.makeText(MainActivity.this, "SMS Sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "SMS Permission Denied!", Toast.LENGTH_SHORT).show();
                    }
                    myDB.addParking(customer_name.getText().toString().trim(),
                            customer_phone_num.getText().toString().trim(),
                            customer_vehc_num.getText().toString().trim(),
                            str_lotno.trim(),
                            dialogTime.trim());

                    myDB.addMaster(customer_name.getText().toString().trim(),
                            customer_phone_num.getText().toString().trim(),
                            customer_vehc_num.getText().toString().trim(),
                            str_lotno.trim(),
                            dialogTime.trim(),
                            str_leave_time);

                    myDB.modFreeLotStatus_1(str_lotno.trim());
                    dialog.dismiss();
                    replaceFragment(new ListFragment());
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Function to replace Fragments on the Frame-Layout
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Function to check if Permission is enabled
    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(MainActivity.this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}