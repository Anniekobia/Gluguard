package com.example.gluconnect.UI;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gluconnect.Adapters.FoodRecommendationsAdapter;
import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BloodGlucoseResponse;
import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodRecommendation;
import com.example.gluconnect.Models.FoodRecommendations;
import com.example.gluconnect.R;
import com.example.gluconnect.Utils.LaravelAPI;
import com.example.gluconnect.Utils.LaravelAPIRetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class TipsFragment extends Fragment {
    private List<FoodRecommendation> foodsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodRecommendationsAdapter mAdapter;
    private View myview;
    private LaravelAPI laravelAPI;
    private ProgressBar progressBar;
    TextView usermsg;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int userid;

    public TipsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_tips, container, false);

        Retrofit retrofit = LaravelAPIRetrofitClient.getRetrofitClient();
        laravelAPI = retrofit.create(LaravelAPI.class);
        sharedPreferences = getContext().getSharedPreferences("MyPreferences", 0);
        editor = sharedPreferences.edit();

        progressBar = myview.findViewById(R.id.progressBar);
        recyclerView = (myview.findViewById(R.id.meals_recycler_view));
        mAdapter = new FoodRecommendationsAdapter(foodsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        usermsg = myview.findViewById(R.id.usermessage);

        Log.e("UserID", String.valueOf(sharedPreferences.getInt("UserID", 6)));
        getBloodGlucoseLevels();
        return myview;
    }

    private void getBloodGlucoseLevels() {
        progressBar.setVisibility(View.VISIBLE);
        userid = sharedPreferences.getInt("UserID", 6);
        Call<BloodGlucoseResponse> bloodGlucoseResponseCall = laravelAPI.getBloodGlucoseLevel();
        bloodGlucoseResponseCall.enqueue(new Callback<BloodGlucoseResponse>() {
            @Override
            public void onResponse(Call<BloodGlucoseResponse> call, Response<BloodGlucoseResponse> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Get Food", "Food Items not picked");
                    usermsg.setText("Something happened, please try again later");
                    usermsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    ArrayList<BloodGlucose> BG = new ArrayList<>();
                    ArrayList<Float> bgLeveValues = new ArrayList<>();
                    BloodGlucoseResponse bloodGlucoseResponse = response.body();
                    for (BloodGlucose bloodGlucose : bloodGlucoseResponse.getBloodGlucoseRecords()) {
                        Log.e("BGw", bloodGlucose.toString());
                        if (bloodGlucose.getUserId() == userid) {
                            BG.add(bloodGlucose);
                            bgLeveValues.add(bloodGlucose.getBloodGlucoseValue());
                        }
                    }
                    try {
                        BloodGlucose latest = BG.get(BG.size() - 1);
                        getFoodRecommendations(latest.getBloodGlucoseValue(), latest.getBloodGlucoseType());
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                        progressBar.setVisibility(View.GONE);
                        usermsg.setText("Record your blood glucose to get recommendations");
                        usermsg.setVisibility(View.VISIBLE);
                    }
                }
            }
                @Override
                public void onFailure (Call < BloodGlucoseResponse > call, Throwable t){
                    Log.e("Get BG", t.getMessage());
                    usermsg.setText("Something happened, please try again later");
                    usermsg.setVisibility(View.VISIBLE);
                }
            });
        }

    private void getFoodRecommendations(final Float bloodglucose, final String bloodGlucoseType) {
        Call<FoodRecommendations> foodRecommendationsCall = laravelAPI.getFoodRecommendations();
        foodRecommendationsCall.enqueue(new Callback<FoodRecommendations>() {
            @Override
            public void onResponse(Call<FoodRecommendations> call, Response<FoodRecommendations> response) {
                if (!response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Get Food", "Food Items not picked");
                    usermsg.setText("Something happened, please try again later");
                    usermsg.setVisibility(View.VISIBLE);
                    return;
                } else {
                    List<FoodRecommendation> lowGIFoods = new ArrayList<>();
                    List<FoodRecommendation> mediumGIFoods = new ArrayList<>();
                    List<FoodRecommendation> highGIFoods = new ArrayList<>();
                    FoodRecommendations foodRecommendations = response.body();
                    for (FoodRecommendation foodRecommendation : foodRecommendations.getFoodRecommendations()) {
                        FoodRecommendation food = new FoodRecommendation(foodRecommendation.getFoodCategory(), foodRecommendation.getFoodName(), foodRecommendation.getServingSize(), foodRecommendation.getGlycemicIndex());
                        if (food.getGlycemicIndex() <= 55) {
                            lowGIFoods.add(food);
                        } else if (food.getGlycemicIndex() > 55 && food.getGlycemicIndex() <= 69) {
                            mediumGIFoods.add(food);
                        } else if (food.getGlycemicIndex() >= 70) {
                            highGIFoods.add(food);
                        }
                    }
                    Random rand = new Random();
                    int numberOfFoodsRecommended = 7;
                    for (int i = 0; i < numberOfFoodsRecommended; i++) {
                        if ((bloodglucose > 126 && bloodGlucoseType.equals("fasting")) || (bloodglucose > 198 && (bloodGlucoseType.equals("before meal") || bloodGlucoseType.equals("after meal") || bloodGlucoseType.equals("at night")))) {
                            int randomIndex = rand.nextInt(lowGIFoods.size());
                            FoodRecommendation food = lowGIFoods.get(randomIndex);
                            lowGIFoods.remove(randomIndex);
                            foodsList.add(food);
                        } else if (bloodglucose > 70.2 && ((bloodglucose <= 126 && bloodGlucoseType.equals("fasting")) || (bloodglucose <= 198 && (bloodGlucoseType.equals("before meal") || bloodGlucoseType.equals("after meal") || bloodGlucoseType.equals("at night"))))) {
                            int randomIndex = rand.nextInt(mediumGIFoods.size());
                            FoodRecommendation food = mediumGIFoods.get(randomIndex);
                            mediumGIFoods.remove(randomIndex);
                            foodsList.add(food);
                        } else if (bloodglucose < 70.2) {
                            int randomIndex = rand.nextInt(highGIFoods.size());
                            FoodRecommendation food = highGIFoods.get(randomIndex);
                            highGIFoods.remove(randomIndex);
                            foodsList.add(food);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FoodRecommendations> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Failed", t.getMessage());
                usermsg.setText("Something happened, please try again later");
                usermsg.setVisibility(View.VISIBLE);
            }
        });
    }

}
