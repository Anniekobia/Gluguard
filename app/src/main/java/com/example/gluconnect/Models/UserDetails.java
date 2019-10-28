
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserDetails {

    @SerializedName("activity_level")
    private String mActivityLevel;
    @SerializedName("age")
    private int mAge;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("daily_calories")
    private Long mDailyCalories;
    @SerializedName("gender")
    private String mGender;
    @SerializedName("height")
    private float mHeight;
    @SerializedName("id")
    private Long mId;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private int mUserId;
    @SerializedName("weight")
    private float mWeight;

    public UserDetails(String mActivityLevel, int mAge, String mGender, float mHeight, int mUserId, float mWeight) {
        this.mActivityLevel = mActivityLevel;
        this.mAge = mAge;
        this.mGender = mGender;
        this.mHeight = mHeight;
        this.mUserId = mUserId;
        this.mWeight = mWeight;
    }


    public String getmActivityLevel() {
        return mActivityLevel;
    }

    public void setmActivityLevel(String mActivityLevel) {
        this.mActivityLevel = mActivityLevel;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public Long getmDailyCalories() {
        return mDailyCalories;
    }

    public void setmDailyCalories(Long mDailyCalories) {
        this.mDailyCalories = mDailyCalories;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public float getmHeight() {
        return mHeight;
    }

    public void setmHeight(float mHeight) {
        this.mHeight = mHeight;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public String getmUpdatedAt() {
        return mUpdatedAt;
    }

    public void setmUpdatedAt(String mUpdatedAt) {
        this.mUpdatedAt = mUpdatedAt;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public float getmWeight() {
        return mWeight;
    }

    public void setmWeight(float mWeight) {
        this.mWeight = mWeight;
    }
}
