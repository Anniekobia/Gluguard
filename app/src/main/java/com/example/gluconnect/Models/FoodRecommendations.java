
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class FoodRecommendations {

    @SerializedName("food_recommendations")
    private List<FoodRecommendation> mFoodRecommendations;
    @SerializedName("success")
    private Long mSuccess;

    public List<FoodRecommendation> getFoodRecommendations() {
        return mFoodRecommendations;
    }

    public void setFoodRecommendations(List<FoodRecommendation> foodRecommendations) {
        mFoodRecommendations = foodRecommendations;
    }

    public Long getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Long success) {
        mSuccess = success;
    }

}
