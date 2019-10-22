package com.example.gluconnect.Utils;

import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BloodGlucoseResponse;
import com.example.gluconnect.Models.LoginResponse;
import com.example.gluconnect.Models.RegisterResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LaravelAPI {

    @Headers({"Content-Type: application/json"})
    @GET("bgGET")
    Call<BloodGlucoseResponse> getBloodGlucoseLevel();


    @Headers({"Content-Type: application/json"})
    @POST("bgPOST")
    Call<BloodGlucose> recordBloodGlucoseLevel(@Body BloodGlucose bloodGlucose);

    @Headers({"Content-Type: application/json"})
    @POST("login")
    Call<LoginResponse> login(@Query("email") String email, @Query("password") String password);

    @Headers({"Content-Type: application/json"})
    @POST("register")
    Call<RegisterResponse> register(@Query("name") String name,
                                    @Query("email") String email,
                                    @Query("password") String password,
                                    @Query("gender") char gender,
                                    @Query("height") double height,
                                    @Query("weight") double weight,
                                    @Query("activity_level") String activity_level,
                                    @Query("date_of_birth") Date date_of_birth );
}
