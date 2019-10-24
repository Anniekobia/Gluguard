
package com.example.gluconnect.Models;

import com.google.gson.annotations.SerializedName;

public class ExerciseDetails {

    @SerializedName("benefits")
    private Object mBenefits;
    @SerializedName("compendium_code")
    private Long mCompendiumCode;
    @SerializedName("description")
    private Object mDescription;
    @SerializedName("duration_min")
    private Long mDurationMin;
    @SerializedName("met")
    private Double mMet;
    @SerializedName("name")
    private String mName;
    @SerializedName("nf_calories")
    private Double mNfCalories;
    @SerializedName("photo")
    private Photo mPhoto;
    @SerializedName("tag_id")
    private Long mTagId;
    @SerializedName("user_input")
    private String mUserInput;

    public Object getBenefits() {
        return mBenefits;
    }

    public void setBenefits(Object benefits) {
        mBenefits = benefits;
    }

    public Long getCompendiumCode() {
        return mCompendiumCode;
    }

    public void setCompendiumCode(Long compendiumCode) {
        mCompendiumCode = compendiumCode;
    }

    public Object getDescription() {
        return mDescription;
    }

    public void setDescription(Object description) {
        mDescription = description;
    }

    public Long getDurationMin() {
        return mDurationMin;
    }

    public void setDurationMin(Long durationMin) {
        mDurationMin = durationMin;
    }

    public Double getMet() {
        return mMet;
    }

    public void setMet(Double met) {
        mMet = met;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Double getNfCalories() {
        return mNfCalories;
    }

    public void setNfCalories(Double nfCalories) {
        mNfCalories = nfCalories;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    public Long getTagId() {
        return mTagId;
    }

    public void setTagId(Long tagId) {
        mTagId = tagId;
    }

    public String getUserInput() {
        return mUserInput;
    }

    public void setUserInput(String userInput) {
        mUserInput = userInput;
    }

    @Override
    public String toString() {
        return
                "Response tag Id=" + mTagId + "\n" +
                        "Exercise Type='" + mName + "\n"+
                "Duration in Minutes=" + mDurationMin + "\n"+
                "Amount of Calories Burnt=" + mNfCalories +"\n";
    }
}
