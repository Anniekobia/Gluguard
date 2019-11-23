
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RegisterResponse {

    @SerializedName("error")
    private Error mError;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Integer mStatus;


    public Error getError() {
        return mError;
    }

    public void setError(Error error) {
        mError = error;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Integer getStatus() {
        return mStatus;
    }

    public void setStatus(Integer status) {
        mStatus = status;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "mError=" + mError +
                ", mMessage='" + mMessage + '\'' +
                ", mStatus=" + mStatus +
                '}';
    }
}
