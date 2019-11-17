
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Exercise {

    @SerializedName("calories_burnt")
    private Double mCaloriesBurnt;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("duration")
    private Double mDuration;
    @SerializedName("distance")
    private Double mDistance;
    @SerializedName("exercise_name")
    private String mExerciseName;
    @SerializedName("id")
    private Long mId;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private Long mUserId;

    public Exercise(Double mCaloriesBurnt, Double mDuration,Double mDistance, String mExerciseName,  Long mUserId) {
        this.mCaloriesBurnt = mCaloriesBurnt;
        this.mDuration = mDuration;
        this.mExerciseName = mExerciseName;
        this.mUserId = mUserId;
        this.mDistance = mDistance;
    }

    public Double getCaloriesBurnt() {
        return mCaloriesBurnt;
    }

    public void setCaloriesBurnt(Double caloriesBurnt) {
        mCaloriesBurnt = caloriesBurnt;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public Double getDuration() {
        return mDuration;
    }

    public void setDuration(Double duration) {
        mDuration = duration;
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        mExerciseName = exerciseName;
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

    public Double getmDistance() {
        return mDistance;
    }

    public void setmDistance(Double mDistance) {
        this.mDistance = mDistance;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "mCaloriesBurnt=" + mCaloriesBurnt +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mDuration=" + mDuration +
                ", mDistance=" + mDistance +
                ", mExerciseName='" + mExerciseName + '\'' +
                ", mId=" + mId +
                ", mUpdatedAt='" + mUpdatedAt + '\'' +
                ", mUserId=" + mUserId +
                '}';
    }
}
