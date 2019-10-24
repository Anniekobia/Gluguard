
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BloodGlucoseResponse {

    @SerializedName("blood_glucose_records")
    private List<BloodGlucose> mBloodGlucoses;
    @SerializedName("success")
    private Long mSuccess;

    public List<BloodGlucose> getBloodGlucoseRecords() {
        return mBloodGlucoses;
    }

    public void setBloodGlucoseRecords(List<BloodGlucose> bloodGlucoses) {
        mBloodGlucoses = bloodGlucoses;
    }

    public Long getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Long success) {
        mSuccess = success;
    }

}
