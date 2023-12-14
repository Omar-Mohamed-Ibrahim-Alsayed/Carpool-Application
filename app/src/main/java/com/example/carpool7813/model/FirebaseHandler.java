package com.example.carpool7813.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.carpool7813.utilities.Rout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FirebaseHandler {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    public void addOrder(Context context, Rout rout){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();
            String orderID = rout.getRideId() + '_' + userID;
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy-HH:mm", Locale.getDefault());
            String timeOfBooking = dateFormat.format(currentDate);

            DatabaseReference ordersRef = db.child("orders").child(orderID);

            Map<String, Object> order = new HashMap<>();
            order.put("orderID", orderID);
            order.put("userID", userID);
            order.put("rideID", rout.getRideId());
            order.put("timeOfBooking", timeOfBooking);
            order.put("paymentStatus", "pending");
            order.put("status", "pending");
            order.put("price", 3.2);

            ordersRef.setValue(order)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                            Log.e("OrderFragment", "Failed: " + e.getMessage());
                        }
                    });
        } else {
            // Handle the case where the user is not signed in
            Toast.makeText(context, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }
}
