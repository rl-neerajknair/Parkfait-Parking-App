package com.example.parkingappfrags_v2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class qr_code_display_screen extends AppCompatActivity {
    Button del_btn_qr_screen;
    ImageView QR_ImageView;
    MyDatabaseHelper myDB = new MyDatabaseHelper(qr_code_display_screen.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_display_screen);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_qr_code);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_back_arrow, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(qr_code_display_screen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        getIncomingIntent();
    }

    // Function to get the intent values from the previous activity
    private void getIncomingIntent() {
        if (getIntent().hasExtra("upi_id") && getIntent().hasExtra("amount") && getIntent().hasExtra("lot_no") && getIntent().hasExtra("id")) {

            String str_upi_id = getIntent().getStringExtra("upi_id");
            String str_amount = getIntent().getStringExtra("amount");
            String str_lotno = getIntent().getStringExtra("lot_no");
            String str_id = getIntent().getStringExtra("id");

            String leave_time = getIntent().getStringExtra("leave_time");

            del_btn_qr_screen = findViewById(R.id.delete_btn_qr_screen);
            QR_ImageView = findViewById(R.id.QR_ImageView);

            // Creating URI
            Uri uri =
                    new Uri.Builder()
                            .scheme("upi")
                            .authority("pay")
                            .appendQueryParameter("pa", str_upi_id)
                            .appendQueryParameter("pn", "Parking Fees")
                            .appendQueryParameter("tn", "Parking Payment")
                            .appendQueryParameter("am", str_amount)
                            .appendQueryParameter("cu", "INR")
                            .build();
            String uri_string = uri.toString();

            // Generating QR Code from the URI
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                BitMatrix matrix = writer.encode(uri_string, BarcodeFormat.QR_CODE, 350, 350);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);
                QR_ImageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            // Delete Button
            del_btn_qr_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(qr_code_display_screen.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.delete_record_confrimation_dialog);
                    MyDatabaseHelper myDB = new MyDatabaseHelper(qr_code_display_screen.this);

                    // Button Click - Insert Record Button (insert_record_btn)
                    Button confirm_change = dialog.findViewById(R.id.btn_delete_record_confirm);
                    confirm_change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Calendar calendar = Calendar.getInstance();
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

                            myDB.modFreeLotStatus_0(str_lotno);
                            myDB.addMaster_leaveTime(str_id, leave_time);
                            myDB.delete1Record(String.valueOf(str_id));
                            Intent intent = new Intent(qr_code_display_screen.this, MainActivity.class);
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
        }
    }
}