package com.example.carpool7813.interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.carpool7813.model.RoutEntity;

import java.util.List;

@Dao
public interface RoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRout(RoutEntity rout);

    @Update
    void updateRout(RoutEntity rout);

    @Query("SELECT * FROM rout_table WHERE rideId = :rideId")
    LiveData<RoutEntity> getRoutById(String rideId);

    @Query("SELECT * FROM rout_table ORDER BY departureTime ASC")
    LiveData<List<RoutEntity>> getAllRouts();

    @Query("DELETE FROM rout_table WHERE rideId = :rideId")
    void deleteRoutById(String rideId);

    @Query("DELETE FROM rout_table")
    void deleteAllRouts();
}

