package com.example.carpool7813.interfaces;

import androidx.lifecycle.LiveData;

import com.example.carpool7813.model.userProfile;

public interface RepoCallBack {
    void onUserReceived(LiveData<userProfile> user);
}
