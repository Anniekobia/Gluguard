package com.example.gluconnect.UI;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gluconnect.Adapters.BloodGlucoseRecordAdapter;
import com.example.gluconnect.Adapters.ExerciseRecordAdapter;
import com.example.gluconnect.Adapters.FoodAutoSuggestAdapter;
import com.example.gluconnect.Adapters.MealRecordAdapter;
import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BloodGlucoseResponse;
import com.example.gluconnect.Models.BrandedFoodItemSuggestions;
import com.example.gluconnect.Models.Exercise;
import com.example.gluconnect.Models.ExerciseResponse;
import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodItemSuggestionsList;
import com.example.gluconnect.Models.Meal;
import com.example.gluconnect.Models.MealResponse;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.example.gluconnect.Utils.NutritionixAPI;
import com.example.gluconnect.Utils.NutritionixAPIRetrofitClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {

    private View myView;
    private LaravelAPI laravelAPI;
    private CalendarView calendarView;
    private TextView datetxtview;

    private CardView cardView;
    RecyclerView meals_rv, exercise_rv, blood_glucose_rv;
    private List<BloodGlucose> bloodGlucoseList = new ArrayList<>();
    private BloodGlucoseRecordAdapter bloodGlucoseRecordAdapter;
    private List<Meal> mealList = new ArrayList<>();
    private MealRecordAdapter mealRecordAdapter;
    private List<Exercise> exerciseList = new ArrayList<>();
    private ExerciseRecordAdapter exerciseRecordAdapter;
    private TextView bg,bgmsg,meal,mealsmsg,exe,exemsg,duration_metric,distance_metric,dailyCalories,dayCalories,dayCaloriesMetic;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private Float initialDayCalories;
    Integer userID;


    public DiaryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_diary, container, false);

        Retrofit retrofitl = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofitl.create(LaravelAPI.class);
        sharedPreferences= getContext().getSharedPreferences("MyPreferences", 0);

        calendarView = myView.findViewById(R.id.calenderview);
        datetxtview = myView.findViewById(R.id.dateView);

        blood_glucose_rv =  myView.findViewById(R.id.blood_glucose_recycler_view);
        bloodGlucoseRecordAdapter = new BloodGlucoseRecordAdapter(bloodGlucoseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        blood_glucose_rv.setLayoutManager(mLayoutManager);
        blood_glucose_rv.setItemAnimator(new DefaultItemAnimator());
        blood_glucose_rv.setAdapter(bloodGlucoseRecordAdapter);
        bgmsg = myView.findViewById(R.id.bloodGlucosemsg);
        bg = myView.findViewById(R.id.bloodGlucose);
        distance_metric = myView.findViewById(R.id.distance_metrics);
        duration_metric = myView.findViewById(R.id.duration_metrics);

        meals_rv =  myView.findViewById(R.id.meals_recycler_view);
        mealRecordAdapter = new MealRecordAdapter(mealList);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext());
        meals_rv.setLayoutManager(mLayoutManager1);
        meals_rv.setItemAnimator(new DefaultItemAnimator());
        meals_rv.setAdapter(mealRecordAdapter);
        mealsmsg = myView.findViewById(R.id.mealsmsg);
        meal = myView.findViewById(R.id.meals);

        exercise_rv =  myView.findViewById(R.id.exercises_recycler_view);
        exerciseRecordAdapter = new ExerciseRecordAdapter(exerciseList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext());
        exercise_rv.setLayoutManager(mLayoutManager2);
        exercise_rv.setItemAnimator(new DefaultItemAnimator());
        exercise_rv.setAdapter(exerciseRecordAdapter);
        exemsg = myView.findViewById(R.id.exercisemsg);
        exe = myView.findViewById(R.id.exercises);
        dailyCalories =myView.findViewById(R.id.daily_calorie_txt);
        progressBar = myView.findViewById(R.id.progressBar);
        dayCalories =myView.findViewById(R.id.dayCaloriesValue);
        dayCaloriesMetic = myView.findViewById(R.id.dayCaloriesmetric);
        initialDayCalories = 0f;
        userID  = sharedPreferences.getInt("UserID", 1);

        setCurrentDate();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar date = new GregorianCalendar(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d");
                String newDate = sdf.format(date.getTime());
                datetxtview.setText(newDate);
                Log.e("New Date", newDate);

                bloodGlucoseList.clear();
                bgmsg.setVisibility(View.GONE);
                bloodGlucoseRecordAdapter.notifyDataSetChanged();
                mealList.clear();
                mealsmsg.setVisibility(View.GONE);
                mealRecordAdapter.notifyDataSetChanged();
                exerciseList.clear();
                exemsg.setVisibility(View.GONE);
                exerciseRecordAdapter.notifyDataSetChanged();
                dayCalories.setVisibility(View.GONE);
                dayCaloriesMetic.setVisibility(View.GONE);
                initialDayCalories = 0f;
                SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd");
                String datePicked = f.format(date.getTime());
                Log.e("Date Formatted 2", datePicked);
                getBloodGlucoseLevels(datePicked);
            }
        });
        Long calories = sharedPreferences.getLong("Daily Calorie Requirement", 0L);
        dailyCalories.setText(calories.toString());

        return myView;
    }

    public void setCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d");
        String date = sdf.format(cal.getTime());
        Log.e("Date ", date);
        datetxtview.setText(date);

        SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd");
        String datePicked = f.format(cal.getTime());
        Log.e("Date Formatted", datePicked);
        getBloodGlucoseLevels(datePicked);
