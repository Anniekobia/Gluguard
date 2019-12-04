
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class DailyStepsResponse {

    @Expose
    private List<DailyStep> dailySteps;
    @Expose
    private Long success;

    public List<DailyStep> getDailySteps() {
        return dailySteps;
    }

    public void setDailySteps(List<DailyStep> dailySteps) {
        this.dailySteps = dailySteps;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

}
