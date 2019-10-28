
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Meal {

    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("day")
    private String mDay;
    @SerializedName("id")
    private Long mId;
    @SerializedName("meal_name")
    private String mMealName;
    @SerializedName("meal_type")
    private String mMealType;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private Long mUserId;

    public Meal( String mMealName, String mMealType, Long mUserId) {
        this.mMealName = mMealName;
        this.mMealType = mMealType;
        this.mUserId = mUserId;
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

    public String getMealType() {
        return mMealType;
    }

    public void setMealType(String mealType) {
        mMealType = mealType;
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
