package com.example.parkingappfrags_v2;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class GridFragment extends Fragment {

    //Declaration
    Integer grid_numberoflots = 0;
    Integer grid_rows, grid_cols;

    MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(getContext());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        CardView cardViewGrid = view.findViewById(R.id.cardView_grid);
        LinearLayout parentLinearLayout = view.findViewById(R.id.grid_parent_linearLayout);

        grid_rows = myDB.retrieveDimensions_rows(); // retrieving the number of rows from the last record in the dimensions table
        grid_cols = myDB.retrieveDimensions_cols(); // retrieving the number of columns from the last record in the dimensions table
        grid_numberoflots = myDB.retrieveDimensions_numberoflots();

        Integer count = 1;
        int i,j;
        for (i=1 ; i<=grid_cols && count<=grid_numberoflots ; i++)
        {
            // Generating rows
            LinearLayout row = new LinearLayout(getActivity());
            row.setOrientation(LinearLayout.HORIZONTAL);

            // Setting the width and height of each row to 'FILL' its container by using LayoutParams
            int width_row = LinearLayout.LayoutParams.MATCH_PARENT;
            int height_row = LinearLayout.LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams lp_row = new LinearLayout.LayoutParams(width_row,height_row);
            lp_row.weight = 1.0f;
            lp_row.gravity = Gravity.FILL;
            lp_row.bottomMargin = 1;
            lp_row.topMargin = 1;
            row.setLayoutParams(lp_row);

            // Adding buttons to rows
            for (j=1 ; j<=grid_cols && count<=grid_numberoflots ; j++)
            {
                Button button = new Button(getActivity());
                int id=count;

                // Setting the width, height and other properties of each button to 'FILL' its container by using LayoutParams
                int width_btn = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_btn = LinearLayout.LayoutParams.MATCH_PARENT;
                LinearLayout.LayoutParams lp_btn = new LinearLayout.LayoutParams(width_btn,height_btn);
                lp_btn.weight = 1.0f;
                lp_btn.gravity = Gravity.FILL;
                lp_btn.leftMargin = 1;
                lp_btn.rightMargin = 1;
                button.setLayoutParams(lp_btn);

                button.setText(""+id);
                button.setId(id);

                int btn_id = Integer.parseInt(String.valueOf(button.getId()));
                String str_btn_id = String.valueOf(btn_id);
                int btn_status = myDB.retrieveFreeLot_status_individual(btn_id);

                // Setting the color for the button
                if (btn_status == 0){
                    button.setBackgroundTintList(getActivity().getColorStateList(R.color.green_bg_tint));
                }
                else {
                    button.setBackgroundTintList(getActivity().getColorStateList(R.color.red_bg_tint));
                }

                // Button functions
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int btn_status = myDB.retrieveFreeLot_status_individual(btn_id);

                        if (btn_status == 0){
                            ((MainActivity)getActivity()).show_add_dialog(str_btn_id);
                        }
                        else {
                            Integer cusID = myDB.retrieveParking_id_individual(btn_id);
                            String str_cusID = String.valueOf(cusID);
                            String cusName = myDB.retrieveParking_name_individual(btn_id);
                            String cusPhno = myDB.retrieveParking_phno_individual(btn_id);
                            String cusVehcno = myDB.retrieveParking_vehcno_individual(btn_id);
                            String cusStartTime = myDB.retrieveParking_starttime_individual(btn_id);

                            Intent intent = new Intent(getActivity() , RecordShowScreen.class);
                            intent.putExtra("cus_id",str_cusID);
                            intent.putExtra("cus_name",cusName);
                            intent.putExtra("cus_ph_no",cusPhno);
                            intent.putExtra("cus_vehc_no",cusVehcno);
                            intent.putExtra("cus_lot_no",str_btn_id);
                            intent.putExtra("cus_start_time",cusStartTime);
                            startActivity(intent);
                        }
                    }
                });

                count++;
                row.addView(button); // Adding buttons to rows
            }

            parentLinearLayout.addView(row); // Adding rows to columns
        }

        return view;
    }
}