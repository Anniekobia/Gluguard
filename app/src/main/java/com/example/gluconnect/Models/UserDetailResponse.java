
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserDetailResponse {

    @SerializedName("success")
    private Long mSuccess;
    @SerializedName("UserDetails")
    private UserDetails mUserDetails;

    public Long getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Long success) {
        mSuccess = success;
    }

    public UserDetails getUserDetails() {
        return mUserDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        mUserDetails = userDetails;
    }

}
