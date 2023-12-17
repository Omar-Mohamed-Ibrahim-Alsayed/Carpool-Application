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
import com.example.carpool7813.interfaces.RoutsCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.utilities.Adaptor;
import com.example.carpool7813.utilities.CartAdaptor;
import com.example.carpool7813.utilities.Order;
import com.example.carpool7813.utilities.Rout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Routs extends Fragment implements RoutsCallback{
    RecyclerView recycler;
    boolean isGrid = true;
    Adaptor routsAdapter;
    ProgressBar pb;
    FirebaseHandler fb;



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

        pb.setVisibility(View.VISIBLE);

        fb = FirebaseHandler.getInstance();

        routsAdapter = new Adaptor(new ArrayList<>(), getParentFragmentManager(), isGrid);

        fb.getRouts(this);

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



    @Override
    public void onRoutsReceived(List<Rout> routs) {
        routsAdapter.updateData(routs);
        recycler.setAdapter(routsAdapter);
        pb.setVisibility(View.INVISIBLE);
    }




}