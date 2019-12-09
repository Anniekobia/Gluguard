package com.example.gluconnect.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gluconnect.Adapters.FoodAutoSuggestAdapter;
import com.example.gluconnect.Adapters.FoodAutoSuggestAdapterSET;
import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BrandedFoodItemSuggestions;
import com.example.gluconnect.Models.Exercise;
import com.example.gluconnect.Models.ExerciseData;
import com.example.gluconnect.Models.ExerciseDetails;
import com.example.gluconnect.Models.ExerciseDetailsList;
import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodItemSuggestionsList;
import com.example.gluconnect.Models.Meal;
import com.example.gluconnect.Models.Medication;
import com.example.gluconnect.Models.MedicationResponse;
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


public class DailyLogsFragmentLOGIC extends Fragment implements View.OnClickListener {

    private static final String BGERROR = "";
    private View myView;
    private Button saveDailyLogsBtn;
    private LaravelAPI laravelAPI;
    private EditText bloodGlucoseLevelEditText;
    private RadioGroup bglevelRadiogroup;
    ProgressBar progressBar;
    private Button bgNextBtn;
    private Button bgSaveBtn;

    private TextView foodItemSuggestionsTextView;
    private NutritionixAPI nutritionixAPI;
    private FoodAutoSuggestAdapter foodAutoSuggestAdapter;
    private FoodAutoSuggestAdapterSET foodAutoSuggestAdapterSET;
    private AutoCompleteTextView auto_complete_edittext;
    private TextView selected_food_item;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private RadioGroup mealTimeRadiogroup;
    private Button mealSaveBtn;
    private EditText selectedFoodItemQuantity;
    private TextView selectedFoodItemMetrc;
    private TextView selectedFoodItemCalories;
    private ConstraintLayout selectedFoodItemlayout;
    Float originalCalories;

    private Spinner exeSpinner;
    private EditText exercise_distance;
    private EditText exercise_duration;
    private Button exeSaveBtn;
    private Button exeNextBtn;
    private TextView exercise_calories_metrics_txtview;
    private TextView exercise_calories_txtview;
    private SharedPreferences sharedPreferences;

    private Spinner medSpinner;
    private EditText medicationUnits;
    private Button medSaveBtn;

    RelativeLayout relativeLayout;
    TextView errormsg;

    //    private Button bgNextBtn;
    public DailyLogsFragmentLOGIC() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_daily_logs_logic, container, false);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        Retrofit retrofit1 = NutritionixAPIRetrofitClient.getRetrofitClient();
        nutritionixAPI = retrofit1.create(NutritionixAPI.class);
        sharedPreferences = getContext().getSharedPreferences("MyPreferences", 0);
        errormsg = myView.findViewById(R.id.error_msg);

        relativeLayout = myView.findViewById(R.id.logs_rl);
        saveDailyLogsBtn = myView.findViewById(R.id.save_logs_btn);
        bloodGlucoseLevelEditText = myView.findViewById(R.id.blood_glucose_level_edittext);
        bglevelRadiogroup = myView.findViewById(R.id.blood_glucose_level_radiogroup);
        progressBar = myView.findViewById(R.id.progressBar);
        progressBar.bringToFront();
