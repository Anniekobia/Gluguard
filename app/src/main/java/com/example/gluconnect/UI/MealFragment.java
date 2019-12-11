package com.example.gluconnect.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gluconnect.Adapters.FoodAutoSuggestAdapter;
import com.example.gluconnect.Adapters.FoodAutoSuggestAdapterSET;
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

import static android.view.View.GONE;


public class MealFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    View myView;
    Spinner mealSpinner;
    LaravelAPI laravelAPI;
    NutritionixAPI nutritionixAPI;
    ProgressBar progressBar;

    private TextView foodItemSuggestionsTextView;
    private FoodAutoSuggestAdapter foodAutoSuggestAdapter;
    private FoodAutoSuggestAdapterSET foodAutoSuggestAdapterSET;
    private AutoCompleteTextView auto_complete_edittext;
    private TextView selected_food_item;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private Button mealSaveBtn;
    private EditText selectedFoodItemQuantity;
    private TextView selectedFoodItemMetrc;
    private TextView selectedFoodItemCalories;
    private ConstraintLayout selectedFoodItemlayout;
    Float originalCalories;
    private SharedPreferences sharedPreferences;

    RelativeLayout relativeLayout;
    TextView errormsg;

    public MealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_meal, container, false);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        Retrofit retrofit1 = NutritionixAPIRetrofitClient.getRetrofitClient();
        nutritionixAPI = retrofit1.create(NutritionixAPI.class);
        sharedPreferences = getContext().getSharedPreferences("MyPreferences", 0);
        errormsg = myView.findViewById(R.id.error_msg);

        relativeLayout = myView.findViewById(R.id.meals_rl);

        progressBar = myView.findViewById(R.id.progressBar);
        progressBar.bringToFront();
        foodItemSuggestionsTextView = myView.findViewById(R.id.post_food_suggestions_requesttest_txtview);
        auto_complete_edittext = myView.findViewById(R.id.food_items_autocomplete_textview);
        selected_food_item = myView.findViewById(R.id.selected_fooditem);
        selectedFoodItemCalories = myView.findViewById(R.id.selected_fooditem_calories);
        selectedFoodItemMetrc = myView.findViewById(R.id.selected_fooditem_metric);
        selectedFoodItemQuantity = myView.findViewById(R.id.selected_fooditem_quantity);
        selectedFoodItemlayout = myView.findViewById(R.id.selected_fooditem_layout);
        mealSaveBtn = myView.findViewById(R.id.save_meal_btn);
        mealSpinner = myView.findViewById(R.id.meal_spinner);


        mealSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordMealData();
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



        getFoodItemsSuggestions();
        setSpinner();
        return myView;
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
        String time = mealSpinner.getSelectedItem().toString();
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
                    Toast.makeText(getContext(),"Meal Successfully Saved",Toast.LENGTH_LONG).show();
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
            }
        });
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

    private void getFoodItemSuggestionsList(final String inputtext) {
        errormsg.setVisibility(GONE);
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

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.meal_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealSpinner.setAdapter(adapter);
        mealSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
