
package com.example.gluconnect.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Error {

    @SerializedName("email")
    private List<String> mEmail;

    public List<String> getEmail() {
        return mEmail;
    }

    public void setEmail(List<String> email) {
        mEmail = email;
    }

}