//        getExerciseRecords(datePicked);
//        getMealRecords(datePicked);

        dayCalories.setVisibility(View.GONE);
        dayCaloriesMetic.setVisibility(View.GONE);
        initialDayCalories = 0f;
    }

    private void getBloodGlucoseLevels(final String selectedDay) {
        progressBar.setVisibility(View.VISIBLE);
        Call<BloodGlucoseResponse> bloodGlucoseResponseCall = laravelAPI.getBloodGlucoseLevel();
        bloodGlucoseResponseCall.enqueue(new Callback<BloodGlucoseResponse>() {
            @Override
            public void onResponse(Call<BloodGlucoseResponse> call, Response<BloodGlucoseResponse> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("BG", "BG unsuccessful");
                    return;
                } else {
//                    progressBar.setVisibility(View.GONE);
                    BloodGlucoseResponse bloodGlucoseResponse = response.body();
                    for (BloodGlucose bloodGlucose : bloodGlucoseResponse.getBloodGlucoseRecords()) {
                        String day = bloodGlucose.getDay();
                        Log.e("BGw", bloodGlucose.toString());
                        if (selectedDay.equals(day)&&userID==bloodGlucose.getUserId().intValue()){
                            bloodGlucoseList.add(bloodGlucose);
                        }
                    }
                    if (bloodGlucoseList.isEmpty()){
                        bgmsg.setVisibility(View.VISIBLE);
                        bg.setVisibility(View.VISIBLE);
                    }else {
                        bg.setVisibility(View.VISIBLE);
                        Log.e("Check",bloodGlucoseList.toString());
                        bloodGlucoseRecordAdapter.notifyDataSetChanged();
                    }
                    getMealRecords(selectedDay);
                }
            }

            @Override
            public void onFailure(Call<BloodGlucoseResponse> call, Throwable t) {
                Log.e("Get BG", t.getMessage());
            }
        });
    }

    private void getMealRecords(final String selectedDay) {
//        progressBar.setVisibility(View.VISIBLE);
        Call<MealResponse> mealResponseCall = laravelAPI.getMeals();
        mealResponseCall.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("BG", "BG unsuccessful");
                    return;
                } else {
                    MealResponse mealResponse = response.body();
                    for (Meal meal : mealResponse.getMeals()) {
//                        progressBar.setVisibility(View.GONE);
                        String day = meal.getDay();
                        Log.e("BGw", meal.toString());
                        if (selectedDay.equals(day)&&userID==meal.getUserId().intValue()){
                            mealList.add(meal);
                            Float mealCals = meal.getCalories();
                            initialDayCalories = initialDayCalories + mealCals;
                        }
                    }
                    if (mealList.isEmpty()){
                        mealsmsg.setVisibility(View.VISIBLE);
                        meal.setVisibility(View.VISIBLE);

                    }else {
                        meal.setVisibility(View.VISIBLE);
                        Log.e("Check", mealList.toString());
                        mealRecordAdapter.notifyDataSetChanged();
                        dayCalories.setVisibility(View.VISIBLE);
                        dayCaloriesMetic.setVisibility(View.VISIBLE);
                        dayCalories.setText(initialDayCalories.toString());
                    }
                    getExerciseRecords(selectedDay);
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.e("Get BG", t.getMessage());
            }
        });
    }

    public void getExerciseRecords(final String selectedDay){
//        progressBar.setVisibility(View.VISIBLE);
        Call<ExerciseResponse> exerciseCall = laravelAPI.getExercises();
        exerciseCall.enqueue(new Callback<ExerciseResponse>() {
            @Override
            public void onResponse(Call<ExerciseResponse> call, Response<ExerciseResponse> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("BG", "BG unsuccessful");
                    return;
                } else {
                    progressBar.setVisibility(View.GONE);
                    ExerciseResponse exerciseResponse = response.body();
                    for (Exercise exercise : exerciseResponse.getExercises()) {
                        String day = exercise.getmDay();
                        Log.e("BGw", exercise.toString());
                        if (selectedDay.equals(day)&&userID==exercise.getUserId().intValue()) {
                            exerciseList.add(exercise);
                            Float exeCals = exercise.getCaloriesBurnt().floatValue();
                            initialDayCalories = initialDayCalories - exeCals;
                        }
                    }
                    if (exerciseList.isEmpty()){
                        exemsg.setVisibility(View.VISIBLE);
                        exe.setVisibility(View.VISIBLE);
                    }else {
                        exe.setVisibility(View.VISIBLE);
                        Log.e("Check", exerciseList.toString());
                        exerciseRecordAdapter.notifyDataSetChanged();
                        dayCalories.setVisibility(View.VISIBLE);
                        dayCaloriesMetic.setVisibility(View.VISIBLE);
                        dayCalories.setText(initialDayCalories.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<ExerciseResponse> call, Throwable t) {
                Log.e("Get BG", t.getMessage());
            }
        });
    }

}
