package com.example.carpool7813.model;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.carpool7813.databases.RoutDatabase;
import com.example.carpool7813.databases.profileDB;
import com.example.carpool7813.interfaces.RoutDao;
import com.example.carpool7813.interfaces.SigninCallback;
import com.example.carpool7813.interfaces.UserCallback;
import com.example.carpool7813.interfaces.userDao;
import com.example.carpool7813.utilities.User;

import java.util.List;

public class Repository implements SigninCallback {
    profileDB usersDataBase;
    RoutDatabase routDatabase;
    userDao userDao;
    RoutDao routDao;
    FirebaseHandler fb;
    private LiveData<List<userProfile>> listUsers;
    private LiveData<List<RoutEntity>> listRouts;
    private LiveData<userProfile> user;
    UserCallback callback;


    public Repository(Application application) {
        usersDataBase = profileDB.getDatabase(application);
        routDatabase = RoutDatabase.getInstance(application);
        fb = FirebaseHandler.getInstance();
        userDao = usersDataBase.userDao();
        routDao = routDatabase.routDao();
        listUsers = userDao.getStudent();
        listRouts = routDao.getAllRouts();

    }

    public void insertUser(userProfile student) {
        profileDB.databaseWriteExecutor.execute(() -> userDao.deleteAll());
        profileDB.databaseWriteExecutor.execute(() -> userDao.insert(student));
    }

    public LiveData<List<userProfile>> getAllStudents() {
        return listUsers;
    }

    public void getUser(UserCallback callback) {
        this.callback = callback;
        if(fb.getUserId()!=null){
            user = userDao.getUser();
            if (user != null) {
                user.observeForever(userProfile -> {
                    if (userProfile != null) {
                    }else{
                        Log.e("USER NAME IS","Couldnt fetch user from room. trying again." );
                        fb.getUser(this);
                    }
                });
            callback.onCallback(user);
        }}
        else{
            Log.e("User in firebase","User in firebase" );
            fb.getUser(this);
        }
    }

    public LiveData<List<RoutEntity>> getAllRouts() {
        return listRouts;
    }

    public void deleteAll(){
        profileDB.databaseWriteExecutor.execute(() -> userDao.deleteAll());
    }


    @Override
    public void onUserReceived(User user) {
        User u = user;
        insertUser(new userProfile(u.getId(),u.getName(),u.getEmail(),u.getUserType()));
        this.user = userDao.getUser();
        if(this.user != null) {
            callback.onCallback(this.user);
        }else{
            Log.e("USER NAME IS","TRYING AGAIN" );
            fb.getUser(this);
        }
    }
}