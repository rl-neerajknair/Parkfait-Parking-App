package com.example.parkingappfrags_v2;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.SmsManager;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingappfrags_v2.databinding.FragmentHomeBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class HomeFragment extends Fragment {

    // Global Declarations

    String input_lot_no = "-1";
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    // Declarations for Parking Information Expandable Card
    TextView parkingInfo;
    CardView parkingInfoCardView;
    LinearLayout parkingInfoLinearLayout;

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(getContext());
        // SMS Permission Check
        if (checkPermission(Manifest.permission.SEND_SMS)){
        }
        else{
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        // Setting up Fragments for the Nav-Bar
        replaceFragment(new ListFragment());
        binding.bottomNav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.list_btn:
                    replaceFragment(new ListFragment());
                    break;
                case R.id.grid_btn:
                    replaceFragment(new GridFragment());
                    break;
                case R.id.add_btn:
                    Integer lotno = myDB.retrieveFreeLot_id();
                    String str_lotno = String.valueOf(lotno);
                    show_add_dialog(str_lotno);
                    break;
                case R.id.search_master_db_btn:
                    replaceFragment(new SearchMasterDbFragment());
                    break;
                case R.id.analytics_btn:
                    replaceFragment(new AnalyticsFragment());
                    break;
            }

            return true;
        });

        // Setting up Expandable Card View for - Parking Information
        parkingInfoCardView = view.findViewById(R.id.parkingInfoCardView);
        parkingInfoLinearLayout = view.findViewById(R.id.parkingInfoLinearLayout);
        parkingInfo = view.findViewById(R.id.parkingInfo);

        // Extracting current day's date from the system
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        String todayDate = mdformat.format(calendar.getTime());

        // Finding the current day's priceAccumulation table's TotalPrice (the total amount recieved today), and checking if it is null or not
        String priceAccumulation = myDB.retrievePriceAccumulation_TotalPrice();
        String priceAccumulationDate = myDB.retrievePriceAccumulation_Date();
        if (priceAccumulation == null || !Objects.equals(priceAccumulationDate, todayDate)){
            priceAccumulation = "" + 0;
        }

        // Finding the total number of lots free
        int grid_numberoflots = myDB.retrieveDimensions_numberoflots();
        int lotsCount = myDB.countParking();
        int noOfLotsFree = grid_numberoflots - lotsCount;

        // Setting the text value of 'parkingInfo'
        String detail_parkingInfo = "Date : " + todayDate + "\nPrice per hour : Rs. " + myDB.retrievePrices() +
                "\nTotal money acquired today : Rs. " + priceAccumulation + "\nAvailable Parking Lots : " + noOfLotsFree + "/" + grid_numberoflots;
        parkingInfo.setText(detail_parkingInfo);

        // Transition Animation for the CardView
        parkingInfoLinearLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        // Calling function to expand the CardView upon clicking it
        parkingInfoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandCard();
            }
        });

        return view;
    }

    // Function to Insert the record to database using a Bottom Sheet Dialog
    public void show_add_dialog(String str_lotno) {

        MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(getContext());

        final Dialog dialog = new Dialog(getActivity());
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

                if(customer_name.getText().toString().trim().equals("") ||
                        customer_phone_num.getText().toString().trim().equals("") ||
                        customer_vehc_num.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Fields cannot be left empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(checkPermission(Manifest.permission.SEND_SMS)){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
                        Toast.makeText(getActivity(), "SMS Sent.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "SMS Permission Denied!", Toast.LENGTH_SHORT).show();
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
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Function to Animate and Expand the CardView containing 'Parking Information'
    private void expandCard() {
        int v = (parkingInfo.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

        TransitionManager.beginDelayedTransition(parkingInfoLinearLayout , new AutoTransition());

        parkingInfo.setVisibility(v);
    }

    // Function to replace Fragments on the Frame-Layout
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout , fragment);
        fragmentTransaction.commit();
    }

    // Function to check if Permission is enabled
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(getContext(),permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}