//        bgNextBtn = myView.findViewById(R.id.next_blood_glucose_btn);
        bgSaveBtn = myView.findViewById(R.id.save_blood_glucose_btn);
        foodItemSuggestionsTextView = myView.findViewById(R.id.post_food_suggestions_requesttest_txtview);
        auto_complete_edittext = myView.findViewById(R.id.food_items_autocomplete_textview);
        selected_food_item = myView.findViewById(R.id.selected_fooditem);
        selectedFoodItemCalories = myView.findViewById(R.id.selected_fooditem_calories);
        selectedFoodItemMetrc = myView.findViewById(R.id.selected_fooditem_metric);
        selectedFoodItemQuantity = myView.findViewById(R.id.selected_fooditem_quantity);
        mealTimeRadiogroup = myView.findViewById(R.id.meal_time_radiogroup);
        selectedFoodItemlayout = myView.findViewById(R.id.selected_fooditem_layout);
        mealSaveBtn = myView.findViewById(R.id.save_meal_btn);

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

        medSpinner = myView.findViewById(R.id.medication_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                R.array.medicine_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medSpinner.setAdapter(adapter1);
        medicationUnits = myView.findViewById(R.id.medication_units);
        medSaveBtn = myView.findViewById(R.id.save_medication_btn);

        bloodGlucoseLevelEditText.setOnClickListener(this);
        exercise_distance.setOnClickListener(this);
        exercise_duration.setOnClickListener(this);
        medicationUnits.setOnClickListener(this);
        bgSaveBtn.setOnClickListener(this);
        mealSaveBtn.setOnClickListener(this);
        saveDailyLogsBtn.setOnClickListener(this);
        exeSaveBtn.setOnClickListener(this);
        medSaveBtn.setOnClickListener(this);
        bloodGlucoseLevelEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mealSaveBtn.setVisibility(GONE);
                if (s.length() != 0 && bglevelRadiogroup.getCheckedRadioButtonId() != -1) {
                    bgSaveBtn.setVisibility(View.VISIBLE);
//                    bgNextBtn.setVisibility(View.VISIBLE);
                } else {
                    bgSaveBtn.setVisibility(GONE);
//                    bgNextBtn.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        bglevelRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String bglevel = bloodGlucoseLevelEditText.getText().toString();
                if (!TextUtils.isEmpty(bglevel)) {
                    bgSaveBtn.setVisibility(View.VISIBLE);
//                    bgNextBtn.setVisibility(View.VISIBLE);
                } else {
                    bgSaveBtn.setVisibility(GONE);
//                    bgNextBtn.setVisibility(GONE);
                }
            }
        });
        mealTimeRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String mealtime = selected_food_item.getText().toString();
                if (!TextUtils.isEmpty(mealtime)) {
//                    mealNextBtn.setVisibility(View.VISIBLE);
                    mealSaveBtn.setVisibility(View.VISIBLE);
                } else {
//                    mealNextBtn.setVisibility(GONE);
                    mealSaveBtn.setVisibility(GONE);
                }
            }
        });
        selected_food_item.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && mealTimeRadiogroup.getCheckedRadioButtonId() != -1) {
//                    mealNextBtn.setVisibility(View.VISIBLE);
                    mealSaveBtn.setVisibility(View.VISIBLE);
                } else {
//                    mealNextBtn.setVisibility(GONE);
                    mealSaveBtn.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        selectedFoodItemQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (selectedFoodItemQuantity.getText().toString().length() > 0 && selectedFoodItemCalories.getText().toString().length() > 0) {
                    Float quantity = Float.parseFloat(selectedFoodItemQuantity.getText().toString());
                    Log.e("Quantity", quantity.toString());
                    Log.e("Original cals", originalCalories.toString());
                    Float calories = (originalCalories * quantity);
                    selectedFoodItemCalories.setText(calories.toString());
                } else {


                }
            }
        });
        exeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        medSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                bgSaveBtn.setVisibility(GONE);
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
                        saveDailyLogsBtn.setVisibility(GONE);
                        getExerciseDetails(query);
                    }
                }
            }
        });
        exercise_distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                bgSaveBtn.setVisibility(GONE);
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
                        saveDailyLogsBtn.setVisibility(GONE);
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
//                exeNextBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        medicationUnits.addTextChangedListener(new TextWatcher() {
            String distance = exercise_distance.getText().toString();
            String duration = exercise_duration.getText().toString();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (exeSaveBtn.isShown()) {
                    exeSaveBtn.setVisibility(GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(bloodGlucoseLevelEditText.getText())){
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                    medSaveBtn.setVisibility(GONE);
                }else{
//                    saveDailyLogsBtn.setVisibility(GONE);
                    medSaveBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        saveDailyLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAll();
            }
        });
        getFoodItemsSuggestions();
        return myView;
    }



    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.save_logs_btn:
//                saveAll();

            case R.id.save_meal_btn:
                Log.e("Save Meal", "Saving");
                saveDailyLogsBtn.setVisibility(GONE);
                recordMealData();

            case R.id.save_blood_glucose_btn:
                String bglevel = bloodGlucoseLevelEditText.getText().toString();
                if (TextUtils.isEmpty(bglevel) || bglevelRadiogroup.getCheckedRadioButtonId() == -1) {
                    return;
                } else {
                    saveDailyLogsBtn.setVisibility(GONE);
                    recordBloodGlucoseLevel();
                }
                break;

            case R.id.save_exercise_btn:
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
                    saveDailyLogsBtn.setVisibility(GONE);
                    recordExerciseData(newExercise);
                }

            case R.id.save_medication_btn:
                String medication = medSpinner.getSelectedItem().toString();
                String medunits = medicationUnits.getText().toString();
                if (TextUtils.isEmpty(medication) || TextUtils.isEmpty(medunits)) {

                } else {
                    Integer userID = sharedPreferences.getInt("UserID", 1);
                    Medication medication1 = new Medication(Float.parseFloat(medunits),medication,userID.longValue());
                    saveDailyLogsBtn.setVisibility(GONE);
                    recordMedicationData(medication1);
                }


            case R.id.blood_glucose_level_edittext:
                mealSaveBtn.setVisibility(GONE);
            case R.id.exercise_distance_edittext:
                bgSaveBtn.setVisibility(GONE);
            case R.id.exercise_duration_edittext:
                bgSaveBtn.setVisibility(GONE);
            case R.id.medication_units:
                if (exeSaveBtn.isShown()) {
                    exeSaveBtn.setVisibility(GONE);
                }

        }
    }


    private void recordMealData() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
        }catch (Exception e) {

        }
        Integer userID = sharedPreferences.getInt("UserID", 1);
        progressBar.setVisibility(View.VISIBLE);
        String name = selected_food_item.getText().toString();
        String time = selectedMealTime();
        Float calories = Float.parseFloat(selectedFoodItemCalories.getText().toString());
        Float quantity = Float.parseFloat(selectedFoodItemQuantity.getText().toString());
        Call<Meal> mealCall = laravelAPI.recordMealData(new Meal(calories, name, time, quantity, userID.longValue()));
        mealCall.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("MealError", response.message());
                    errormsg.setText("Meal not saved, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    selectedFoodItemlayout.setVisibility(GONE);
                    auto_complete_edittext.getText().clear();
                    selected_food_item.setText("");
                    selectedFoodItemCalories.setText("");
                    selectedFoodItemMetrc.setText("");
                    selectedFoodItemQuantity.getText().clear();
                    mealTimeRadiogroup.clearCheck();
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                    return;
                } else {
                    progressBar.setVisibility(GONE);
                    Log.e("Meal", "Record saved");
                    selectedFoodItemlayout.setVisibility(GONE);
                    auto_complete_edittext.getText().clear();
                    selected_food_item.setText("");
                    selectedFoodItemCalories.setText("");
                    selectedFoodItemMetrc.setText("");
                    selectedFoodItemQuantity.getText().clear();
                    mealTimeRadiogroup.clearCheck();
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Log.e("Meal Failed", t.getMessage());
                errormsg.setText("Meal not saved, please try again later");
                errormsg.setVisibility(View.VISIBLE);
                selectedFoodItemlayout.setVisibility(GONE);
                auto_complete_edittext.getText().clear();
                selected_food_item.setText("");
                selectedFoodItemCalories.setText("");
                selectedFoodItemMetrc.setText("");
                selectedFoodItemQuantity.getText().clear();
                mealTimeRadiogroup.clearCheck();
                saveDailyLogsBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private  void recordMedicationData(Medication medication){
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
        }catch (Exception e) {

        }
        Log.e("Med recorded", medication.toString());
        progressBar.setVisibility(View.VISIBLE);
        Call<MedicationResponse> medicationResponseCall = laravelAPI.recordMedication(medication);
        medicationResponseCall.enqueue(new Callback<MedicationResponse>() {
            @Override
            public void onResponse(Call<MedicationResponse> call, Response<MedicationResponse> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("Med Saved Unsuccessful", "Code: " + response.code() + "\n" + "Message" + response.message());
                    errormsg.setText("Medication not saved, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    medicationUnits.getText().clear();
                    medSaveBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                    return;
                } else {
                    medicationUnits.getText().clear();
                    medSaveBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MedicationResponse> call, Throwable t) {
                Log.e("Exe Saved Failed", t.getMessage());
                progressBar.setVisibility(GONE);
                errormsg.setText("Medication not saved, please try again later");
                errormsg.setVisibility(View.VISIBLE);
                medicationUnits.getText().clear();
                medSaveBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                saveDailyLogsBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void recordBloodGlucoseLevel() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
        }catch (Exception e) {

        }
        Integer userID = sharedPreferences.getInt("UserID", 1);
        progressBar.setVisibility(View.VISIBLE);
        final Float bgValue = Float.parseFloat(bloodGlucoseLevelEditText.getText().toString());
        String bgTime = selectedBloodGlucoseTime();
        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
                bgTime, bgValue, userID.longValue()));
        bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
            @Override
            public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("Blood Glucose", response.message());
                    errormsg.setText("Blood glucose not saved, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                    return;
                } else {
                    progressBar.setVisibility(GONE);
                    Log.e("Blood Glucose", "Record saved");
                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BloodGlucose> call, Throwable t) {
                progressBar.setVisibility(GONE);
                Log.e("Blood Glucose", t.getMessage());
                errormsg.setText("Blood glucose not saved, please try again later");
                errormsg.setVisibility(View.VISIBLE);
                bloodGlucoseLevelEditText.getText().clear();
                bglevelRadiogroup.clearCheck();
                saveDailyLogsBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private String selectedBloodGlucoseTime() {
        int selectedRadioBtn = bglevelRadiogroup.getCheckedRadioButtonId();
        RadioButton radioTimeButton = myView.findViewById(selectedRadioBtn);
        return radioTimeButton.getText().toString();
    }

    private void getFoodItemsSuggestions() {
        foodAutoSuggestAdapter = new FoodAutoSuggestAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line);
        foodAutoSuggestAdapterSET = new FoodAutoSuggestAdapterSET(getContext(),
                android.R.layout.simple_dropdown_item_1line);
        auto_complete_edittext.setThreshold(1);
        auto_complete_edittext.setAdapter(foodAutoSuggestAdapter);
        auto_complete_edittext.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String food = foodAutoSuggestAdapterSET.getObject(position);
                        String[] separated = food.split(":");
                        selectedFoodItemlayout.setVisibility(View.VISIBLE);
                        selected_food_item.setText(separated[0].trim());
                        selectedFoodItemQuantity.setText(separated[1]);
                        selectedFoodItemMetrc.setText(separated[2]);
                        selectedFoodItemCalories.setText(separated[3]);
                        originalCalories = Float.parseFloat(selectedFoodItemCalories.getText().toString()) / Float.parseFloat(selectedFoodItemQuantity.getText().toString());
                    }
                });
        auto_complete_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
                selectedFoodItemlayout.setVisibility(GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(auto_complete_edittext.getText())) {
                        getFoodItemSuggestionsList(auto_complete_edittext.getText().toString());
                    }
                }
                return false;
            }
        });
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
                        Log.e("Exe Exercise", exercise.toString());
                        saveDailyLogsBtn.setVisibility(GONE);
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
                    exeSaveBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                    return;
                } else {
                    Log.e("Exe  Saved", recordedExercise.toString());
                    exercise_distance.getText().clear();
                    exercise_duration.getText().clear();
                    exercise_calories_txtview.setVisibility(View.GONE);
                    exercise_calories_metrics_txtview.setVisibility(GONE);
                    exeSaveBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
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
                saveDailyLogsBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    public void saveAll(){
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
        }catch (Exception e) {

        }
        //SaveMeal
        Integer userID = sharedPreferences.getInt("UserID", 1);
        progressBar.setVisibility(View.VISIBLE);
        String name = selected_food_item.getText().toString();
        String time = selectedMealTime();
        Float calories = Float.parseFloat(selectedFoodItemCalories.getText().toString());
        Float quantity = Float.parseFloat(selectedFoodItemQuantity.getText().toString());
        Call<Meal> mealCall = laravelAPI.recordMealData(new Meal(calories, name, time, quantity, userID.longValue()));
        mealCall.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if (!response.isSuccessful()) {
                    Log.e("MealError", response.message());
                    errormsg.setText("Something happened, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    Log.e("Meal", "Record saved");
                    selectedFoodItemlayout.setVisibility(GONE);
                    auto_complete_edittext.getText().clear();
                    selected_food_item.setText("");
                    selectedFoodItemCalories.setText("");
                    selectedFoodItemMetrc.setText("");
                    selectedFoodItemQuantity.getText().clear();
                    mealTimeRadiogroup.clearCheck();

                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();

                    exercise_distance.getText().clear();
                    exercise_duration.getText().clear();

                    medicationUnits.getText().clear();

                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
                Log.e("Meal Failed", t.getMessage());
                errormsg.setText("Something happened, please try again later");
                errormsg.setVisibility(View.VISIBLE);
            }
        });

        //Savebg
        final Float bgValue = Float.parseFloat(bloodGlucoseLevelEditText.getText().toString());
        String bgTime = selectedBloodGlucoseTime();
        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
                bgTime, bgValue, userID.longValue()));
        bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
            @Override
            public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
                if (!response.isSuccessful()) {
                    Log.e("Blood Glucose", response.message());
                    errormsg.setText("Something happened, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    Log.e("Blood Glucose", "Record saved");
                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();

                    auto_complete_edittext.getText().clear();
                    selected_food_item.setText("");
                    selectedFoodItemCalories.setText("");
                    selectedFoodItemMetrc.setText("");
                    selectedFoodItemQuantity.getText().clear();
                    mealTimeRadiogroup.clearCheck();

                    exercise_distance.getText().clear();
                    exercise_duration.getText().clear();

                    medicationUnits.getText().clear();
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BloodGlucose> call, Throwable t) {
                Log.e("Blood Glucose", t.getMessage());
                errormsg.setText("Something happened, please try again later");
                errormsg.setVisibility(View.VISIBLE);
            }
        });

        //saveexercise
        String exercise = exeSpinner.getSelectedItem().toString();
        String distance = exercise_distance.getText().toString();
        String duration = exercise_duration.getText().toString();
        String calories1 = exercise_calories_txtview.getText().toString();
        Exercise newExercise = new Exercise(Double.parseDouble(calories1), Double.parseDouble(duration), Double.parseDouble(distance), exercise, userID.longValue());
        Call<Exercise> exerciseCall = laravelAPI.recordExerciseData(newExercise);
        exerciseCall.enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                if (!response.isSuccessful()) {
                    Log.e("Exe Saved Unsuccessful", "Code: " + response.code() + "\n" + "Message" + response.message());
                    errormsg.setText("Something happened, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    exercise_distance.getText().clear();
                    exercise_duration.getText().clear();
                    exercise_calories_txtview.setVisibility(View.GONE);
                    exercise_calories_metrics_txtview.setVisibility(GONE);
                    exeSaveBtn.setVisibility(View.GONE);

                    auto_complete_edittext.getText().clear();
                    selected_food_item.setText("");
                    selectedFoodItemCalories.setText("");
                    selectedFoodItemMetrc.setText("");
                    selectedFoodItemQuantity.getText().clear();
                    mealTimeRadiogroup.clearCheck();

                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();

                    medicationUnits.getText().clear();

                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                Log.e("Exe Saved Failed", t.getMessage());
                progressBar.setVisibility(GONE);
                errormsg.setText("Something happened, please try again later");
                errormsg.setVisibility(View.VISIBLE);
            }
        });

        //savemedication
        String medication = medSpinner.getSelectedItem().toString();
        String medunits = medicationUnits.getText().toString();
        Medication medication1 = new Medication(Float.parseFloat(medunits),medication,userID.longValue());
        Call<MedicationResponse> medicationResponseCall = laravelAPI.recordMedication(medication1);
        medicationResponseCall.enqueue(new Callback<MedicationResponse>() {
            @Override
            public void onResponse(Call<MedicationResponse> call, Response<MedicationResponse> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(GONE);
                    Log.e("Med Saved Unsuccessful", "Code: " + response.code() + "\n" + "Message" + response.message());
                    errormsg.setText("Something happened, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    medicationUnits.getText().clear();
                    medSaveBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    saveDailyLogsBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MedicationResponse> call, Throwable t) {
                Log.e("Exe Saved Failed", t.getMessage());
                errormsg.setText("Something happened, please try again later");
                errormsg.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
            }
        });

    }

    private void getFoodItemSuggestionsList(final String inputtext) {
        Call<FoodItemSuggestionsList> foodItemSuggestionsListCall = nutritionixAPI.getFoodItemSuggestionsList(inputtext);
        foodItemSuggestionsListCall.enqueue(new Callback<FoodItemSuggestionsList>() {
            @Override
            public void onResponse(Call<FoodItemSuggestionsList> call, Response<FoodItemSuggestionsList> response) {
                List<String> stringList = new ArrayList<>();
                List<String> stringListSet = new ArrayList<>();
                if (!response.isSuccessful()) {
                    errormsg.setText("Something happened, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                } else {
                    String text = "";
                    String txt = "";
                    FoodItemSuggestionsList foodItemSuggestionsList = response.body();
                    if (foodItemSuggestionsList != null) {
                        for (BrandedFoodItemSuggestions branded : foodItemSuggestionsList.getBranded()) {
                            if (branded == null) {
                            } else {
                                stringList.add(branded.getFoodName());
                                stringListSet.add(branded.getFoodName() + ":" + branded.getServingQty() + ":" + branded.getServingUnit() + ":" + branded.getNfCalories());
                            }
                        }
                        foodAutoSuggestAdapter.setData(stringList);
                        foodAutoSuggestAdapter.notifyDataSetChanged();
                        foodAutoSuggestAdapterSET.setData(stringListSet);
                        foodAutoSuggestAdapterSET.notifyDataSetChanged();
                        response.errorBody();
                    } else {
                        getFoodItemDetails(inputtext);
                    }
                }
            }

            @Override
            public void onFailure(Call<FoodItemSuggestionsList> call, Throwable t) {
                errormsg.setText("Something happened, please try again later");
                errormsg.setVisibility(View.VISIBLE);
            }
        });
    }

    private String getFoodItemDetails(String inputtext) {
        String foodName = "";
        Call<Food> foodCall = nutritionixAPI.getFoodItemDetails(new Food(inputtext));
        foodCall.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (!response.isSuccessful()) {
                    Log.e("Meal Suggestions", "Code: " + response.code() + "\n" + "Message" + response.message());
                    errormsg.setText("Something happened, please try again later");
                    errormsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    Food food = response.body();
                    String foodName = food.getFoodName();
//                    selected_food_item.setText(foodName);
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                errormsg.setText("Something happened, please try again later");
                errormsg.setVisibility(View.VISIBLE);
            }
        });
        return foodName;
    }

    private String selectedMealTime() {
        int selectedRadioBtn = mealTimeRadiogroup.getCheckedRadioButtonId();
        RadioButton radioTimeButton = myView.findViewById(selectedRadioBtn);
        return radioTimeButton.getText().toString();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
