package com.bypassmobile.octo.model;


import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("login")
    private final String name;

    @SerializedName("avatar_url")
    private final String profileURL;

    private final int followersCount;

    public User(String name, String profileURL, int followersCount) {
        this.name = name;
        this.profileURL = profileURL;
        this.followersCount = followersCount;
    }

    public String getName() {
        return name;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public int getFollowersCount() {
        return followersCount;
    }
}
