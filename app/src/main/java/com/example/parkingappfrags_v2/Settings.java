package com.example.parkingappfrags_v2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Settings extends AppCompatActivity {

    //Global Declarataions
    Dialog dialog;
    //EditText newRows , newCols , newNumberOfParkingLots , newPrice;
    Button accept_button;
    Button cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MyDatabaseHelper myDB = new MyDatabaseHelper(Settings.this);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_back_arrow, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //Button Declarations
        Button edit_dimensions = findViewById(R.id.btn_edit_dim);
        Button edit_price_amount = findViewById(R.id.btn_edit_amt);
        ImageButton delete_parking_table = findViewById(R.id.btn_del_parking);
        ImageButton delete_master_table = findViewById(R.id.btn_del_master);
        Button edit_upi_id = findViewById(R.id.btn_edit_upi);

        //TextView Declarations
        TextView current_dimensions = findViewById(R.id.dimensions_count_settings);
        TextView current_max_lots = findViewById(R.id.maxLots_settings);
        TextView current_price = findViewById(R.id.current_price_settings);
        TextView current_upi_id = findViewById(R.id.upi_textView);

        int row_count = myDB.retrieveDimensions_rows();
        int col_count = myDB.retrieveDimensions_cols();
        String dimensions_count = row_count + " rows  x  " + col_count + " columns";
        current_dimensions.setText(dimensions_count);

        int maxlots = myDB.retrieveDimensions_numberoflots();
        String str_maxlots = maxlots + " Lots";
        current_max_lots.setText(str_maxlots);

        int price = myDB.retrievePrices();
        String cur_price = "Rs. " + price;
        current_price.setText(cur_price);

        String str_upiid = myDB.retrieveUPIID();
        current_upi_id.setText(str_upiid);

        edit_dimensions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDimensionsDialog();
            }
        });

        edit_price_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPriceDialog();
            }
        });

        delete_parking_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDeleteParkingDialog();
            }
        });

        delete_master_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDeleteMasterDialog();
            }
        });

        edit_upi_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { callUPIDialog(); }
        });

    }

    // Function to call the UPI Dialog to change UPI ID
    private void callUPIDialog() {
        final Dialog dialog = new Dialog(Settings.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_upi_id_dialog);
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);

        // Input Values - New UPI-ID (edittext_upi_id)
        TextInputEditText new_upi_id = dialog.findViewById(R.id.edittext_upi_id);

        // Button Click - Insert Record Button (insert_record_btn)
        Button confirm_change = dialog.findViewById(R.id.confirm_upi_change);
        confirm_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new_upi_id.getText().toString().trim().equals("")){
                    Toast.makeText(Settings.this, "Fields cannot be left empty!", Toast.LENGTH_SHORT).show();
                    //insert_warning.setText("Fields cannot be left empty!");
                }
                else{

                    myDB.addUPI(new_upi_id.getText().toString().trim());

                    //myDB.modFreeLotStatus_1(textViewLotNo.getText().toString().trim());

                    Intent i = new Intent(Settings.this, MainActivity.class);
                    startActivity(i);

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Function to call the dialog to delete Master DB/Customer History
    private void callDeleteMasterDialog() {
        final Dialog dialog = new Dialog(Settings.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_master_table_dialog);
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);

        // Button Click - Insert Record Button (insert_record_btn)
        Button confirm_change = dialog.findViewById(R.id.confirm_delete_master);
        confirm_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Settings.this, "Master Table Deleted!", Toast.LENGTH_SHORT).show();

                myDB.deleteAllMaster();

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Function to call the dialog to delete current list of parked customers
    private void callDeleteParkingDialog() {
        final Dialog dialog = new Dialog(Settings.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_parking_table_dialog);
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);

        // Button Click - Insert Record Button (insert_record_btn)
        Button confirm_change = dialog.findViewById(R.id.confirm_delete_parking);
        confirm_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Settings.this, "Current Parking Table Deleted!", Toast.LENGTH_SHORT).show();
                myDB.deleteAllParking();

                int count = myDB.retrieveDimensions_numberoflots();
                for (int i=1; i<=count; i++)
                {
                    myDB.addFreeLot(count, 0);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Function to call the dialog to change the price-per-hour for parking
    private void callPriceDialog() {
        final Dialog dialog = new Dialog(Settings.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_parking_price_dialog);
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);

        // Input Values - New UPI-ID (edittext_upi_id)
        TextInputEditText new_parking_amt = dialog.findViewById(R.id.edittext_parking_amt);

        // Button Click - Insert Record Button (insert_record_btn)
        Button confirm_change = dialog.findViewById(R.id.confirm_amount_change);
        confirm_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new_parking_amt.getText().toString().trim().equals("")){
                    Toast.makeText(Settings.this, "Fields cannot be left empty!", Toast.LENGTH_SHORT).show();
                    //insert_warning.setText("Fields cannot be left empty!");
                }
                else{

                    myDB.addPrice(Integer.valueOf(new_parking_amt.getText().toString().trim()));

                    //myDB.modFreeLotStatus_1(textViewLotNo.getText().toString().trim());

                    Intent i = new Intent(Settings.this, MainActivity.class);
                    startActivity(i);

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Function to call the dialog to change the dimensions of the parking lot
    private void callDimensionsDialog() {
        final Dialog dialog = new Dialog(Settings.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_dimensions_dialog);
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);

        // Input Values - New Rows, Columns, Maximum no of Parking Lots
        TextInputEditText new_rows = dialog.findViewById(R.id.edit_dim_rows);
        TextInputEditText new_cols = dialog.findViewById(R.id.edit_dim_cols);
        TextInputEditText new_number_of_lots = dialog.findViewById(R.id.edit_dim_num_parking_lots);

        // Button Click - Insert Record Button (insert_record_btn)
        Button confirm_change = dialog.findViewById(R.id.confirm_dimension_change);
        confirm_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer maxnum = Integer.parseInt(new_rows.getText().toString().trim()) * Integer.parseInt(new_cols.getText().toString().trim()); // Finding the maximum possible number of lots
                if(new_rows.getText().toString().trim().equals("") || new_cols.getText().toString().trim().equals("")){
                    Toast.makeText(Settings.this, "Fields cannot be left empty!", Toast.LENGTH_SHORT).show();
                    //insert_warning.setText("Fields cannot be left empty!");
                }
                else if (Integer.parseInt(new_number_of_lots.getText().toString().trim()) > maxnum) {
                    Toast.makeText(Settings.this, "Cannot have that many parking lots. Adjust the number of rows and columns if needed.", Toast.LENGTH_SHORT).show();
                }
                else {
                    myDB.deleteAllFreeLots();
                    myDB.deleteAllParking();

                    myDB.addDimensions(Integer.valueOf(new_rows.getText().toString().trim()),
                            Integer.valueOf(new_cols.getText().toString().trim()),
                            Integer.valueOf(new_number_of_lots.getText().toString().trim()));

                    int count = Integer.parseInt(new_number_of_lots.getText().toString().trim());
                    for (int i=1; i<=count; i++)
                    {
                        myDB.addFreeLot(count, 0);
                    }

                    Intent i = new Intent(Settings.this, MainActivity.class);
                    startActivity(i);

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}