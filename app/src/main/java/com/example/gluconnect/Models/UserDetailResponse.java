
package com.example.gluconnect.Models;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class UserDetailResponse {

    @Expose
    private Long success;
    @Expose
    private UserDetails userDetails;

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

}
