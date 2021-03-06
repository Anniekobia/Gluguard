package com.example.gluconnect.Utils;

import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BloodGlucoseResponse;
import com.example.gluconnect.Models.DailyStepsResponse;
import com.example.gluconnect.Models.DailyStep;
import com.example.gluconnect.Models.DailyStepPOST;
import com.example.gluconnect.Models.Exercise;
import com.example.gluconnect.Models.ExerciseResponse;
import com.example.gluconnect.Models.FoodRecommendations;
import com.example.gluconnect.Models.LoginPost;
import com.example.gluconnect.Models.LoginResponse;
import com.example.gluconnect.Models.Meal;
import com.example.gluconnect.Models.MealResponse;
import com.example.gluconnect.Models.Medication;
import com.example.gluconnect.Models.MedicationResponse;
import com.example.gluconnect.Models.MedicationsResponse;
import com.example.gluconnect.Models.RegisterPOST;
import com.example.gluconnect.Models.RegisterResponse;
import com.example.gluconnect.Models.UserDetailResponse;
import com.example.gluconnect.Models.UserDetailsN;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LaravelAPI {

    @Headers({"Content-Type: application/json"})
    @GET("steps")
    Call<DailyStepsResponse> getDailySteps();

    @Headers({"Content-Type: application/json"})
    @POST("daily/steps")
    Call<DailyStep> recordDailySteps(@Body DailyStep dailystep);

    @Headers({"Content-Type: application/json"})
    @POST("daily/steps/update")
    Call<DailyStep> updatedDailySteps(@Body DailyStepPOST dailyStepPOST);

    @Headers({"Content-Type: application/json"})
    @POST("daily/bloodglucose")
    Call<BloodGlucose> recordBloodGlucoseLevel(@Body BloodGlucose bloodGlucose);

    @Headers({"Content-Type: application/json"})
    @GET("meals")
    Call<MealResponse> getMeals();

    @Headers({"Content-Type: application/json"})
    @GET("exercises")
    Call<ExerciseResponse> getExercises();

    @Headers({"Content-Type: application/json"})
    @GET("bloodglucose")
    Call<BloodGlucoseResponse> getBloodGlucoseLevel();

    @Headers({"Content-Type: application/json"})
    @GET("medications")
    Call<MedicationsResponse> getMedications();

    @Headers({"Content-Type: application/json"})
    @GET("food/recommendations")
    Call<FoodRecommendations> getFoodRecommendations();

    @Headers({"Content-Type: application/json"})
    @POST("daily/medications")
    Call<MedicationResponse> recordMedication(@Body Medication medication);

    @Headers({"Content-Type: application/json"})
    @POST("daily/meals")
    Call<Meal> recordMealData(@Body Meal meal);

    @Headers({"Content-Type: application/json"})
    @POST("user/details")
    Call<UserDetailsN> saveUserDetails(@Body UserDetailsN userDetailsN);

    @Headers({"Content-Type: application/json"})
    @POST("daily/exercise")
    Call<Exercise> recordExerciseData(@Body Exercise exercise);

    @Headers({"Content-Type: application/json"})
    @POST("auth/signup")
    Call<RegisterResponse> register(@Body RegisterPOST registerPOST);

    @Headers({"Content-Type: application/json"})
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginPost loginPost);

    @Headers({"Content-Type: application/json"})
    @GET("userdetails")
    Call<UserDetailResponse> getUserDetails(@Query("user_id") int user_id);


}
