package com.example.carpool7813.model;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.carpool7813.R;
import com.example.carpool7813.databases.RoutDatabase;
import com.example.carpool7813.databases.profileDB;
import com.example.carpool7813.interfaces.RoutDao;
import com.example.carpool7813.interfaces.SigninCallback;
import com.example.carpool7813.interfaces.UserCallback;
import com.example.carpool7813.interfaces.userDao;
import com.example.carpool7813.utilities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Repository implements SigninCallback {
    profileDB usersDataBase;
    RoutDatabase routDatabase;
    userDao userDao;
    RoutDao routDao;
    FirebaseHandler fb;
    private LiveData<userProfile> user = null;
    UserCallback callback;
    private boolean isFetchingUser = false;




    public Repository(Application application) {
        usersDataBase = profileDB.getDatabase(application);
        routDatabase = RoutDatabase.getInstance(application);
        fb = FirebaseHandler.getInstance();
        userDao = usersDataBase.userDao();
        routDao = routDatabase.routDao();

        user = userDao.getUser();
        if(user == null){
            fb.getUser(this);
        }

    }

    public void insertUser(userProfile u) {
        profileDB.databaseWriteExecutor.execute(() -> userDao.deleteAll());
        Log.e("USER NAME IS","USER TO ADD HAS NAME: "+u.getName() );
        profileDB.databaseWriteExecutor.execute(() -> userDao.insert(u));
    }


    public void getUser(UserCallback callback) {
        this.callback = callback;

        if (user == null) {
            user = userDao.getUser();
            if (user != null) {
                userProfile userProfile = user.getValue();
                if (userProfile != null) {
                    insertUser(userProfile);
                    callback.onCallback(user);
                }else{
                }
            }else{
                    if (!isFetchingUser) {
                        Log.e("USER NAME IS","TRYING AGAIN" );

                        isFetchingUser = true;
                        fb.getUser(this);
                    }
            }
        }else{
            Log.e("USER NAME IS","TRYING AGAIN" );

            isFetchingUser = true;
            fb.getUser(this);
        }

    }


    public void deleteAll(){
        profileDB.databaseWriteExecutor.execute(() -> userDao.deleteAll());
    }


    @Override
    public void onUserReceived(User u) {
        if (u != null) {
            Log.e("USER NAME IS", "User received from fb");
            insertUser(new userProfile(u.getId(), u.getName(), u.getEmail(), u.getUserType()));

            userDao.getUser().observeForever(new Observer<userProfile>() {
                @Override
                public void onChanged(userProfile userProfile) {
                    if (userProfile != null) {
                        Log.e("USER NAME IS", "User inserted to room: " + userProfile.getEmail());



                        callback.onSpecialCall(userProfile);
                    } else {
                        Log.e("USER NAME IS", "User is not returned to profile");
                    }
                }
            });
        } else {
            Log.e("USER NAME IS", "TRYING AGAIN");
            isFetchingUser = false;
            fb.getUser(this);
        }
    }

}