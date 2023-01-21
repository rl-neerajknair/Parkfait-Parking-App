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


public class SearchMasterDbFragment extends Fragment {

    // Declarations
    RecyclerView masterRecyclerView;
    MyDatabaseHelper myDB;
    private SearchView masterSearchView;

    // ArrayLists of fields to be used before searching and after searching
    public ArrayList<String> master_id, master_name, master_ph_no, master_vehc_no, master_lot_no, master_start_time, master_leave_time;
    public ArrayList<String> filtered_master_id, filtered_master_name, filtered_master_ph_no, filtered_master_vehc_no, filtered_master_lot_no, filtered_master_start_time, filtered_master_leave_time;

    MasterCustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_master_db, container, false);

        myDB = MyDatabaseHelper.getInstance(getContext());

        // Setting up RecyclerView
        masterRecyclerView = view.findViewById(R.id.master_recyclerview);
        master_id = new ArrayList<>();
        master_name = new ArrayList<>();
        master_ph_no = new ArrayList<>();
        master_vehc_no = new ArrayList<>();
        master_lot_no = new ArrayList<>();
        master_start_time = new ArrayList<>();
        master_leave_time = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new MasterCustomAdapter(getActivity(), master_id, master_name, master_ph_no, master_vehc_no, master_lot_no, master_start_time, master_leave_time);
        masterRecyclerView.setAdapter(customAdapter);
        masterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Search Set-Up
        filtered_master_id = new ArrayList<>();
        filtered_master_name = new ArrayList<>();
        filtered_master_ph_no = new ArrayList<>();
        filtered_master_vehc_no = new ArrayList<>();
        filtered_master_lot_no = new ArrayList<>();
        filtered_master_start_time = new ArrayList<>();
        filtered_master_leave_time = new ArrayList<>();

        masterSearchView = view.findViewById(R.id.master_searchView);
        masterSearchView.clearFocus();
        masterSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    // Storing to Arraylists after searching
    private void filterList(String text) {
        Cursor cursor = myDB.readAllData_Master();

        filtered_master_id.clear();
        filtered_master_name.clear();
        filtered_master_ph_no.clear();
        filtered_master_vehc_no.clear();
        filtered_master_lot_no.clear();
        filtered_master_start_time.clear();
        filtered_master_leave_time.clear();

        if (cursor.getCount() == 0){
            //Toast.makeText(, "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                if (cursor.getString(1).toLowerCase().contains(text.toLowerCase())) {
                    filtered_master_id.add(cursor.getString(0));
                    filtered_master_name.add(cursor.getString(1));
                    filtered_master_ph_no.add(cursor.getString(2));
                    filtered_master_vehc_no.add(cursor.getString(3));
                    filtered_master_lot_no.add(cursor.getString(4));
                    filtered_master_start_time.add(cursor.getString(5));
                    filtered_master_leave_time.add(cursor.getString(6));
                }
            }

            if (filtered_master_name.isEmpty()){
                Toast.makeText(getActivity(), "No Data Found.", Toast.LENGTH_SHORT).show();
            }
            else {
                customAdapter.setFilteredList(getActivity(),filtered_master_id, filtered_master_name, filtered_master_ph_no, filtered_master_vehc_no, filtered_master_lot_no, filtered_master_start_time, filtered_master_leave_time);
            }
        }
    }

    // Storing to Arraylists to show before searching
    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData_Master();
        if (cursor.getCount() == 0){
            //Toast.makeText(, "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                master_id.add(cursor.getString(0));
                master_name.add(cursor.getString(1));
                master_ph_no.add(cursor.getString(2));
                master_vehc_no.add(cursor.getString(3));
                master_lot_no.add(cursor.getString(4));
                master_start_time.add(cursor.getString(5));
                master_leave_time.add(cursor.getString(6));
            }
        }
    }
}