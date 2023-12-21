package com.example.carpool7813.ViewModel;

import android.app.Application;
import android.util.Log;

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
    private static userViewModel instance;
    private Repository studentRepository;
    private static userProfile user;
    UserCallback callback;
    private boolean isFetchingUser = false;

    private userViewModel(Application application) {
        super(application);
        Log.e("DSAS","creating viewmodel");
        studentRepository = new Repository(application);
    }

    public static userViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new userViewModel(application);
        }
        if(user ==null){
            Log.e("DSAS","NO USER");
        }
        return instance;
    }

    private void updateUser(){
        studentRepository.getUser(this);
    }


    public void getStudentFromVm(UserCallback callback) {
        this.callback = callback;
        if(callback !=null){
            if(user != null){
                callback.onSpecialCall(user);
            }else{
            if (!isFetchingUser) {
                isFetchingUser = true;
                updateUser();
            } else {
            }}
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
        this.user = user.getValue();
        callback.onCallback(user);
    }

    @Override
    public void onCallback(LiveData<userProfile> user) {
        this.user = user.getValue();
        if (user != null) {
            isFetchingUser = false;
            callback.onCallback(user);
        } else {
            if(!isFetchingUser){
                isFetchingUser = true;
                updateUser();
            }
        }
    }

    @Override
    public void onSpecialCall(userProfile user) {
        this.user = user;
        if (user != null) {
            isFetchingUser = false;
            callback.onSpecialCall(user);
        } else {
            if(!isFetchingUser){
            isFetchingUser = true;
            updateUser();
            }
        }
    }
}
