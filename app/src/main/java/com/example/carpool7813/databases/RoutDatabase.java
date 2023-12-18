package com.example.carpool7813.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.carpool7813.interfaces.RoutDao;
import com.example.carpool7813.model.RoutEntity;

@Database(entities = {RoutEntity.class}, version = 1)
public abstract class RoutDatabase extends RoomDatabase {
    public abstract RoutDao routDao();

    private static volatile RoutDatabase INSTANCE;

    // Method to get the database instance
    public static synchronized RoutDatabase getInstance(android.content.Context context) {
        if (INSTANCE == null) {
            INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                            RoutDatabase.class, "rout_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
