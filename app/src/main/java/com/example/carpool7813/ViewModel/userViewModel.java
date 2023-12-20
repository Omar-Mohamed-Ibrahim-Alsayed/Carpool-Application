package com.example.carpool7813.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carpool7813.interfaces.RepoCallBack;
import com.example.carpool7813.interfaces.UserCallback;
import com.example.carpool7813.interfaces.userDao;
import com.example.carpool7813.model.Repository;
import com.example.carpool7813.model.userProfile;
import com.google.firebase.database.core.Repo;

import java.util.List;

public class userViewModel extends AndroidViewModel implements RepoCallBack, UserCallback {
    private Repository studentRepository;
    private LiveData<userProfile> user;
    UserCallback callback;
    private boolean isFetchingUser = false;

    public userViewModel(Application application) {
        super(application);
        studentRepository = new Repository(application);

    }

    private void updateUser(){
        studentRepository.getUser(this);
    }


    public void getStudentFromVm(UserCallback callback) {
        this.callback = callback;
        if (!isFetchingUser) {
            isFetchingUser = true;
            updateUser();
        } else {
        }
    }


    public void insertUser(userProfile student) {
        studentRepository.insertUser(student);
    }

    public void deleteAll(){
        studentRepository.deleteAll();
    }

    @Override
    public void onUserReceived(LiveData<userProfile> user) {
        this.user = user;
        callback.onCallback(user);
    }

    @Override
    public void onCallback(LiveData<userProfile> user) {
        this.user = user;
        if (user != null) {
            callback.onCallback(user);
        } else {
            isFetchingUser = false;
            updateUser();
        }
    }

    @Override
    public void onSpecialCall(userProfile user) {
        if (user != null) {
            callback.onSpecialCall(user);
        } else {
            isFetchingUser = false;
            updateUser();
        }
    }
}
