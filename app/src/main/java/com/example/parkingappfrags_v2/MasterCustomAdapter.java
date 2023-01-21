package com.example.parkingappfrags_v2;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MasterCustomAdapter extends RecyclerView.Adapter<MasterCustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList master_id, master_name, master_ph_no, master_vehc_no, master_lot_no, master_start_time, master_leave_time;

    private int lastPosition = -1;

    CardView masterRowCardView;
    LinearLayout masterRowLinearLayout;

    MasterCustomAdapter(Context context,
                        ArrayList master_cus_id, ArrayList master_cus_name, ArrayList master_cus_ph_no, ArrayList master_cus_vehc_no, ArrayList master_lot_no, ArrayList master_start_time, ArrayList master_leave_time) {

        this.context = context;
        this.master_id = master_cus_id;
        this.master_name = master_cus_name;
        this.master_ph_no = master_cus_ph_no;
        this.master_vehc_no = master_cus_vehc_no;
        this.master_lot_no = master_lot_no;
        this.master_start_time = master_start_time;
        this.master_leave_time = master_leave_time;

    }

    public  void setFilteredList(Context context, ArrayList<String> filtered_master_id, ArrayList<String> filtered_master_name, ArrayList<String> filtered_master_ph_no, ArrayList<String> filtered_master_vehc_no, ArrayList<String> filtered_master_lot_no, ArrayList<String> filtered_master_start_time, ArrayList<String> filtered_master_leave_time){

        this.context = context;
        this.master_id = filtered_master_id;
        this.master_name = filtered_master_name;
        this.master_ph_no = filtered_master_ph_no;
        this.master_vehc_no = filtered_master_vehc_no;
        this.master_lot_no = filtered_master_lot_no;
        this.master_start_time = filtered_master_start_time;
        this.master_leave_time = filtered_master_leave_time;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.master_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        setAnimation(holder.itemView, position);

        holder.master_id_txt.setText(String.valueOf(master_id.get(position)));
        holder.master_name_txt.setText(String.valueOf(master_name.get(position)));
        holder.master_ph_no_txt.setText(String.valueOf(master_ph_no.get(position)));
        holder.master_vehc_no_txt.setText(String.valueOf(master_vehc_no.get(position)));
        holder.master_lot_no_txt.setText(String.valueOf(master_lot_no.get(position)));
        holder.master_start_time.setText(String.valueOf(master_start_time.get(position)));
        holder.master_leave_time.setText(String.valueOf(master_leave_time.get(position)));

        holder.masterRowCardViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandCard(holder.masterRowLinearLayoutClick , holder.masterRowCardViewClick);
            }
        });


    }

    @Override
    public int getItemCount() {
        return master_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView master_id_txt, master_name_txt, master_ph_no_txt, master_vehc_no_txt, master_lot_no_txt, master_start_time, master_leave_time;

        CardView masterRowCardViewClick;
        LinearLayout masterRowLinearLayoutClick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            master_id_txt = itemView.findViewById(R.id.master_id_txt);
            master_name_txt = itemView.findViewById(R.id.master_name_txt);
            master_ph_no_txt = itemView.findViewById(R.id.master_ph_no_txt);
            master_vehc_no_txt = itemView.findViewById(R.id.master_vehc_no_txt);
            master_lot_no_txt = itemView.findViewById(R.id.master_lot_no_txt);
            master_start_time = itemView.findViewById(R.id.master_start_time_txt);
            master_leave_time = itemView.findViewById(R.id.master_leave_time_txt);

            // Setting up Expandable Card View for - Customer-Records (Master Table)
            masterRowCardViewClick = itemView.findViewById(R.id.master_row_cardView);
            masterRowLinearLayoutClick = itemView.findViewById(R.id.master_row_expandable_linearLayout);
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
    private void expandCard(LinearLayout masterRowLinearLayoutClick , CardView masterRowCardViewClick) {
        int v = (masterRowLinearLayoutClick.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

        TransitionManager.beginDelayedTransition(masterRowCardViewClick , new AutoTransition());

        masterRowLinearLayoutClick.setVisibility(v);
    }
}
