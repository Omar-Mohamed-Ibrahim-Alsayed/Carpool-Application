package com.example.carpool7813.fragments;

import android.graphics.Movie;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carpool7813.R;
import com.example.carpool7813.utilities.Rout;

public class Order extends Fragment {

    private Rout rout;

    public Order(Rout rout) {
        this.rout = rout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }
}