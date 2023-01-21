package com.example.parkingappfrags_v2;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    public ArrayList<String> parking_cus_id, parking_cus_name, parking_cus_ph_no, parking_cus_vehc_no, parking_cus_start_time, parking_lot_no;
    public ArrayList<String> parking_filtered_id, parking_filtered_name, parking_filtered_ph_no, parking_filtered_vehc_no, parking_filtered_start_time, parking_filtered_lot_no;
    RecyclerView parkingRecyclerView;
    private SearchView parkingSearchView;
    MyDatabaseHelper myDB;
    ParkingCustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        myDB = MyDatabaseHelper.getInstance(getContext());

        // Setting up RecyclerView
        parkingRecyclerView = view.findViewById(R.id.parking_recyclerview);
        parking_cus_id = new ArrayList<>();
        parking_cus_name = new ArrayList<>();
        parking_cus_ph_no = new ArrayList<>();
        parking_cus_vehc_no = new ArrayList<>();
        parking_cus_start_time = new ArrayList<>();
        parking_lot_no = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new ParkingCustomAdapter(getActivity(),parking_cus_id, parking_cus_name, parking_cus_ph_no, parking_cus_vehc_no, parking_cus_start_time, parking_lot_no);
        parkingRecyclerView.setAdapter(customAdapter);
        parkingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        parking_filtered_id = new ArrayList<>();
        parking_filtered_name = new ArrayList<>();
        parking_filtered_ph_no = new ArrayList<>();
        parking_filtered_vehc_no = new ArrayList<>();
        parking_filtered_start_time = new ArrayList<>();
        parking_filtered_lot_no = new ArrayList<>();

        parkingSearchView = view.findViewById(R.id.parking_searchView);
        parkingSearchView.clearFocus();
        parkingSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


        return view;
    }

    private void filterList(String text) {
        Cursor cursor = myDB.readAllData_Parking();

        parking_filtered_id.clear();
        parking_filtered_name.clear();
        parking_filtered_ph_no.clear();
        parking_filtered_vehc_no.clear();
        parking_filtered_start_time.clear();
        parking_filtered_lot_no.clear();

        if (cursor.getCount() == 0){
            //Toast.makeText(, "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                if (cursor.getString(1).toLowerCase().contains(text.toLowerCase())) {
                    parking_filtered_id.add(cursor.getString(0));
                    parking_filtered_name.add(cursor.getString(1));
                    parking_filtered_ph_no.add(cursor.getString(2));
                    parking_filtered_vehc_no.add(cursor.getString(3));
                    parking_filtered_lot_no.add(cursor.getString(4));
                    parking_filtered_start_time.add(cursor.getString(5));
                }
            }

            if (parking_filtered_name.isEmpty()){
                Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_SHORT).show();
            }
            else {
                customAdapter.setFilteredList(getActivity(),parking_filtered_id, parking_filtered_name, parking_filtered_ph_no, parking_filtered_vehc_no, parking_filtered_start_time, parking_filtered_lot_no);
            }
        }
    }

    private void storeDataInArrays() {
        Cursor cursor = myDB.readAllData_Parking();
        if (cursor.getCount() == 0){
            //Toast.makeText(, "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                parking_cus_id.add(cursor.getString(0));
                parking_cus_name.add(cursor.getString(1));
                parking_cus_ph_no.add(cursor.getString(2));
                parking_cus_vehc_no.add(cursor.getString(3));
                parking_lot_no.add(cursor.getString(4));
                parking_cus_start_time.add(cursor.getString(5));
            }
        }
    }
}