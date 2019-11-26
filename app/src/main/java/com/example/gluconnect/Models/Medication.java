package com.example.gluconnect.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medication {

    @SerializedName("units")
    @Expose
    private Float units;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("user_id")
    @Expose
    private Long user_id;

    public Medication(Float units, String name, Long user_id) {
        this.units = units;
        this.name = name;
        this.user_id = user_id;
    }

    public Float getUnits() {
        return units;
    }

    public void setUnits(Float units) {
        this.units = units;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
