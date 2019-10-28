
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BloodGlucoseResponse {

    @SerializedName("blood_glucose_records")
    private List<BloodGlucoseOld> mBloodGlucoseOlds;
    @SerializedName("success")
    private Long mSuccess;

    public List<BloodGlucoseOld> getBloodGlucoseRecords() {
        return mBloodGlucoseOlds;
    }

    public void setBloodGlucoseRecords(List<BloodGlucoseOld> bloodGlucoseOlds) {
        mBloodGlucoseOlds = bloodGlucoseOlds;
    }

    public Long getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Long success) {
        mSuccess = success;
    }

}
