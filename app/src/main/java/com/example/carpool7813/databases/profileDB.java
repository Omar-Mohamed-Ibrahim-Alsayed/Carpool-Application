package com.example.carpool7813.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.carpool7813.interfaces.userDao;
import com.example.carpool7813.model.userProfile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {userProfile.class}, version = 1, exportSchema = false)
public abstract class profileDB extends RoomDatabase {
    public abstract userDao userDao();

    private static volatile profileDB userRoomDatabase;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static profileDB getDatabase(final Context context) {
        if (userRoomDatabase == null) {
            synchronized (profileDB.class) {
                if (userRoomDatabase == null) {
                    userRoomDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    profileDB.class, "student_database")
                            .build();
                }
            }
        }
        return userRoomDatabase;
    }
}