
package com.example.gluconnect.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MedicationResponse {

    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String units;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("user_id")
    private String userId;

    public MedicationResponse(String createdAt, Long id, String name, String units, String updatedAt, String userId) {
        this.createdAt = createdAt;
        this.id = id;
        this.name = name;
        this.units = units;
        this.updatedAt = updatedAt;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
