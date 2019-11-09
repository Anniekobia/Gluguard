package com.example.gluconnect.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.FoodRecommendation;
import com.example.gluconnect.R;

import java.util.List;

public class BloodGlucoseRecordAdapter extends RecyclerView.Adapter<BloodGlucoseRecordAdapter.MyViewHolder> {

    private List<BloodGlucose> bloodGlucoseList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bgValue, bgtime;

        public MyViewHolder(View view) {
            super(view);
            bgtime = (TextView) view.findViewById(R.id.bgtime);
            bgValue = (TextView) view.findViewById(R.id.bgvalue);
        }
    }


    public BloodGlucoseRecordAdapter(List<BloodGlucose> bloodGlucoseList) {
        this.bloodGlucoseList = bloodGlucoseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_recommendation_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BloodGlucose bloodGlucose = bloodGlucoseList.get(position);
        holder.bgValue.setText(bloodGlucose.getBloodGlucoseValue().toString());
        holder.bgtime.setText(bloodGlucose.getBloodGlucoseValue().toString());
    }

    @Override
    public int getItemCount() {
        return bloodGlucoseList.size();
    }
}