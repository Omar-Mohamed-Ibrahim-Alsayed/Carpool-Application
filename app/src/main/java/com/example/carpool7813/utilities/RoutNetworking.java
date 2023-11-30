package com.example.carpool7813.utilities;

import com.example.carpool7813.utilities.Rout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoutNetworking {
    private List<Rout> ridesList; // Define a class-level list to store Routs

    public RoutNetworking() {

    }

    // Define a method to retrieve ridesList if needed by other classes
    public List<Rout> getRidesList() {
        return ridesList;
    }
}
