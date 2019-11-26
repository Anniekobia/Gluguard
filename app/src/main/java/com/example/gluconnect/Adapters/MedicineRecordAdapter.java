package com.example.gluconnect.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gluconnect.Models.Exercise;
import com.example.gluconnect.Models.Medication;
import com.example.gluconnect.R;

import java.util.List;

public class MedicineRecordAdapter extends RecyclerView.Adapter<MedicineRecordAdapter.MyViewHolder> {

    private List<Medication> medications;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, unit;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.medName);
            unit = view.findViewById(R.id.medUnits);
        }
    }


    public MedicineRecordAdapter(List<Medication> medications) {
        this.medications = medications;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medications_record_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Medication medication = medications.get(position);
        holder.name.setText(medication.getName());
        holder.unit.setText(medication.getUnits().toString());

    }

    @Override
    public int getItemCount() {
        return medications.size();
    }
}