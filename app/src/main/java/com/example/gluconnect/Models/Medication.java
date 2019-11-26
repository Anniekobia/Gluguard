
package com.example.gluconnect.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Medication {

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("units")
    @Expose
    private Float units;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("user_id")
    private Long userId;


    @SerializedName("day")
    private String mDay;

    public Medication(String createdAt, Long id, String name, Float units, String updatedAt, Long userId) {
        this.createdAt = createdAt;
        this.id = id;
        this.name = name;
        this.units = units;
        this.updatedAt = updatedAt;
        this.userId = userId;
    }

    public Medication(Float units,String name, Long userId) {
        this.name = name;
        this.units = units;
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getUnits() {
        return units;
    }

    public void setUnits(Float units) {
        this.units = units;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

}
