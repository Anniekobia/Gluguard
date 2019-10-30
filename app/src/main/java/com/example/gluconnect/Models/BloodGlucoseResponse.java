
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BloodGlucoseResponse {

    @SerializedName("blood_glucose_records")
    private List<BloodGlucose> bloodGlucoses;
    @SerializedName("success")
    private Long Success;

    public List<BloodGlucose> getBloodGlucoseRecords() {
        return bloodGlucoses;
    }

    public void setBloodGlucoseRecords(List<BloodGlucose> bloodGlucoseOlds) {
        bloodGlucoses = bloodGlucoseOlds;
    }

    public Long getSuccess() {
        return Success;
    }

    public void setSuccess(Long success) {
        Success = success;
    }

}
