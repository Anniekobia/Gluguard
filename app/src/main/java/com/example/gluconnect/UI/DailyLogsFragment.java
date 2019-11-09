package com.example.gluconnect.UI;

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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gluconnect.Adapters.FoodAutoSuggestAdapter;
import com.example.gluconnect.Adapters.FoodAutoSuggestAdapterSET;
import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BrandedFoodItemSuggestions;
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


public class DailyLogsFragment extends Fragment implements View.OnClickListener {

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
    private Button mealNextBtn;
    private Button mealSaveBtn;
    private EditText selectedFoodItemQuantity;
    private TextView selectedFoodItemMetrc;
    private TextView selectedFoodItemCalories;
    private ConstraintLayout selectedFoodItemlayout;
    Float originalCalories;

//    private Button bgNextBtn;
    public DailyLogsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_daily_logs, container, false);
        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        Retrofit retrofit1 = NutritionixAPIRetrofitClient.getRetrofitClient();
        nutritionixAPI = retrofit1.create(NutritionixAPI.class);

        saveDailyLogsBtn = myView.findViewById(R.id.save_logs_btn);
        bloodGlucoseLevelEditText = myView.findViewById(R.id.blood_glucose_level_edittext);
        bglevelRadiogroup = myView.findViewById(R.id.blood_glucose_level_radiogroup);
        progressBar = myView.findViewById(R.id.progressBar);
        progressBar.bringToFront();
        bgNextBtn = myView.findViewById(R.id.next_blood_glucose_btn);
        bgSaveBtn = myView.findViewById(R.id.save_blood_glucose_btn);
        foodItemSuggestionsTextView = myView.findViewById(R.id.post_food_suggestions_requesttest_txtview);
        auto_complete_edittext = myView.findViewById(R.id.food_items_autocomplete_textview);
        selected_food_item = myView.findViewById(R.id.selected_fooditem);
        selectedFoodItemCalories = myView.findViewById(R.id.selected_fooditem_calories);
        selectedFoodItemMetrc = myView.findViewById(R.id.selected_fooditem_metric);
        selectedFoodItemQuantity = myView.findViewById(R.id.selected_fooditem_quantity);
        mealTimeRadiogroup = myView.findViewById(R.id.meal_time_radiogroup);
        selectedFoodItemlayout = myView.findViewById(R.id.selected_fooditem_layout);
        mealNextBtn = myView.findViewById(R.id.next_meal_btn);
        mealSaveBtn = myView.findViewById(R.id.save_meal_btn);


        bloodGlucoseLevelEditText.setOnClickListener(this);
        bgSaveBtn.setOnClickListener(this);
        bgNextBtn.setOnClickListener(this);
        mealSaveBtn.setOnClickListener(this);
        mealNextBtn.setOnClickListener(this);
        saveDailyLogsBtn.setOnClickListener(this);
        bloodGlucoseLevelEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0&&bglevelRadiogroup.getCheckedRadioButtonId() != -1){
                    bgSaveBtn.setVisibility(View.VISIBLE);
                    bgNextBtn.setVisibility(View.VISIBLE);
                }else {
                    bgSaveBtn.setVisibility(View.GONE);
                    bgNextBtn.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });
        bglevelRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String bglevel = bloodGlucoseLevelEditText.getText().toString();
                if (!TextUtils.isEmpty(bglevel)){
                    bgSaveBtn.setVisibility(View.VISIBLE);
                    bgNextBtn.setVisibility(View.VISIBLE);
                }else {
                    bgSaveBtn.setVisibility(View.GONE);
                    bgNextBtn.setVisibility(View.GONE);
                }
            }
        });
        mealTimeRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String mealtime = selected_food_item.getText().toString();
                if (!TextUtils.isEmpty(mealtime)){
                    mealNextBtn.setVisibility(View.VISIBLE);
                    mealSaveBtn.setVisibility(View.VISIBLE);
                }else {
                    mealNextBtn.setVisibility(View.GONE);
                    mealSaveBtn.setVisibility(View.GONE);
                }
            }
        });
        selected_food_item.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0&&mealTimeRadiogroup.getCheckedRadioButtonId() != -1){
                    mealNextBtn.setVisibility(View.VISIBLE);
                    mealSaveBtn.setVisibility(View.VISIBLE);
                }else {
                    mealNextBtn.setVisibility(View.GONE);
                    mealSaveBtn.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
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
                if(selectedFoodItemQuantity.getText().toString().length()>0&&selectedFoodItemCalories.getText().toString().length()>0){
                    Float quantity = Float.parseFloat(selectedFoodItemQuantity.getText().toString());
                    Log.e("Quantity",quantity.toString());
                    Log.e("Original cals",originalCalories.toString());
                    Float calories = (originalCalories*quantity);
                    selectedFoodItemCalories.setText(calories.toString());
                }else {


                }
            }
        });
        getFoodItemsSuggestions();
        return myView;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_blood_glucose_btn:
                String bglevel = bloodGlucoseLevelEditText.getText().toString();
                if (TextUtils.isEmpty(bglevel) || bglevelRadiogroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Please fill in all the details", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    recordBloodGlucoseLevel();
                }
                break;
            case R.id.save_meal_btn:
                Log.e("Save","Saving");
                recordMealData();

        }
    }

    private void recordMealData(){
        progressBar.setVisibility(View.VISIBLE);
        String name = selected_food_item.getText().toString();
        String time = selectedMealTime();
        Float calories = Float.parseFloat(selectedFoodItemCalories.getText().toString());
        Float quantity = Float.parseFloat(selectedFoodItemQuantity.getText().toString());
        Call<Meal> mealCall = laravelAPI.recordMealData(new Meal(calories,name,time,quantity,1L));
        mealCall.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("MealError",response.message());
                    Toast.makeText(getContext(),"Record not saved",Toast.LENGTH_LONG).show();
                    return;
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Record saved", Toast.LENGTH_LONG).show();
                    selectedFoodItemlayout.setVisibility(View.GONE);
                    auto_complete_edittext.getText().clear();
                    selected_food_item.setText("");
                    selectedFoodItemCalories.setText("");
                    selectedFoodItemMetrc.setText("");
                    selectedFoodItemQuantity.getText().clear();
                    mealTimeRadiogroup.clearCheck();
                }
            }
            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void recordBloodGlucoseLevel() {
        progressBar.setVisibility(View.VISIBLE);
        final Float bgValue = Float.parseFloat(bloodGlucoseLevelEditText.getText().toString());
        String bgTime = selectedBloodGlucoseTime();
        Call<BloodGlucose> bloodGlucoseCall = laravelAPI.recordBloodGlucoseLevel(new BloodGlucose(
                bgTime, bgValue,1L));
        bloodGlucoseCall.enqueue(new Callback<BloodGlucose>() {
            @Override
            public void onResponse(Call<BloodGlucose> call, Response<BloodGlucose> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e(BGERROR,response.message());
                    Toast.makeText(getContext(),"Record not saved",Toast.LENGTH_LONG).show();
                    return;
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Record saved", Toast.LENGTH_LONG).show();
                    bloodGlucoseLevelEditText.getText().clear();
                    bglevelRadiogroup.clearCheck();
                }
            }
            @Override
            public void onFailure(Call<BloodGlucose> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
                        originalCalories = Float.parseFloat(selectedFoodItemCalories.getText().toString())/Float.parseFloat(selectedFoodItemQuantity.getText().toString());
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
                selectedFoodItemlayout.setVisibility(View.GONE);
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
                List<String> stringListSet = new ArrayList<>();
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
                                stringList.add(branded.getFoodName());
                                stringListSet.add(branded.getFoodName()+":"+branded.getServingQty()+":"+branded.getServingUnit()+":"+branded.getNfCalories());
                            }
                        }
                        foodAutoSuggestAdapter.setData(stringList);
                        foodAutoSuggestAdapter.notifyDataSetChanged();
                        foodAutoSuggestAdapterSET.setData(stringListSet);
                        foodAutoSuggestAdapterSET.notifyDataSetChanged();
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
                    Toast.makeText(getContext(),"Code: " + response.code() + "\n" + "Message" + response.message(),Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Food food = response.body();
                    String foodName = food.getFoodName();
//                    selected_food_item.setText(foodName);
                }
            }
            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                auto_complete_edittext.setText(t.getMessage());
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
