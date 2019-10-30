package com.example.gluconnect.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gluconnect.Adapters.FoodAutoSuggestAdapter;
import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BrandedFoodItemSuggestions;
import com.example.gluconnect.Models.Exercise;
import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodItemSuggestionsList;
import com.example.gluconnect.Models.ExerciseData;
import com.example.gluconnect.Models.ExerciseDetails;
import com.example.gluconnect.Models.ExerciseDetailsList;
import com.example.gluconnect.Models.Meal;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.example.gluconnect.Utils.NutritionixAPI;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.NutritionixAPIRetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DailyLogsActivityTest extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView exerciseTextView;
    private TextView foodItemSuggestionsTextView;
    private NutritionixAPI nutritionixAPI;
    private FoodAutoSuggestAdapter foodAutoSuggestAdapter;
    private AutoCompleteTextView auto_complete_edittext;
    private TextView selected_food_item;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;

    private LaravelAPI laravelAPI;
    private Button saveDailyLogsBtn;
    private EditText bloodGlucoseLevelEditText;
    private RadioGroup bglevelRadiogroup;
    private RadioGroup mealTimeRadiogroup;
    private EditText exerciseSpinnerEdittext;
    private AutoCompleteTextView mealsAutoCompleteTextView;

    private BloodGlucose bloodGlucoseSaved;
    private Meal mealSaved;
    private Exercise exerciseSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_logs_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Retrofit retrofit = NutritionixAPIRetrofitClient.getRetrofitClient();
        nutritionixAPI = retrofit.create(NutritionixAPI.class);
        Retrofit retrofitl = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofitl.create(LaravelAPI.class);

        exerciseTextView = findViewById(R.id.post_exercise_requesttest_txtview);
        foodItemSuggestionsTextView = findViewById(R.id.post_food_suggestions_requesttest_txtview);
        auto_complete_edittext = findViewById(R.id.food_items_autocomplete_textview);
        selected_food_item = findViewById(R.id.selected_fooditem);
        exerciseSpinnerEdittext = findViewById(R.id.planets_spinner_edittext);
        mealsAutoCompleteTextView = findViewById(R.id.food_items_autocomplete_textview);
        saveDailyLogsBtn = findViewById(R.id.save_logs_btn);
        bloodGlucoseLevelEditText = findViewById(R.id.blood_glucose_level_edittext);
        bglevelRadiogroup = findViewById(R.id.blood_glucose_level_radiogroup);
        mealTimeRadiogroup = findViewById(R.id.meal_time_radiogroup);

        getExerciseDetails();
        getFoodItemsSuggestions();
        saveDailyLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save_logs_btn:
                        String bglevel = bloodGlucoseLevelEditText.getText().toString();
                            if (TextUtils.isEmpty(bglevel)|| bglevelRadiogroup.getCheckedRadioButtonId() == -1) {

                                Toast.makeText(getApplicationContext(), "Please fill in all the details", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            recordBloodGlucoseLevel();
                        }
                        break;
//            case R.id.responseButton2:
//                break;
//            default:
//                break;
                }
            }
        });


        Spinner spinner = findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void getFoodItemsSuggestions() {
        foodAutoSuggestAdapter = new FoodAutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        auto_complete_edittext.setThreshold(2);
        auto_complete_edittext.setAdapter(foodAutoSuggestAdapter);
        auto_complete_edittext.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        selected_food_item.setText(foodAutoSuggestAdapter.getObject(position));
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

    private void getFoodItemSuggestionsList(final String inputtext) {
        Call <FoodItemSuggestionsList> foodItemSuggestionsListCall = nutritionixAPI.getFoodItemSuggestionsList(inputtext);
        foodItemSuggestionsListCall.enqueue(new Callback<FoodItemSuggestionsList>() {
            @Override
            public void onResponse(Call<FoodItemSuggestionsList> call, Response<FoodItemSuggestionsList> response) {
                List<String> stringList = new ArrayList<>();
                if (!response.isSuccessful()){
                    foodItemSuggestionsTextView.setText("Code: "+response.code()+"\n Message: "+response.message());
                }
                else {
                    String text="";
                    String txt ="";
                    FoodItemSuggestionsList foodItemSuggestionsList = response.body();
                    if (foodItemSuggestionsList!=null) {
                        for (BrandedFoodItemSuggestions branded : foodItemSuggestionsList.getBranded()) {
                            if (branded == null) {
                            } else {
                                stringList.add(branded.getFoodName() + "\t\t" + branded.getNfCalories() + "cals");
                            }
                        }
                        foodAutoSuggestAdapter.setData(stringList);
                        foodAutoSuggestAdapter.notifyDataSetChanged();
                        response.errorBody();
                    }else {
                        getFoodItemDetails(inputtext);
                    }
                }
            }

            @Override
            public void onFailure(Call<FoodItemSuggestionsList> call, Throwable t) {
                foodItemSuggestionsTextView.setText(t.getMessage());
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
                    Toast.makeText(getApplicationContext(),"Code: " + response.code() + "\n" + "Message" + response.message(),Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Food food = response.body();
                    String foodName = food.getFoodName();
                    String mealTime = selectedMealTime();
                    Meal mealnew = new Meal(foodName,mealTime,1L);
                    mealSaved = mealnew;
                    }
                }
            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                auto_complete_edittext.setText(t.getMessage());
            }
        });
        return foodName;
    }

    private void getExerciseDetails() {

        Call<ExerciseDetailsList> exerciseDetailsListCall = nutritionixAPI.getExerciseDetailsList(new ExerciseData(21, "female", 157.0, "running 6 km 50 mins", 44.5));
        exerciseDetailsListCall.enqueue(new Callback<ExerciseDetailsList>() {
            @Override
            public void onResponse(Call<ExerciseDetailsList> call, Response<ExerciseDetailsList> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Code: " + response.code() + "\n" + "Message" + response.message(),Toast.LENGTH_LONG).show();
                    return;
                } else {
                    ExerciseDetailsList exerciseDetailsList = response.body();
                    for (ExerciseDetails exercise : exerciseDetailsList.getExercises()) {
                        exerciseTextView.setText(exercise.toString());
                        exerciseSaved = new Exercise(exercise.getNfCalories(),Double.parseDouble(exercise.getDurationMin().toString()),exercise.getName(),1L);
                    }
                }
            }

            @Override
            public void onFailure(Call<ExerciseDetailsList> call, Throwable t) {
                exerciseTextView.setText(t.getMessage());
            }
        });
    }

    private void recordBloodGlucoseLevel() {
        final Float bgValue = Float.parseFloat(bloodGlucoseLevelEditText.getText().toString());
        final String bgTime = selectedBloodGlucoseTime();
        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
                bgTime, bgValue,1L));
        bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
            @Override
            public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Code: " + response.code() + "\n" + "Message" + response.message(),Toast.LENGTH_LONG).show();
                    System.err.println("Code: " + response.code() + "\n" + "Message" + response.message());
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Record saved", Toast.LENGTH_LONG).show();
                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();
                    bloodGlucoseSaved = new BloodGlucose(bgTime,bgValue,1L);
                }
            }
            @Override
            public void onFailure(Call<BloodGlucose> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                System.err.println(t.getMessage());
            }
        });
    }

    private String selectedBloodGlucoseTime() {
        int selectedRadioBtn = bglevelRadiogroup.getCheckedRadioButtonId();
        RadioButton radioTimeButton = findViewById(selectedRadioBtn);
        return radioTimeButton.getText().toString();
    }

    private String selectedMealTime() {
        int selectedRadioBtn = mealTimeRadiogroup.getCheckedRadioButtonId();
        RadioButton radioTimeButton = findViewById(selectedRadioBtn);
        return radioTimeButton.getText().toString();
    }

    public static void resetFields(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).getText().clear();
            }

            if (view instanceof RadioGroup) {
                ((RadioButton)((RadioGroup) view).getChildAt(0)).setChecked(true);
            }

            if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0);
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                resetFields((ViewGroup) view);
        }
    }

//    public void recordDailyData(){
//        bloodGlucoseSaved = new BloodGlucose(55D,"fasting",1L);
//        exerciseSaved = new Exercise(23D,30D,"Running",1L);
//        mealSaved = new Meal("Rice","Lunch",1L);
//        Call<DailyLogs> dailyLogsCall = laravelAPI.recordDailyData(new DailyLogs(bloodGlucoseSaved,exerciseSaved,mealSaved));
//        dailyLogsCall.enqueue(new Callback<DailyLogs>() {
//            @Override
//            public void onResponse(Call<DailyLogs> call, Response<DailyLogs> response) {
//                if (!response.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(),"Code: " + response.code() + "\n" + "Message: " + response.message(),Toast.LENGTH_LONG).show();
//                    return;
//                } else {
//                    DailyLogs dailyLogs = response.body();
//                    Toast.makeText(getApplicationContext(),"Record saved",Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DailyLogs> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
//                exerciseTextView.setText(t.getMessage());
//            }
//        });
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
