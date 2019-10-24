
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BrandedFoodItemSuggestions {

    @SerializedName("brand_name")
    private String mBrandName;
    @SerializedName("brand_name_item_name")
    private String mBrandNameItemName;
    @SerializedName("brand_type")
    private Long mBrandType;
    @SerializedName("food_name")
    private String mFoodName;
    @SerializedName("image")
    private Object mImage;
    @SerializedName("nf_calories")
    private Float mNfCalories;
    @SerializedName("nix_brand_id")
    private String mNixBrandId;
    @SerializedName("nix_item_id")
    private String mNixItemId;
    @SerializedName("serving_qty")
    private Float mServingQty;
    @SerializedName("serving_unit")
    private String mServingUnit;

    public String getBrandName() {
        return mBrandName;
    }

    public void setBrandName(String brandName) {
        mBrandName = brandName;
    }

    public String getBrandNameItemName() {
        return mBrandNameItemName;
    }

    public void setBrandNameItemName(String brandNameItemName) {
        mBrandNameItemName = brandNameItemName;
    }

    public Long getBrandType() {
        return mBrandType;
    }

    public void setBrandType(Long brandType) {
        mBrandType = brandType;
    }

    public String getFoodName() {
        return mFoodName;
    }

    public void setFoodName(String foodName) {
        mFoodName = foodName;
    }

    public Object getImage() {
        return mImage;
    }

    public void setImage(Object image) {
        mImage = image;
    }

    public Float getNfCalories() {
        return mNfCalories;
    }

    public void setNfCalories(Float nfCalories) {
        mNfCalories = nfCalories;
    }

    public String getNixBrandId() {
        return mNixBrandId;
    }

    public void setNixBrandId(String nixBrandId) {
        mNixBrandId = nixBrandId;
    }

    public String getNixItemId() {
        return mNixItemId;
    }

    public void setNixItemId(String nixItemId) {
        mNixItemId = nixItemId;
    }

    public Float getServingQty() {
        return mServingQty;
    }

    public void setServingQty(Float servingQty) {
        mServingQty = servingQty;
    }

    public String getServingUnit() {
        return mServingUnit;
    }

    public void setServingUnit(String servingUnit) {
        mServingUnit = servingUnit;
    }

    @Override
    public String toString() {
        return "BrandedFoodItemSuggestions{" +
//                "mBrandName='" + mBrandName + '\'' +
//                ", mBrandNameItemName='" + mBrandNameItemName + '\'' +
//                ", mBrandType=" + mBrandType +
                ", mFoodName='" + mFoodName + '\'' +
//                ", mImage=" + mImage +
                ", mNfCalories=" + mNfCalories +
//                ", mNixBrandId='" + mNixBrandId + '\'' +
//                ", mNixItemId='" + mNixItemId + '\'' +
//                ", mServingQty=" + mServingQty +
//                ", mServingUnit='" + mServingUnit + '\'' +
                '}';
    }
}
