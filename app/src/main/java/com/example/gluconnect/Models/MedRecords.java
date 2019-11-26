
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class MedRecords {

    @Expose
    private List<Medication> medications;
    @Expose
    private Long success;

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

}
