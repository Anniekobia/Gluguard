
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RegisterResponse {

    @SerializedName("success")
    private Success mSuccess;

    public Success getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Success success) {
        mSuccess = success;
    }

}
