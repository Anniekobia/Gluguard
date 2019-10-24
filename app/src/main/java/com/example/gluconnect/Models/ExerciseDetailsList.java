
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ExerciseDetailsList {

    @SerializedName("exercises")
    private List<ExerciseDetails> mExercises;

    public List<ExerciseDetails> getExercises() {
        return mExercises;
    }

    public void setExercises(List<ExerciseDetails> exercises) {
        mExercises = exercises;
    }

}
