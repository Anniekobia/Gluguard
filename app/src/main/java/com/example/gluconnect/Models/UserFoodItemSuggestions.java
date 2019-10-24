
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserFoodItemSuggestions {

    @SerializedName("brand_name")
    private Object mBrandName;
    @SerializedName("food_name")
    private String mFoodName;
    @SerializedName("nf_calories")
    private Double mNfCalories;
    @SerializedName("nix_brand_id")
    private Object mNixBrandId;
    @SerializedName("nix_item_id")
    private Object mNixItemId;
    @SerializedName("serving_qty")
    private Long mServingQty;
    @SerializedName("serving_unit")
    private String mServingUnit;
    @SerializedName("uuid")
    private String mUuid;

    public Object getBrandName() {
        return mBrandName;
    }

    public void setBrandName(Object brandName) {
        mBrandName = brandName;
    }

    public String getFoodName() {
        return mFoodName;
    }

    public void setFoodName(String foodName) {
        mFoodName = foodName;
    }

    public Double getNfCalories() {
        return mNfCalories;
    }

    public void setNfCalories(Double nfCalories) {
        mNfCalories = nfCalories;
    }

    public Object getNixBrandId() {
        return mNixBrandId;
    }

    public void setNixBrandId(Object nixBrandId) {
        mNixBrandId = nixBrandId;
    }

    public Object getNixItemId() {
        return mNixItemId;
    }

    public void setNixItemId(Object nixItemId) {
        mNixItemId = nixItemId;
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

    public String getUuid() {
        return mUuid;
    }

    public void setUuid(String uuid) {
        mUuid = uuid;
    }

}
