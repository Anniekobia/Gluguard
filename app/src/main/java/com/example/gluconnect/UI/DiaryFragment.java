package com.example.gluconnect.UI;


import android.os.Bundle;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gluconnect.Adapters.FoodAutoSuggestAdapter;
import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BrandedFoodItemSuggestions;
import com.example.gluconnect.Models.DailyLogs;
import com.example.gluconnect.Models.Exercise;
import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodItemSuggestionsList;
import com.example.gluconnect.Models.Meal;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;
import com.example.gluconnect.Utils.NutritionixAPI;
import com.example.gluconnect.Utils.NutritionixAPIRetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment {

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
    private RadioGroup mealTimeRadiogroup;
    private AutoCompleteTextView mealsAutoCompleteTextView;

    private  View myView;

    public DiaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_diary, container, false);

        Retrofit retrofit = NutritionixAPIRetrofitClient.getRetrofitClient();
        nutritionixAPI = retrofit.create(NutritionixAPI.class);
        Retrofit retrofitl = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofitl.create(LaravelAPI.class);

        foodItemSuggestionsTextView = myView.findViewById(R.id.post_food_suggestions_requesttest_txtview);
        auto_complete_edittext = myView.findViewById(R.id.food_items_autocomplete_textview);
        selected_food_item = myView.findViewById(R.id.selected_fooditem);
        mealsAutoCompleteTextView = myView.findViewById(R.id.food_items_autocomplete_textview);
        saveDailyLogsBtn = myView.findViewById(R.id.save_logs_btn);
        mealTimeRadiogroup = myView.findViewById(R.id.meal_time_radiogroup);

        getFoodItemsSuggestions();
        saveDailyLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save_logs_btn:

                        if (mealTimeRadiogroup.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(getContext(), "Please fill in all the details", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(getContext(),"Record Saved",Toast.LENGTH_LONG).show();
//                            getFoodItemsSuggestions();
//                            recordMeals();
                        }
                        break;
//            case R.id.responseButton2:
//                break;
//            default:
//                break;
                }
            }
        });
        return myView;
    }

    private void getFoodItemsSuggestions() {
        foodAutoSuggestAdapter = new FoodAutoSuggestAdapter(getContext(),
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
        Call<FoodItemSuggestionsList> foodItemSuggestionsListCall = nutritionixAPI.getFoodItemSuggestionsList(inputtext);
        foodItemSuggestionsListCall.enqueue(new Callback<FoodItemSuggestionsList>() {
            @Override
            public void onResponse(Call<FoodItemSuggestionsList> call, Response<FoodItemSuggestionsList> response) {
                List<String> stringList = new ArrayList<>();
                if (!response.isSuccessful()){
//                    foodItemSuggestionsTextView.setText("Code: "+response.code()+"\n Message: "+response.message());
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
//                    Toast.makeText(getContext(),"Code: " + response.code() + "\n" + "Message" + response.message(),Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Food food = response.body();
                    String foodName = food.getFoodName();
                    String mealTime = selectedMealTime();
                    Meal mealnew = new Meal(foodName,mealTime,1L);
                    recordMeals(mealnew);
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

    private void recordMeals(Meal meal) {
        Call<Meal> mealCall = laravelAPI.recordMealData(meal);
        mealCall.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if (!response.isSuccessful()) {
//                    Toast.makeText(getContext(),"Code: " + response.code() + "\n" + "Message" + response.message(),Toast.LENGTH_LONG).show();
                    try {
                        Log.e("Response Failed",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                } else {
                    Meal meal1 = response.body();
                    Toast.makeText(getContext(),"Record saved",Toast.LENGTH_LONG).show();
                    Log.e("Response Success",response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
//                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                Log.e("Response Error",t.getMessage());
            }
        });

    }

}
