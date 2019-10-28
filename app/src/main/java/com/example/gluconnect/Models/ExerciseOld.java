package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

public class ExerciseOld {
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("name")
    private String name;

    @SerializedName("duration")
    private float duration;

    @SerializedName("calories_burnt")
    private String calories_burnt;

    public ExerciseOld(int user_id, String name, float duration, String calories_burnt) {
        this.user_id = user_id;
        this.name = name;
        this.duration = duration;
        this.calories_burnt = calories_burnt;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getCalories_burnt() {
        return calories_burnt;
    }

    public void setCalories_burnt(String calories_burnt) {
        this.calories_burnt = calories_burnt;
    }
}
