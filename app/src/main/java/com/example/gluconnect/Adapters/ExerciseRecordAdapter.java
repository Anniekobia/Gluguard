package com.example.gluconnect.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gluconnect.Models.Exercise;
import com.example.gluconnect.R;

import java.util.List;

public class ExerciseRecordAdapter extends RecyclerView.Adapter<ExerciseRecordAdapter.MyViewHolder> {

    private List<Exercise> exercises;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView exerciseName, duration,distance;

        public MyViewHolder(View view) {
            super(view);
            duration =  view.findViewById(R.id.duration);
            distance = view.findViewById(R.id.distance);
            exerciseName = view.findViewById(R.id.exercisename);
        }
    }


    public ExerciseRecordAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_record_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.exerciseName.setText(exercise.getExerciseName());
        holder.distance.setText(exercise.getmDistance().toString());
//        Integer duration = Integer.parseInt(exercise.getDuration().toString());
        holder.duration.setText(exercise.getDuration().toString());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}