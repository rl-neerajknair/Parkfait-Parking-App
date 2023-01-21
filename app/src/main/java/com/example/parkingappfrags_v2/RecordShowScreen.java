package com.example.parkingappfrags_v2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecordShowScreen extends AppCompatActivity {

    MyDatabaseHelper myDB = new MyDatabaseHelper(RecordShowScreen.this);
    int diffH, diffM;
    int priceAccumulationFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_show_screen);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_record);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_back_arrow, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordShowScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        getIncomingIntent();
    }

    // Taking value of the record from the previous activity
    private void getIncomingIntent(){
        if (getIntent().hasExtra("cus_id") && getIntent().hasExtra("cus_name") && getIntent().hasExtra("cus_ph_no") &&
                getIntent().hasExtra("cus_vehc_no") && getIntent().hasExtra("cus_lot_no") && getIntent().hasExtra("cus_start_time")){

            String cus_id = getIntent().getStringExtra("cus_id");
            String cus_name = getIntent().getStringExtra("cus_name");
            String cus_ph_no = getIntent().getStringExtra("cus_ph_no");
            String cus_vehc_no = getIntent().getStringExtra("cus_vehc_no");
            String cus_lot_no = getIntent().getStringExtra("cus_lot_no");
            String cus_start_time = getIntent().getStringExtra("cus_start_time");

            setRecordValues(cus_id, cus_name, cus_ph_no, cus_vehc_no, cus_lot_no, cus_start_time);
        }
    }

    // Setting the intent values from the previous activity from getIncomingIntent() function to the TextViews and EditTexts
    private void setRecordValues(String id, String name, String ph_no, String vehc_no, String lot_no, String start_time){

        TextView cusID = findViewById(R.id.cusRec_cusID);
        cusID.setText(id);

        EditText cusName = findViewById(R.id.cusRec_cusName);
        cusName.setText(name);

        EditText cusVehcNo = findViewById(R.id.cusRec_cusVehcNo);
        cusVehcNo.setText(vehc_no);

        EditText cusPhNo = findViewById(R.id.cusRec_cusPhNo);
        cusPhNo.setText(ph_no);

        TextView cusLotNo = findViewById(R.id.cusRec_LotNo);
        cusLotNo.setText(lot_no);

        TextView cusStartTime = findViewById(R.id.cusRec_ParkingTime);
        cusStartTime.setText(start_time);

        TextView cusLeaveTime = findViewById(R.id.cusRec_LeavingTime);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String dialogDate = mdformat.format(calendar.getTime());
        cusLeaveTime.setText(dialogDate);

        String pkh = start_time.split(":")[0]; // Storing the parking time - hour value to string pkh
        int pkhI = Integer.parseInt(pkh); // parsing the pkh value to int pkhI

        String pkm = start_time.split(":")[1]; // Storing the parking time - minute value to string pkm
        int pkmI = Integer.parseInt(pkm); // parsing the pkm value to int pkmI

        String lvh = dialogDate.split(":")[0]; // Storing the leaving time - hour value to string lvh
        int lvhI = Integer.parseInt(lvh); // parsing the lvh value to int lvhI

        String lvm = dialogDate.split(":")[1]; // Storing the leaving time - minute value to string lvm
        int lvmI = Integer.parseInt(lvm); // parsing the lvm value to int lvmI

        diffH = pkhI - lvhI; // Difference in the hour value of parking time and leaving time
        diffM = pkmI - lvmI; // DIfference in the minute value of parking time and leaving time

        // Converting difference in minute and hour to positive in case it is negative.
        if (diffM < 0)
        {
            diffM = diffM * ( -1 );
        }
        if (diffH < 0)
        {
            diffH = diffH * ( -1 );
        }

        int pph = myDB.retrievePrices();
        int timeParked = ( diffH * 60 ) + diffM;
        int amount = ( timeParked / 60 ) * pph;

        // If the difference in time is less than 1 hr , i.e. less than or equal to 0hr 59mins , set the amount as the value for 1hr by default.
        if (diffH==0 && diffM<=59){
            amount = pph;
        }

        TextView cusAmount = findViewById(R.id.cusRec_Amount);
        cusAmount.setText(String.valueOf(amount));

        // Updating values for Customer Name , Vehicle Number and Phone Number upon change in EditText values.
        Button apply_change_btn = findViewById(R.id.apply_changes_record_btn);
        // Checking the new values with previous values of customer Name
        cusName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (cusName.getText().toString().trim().equals("") || cusPhNo.getText().toString().trim().equals("") || cusVehcNo.getText().toString().trim().equals("")){
                    Toast.makeText(RecordShowScreen.this, "Fields cannot be left empty", Toast.LENGTH_SHORT).show();
                    apply_change_btn.setVisibility(View.GONE);
                }
                else if(!cusName.getText().toString().trim().equals(name) || !cusPhNo.getText().toString().trim().equals(ph_no) || !cusVehcNo.getText().toString().trim().equals(vehc_no)){
                    apply_change_btn.setVisibility(View.VISIBLE);
                }
                else {
                    apply_change_btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Checking the new values with previous values of customer Phone No.
        cusPhNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (cusName.getText().toString().trim().equals("") || cusPhNo.getText().toString().trim().equals("") || cusVehcNo.getText().toString().trim().equals("")){
                    Toast.makeText(RecordShowScreen.this, "Fields cannot be left empty", Toast.LENGTH_SHORT).show();
                    apply_change_btn.setVisibility(View.GONE);
                }
                else if(!cusName.getText().toString().trim().equals(name) || !cusPhNo.getText().toString().trim().equals(ph_no) || !cusVehcNo.getText().toString().trim().equals(vehc_no)){
                    apply_change_btn.setVisibility(View.VISIBLE);
                }
                else {
                    apply_change_btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Checking the new values with previous values of customer Vehicle No.
        cusVehcNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (cusName.getText().toString().trim().equals("") || cusPhNo.getText().toString().trim().equals("") || cusVehcNo.getText().toString().trim().equals("")){
                    Toast.makeText(RecordShowScreen.this, "Fields not updated as some fields might be empty.", Toast.LENGTH_SHORT).show();
                    apply_change_btn.setVisibility(View.GONE);
                }
                else if(!cusName.getText().toString().trim().equals(name) || !cusPhNo.getText().toString().trim().equals(ph_no) || !cusVehcNo.getText().toString().trim().equals(vehc_no)){
                    apply_change_btn.setVisibility(View.VISIBLE);
                }
                else {
                    apply_change_btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        apply_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking again if any fields are empty after changing.
                if (cusName.getText().toString().trim().equals("") || cusPhNo.getText().toString().trim().equals("") || cusVehcNo.getText().toString().trim().equals("")){
                    Toast.makeText(RecordShowScreen.this, "Fields cannot be left empty", Toast.LENGTH_SHORT).show();
                    apply_change_btn.setVisibility(View.GONE);
                }
                else{
                    // Calling functions to modify the Name, Ph No., and Vehicle No. in the current Parking table
                    myDB.modParkingName(cusName.getText().toString().trim(),id);
                    myDB.modParkingPhoneNumber(cusPhNo.getText().toString().trim(),id);
                    myDB.modParkingVehicleNumber(cusVehcNo.getText().toString().trim(),id);

                    // Calling functions to modify the Name, Ph No., and Vehicle No. in the History/Master table
                    myDB.modMasterName(cusName.getText().toString().trim(),id);
                    myDB.modMasterPhoneNumber(cusPhNo.getText().toString().trim(),id);
                    myDB.modMasterVehicleNumber(cusVehcNo.getText().toString().trim(),id);

                    apply_change_btn.setVisibility(View.GONE);
                }
            }
        });


        String str_upiid = myDB.retrieveUPIID();
        String str_amount = String.valueOf(amount);

        //Button to Delete Record
        Button Del_Button = findViewById(R.id.cusRec_del_btn);
        Del_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(RecordShowScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.delete_record_confrimation_dialog);
                MyDatabaseHelper myDB = new MyDatabaseHelper(RecordShowScreen.this);

                Button confirm_change = dialog.findViewById(R.id.btn_delete_record_confirm);
                confirm_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phoneNumber = cusPhNo.getText().toString().trim();
                        String smsMessage = "You have stayed for : " + diffH +" hrs and " + diffM +"mins. \nPlease pay Rs. " + str_amount.trim() + ".\nThank you! Have a nice day :)";
                        if(checkPermission(Manifest.permission.SEND_SMS)){
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);

                            Toast.makeText(RecordShowScreen.this, "SMS Sent.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RecordShowScreen.this, "SMS Permission Denied!", Toast.LENGTH_SHORT).show();
                        }

                        SimpleDateFormat mdformatPriceAccumulation_DATE = new SimpleDateFormat("dd-MM-yyyy");
                        String todayPriceAccumulation_DATE = mdformatPriceAccumulation_DATE.format(calendar.getTime());

                        SimpleDateFormat mdformatPriceAccumulation_DAY = new SimpleDateFormat("E");
                        String todayPriceAccumulation_DAY = mdformatPriceAccumulation_DAY.format(calendar.getTime());

                        String priceAccumulationDate = myDB.retrievePriceAccumulation_Date();
                        String priceAccumulationTotalPrice = myDB.retrievePriceAccumulation_TotalPrice();
                        String newPriceAccumulationTotal_Price;

                        if (priceAccumulationDate == null || !priceAccumulationDate.equals(todayPriceAccumulation_DATE)){
                            myDB.addPriceAccumulation(todayPriceAccumulation_DATE,todayPriceAccumulation_DAY,str_amount);
                        }
                        else {
                            newPriceAccumulationTotal_Price = String.valueOf(Integer.parseInt(priceAccumulationTotalPrice) + Integer.parseInt(str_amount));
                            myDB.modPriceAccumulation_TotalPrice(newPriceAccumulationTotal_Price, todayPriceAccumulation_DATE);
                        }

                        myDB.modFreeLotStatus_0(String.valueOf(lot_no));
                        myDB.addMaster_leaveTime(id, dialogDate);
                        myDB.delete1Record(String.valueOf(id));
                        Intent intent = new Intent(RecordShowScreen.this, MainActivity.class);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });

        // Button to view QR Code
        Button QR_Button = findViewById(R.id.cusRec_QR_Btn);
        QR_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = cusPhNo.getText().toString().trim();
                String smsMessage = "You have stayed for : " + diffH +" hrs and " + diffM +"mins. \nPlease pay Rs. " + str_amount.trim() + " through the QR code shown to you by our manager.\nThank you! Have a nice day :)";
                if(checkPermission(Manifest.permission.SEND_SMS)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);

                    Toast.makeText(RecordShowScreen.this, "SMS Sent.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RecordShowScreen.this, "SMS Permission Denied!", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(RecordShowScreen.this, qr_code_display_screen.class);
                intent.putExtra("upi_id",str_upiid);
                intent.putExtra("amount",str_amount);
                intent.putExtra("lot_no",lot_no);
                intent.putExtra("id",id);
                intent.putExtra("leave_time" , dialogDate);
                startActivity(intent);
            }
        });

    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(RecordShowScreen.this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onBackPressed() {
        Intent intent =  new Intent(RecordShowScreen.this, MainActivity.class);
        startActivity(intent);
    }
}