package com.example.carpool7813.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carpool7813.interfaces.RepoCallBack;
import com.example.carpool7813.interfaces.userDao;
import com.example.carpool7813.model.Repository;
import com.example.carpool7813.model.userProfile;
import com.google.firebase.database.core.Repo;

import java.util.List;

public class userViewModel extends AndroidViewModel implements RepoCallBack {
    private Repository studentRepository;
    private final LiveData<List<userProfile>> listLiveData;
    private LiveData<userProfile> user;

    public userViewModel(Application application) {
        super(application);
        studentRepository = new Repository(application);
        listLiveData = studentRepository.getAllStudents();
        user = studentRepository.getUser();

    }

    public LiveData<List<userProfile>> getAllStudentsFromVm() {
        return listLiveData;
    }

    public LiveData<userProfile> getStudentFromVm() {
        return user;
    }

    public void insertUser(userProfile student) {
        studentRepository.insertUser(student);
    }

    @Override
    public void onUserReceived(LiveData<userProfile> user) {
        this.user = user;
    }
}
