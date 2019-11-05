
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class FoodRecommendation {

    @SerializedName("food_category")
    private String mFoodCategory;
    @SerializedName("food_name")
    private String mFoodName;
    @SerializedName("glycemic_index")
    private Long mGlycemicIndex;
    @SerializedName("id")
    private Long mId;
    @SerializedName("serving_size")
    private Long mServingSize;


    public FoodRecommendation(String mFoodCategory, String mFoodName, Long mServingSize,Long mGlycemicIndex) {
        this.mFoodCategory = mFoodCategory;
        this.mFoodName = mFoodName;
        this.mServingSize = mServingSize;
        this.mGlycemicIndex =mGlycemicIndex;
    }

    public String getFoodCategory() {
        return mFoodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        mFoodCategory = foodCategory;
    }

    public String getFoodName() {
        return mFoodName;
    }

    public void setFoodName(String foodName) {
        mFoodName = foodName;
    }

    public Long getGlycemicIndex() {
        return mGlycemicIndex;
    }

    public void setGlycemicIndex(Long glycemicIndex) {
        mGlycemicIndex = glycemicIndex;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getServingSize() {
        return mServingSize;
    }

    public void setServingSize(Long servingSize) {
        mServingSize = servingSize;
    }

    @Override
    public String toString() {
        return "FoodRecommendation{" +
                "mFoodCategory='" + mFoodCategory + '\'' +
                ", mFoodName='" + mFoodName + '\'' +
                ", mGlycemicIndex=" + mGlycemicIndex +
                ", mId=" + mId +
                ", mServingSize=" + mServingSize +
                '}';
    }
}
