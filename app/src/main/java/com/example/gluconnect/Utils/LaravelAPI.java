package com.example.gluconnect.Utils;

import com.example.gluconnect.Models.BloodGlucose;
import com.example.gluconnect.Models.BloodGlucoseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LaravelAPI {

    @Headers({"Content-Type: application/json"})
    @GET("bgGET")
    Call<BloodGlucoseResponse> getBloodGlucoseLevel();


    @Headers({"Content-Type: application/json"})
    @POST("bgPOST")
    Call<BloodGlucose> recordBloodGlucoseLevel(@Body BloodGlucose bloodGlucose);
}
