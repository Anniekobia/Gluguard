
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class MealResponse {

    @SerializedName("meals")
    private List<Meal> meals;
    @SerializedName("success")
    private Long Success;

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public Long getSuccess() {
        return Success;
    }

    public void setSuccess(Long success) {
        Success = success;
    }

}
