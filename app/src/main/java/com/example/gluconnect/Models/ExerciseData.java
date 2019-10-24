
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

public class ExerciseData {

    @SerializedName("age")
    private int mAge;
    @SerializedName("gender")
    private String mGender;
    @SerializedName("height_cm")
    private Double mHeightCm;
    @SerializedName("query")
    private String mQuery;
    @SerializedName("weight_kg")
    private Double mWeightKg;

    public ExerciseData(int mAge, String mGender, Double mHeightCm, String mQuery, Double mWeightKg) {
        this.mAge = mAge;
        this.mGender = mGender;
        this.mHeightCm = mHeightCm;
        this.mQuery = mQuery;
        this.mWeightKg = mWeightKg;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public Double getHeightCm() {
        return mHeightCm;
    }

    public void setHeightCm(Double heightCm) {
        mHeightCm = heightCm;
    }

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public Double getWeightKg() {
        return mWeightKg;
    }

    public void setWeightKg(Double weightKg) {
        mWeightKg = weightKg;
    }

}
