package com.example.carpool7813.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.carpool7813.R;
import com.example.carpool7813.utilities.Adaptor;
import com.example.carpool7813.utilities.CartAdaptor;
import com.example.carpool7813.utilities.Order;
import com.example.carpool7813.utilities.Rout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends Fragment {

    ProgressBar pb;
    CartAdaptor ordersAdapter;
    RecyclerView recycler;
    Button pay;
    FragmentManager fragmentManager;
    TextView total;
    float tprice;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        pb = view.findViewById(R.id.progressBar);
        recycler = view.findViewById(R.id.rview);
        pay = view.findViewById(R.id.payAll);
        total = view.findViewById(R.id.total);
        if (isAdded()) {
            FragmentManager fragmentManager = getParentFragmentManager();

        }


        List<Order> orders = new ArrayList<>();
        getOrders(orders);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction().replace(R.id.flFragment, new Payment(orders)).addToBackStack(null).commit();
                }
            }
        });

        return view;
    }


    private void getOrders(List<Order> orders) {
        pb.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ridesRef = database.getReference("orders");

        ridesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders.clear();
                tprice = 0;

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderID = orderSnapshot.child("orderID").getValue(String.class);
                    String userID = orderSnapshot.child("userID").getValue(String.class);
                    String rideID = orderSnapshot.child("rideID").getValue(String.class);
                    String timeOfBookingString = orderSnapshot.child("timeOfBooking").getValue(String.class);
                    String paymentStatus = orderSnapshot.child("paymentStatus").getValue(String.class);
                    String status = orderSnapshot.child("status").getValue(String.class);
                    float price = orderSnapshot.child("price").getValue(Float.class);
                    tprice += price;

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy-HH:mm", Locale.ENGLISH);
                    LocalDateTime timeOfBooking = LocalDateTime.parse(timeOfBookingString, formatter);


                    Order order = new Order(orderID,paymentStatus,rideID, status,timeOfBooking,userID,price);
                    orders.add(order);
                }

                total.setText("Total = " + tprice);
                ordersAdapter = new CartAdaptor(orders, getParentFragmentManager());
                recycler.setAdapter(ordersAdapter);
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