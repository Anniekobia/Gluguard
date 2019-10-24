
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Food {

    @SerializedName("consumed_at")
    private String mConsumedAt;
    @SerializedName("food_name")
    private String mFoodName;
    @SerializedName("foods")
    private List<Food> mFoods;
    @SerializedName("full_nutrients")
    private List<FoodFullNutrient> mFullNutrients;
    @SerializedName("meal_type")
    private Long mMealType;
    @SerializedName("nf_calories")
    private Double mNfCalories;
    @SerializedName("serving_qty")
    private Long mServingQty;
    @SerializedName("serving_unit")
    private String mServingUnit;
    @SerializedName("serving_weight_grams")
    private Long mServingWeightGrams;

    public Food(String mFoodName) {
        this.mFoodName = mFoodName;
    }

    public String getConsumedAt() {
        return mConsumedAt;
    }

    public void setConsumedAt(String consumedAt) {
        mConsumedAt = consumedAt;
    }

    public String getFoodName() {
        return mFoodName;
    }

    public void setFoodName(String foodName) {
        mFoodName = foodName;
    }

    public List<Food> getFoods() {
        return mFoods;
    }

    public void setFoods(List<Food> foods) {
        mFoods = foods;
    }

    public List<FoodFullNutrient> getFullNutrients() {
        return mFullNutrients;
    }

    public void setFullNutrients(List<FoodFullNutrient> fullNutrients) {
        mFullNutrients = fullNutrients;
    }
    public Long getMealType() {
        return mMealType;
    }

    public void setMealType(Long mealType) {
        mMealType = mealType;
    }

    public Double getNfCalories() {
        return mNfCalories;
    }

    public void setNfCalories(Double nfCalories) {
        mNfCalories = nfCalories;
    }
    public Long getServingQty() {
        return mServingQty;
    }

    public void setServingQty(Long servingQty) {
        mServingQty = servingQty;
    }

    public String getServingUnit() {
        return mServingUnit;
    }

    public void setServingUnit(String servingUnit) {
        mServingUnit = servingUnit;
    }

    public Long getServingWeightGrams() {
        return mServingWeightGrams;
    }

    public void setServingWeightGrams(Long servingWeightGrams) {
        mServingWeightGrams = servingWeightGrams;
    }
}
