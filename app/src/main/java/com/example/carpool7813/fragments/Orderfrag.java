package com.example.carpool7813.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.carpool7813.R;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.utilities.Rout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Orderfrag extends Fragment {

    private Rout rout;
    private TextView pickup, destination, date, seats, status;
    Button addToCart;
    FirebaseHandler fb;

    public Orderfrag(Rout rout) {
        this.rout = rout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        pickup = view.findViewById(R.id.pickup);
        destination = view.findViewById(R.id.distination);
        date = view.findViewById(R.id.date);
        seats = view.findViewById(R.id.seatsText);
        status = view.findViewById(R.id.rideText);
        addToCart = view.findViewById(R.id.addToCart);
        fb = FirebaseHandler.getInstance();

        pickup.setText("From: " + rout.getStartLocation());
        destination.setText("To: " + rout.getDestination());
        date.setText("Departure Time: " + rout.getFormattedDepartureTime().toString());
        seats.setText("Available Seats: " + String.valueOf(rout.getSeatsAvailable()));
        status.setText("Status: " + rout.getStatus());

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext() != null) {
                    fb.addOrder(getContext(), rout);
                } else {
                    Log.e("OrderFragment", "Context is null");
                }
            }
        });


        return view;
    }
}
