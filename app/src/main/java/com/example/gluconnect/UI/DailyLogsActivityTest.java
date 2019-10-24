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
import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodItemSuggestionsList;
import com.example.gluconnect.Models.ExerciseData;
import com.example.gluconnect.Models.ExerciseDetails;
import com.example.gluconnect.Models.ExerciseDetailsList;
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

public class DailyLogsActivityTest extends AppCompatActivity {

    private TextView exerciseTextView;
    private TextView foodItemSuggestionsTextView;
    private NutritionixAPI nutritionixAPI;
    private FoodAutoSuggestAdapter foodAutoSuggestAdapter;
    private AutoCompleteTextView auto_complete_edittext;
    private TextView selected_food_item;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;

    private RadioGroup radioGroup;
    private Button saveDailyLogsBtn;
    private LaravelAPI laravelAPI;
    private TextView bgLeveltextView;
    private TextView showSavedbgLeveltextView;
    private EditText bloodGlucoseLevelEditText;
    private RadioGroup bglevelRadiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_logs_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        exerciseTextView = findViewById(R.id.post_exercise_requesttest_txtview);
        foodItemSuggestionsTextView = findViewById(R.id.post_food_suggestions_requesttest_txtview);
        auto_complete_edittext = findViewById(R.id.food_items_autocomplete_textview);
        selected_food_item = findViewById(R.id.selected_fooditem);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        nutritionixAPI = retrofit.create(NutritionixAPI.class);

        saveDailyLogsBtn = findViewById(R.id.save_logs_btn);
//        bgLeveltextView = findViewById(R.id.all_log_txtview);
//        showSavedbgLeveltextView = myView.findViewById(R.id.saved_log_txtview);
        bloodGlucoseLevelEditText = findViewById(R.id.blood_glucose_level_edittext);
        bglevelRadiogroup = findViewById(R.id.blood_glucose_level_radiogroup);


        saveDailyLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save_logs_btn:
                        String bglevel = bloodGlucoseLevelEditText.getText().toString();
                        if (TextUtils.isEmpty(bglevel) || bglevelRadiogroup.getCheckedRadioButtonId() == -1) {
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

        getExerciseDetails();
        getFoodItemsSuggestions();
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
//                        for (UserFoodItemSuggestions self : foodItemSuggestionsList.getSelf()) {
//                            if (self == null) {
//                            } else {
//                                stringList.add(self.getFoodName() + "\t\t" + self.getNfCalories() + "cals");
//
//                            }
//                        }
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

    private void getFoodItemDetails(String inputtext) {
        Call<Food> foodCall = nutritionixAPI.getFoodItemDetails(new Food(inputtext));
        foodCall.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (!response.isSuccessful()) {
                    selected_food_item.setText("Code: " + response.code() + "\n" + "Message" + response.message());
                    return;
                } else {
                    Food food = response.body();
                        selected_food_item.setText(food.getFoodName()+"\t"+food.getNfCalories());
                    }
                }
            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                auto_complete_edittext.setText(t.getMessage());
            }
        });
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
                        exerciseTextView.setText(exercise.toString());
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
        Double bgValue = Double.parseDouble(bloodGlucoseLevelEditText.getText().toString());
        String bgTime = selectedBloodGlucoseTime();
        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
                bgValue, bgTime,1));
        bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
            @Override
            public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
                if (!response.isSuccessful()) {
                    showSavedbgLeveltextView.setText("Code: " + response.code() + "\n" + "Message" + response.message());
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Record saved", Toast.LENGTH_LONG).show();
                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();
                }
            }

            @Override
            public void onFailure(Call<BloodGlucose> call, Throwable t) {
                showSavedbgLeveltextView.setText(t.getMessage());
            }
        });
    }

    private String selectedBloodGlucoseTime() {
        int selectedRadioBtn = bglevelRadiogroup.getCheckedRadioButtonId();
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
}
