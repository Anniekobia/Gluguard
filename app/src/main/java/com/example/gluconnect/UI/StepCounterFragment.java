package com.example.gluconnect.UI;


import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.gluconnect.Models.DailyStep;
import com.example.gluconnect.Models.DailyStepPOST;
import com.example.gluconnect.Models.DailyStepsResponse;
import com.example.gluconnect.Models.ExerciseData;
import com.example.gluconnect.Models.ExerciseDetails;
import com.example.gluconnect.Models.ExerciseDetailsList;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.example.gluconnect.Utils.NutritionixAPI;
import com.example.gluconnect.Utils.NutritionixAPIRetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.SENSOR_SERVICE;
import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepCounterFragment extends Fragment implements SensorEventListener {
    private View myview;
    private LaravelAPI laravelAPI;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int userID;
    private boolean running = false;
    private SensorManager sensorManager = null;
    private TextView stepsValue,caloriesMetrics,caloriesBurnt;
    private  NutritionixAPI nutritionixAPI;
    public StepCounterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_step_counter, container, false);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        Retrofit retrofit1 = NutritionixAPIRetrofitClient.getRetrofitClient();
        nutritionixAPI = retrofit1.create(NutritionixAPI.class);
        laravelAPI = retrofit.create(LaravelAPI.class);
        sharedPreferences= getContext().getSharedPreferences("MyPreferences", 0);
        editor = sharedPreferences.edit();

        progressBar = myview.findViewById(R.id.progressBar);
        userID = sharedPreferences.getInt("UserID",6);
        caloriesBurnt = myview.findViewById(R.id.caloriesBurnt);
        caloriesMetrics = myview.findViewById(R.id.caloriesMetric);

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        stepsValue = myview.findViewById(R.id.stepsValue);
        return myview;
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;

        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor==null){
            Toast.makeText(getContext(), "No Step Counter Sensor !", Toast.LENGTH_SHORT).show();
        }else {
            sensorManager.registerListener(this,stepSensor,sensorManager.SENSOR_DELAY_UI);
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        running = false;
//
//        sensorManager.unregisterListener(this);
//    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running){
            int steps = Integer.valueOf((int) sensorEvent.values[0]);
            stepsValue.setText(String.valueOf(steps));
            String query = "walked"+"\t"+ steps +"\t steps";
            checkSteps(query,steps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private void getExerciseDetails(String query,int steps,boolean saved,int recordId) {
        Log.e("Exe query", query);
        progressBar.setVisibility(View.VISIBLE);
        Call<ExerciseDetailsList> exerciseDetailsListCall = nutritionixAPI.getExerciseDetailsList(new ExerciseData(21, "female", 157.0, query, 44.5));
        exerciseDetailsListCall.enqueue(new Callback<ExerciseDetailsList>() {
            @Override
            public void onResponse(Call<ExerciseDetailsList> call, Response<ExerciseDetailsList> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("Exe Unsuccessful", "Code: " + response.code() + "\n" + "Message" + response.message());
                    return;
                } else {
                    ExerciseDetailsList exerciseDetailsList = response.body();
                    for (ExerciseDetails exercise : exerciseDetailsList.getExercises()) {
                        progressBar.setVisibility(GONE);
                        caloriesBurnt.setText(exercise.getNfCalories().toString());
                        caloriesBurnt.setVisibility(View.VISIBLE);
                        caloriesMetrics.setVisibility(View.VISIBLE);
                        DailyStep dailyStep = new DailyStep(exercise.getNfCalories().floatValue(),steps,Long.valueOf(userID));
                        DailyStepPOST dailyStepPOST = new DailyStepPOST(exercise.getNfCalories().floatValue(),Long.valueOf(recordId),steps);
                        if (saved){
                            updateDaySteps(dailyStepPOST);
                        }else {
                            saveDaySteps(dailyStep);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ExerciseDetailsList> call, Throwable t) {
                Log.e("Exe Failed", t.getMessage());
                progressBar.setVisibility(GONE);
            }
        });
    }

    private void checkSteps(String query,int steps) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd");
        String datePicked = f.format(cal.getTime());
        Log.e("Date Formatted", datePicked);
        Call<DailyStepsResponse> dailyStepsResponseCall = laravelAPI.getDailySteps();
        dailyStepsResponseCall.enqueue(new Callback<DailyStepsResponse>() {
            @Override
            public void onResponse(Call<DailyStepsResponse> call, Response<DailyStepsResponse> response) {
                ArrayList<DailyStep> record = new ArrayList<>();
                ArrayList<DailyStep> pastRecords = new ArrayList<>();
                DailyStepsResponse dailyStepsResponse = response.body();
                if (response.body() == null ){

                }
                else {
                    for (DailyStep dailyStep : dailyStepsResponse.getDailySteps()) {
                        String day = dailyStep.getDay();
                        if (datePicked.equals(day) && userID == dailyStep.getUserId().intValue()) {
                            record.add(dailyStep);
                        } else {
                            pastRecords.add(dailyStep);
                        }
                    }
                }
                if (record.isEmpty()){
                    DailyStep dailyStep = pastRecords.get(pastRecords.size() - 1);
                    int daySteps = steps-dailyStep.getStepCount();
                    boolean saved = false;
                    getExerciseDetails(query,daySteps,saved,0);
                }else {
                    boolean saved = true;
                    DailyStep dailyStep = record.get(0);
                    int recordId  = dailyStep.getId().intValue();
                    getExerciseDetails(query,steps,saved,recordId);
                }

            }
            @Override
            public void onFailure(Call<DailyStepsResponse> call, Throwable t) {
                Log.e("Get BG",  t.getMessage());
            }
        });
    }

    public void saveDaySteps(DailyStep dailyStep){
        Call<DailyStep> dailyStepCall = laravelAPI.recordDailySteps(dailyStep);
        dailyStepCall.enqueue(new Callback<DailyStep>() {
            @Override
            public void onResponse(Call<DailyStep> call, Response<DailyStep> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("SAVESTEPS Unsuccessful", "Code: " + response.code() + "\n" + "Message" + response.message());
                    return;
                } else {
                    Log.e("SAVESTEPS succesful", "Saved");
                }
            }

            @Override
            public void onFailure(Call<DailyStep> call, Throwable t) {
                Log.e("SAVESTEPS Failed", t.getMessage());
                progressBar.setVisibility(GONE);
            }
        });
    }

    public void updateDaySteps(DailyStepPOST dailyStepPOST){
        Call<DailyStep> dailyStepCall = laravelAPI.updatedDailySteps(dailyStepPOST);
        dailyStepCall.enqueue(new Callback<DailyStep>() {
            @Override
            public void onResponse(Call<DailyStep> call, Response<DailyStep> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("UPDATESTEPS Unsuccessful", "Code: " + response.code() + "\n" + "Message" + response.message());
                    return;
                } else {
                    Log.e("UPDATESTEPS succesful", "Updated");
                }
            }

            @Override
            public void onFailure(Call<DailyStep> call, Throwable t) {
                Log.e("UPDATESTEPS Failed", t.getMessage());
                progressBar.setVisibility(GONE);
            }
        });
    }

}
