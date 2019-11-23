
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserID {


    @SerializedName("user_id")
    private Long mId;

    public UserID(Long mId) {
        this.mId = mId;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

}
