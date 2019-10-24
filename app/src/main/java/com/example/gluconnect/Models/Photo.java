
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("highres")
    private String mHighres;
    @SerializedName("is_user_uploaded")
    private Boolean mIsUserUploaded;
    @SerializedName("thumb")
    private String mThumb;

    public String getHighres() {
        return mHighres;
    }

    public void setHighres(String highres) {
        mHighres = highres;
    }

    public Boolean getIsUserUploaded() {
        return mIsUserUploaded;
    }

    public void setIsUserUploaded(Boolean isUserUploaded) {
        mIsUserUploaded = isUserUploaded;
    }

    public String getThumb() {
        return mThumb;
    }

    public void setThumb(String thumb) {
        mThumb = thumb;
    }

}
