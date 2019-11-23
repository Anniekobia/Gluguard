
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RegisterPOST {

    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("password_confirmation")
    private String mPasswordConfirmation;
    @SerializedName("user_type")
    private String mUserType;
    @SerializedName("username")
    private String mUsername;

    public RegisterPOST(String mEmail, String mPassword, String mPasswordConfirmation, String mUserType, String mUsername) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mPasswordConfirmation = mPasswordConfirmation;
        this.mUserType = mUserType;
        this.mUsername = mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPasswordConfirmation() {
        return mPasswordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        mPasswordConfirmation = passwordConfirmation;
    }

    public String getUserType() {
        return mUserType;
    }

    public void setUserType(String userType) {
        mUserType = userType;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}
