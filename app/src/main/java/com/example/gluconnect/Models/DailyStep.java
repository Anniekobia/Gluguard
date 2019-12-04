
package com.example.gluconnect.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DailyStep {

    @SerializedName("calories_burnt")
    private Float caloriesBurnt;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("day")
    private String day;
    @SerializedName("id")
    private Long id;
    @SerializedName("step_count")
    private int stepCount;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("user_id")
    private Long userId;

    public DailyStep(Float caloriesBurnt, int stepCount, Long userId) {
        this.caloriesBurnt = caloriesBurnt;
        this.stepCount = stepCount;
        this.userId = userId;
    }

    public Float getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public void setCaloriesBurnt(Float caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
