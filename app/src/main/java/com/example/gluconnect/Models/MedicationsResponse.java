
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MedicationsResponse {

    @SerializedName("medications")
    private List<Medication> mMedications;
    @SerializedName("success")
    private Long mSuccess;

    public List<Medication> getMedications() {
        return mMedications;
    }

    public void setMedications(List<Medication> medications) {
        mMedications = medications;
    }

    public Long getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Long success) {
        mSuccess = success;
    }

}
