package com.example.gluconnect.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.gluconnect.Adapters.FoodAutoSuggestAdapter;
import com.example.gluconnect.Adapters.FoodAutoSuggestAdapterSET;
import com.example.gluconnect.Models.BrandedFoodItemSuggestions;
import com.example.gluconnect.Models.Exercise;
import com.example.gluconnect.Models.ExerciseData;
import com.example.gluconnect.Models.ExerciseDetails;
import com.example.gluconnect.Models.ExerciseDetailsList;
import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodItemSuggestionsList;
import com.example.gluconnect.Models.Meal;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.example.gluconnect.Utils.NutritionixAPI;
import com.example.gluconnect.Utils.NutritionixAPIRetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;


public class ExerciseFragment extends Fragment {

    private View myView;
    private LaravelAPI laravelAPI;
    ProgressBar progressBar;

    private NutritionixAPI nutritionixAPI;

    private Spinner exeSpinner;
    private EditText exercise_distance;
    private EditText exercise_duration;
    private Button exeSaveBtn;
    private TextView exercise_calories_metrics_txtview, caloriesBurntTextview;
    private TextView exercise_calories_txtview;
    private SharedPreferences sharedPreferences;

    RelativeLayout relativeLayout;
    TextView errormsg;

    public ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_exercise, container, false);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        Retrofit retrofit1 = NutritionixAPIRetrofitClient.getRetrofitClient();
        nutritionixAPI = retrofit1.create(NutritionixAPI.class);
        sharedPreferences = getContext().getSharedPreferences("MyPreferences", 0);
        errormsg = myView.findViewById(R.id.error_msg);

        relativeLayout = myView.findViewById(R.id.exercise_rl);

        progressBar = myView.findViewById(R.id.progressBar);
        progressBar.bringToFront();

        exeSpinner = myView.findViewById(R.id.exercises_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.exercises_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exeSpinner.setAdapter(adapter);
        exercise_distance = myView.findViewById(R.id.exercise_distance_edittext);
        exercise_duration = myView.findViewById(R.id.exercise_duration_edittext);
        exeSaveBtn = myView.findViewById(R.id.save_exercise_btn);
        exercise_calories_txtview = myView.findViewById(R.id.exercise_calories);
        exercise_calories_metrics_txtview = myView.findViewById(R.id.exercise_calories_m);
        caloriesBurntTextview = myView.findViewById(R.id.calories_burnt);

        exeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        exercise_duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String exercise = exeSpinner.getSelectedItem().toString();
                String distance = exercise_distance.getText().toString();
                String duration = exercise_duration.getText().toString();
                if (TextUtils.isEmpty(exercise)) {

                } else {
                    if (TextUtils.isEmpty(distance) || TextUtils.isEmpty(duration)) {

                    } else {
                        String query = exercise + "\t" + distance + "km\t" + duration + "mins";
                        getExerciseDetails(query);
                    }
                }
            }
        });
        exercise_distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String exercise = exeSpinner.getSelectedItem().toString();
                String distance = exercise_distance.getText().toString();
                String duration = exercise_duration.getText().toString();
                if (TextUtils.isEmpty(exercise)) {

                } else {
                    if (TextUtils.isEmpty(distance) || TextUtils.isEmpty(duration)) {

                    } else {
                        String query = exercise + "\t" + distance + "km\t" + duration + "mins";
                        getExerciseDetails(query);
                    }

                }
            }
        });
        exercise_calories_txtview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                exeSaveBtn.setVisibility(View.VISIBLE);
                errormsg.setVisibility(GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        exeSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exercise = exeSpinner.getSelectedItem().toString();
                String distance = exercise_distance.getText().toString();
                String duration = exercise_duration.getText().toString();
                String calories = exercise_calories_txtview.getText().toString();
                if (TextUtils.isEmpty(exercise) || TextUtils.isEmpty(calories)) {

                } else {
                    if (TextUtils.isEmpty(distance) && TextUtils.isEmpty(duration)) {
                        distance = "0";
                        duration = "1";
                    }
                    Integer userID = sharedPreferences.getInt("UserID", 1);
                    Exercise newExercise = new Exercise(Double.parseDouble(calories), Double.parseDouble(duration), Double.parseDouble(distance), exercise, userID.longValue());
                    recordExerciseData(newExercise);
                }
            }
        });

        return myView;
    }

    private void getExerciseDetails(String query) {
        Log.e("Exe query", query);
        progressBar.setVisibility(View.VISIBLE);
        Call<ExerciseDetailsList> exerciseDetailsListCall = nutritionixAPI.getExerciseDetailsList(new ExerciseData(21, "female", 157.0, query, 44.5));
        exerciseDetailsListCall.enqueue(new Callback<ExerciseDetailsList>() {
            @Override
            public void onResponse(Call<ExerciseDetailsList> call, Response<ExerciseDetailsList> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("Exe Unsuccessful", "Code: " + response.code() + "\n" + "Message" + response.message());
                    errormsg.setText("Something happened, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    ExerciseDetailsList exerciseDetailsList = response.body();
                    for (ExerciseDetails exercise : exerciseDetailsList.getExercises()) {
                        progressBar.setVisibility(GONE);
                        exercise_calories_txtview.setText(exercise.getNfCalories().toString());
                        exercise_calories_txtview.setVisibility(View.VISIBLE);
                        exercise_calories_metrics_txtview.setVisibility(View.VISIBLE);
                        caloriesBurntTextview.setVisibility(View.VISIBLE);
                        Log.e("Exe Exercise", exercise.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ExerciseDetailsList> call, Throwable t) {
                Log.e("Exe Failed", t.getMessage());
                progressBar.setVisibility(GONE);
                errormsg.setText("Something happened, please try again later");
                errormsg.setVisibility(View.VISIBLE);
            }
        });
    }

    private void recordExerciseData(final Exercise recordedExercise) {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
        }catch (Exception e) {

        }
        Log.e("Exe recorded", recordedExercise.toString());
        progressBar.setVisibility(View.VISIBLE);
        Call<Exercise> exerciseCall = laravelAPI.recordExerciseData(recordedExercise);
        exerciseCall.enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("Exe Saved Unsuccessful", "Code: " + response.code() + "\n" + "Message" + response.message());
                    errormsg.setText("Exercise not saved, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    exercise_distance.getText().clear();
                    exercise_duration.getText().clear();
                    exercise_calories_txtview.setVisibility(View.GONE);
                    exercise_calories_metrics_txtview.setVisibility(GONE);
                    caloriesBurntTextview.setVisibility(GONE);
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    Log.e("Exe  Saved", recordedExercise.toString());
                    exercise_distance.getText().clear();
                    exercise_duration.getText().clear();
                    exercise_calories_txtview.setVisibility(View.GONE);
                    exercise_calories_metrics_txtview.setVisibility(GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Exercise Successfully Saved",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                Log.e("Exe Saved Failed", t.getMessage());
                progressBar.setVisibility(GONE);
                errormsg.setText("Exercise not saved, please try again later");
                errormsg.setVisibility(View.VISIBLE);
                exercise_distance.getText().clear();
                exercise_duration.getText().clear();
                exercise_calories_txtview.setVisibility(View.GONE);
                exercise_calories_metrics_txtview.setVisibility(GONE);
                exeSaveBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
