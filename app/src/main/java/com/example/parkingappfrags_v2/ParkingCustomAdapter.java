package com.example.parkingappfrags_v2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParkingCustomAdapter extends RecyclerView.Adapter<ParkingCustomAdapter.MyViewHolder> {

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private Activity context;
    ArrayList parking_cus_id, parking_cus_name, parking_cus_ph_no, parking_cus_vehc_no, parking_cus_start_time, parking_lot_no;

    TextView cus_id_del, cus_name_del, cus_vehc_no_del, lot_no_del, start_time_del, end_time_del, del_amt;
    int diffH, diffM;

    CardView parkingRowCardView;
    LinearLayout parkingRowLinearLayout;

    private int lastPosition = -1;


    ParkingCustomAdapter(Activity context, ArrayList parking_cus_id, ArrayList parking_cus_name,
                         ArrayList parking_cus_ph_no, ArrayList parking_cus_vehc_no,
                         ArrayList parking_cus_start_time, ArrayList parking_lot_no){
        this.context = context;
        this.parking_cus_id = parking_cus_id;
        this.parking_cus_name = parking_cus_name;
        this.parking_cus_ph_no = parking_cus_ph_no;
        this.parking_cus_vehc_no = parking_cus_vehc_no;
        this.parking_cus_start_time = parking_cus_start_time;
        this.parking_lot_no = parking_lot_no;
    }


    public  void setFilteredList(Activity context, ArrayList<String> filtered_parking_id, ArrayList<String> filtered_parking_name, ArrayList<String> filtered_parking_ph_no, ArrayList<String> filtered_parking_vehc_no, ArrayList<String> filtered_parking_start_time, ArrayList<String> filtered_parking_lot_no){

        this.context = context;
        this.parking_cus_id = filtered_parking_id;
        this.parking_cus_name = filtered_parking_name;
        this.parking_cus_ph_no = filtered_parking_ph_no;
        this.parking_cus_vehc_no = filtered_parking_vehc_no;
        this.parking_cus_start_time = filtered_parking_start_time;
        this.parking_lot_no = filtered_parking_lot_no;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.parking_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();

        setAnimation(holder.itemView, position);
        Animation slideOut = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_out_right);
        //holder.itemView.startAnimation(slideOut);

        holder.parking_cus_id_txt.setText(String.valueOf(parking_cus_id.get(position)));
        holder.parking_cus_name_txt.setText(String.valueOf(parking_cus_name.get(position)));
        holder.parking_cus_ph_no_txt.setText(String.valueOf(parking_cus_ph_no.get(position)));
        holder.parking_cus_vehc_no_txt.setText(String.valueOf(parking_cus_vehc_no.get(position)));
        holder.parking_lot_no_txt.setText(String.valueOf(parking_lot_no.get(position)));
        holder.parking_start_time_txt.setText(String.valueOf(parking_cus_start_time.get(position)));

        holder.parkingRowCardViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandCard(holder.parkingRowLinearLayoutClick , holder.parkingRowCardViewClick);
            }
        });




        String pass_id = String.valueOf(parking_cus_id.get(pos));
        String pass_name = String.valueOf(parking_cus_name.get(pos));
        String pass_ph_no = String.valueOf(parking_cus_ph_no.get(pos));
        String pass_vehc_no = String.valueOf(parking_cus_vehc_no.get(pos));
        String pass_lot_no = String.valueOf(parking_lot_no.get(pos));
        String pass_start_time = String.valueOf(parking_cus_start_time.get(pos));

        holder.button_view_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , RecordShowScreen.class);
                intent.putExtra("cus_id",pass_id);
                intent.putExtra("cus_name",pass_name);
                intent.putExtra("cus_ph_no",pass_ph_no);
                intent.putExtra("cus_vehc_no",pass_vehc_no);
                intent.putExtra("cus_lot_no",pass_lot_no);
                intent.putExtra("cus_start_time",pass_start_time);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return parking_cus_id.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView parking_cus_id_txt, parking_cus_name_txt, parking_cus_ph_no_txt,
                parking_cus_vehc_no_txt, parking_start_time_txt, parking_lot_no_txt;
        Button button_view_row;

        CardView parkingRowCardViewClick;
        LinearLayout parkingRowLinearLayoutClick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //cus_id_del = itemView.findViewById(R.id.customer_id_del);
            parking_cus_id_txt = itemView.findViewById(R.id.cus_id_txt);
            parking_cus_name_txt = itemView.findViewById(R.id.cus_name_txt);
            parking_cus_ph_no_txt = itemView.findViewById(R.id.cus_ph_no_txt);
            parking_cus_vehc_no_txt = itemView.findViewById(R.id.cus_vehc_no_txt);
            parking_start_time_txt = itemView.findViewById(R.id.start_time_txt);
            parking_lot_no_txt = itemView.findViewById(R.id.lot_no_txt);
            button_view_row = (Button) itemView.findViewById(R.id.button_view_row);

            // Setting up Expandable Card View for - Customer-Records (Parking Table)
            parkingRowCardViewClick = itemView.findViewById(R.id.parking_row_cardView);
            parkingRowLinearLayoutClick = itemView.findViewById(R.id.parking_row_expandable_linearLayout);

        }
    }

    private void setAnimation(View view, int position){

        if (position>lastPosition){
            Animation slideIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            view.startAnimation(slideIn);
            lastPosition = position;
        }

    }

    // Function to Animate and Expand the CardView containing 'Customer-Records (Parking Table)'
    private void expandCard(LinearLayout parkingRowLinearLayoutClick , CardView parkingRowCardViewClick) {
        int v = (parkingRowLinearLayoutClick.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

        TransitionManager.beginDelayedTransition(parkingRowCardViewClick , new AutoTransition());

        parkingRowLinearLayoutClick.setVisibility(v);
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(context,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
