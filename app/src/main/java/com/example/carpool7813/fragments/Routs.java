package com.example.carpool7813.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.carpool7813.R;
import com.example.carpool7813.utilities.Adaptor;
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


public class Routs extends Fragment {
    RecyclerView recycler;
    boolean isGrid = true;
    Adaptor routsAdapter;
    ProgressBar pb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routs, container, false);

        recycler = view.findViewById(R.id.rview);

        setLayoutManager(isGrid);

        Button toggleButton = view.findViewById(R.id.toggleButton);

        pb = view.findViewById(R.id.progressBar);

        List<Rout> routs = new ArrayList<>();

        // Retrieve data and set the adapter in ValueEventListener
        getRouts(routs);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView();
            }
        });

        return view;
    }

    private void setLayoutManager(boolean isGrid) {
        RecyclerView.LayoutManager layoutManager;
        if (isGrid) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            layoutManager = new GridLayoutManager(getContext(), 1);
        }
        recycler.setLayoutManager(layoutManager);
    }

    private void toggleView() {
        isGrid = !isGrid;
        setLayoutManager(isGrid);
        routsAdapter.updateLayout(isGrid);
        recycler.getAdapter().notifyDataSetChanged();
        recycler.setAdapter(routsAdapter);
    }

    private void getRouts(List<Rout> routs) {
        pb.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ridesRef = database.getReference("rides");

        ridesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                routs.clear();

                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    String rideID = rideSnapshot.getKey();
                    String driver = rideSnapshot.child("driver").getValue(String.class);
                    String clients = rideSnapshot.child("rider").getValue(String.class);
                    String time = rideSnapshot.child("time").getValue(String.class);
                    String start = rideSnapshot.child("start").getValue(String.class);
                    String end = rideSnapshot.child("end").getValue(String.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMMM/yyyy-HH:mm");


                    // Parse the string into a LocalDateTime object
                    LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
                    List<String> clientList = Arrays.asList(clients);
                    List<String> stopsList = Arrays.asList("empty");


                    Rout rout = new Rout(driver, clientList, stopsList, start, end, dateTime);
                    routs.add(rout);
                }

                // Set the adapter after data retrieval
                routsAdapter = new Adaptor(routs, getParentFragmentManager(), isGrid);
                recycler.setAdapter(routsAdapter);
                pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle cancellation

                pb.setVisibility(View.INVISIBLE);
            }
        });
    }
}