package com.example.gluconnect.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gluconnect.Models.Meal;
import com.example.gluconnect.R;

import java.util.List;

public class MealRecordAdapter extends RecyclerView.Adapter<MealRecordAdapter.MyViewHolder> {

    private List<Meal> mealList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mealname, mealtime;

        public MyViewHolder(View view) {
            super(view);
            mealname =  view.findViewById(R.id.mealname);
            mealtime = view.findViewById(R.id.mealtime);
        }
    }


    public MealRecordAdapter(List<Meal> meals) {
        this.mealList = meals;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meals_record_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealtime.setText(meal.getMealTime());
        holder.mealname.setText(meal.getMealName());
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}