package com.example.carpool7813.interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.carpool7813.model.userProfile;

import java.util.List;

@Dao
public interface userDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(userProfile user);

    @Update
    void update(userProfile user);

    @Query("SELECT * from user_table ORDER By name Asc")
    LiveData<List<userProfile>> getStudent();

    @Query("SELECT * FROM user_table LIMIT 1")
    LiveData<userProfile> getUser();


    @Query("DELETE from user_table")
    void deleteAll();

    @Delete
    void deleteuser(userProfile user);
}