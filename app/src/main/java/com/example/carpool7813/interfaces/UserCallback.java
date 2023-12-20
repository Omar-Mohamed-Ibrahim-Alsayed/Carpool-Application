package com.example.carpool7813.interfaces;

import androidx.lifecycle.LiveData;

import com.example.carpool7813.model.userProfile;
import com.example.carpool7813.utilities.User;

public interface UserCallback {
    void onCallback(LiveData<userProfile> user);
    void onSpecialCall(userProfile user);
}
