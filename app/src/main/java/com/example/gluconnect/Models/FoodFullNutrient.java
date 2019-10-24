
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class FoodFullNutrient {

    @SerializedName("attr_id")
    private Long mAttrId;
    @SerializedName("value")
    private Double mValue;

    public Long getAttrId() {
        return mAttrId;
    }

    public void setAttrId(Long attrId) {
        mAttrId = attrId;
    }

    public Double getValue() {
        return mValue;
    }

    public void setValue(Double value) {
        mValue = value;
    }

}
