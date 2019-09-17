package com.example.gluconnect.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import com.example.gluconnect.Models.ExerciseData;
import com.example.gluconnect.Models.ExerciseDetails;
import com.example.gluconnect.Models.ExerciseDetailsList;
import com.example.gluconnect.Utils.NutritionixAPI;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DailyLogsActivity extends AppCompatActivity {

    private TextView exerciseTextView;
    private NutritionixAPI nutritionixAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_logs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        exerciseTextView = findViewById(R.id.post_requesttest_txtview);

        Retrofit retrofit = RetrofitClient.getRetrofitClient();

        nutritionixAPI = retrofit.create(NutritionixAPI.class);
        getExerciseDetails();
    }

    private void getExerciseDetails() {
        Call<ExerciseDetailsList> exerciseDetailsListCall = nutritionixAPI.getExerciseDetailsList(new ExerciseData(21, "female", 157.0, "running 6 km 50 mins", 44.5));

        exerciseDetailsListCall.enqueue(new Callback<ExerciseDetailsList>() {
            @Override
            public void onResponse(Call<ExerciseDetailsList> call, Response<ExerciseDetailsList> response) {
                if (!response.isSuccessful()) {
                    exerciseTextView.setText("Code: " + response.code() + "\n" + "Message" + response.message());
                    return;
                } else {
                    ExerciseDetailsList exerciseDetailsList = response.body();
                    for (ExerciseDetails exercise : exerciseDetailsList.getExercises()) {
                        exerciseTextView.setText(exercise.getName() + exercise.getNfCalories());
                    }
                }
            }

            @Override
            public void onFailure(Call<ExerciseDetailsList> call, Throwable t) {
                exerciseTextView.setText(t.getMessage());
            }
        });
    }


}
