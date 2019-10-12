package com.example.gluconnect.Utils;


import com.example.gluconnect.Models.Food;
import com.example.gluconnect.Models.FoodItemSuggestionsList;
import com.example.gluconnect.Models.ExerciseData;
import com.example.gluconnect.Models.ExerciseDetailsList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NutritionixAPI {

    @Headers({
            "Content-Type: application/json",
            "x-app-id: a772bb52",
            "x-app-key: 50a93c9662c38ada92b3f1aebcbfd2d7"
    })
    @POST("natural/exercise")
    Call<ExerciseDetailsList> getExerciseDetailsList(@Body ExerciseData exerciseData);


    @Headers({
            "Content-Type: application/json",
            "x-app-id: a772bb52",
            "x-app-key: 50a93c9662c38ada92b3f1aebcbfd2d7"
    })
    @GET("search/instant")
    Call<FoodItemSuggestionsList> getFoodItemSuggestionsList(@Query("query") String query);


    @Headers({
            "Content-Type: application/json",
            "x-app-id: a772bb52",
            "x-app-key: 50a93c9662c38ada92b3f1aebcbfd2d7"
    })
    @POST("natural/nutrients")
    Call<Food> getFoodItemDetails(@Body Food food);


}
