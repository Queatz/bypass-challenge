package com.bypassmobile.octo.model;


import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("login")
    private final String name;

    @SerializedName("avatar_url")
    private final String profileURL;

    @SerializedName("following")
    private Integer following;

    public User(String name, String profileURL, Integer following) {
        this.name = name;
        this.profileURL = profileURL;
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }
}
