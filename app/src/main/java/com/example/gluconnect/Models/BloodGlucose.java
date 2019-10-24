
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BloodGlucose {

    @SerializedName("blood_glucose_level")
    private Double mBloodGlucoseLevel;

    @SerializedName("id")
    private Long mId;

    @SerializedName("type")
    private String mType;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("created_at")
    private String mCreatedAt;

    @SerializedName("updated_at")
    private String mUpdatedAt;


    public BloodGlucose(double mBloodGlucoseLevel, String mType, int user_id) {
        this.mBloodGlucoseLevel = mBloodGlucoseLevel;
        this.mType = mType;
        this.user_id = user_id;
    }


    public Double getBloodGlucoseLevel() {
        return mBloodGlucoseLevel;
    }

    public void setBloodGlucoseLevel(Double bloodGlucoseLevel) {
        mBloodGlucoseLevel = bloodGlucoseLevel;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public String getmUpdatedAt() {
        return mUpdatedAt;
    }

    public void setmUpdatedAt(String mUpdatedAt) {
        this.mUpdatedAt = mUpdatedAt;
    }

    @Override
    public String toString() {
        return "BloodGlucose{" +
                "mBloodGlucoseLevel=" + mBloodGlucoseLevel +
                ", mId=" + mId +
                ", mType='" + mType + '\'' +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                '}';
    }
}
