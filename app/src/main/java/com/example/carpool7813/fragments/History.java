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
import com.example.carpool7813.interfaces.OrdersCallback;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.utilities.CartAdaptor;
import com.example.carpool7813.utilities.HistoryAdaptor;
import com.example.carpool7813.utilities.Order;

import java.util.ArrayList;
import java.util.List;

public class History extends Fragment implements OrdersCallback {

    ProgressBar pb;
    HistoryAdaptor ordersAdapter;
    RecyclerView recycler;
    FirebaseHandler fb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        pb = view.findViewById(R.id.progressBar);
        recycler = view.findViewById(R.id.rview);
        pb.setVisibility(View.INVISIBLE);
        fb = FirebaseHandler.getInstance();

        List<Order> orders = new ArrayList<>();

        ordersAdapter = new HistoryAdaptor(orders, getParentFragmentManager());
        getOrders();


        return view;
    }
    @Override
    public void onOrdersReceived(List<Order> orders) {
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getPaymentStatus().equals("paid")) {
                filteredOrders.add(order);
            }
        }
        ordersAdapter.updateData(filteredOrders);
        recycler.setAdapter(ordersAdapter);
        pb.setVisibility(View.INVISIBLE);
    }

    private void getOrders() {
        pb.setVisibility(View.VISIBLE);
        String id = fb.getUserId();
        fb.getOrdersForUser(id,this);
    }
}