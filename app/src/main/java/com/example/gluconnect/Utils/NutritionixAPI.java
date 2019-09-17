package com.example.gluconnect.Utils;


import com.example.gluconnect.Models.ExerciseData;
import com.example.gluconnect.Models.ExerciseDetailsList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NutritionixAPI {

    @Headers({
            "Content-Type: application/json",
            "x-app-id: a772bb52",
            "x-app-key: 50a93c9662c38ada92b3f1aebcbfd2d7"
    })
    @POST("natural/exercise")
    Call<ExerciseDetailsList> getExerciseDetailsList(@Body ExerciseData exerciseData);


}
