
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CommonFoodItemSuggestions {

    @SerializedName("food_name")
    private String mFoodName;
    @SerializedName("image")
    private String mImage;
    @SerializedName("tag_id")
    private String mTagId;
    @SerializedName("tag_name")
    private String mTagName;

    public String getFoodName() {
        return mFoodName;
    }

    public void setFoodName(String foodName) {
        mFoodName = foodName;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getTagId() {
        return mTagId;
    }

    public void setTagId(String tagId) {
        mTagId = tagId;
    }

    public String getTagName() {
        return mTagName;
    }

    public void setTagName(String tagName) {
        mTagName = tagName;
    }

    @Override
    public String toString() {
        return "CommonFoodItemSuggestions{" +
                "mFoodName='" + mFoodName + '\'' +
                "first"+
//                ", mImage='" + mImage + '\'' +
//                ", mTagId='" + mTagId + '\'' +
//                ", mTagName='" + mTagName + '\'' +
                '}';
    }
}
