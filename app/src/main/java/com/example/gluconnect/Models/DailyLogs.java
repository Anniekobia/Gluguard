
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DailyLogs {

    @SerializedName("blood glucose")
    private BloodGlucose mBloodGlucose;
    @SerializedName("exercise")
    private Exercise mExercise;
    @SerializedName("meal")
    private Meal mMeal;

    public DailyLogs(BloodGlucose mBloodGlucose, Exercise mExercise, Meal mMeal) {
        this.mBloodGlucose = mBloodGlucose;
        this.mExercise = mExercise;
        this.mMeal = mMeal;
    }

    public BloodGlucose getBloodGlucose() {
        return mBloodGlucose;
    }

    public void setBloodGlucose(BloodGlucose bloodGlucose) {
        mBloodGlucose = bloodGlucose;
    }

    public Exercise getExercise() {
        return mExercise;
    }

    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }

    public Meal getMeal() {
        return mMeal;
    }

    public void setMeal(Meal meal) {
        mMeal = meal;
    }

}
