package com.example.carpool7813.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.carpool7813.R;
import com.example.carpool7813.model.FirebaseHandler;
import com.example.carpool7813.utilities.Order;
import com.example.carpool7813.utilities.Rout;

import java.util.List;


public class Payment extends Fragment {
    List<Order> orders;
    Button pay;
    FirebaseHandler fb;
    public Payment(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fb = FirebaseHandler.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        pay = view.findViewById(R.id.payNow);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb.pay(getContext(),orders);
            }
        });

        return view;
    }
}