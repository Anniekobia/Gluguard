package com.example.gluconnect.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gluconnect.Models.FoodRecommendation;
import com.example.gluconnect.R;

import java.util.List;

public class FoodRecommendationsAdapter extends RecyclerView.Adapter<FoodRecommendationsAdapter.MyViewHolder> {

    private List<FoodRecommendation> foodsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, serving_size, category,metric;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.food_name);
            category = (TextView) view.findViewById(R.id.food_category);
            serving_size = (TextView) view.findViewById(R.id.serving_size);
            metric = view.findViewById(R.id.metric);
        }
    }


    public FoodRecommendationsAdapter(List<FoodRecommendation> foodsList) {
        this.foodsList = foodsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_recommendation_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FoodRecommendation foodRecommendation = foodsList.get(position);
        if(foodRecommendation.getFoodCategory().equals("BEVERAGES")||foodRecommendation.getFoodName().equals("Milk, full fat")||foodRecommendation.getFoodName().equals("Milk, skim")){
            holder.metric.setText("ml");
        }else {
            holder.metric.setText("g");
        }
        holder.name.setText(foodRecommendation.getFoodName());
        holder.category.setText(foodRecommendation.getFoodCategory());
        holder.serving_size.setText(foodRecommendation.getServingSize().toString());
    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }
}