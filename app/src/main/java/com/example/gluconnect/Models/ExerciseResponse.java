
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class ExerciseResponse {

    @SerializedName("exercises")
    private List<Exercise> exercises;
    @SerializedName("success")
    private Long Success;

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public Long getSuccess() {
        return Success;
    }

    public void setSuccess(Long success) {
        Success = success;
    }

}
