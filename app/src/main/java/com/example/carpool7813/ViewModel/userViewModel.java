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
    private final LiveData<List<userProfile>> listLiveData;
    private LiveData<userProfile> user;
    UserCallback callback;

    public userViewModel(Application application) {
        super(application);
        studentRepository = new Repository(application);
        listLiveData = studentRepository.getAllStudents();

    }

    private void updateUser(){
        studentRepository.getUser(this);
    }

    public LiveData<List<userProfile>> getAllStudentsFromVm() {
        return listLiveData;
    }

    public void getStudentFromVm(UserCallback callback) {
        this.callback = callback;
        updateUser();
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
        callback.onCallback(user);

    }
}
