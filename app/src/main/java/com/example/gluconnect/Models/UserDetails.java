
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserDetails {

    @SerializedName("activity_level")
    private String mActivityLevel;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("daily_calories")
    private Double mDailyCalories;
    @SerializedName("date_of_birth")
    private String mDateOfBirth;
    @SerializedName("gender")
    private String mGender;
    @SerializedName("height")
    private Long mHeight;
    @SerializedName("hospital")
    private String mHospital;
    @SerializedName("id")
    private Long mId;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private Long mUserId;
    @SerializedName("weight")
    private Long mWeight;

    public String getActivityLevel() {
        return mActivityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        mActivityLevel = activityLevel;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public Double getDailyCalories() {
        return mDailyCalories;
    }

    public void setDailyCalories(Double dailyCalories) {
        mDailyCalories = dailyCalories;
    }

    public String getDateOfBirth() {
        return mDateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        mDateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public Long getHeight() {
        return mHeight;
    }

    public void setHeight(Long height) {
        mHeight = height;
    }

    public String getHospital() {
        return mHospital;
    }

    public void setHospital(String hospital) {
        mHospital = hospital;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
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

    public Long getWeight() {
        return mWeight;
    }

    public void setWeight(Long weight) {
        mWeight = weight;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "mActivityLevel='" + mActivityLevel + '\'' +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mDailyCalories=" + mDailyCalories +
                ", mDateOfBirth='" + mDateOfBirth + '\'' +
                ", mGender='" + mGender + '\'' +
                ", mHeight=" + mHeight +
                ", mHospital='" + mHospital + '\'' +
                ", mId=" + mId +
                ", mUpdatedAt='" + mUpdatedAt + '\'' +
                ", mUserId=" + mUserId +
                ", mWeight=" + mWeight +
                '}';
    }
}
