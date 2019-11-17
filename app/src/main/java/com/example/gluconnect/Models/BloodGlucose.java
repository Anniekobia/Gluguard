
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BloodGlucose {

    @SerializedName("blood_glucose_type")
    private String mBloodGlucoseType;
    @SerializedName("blood_glucose_value")
    private Float mBloodGlucoseValue;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("day")
    private String mDay;
    @SerializedName("id")
    private Long mId;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private Long mUserId;

    public BloodGlucose(String mBloodGlucoseType, Float mBloodGlucoseValue, Long mUserId) {
        this.mBloodGlucoseType = mBloodGlucoseType;
        this.mBloodGlucoseValue = mBloodGlucoseValue;
        this.mUserId = mUserId;
    }

    public String getBloodGlucoseType() {
        return mBloodGlucoseType;
    }

    public void setBloodGlucoseType(String bloodGlucoseType) {
        mBloodGlucoseType = bloodGlucoseType;
    }

    public Float getBloodGlucoseValue() {
        return mBloodGlucoseValue;
    }

    public void setBloodGlucoseValue(Float bloodGlucoseValue) {
        mBloodGlucoseValue = bloodGlucoseValue;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

    @Override
    public String toString() {
        return "BloodGlucose{" +
                "mBloodGlucoseType='" + mBloodGlucoseType + '\'' +
                ", mBloodGlucoseValue=" + mBloodGlucoseValue +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mDay='" + mDay + '\'' +
                ", mId=" + mId +
                ", mUpdatedAt='" + mUpdatedAt + '\'' +
                ", mUserId=" + mUserId +
                '}';
    }
}
