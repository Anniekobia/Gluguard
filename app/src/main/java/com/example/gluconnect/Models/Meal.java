
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

public class Meal {

    @SerializedName("calories")
    private Float mCalories;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("day")
    private String mDay;
    @SerializedName("id")
    private Long mId;
    @SerializedName("meal_name")
    private String mMealName;
    @SerializedName("meal_time")
    private String mMealTime;
    @SerializedName("quantity")
    private Float mQuantity;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private Long mUserId;

    public Meal(Float mCalories, String mMealName, String mMealTime, Float mQuantity, Long mUserId) {
        this.mCalories = mCalories;
        this.mMealName = mMealName;
        this.mMealTime = mMealTime;
        this.mQuantity = mQuantity;
        this.mUserId = mUserId;
    }

    public Float getCalories() {
        return mCalories;
    }

    public void setCalories(Float calories) {
        mCalories = calories;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getMealName() {
        return mMealName;
    }

    public void setMealName(String mealName) {
        mMealName = mealName;
    }

    public String getMealTime() {
        return mMealTime;
    }

    public void setMealTime(String mealTime) {
        mMealTime = mealTime;
    }

    public Float getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Float quantity) {
        mQuantity = quantity;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

}
