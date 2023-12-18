package com.example.carpool7813.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.carpool7813.R;
import com.example.carpool7813.interfaces.OrdersCallback;
import com.example.carpool7813.interfaces.RoutsCallback;
import com.example.carpool7813.interfaces.SignUpCallback;
import com.example.carpool7813.interfaces.SigninCallback;
import com.example.carpool7813.utilities.Adaptor;
import com.example.carpool7813.utilities.CartAdaptor;
import com.example.carpool7813.utilities.Order;
import com.example.carpool7813.utilities.Rout;
import com.example.carpool7813.utilities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FirebaseHandler {
    private static FirebaseHandler instance;
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    List<Order> orders;
    List<Rout> routs;
    User user;


    private FirebaseHandler() {
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        orders = new ArrayList<>();
        routs = new ArrayList<>();
    }

    public static synchronized FirebaseHandler getInstance() {
        if (instance == null) {
            instance = new FirebaseHandler();
        }
        return instance;
    }

    public void addOrder(Context context, Rout rout){
        if (currentUser != null) {
            String userID = currentUser.getUid();
            String orderID = rout.getRideId() + '_' + userID;
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy-HH:mm", Locale.getDefault());
            String timeOfBooking = dateFormat.format(currentDate);

            DatabaseReference ordersRef = db.getReference().child("orders").child(orderID);

            Map<String, Object> order = new HashMap<>();
            order.put("orderID", orderID);
            order.put("userID", userID);
            order.put("rideID", rout.getRideId());
            order.put("timeOfBooking", timeOfBooking);
            order.put("paymentStatus", "pending");
            order.put("status", "pending");
            order.put("price", "10.2");

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

    public void getOrders(OrdersCallback callback) {
        DatabaseReference ridesRef = db.getReference("orders");

        ridesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Order> orders = new ArrayList<>();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderID = orderSnapshot.child("orderID").getValue(String.class);
                    String userID = orderSnapshot.child("userID").getValue(String.class);
                    String rideID = orderSnapshot.child("rideID").getValue(String.class);
                    String timeOfBookingString = orderSnapshot.child("timeOfBooking").getValue(String.class);
                    String paymentStatus = orderSnapshot.child("paymentStatus").getValue(String.class);
                    String status = orderSnapshot.child("status").getValue(String.class);
                    String sprice = orderSnapshot.child("price").getValue(String.class);
                    float price = 0.0f;

                    if (sprice != null) {
                        try {
                            float parsedPrice = Float.parseFloat(sprice);
                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            String formattedPrice = decimalFormat.format(parsedPrice);
                            price = Float.parseFloat(formattedPrice);
                        } catch (NumberFormatException e) {
                            // Handle parsing errors if necessary
                        }
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy-HH:mm", Locale.ENGLISH);
                    LocalDateTime timeOfBooking = LocalDateTime.parse(timeOfBookingString, formatter);

                    Order order = new Order(orderID, paymentStatus, rideID, status, timeOfBooking, userID, price);
                    orders.add(order);
                }

                callback.onOrdersReceived(orders);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle cancellation if needed
            }
        });
    }



    public void getRouts(RoutsCallback callback) {
        DatabaseReference ridesRef2 = db.getReference("rides");

        ridesRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Rout> updatedRouts = new ArrayList<>();

                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    String rideID = rideSnapshot.child("rideID").getValue(String.class);
                    String startLocation = rideSnapshot.child("startLocation").getValue(String.class);
                    String destination = rideSnapshot.child("destination").getValue(String.class);
                    String departureTimeString = rideSnapshot.child("departureTime").getValue(String.class);
                    String reservationDeadlineString = rideSnapshot.child("reservationDeadline").getValue(String.class);
                    int seatsAvailable = rideSnapshot.child("seatsAvailable").getValue(Integer.class);
                    String driverID = rideSnapshot.child("driverID").getValue(String.class);
                    String status = rideSnapshot.child("status").getValue(String.class);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMMM/yyyy-HH:mm");
                    LocalDateTime departureTime = LocalDateTime.parse(departureTimeString, formatter);
                    LocalDateTime reservationDeadline = LocalDateTime.parse(reservationDeadlineString, formatter);

                    Rout rout = new Rout(rideID, startLocation, destination, departureTime, reservationDeadline, seatsAvailable, driverID, status);
                    updatedRouts.add(rout);
                }

                callback.onRoutsReceived(updatedRouts);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void addUser(User user2){
        DatabaseReference usersRef = db.getReference("users");

        //FirebaseUser user = mAuth.getCurrentUser();
        user = new User(user2.getId(),user2.getUserType(), user2.getEmail(), user2.getName());

        String userId = getUserId();
        usersRef.child(userId).setValue(user);
    }

    public void getUser(SigninCallback callback) {
        DatabaseReference usersRef = db.getReference("users");
        String userId = getUserId();

        usersRef.orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String email = userSnapshot.child("email").getValue(String.class);
                        String name = userSnapshot.child("name").getValue(String.class);
                        String userType = userSnapshot.child("userType").getValue(String.class);

                        user = new User(userId, userType, email, name);
                        callback.onUserReceived(user);
                        return;
                    }
                }
                callback.onUserReceived(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onUserReceived(null);
            }
        });
    }


    public String getUserId(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = null;
        if (currentUser != null) {
            userId = currentUser.getUid();

        }
        return userId;
    }

    public void loginUser( Context context, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                user = new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail(),"");
                            }
                        } else {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void registerUser(SignUpCallback callback, Activity activity, Context context, String name, String email, String password, String type) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user2 = mAuth.getCurrentUser();

                            // Set the display name here
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(type)
                                    .build();

                            if (user2 != null) {
                                user2.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> updateTask) {
                                                if (updateTask.isSuccessful()) {
                                                    Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();
                                                    user = new User(getUserId(), type, email, name);

                                                    // Now user should be instantiated before callback
                                                    Log.e("INNNN ADD USER", "FROM Sigup" + user.getId().toString());
                                                    callback.onUserReceived(user);
                                                } else {
                                                    Toast.makeText(context, "Failed to set display name", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                            }
                        } else {
                            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}