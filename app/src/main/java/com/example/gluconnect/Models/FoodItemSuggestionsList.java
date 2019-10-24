
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class FoodItemSuggestionsList {

    @SerializedName("branded")
    private List<BrandedFoodItemSuggestions> mBranded;
    @SerializedName("common")
    private List<CommonFoodItemSuggestions> mCommon;
    @SerializedName("self")
    private List<UserFoodItemSuggestions> mSelf;

    public List<BrandedFoodItemSuggestions> getBranded() {
        return mBranded;
    }

    public void setBranded(List<BrandedFoodItemSuggestions> branded) {
        mBranded = branded;
    }

    public List<CommonFoodItemSuggestions> getCommon() {
        return mCommon;
    }

    public void setCommon(List<CommonFoodItemSuggestions> common) {
        mCommon = common;
    }

    public List<UserFoodItemSuggestions> getSelf() {
        return mSelf;
    }

    public void setSelf(List<UserFoodItemSuggestions> self) {
        mSelf = self;
    }

}
