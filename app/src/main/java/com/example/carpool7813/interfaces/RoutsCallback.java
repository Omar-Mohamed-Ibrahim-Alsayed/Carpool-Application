package com.example.carpool7813.interfaces;

import com.example.carpool7813.utilities.Order;
import com.example.carpool7813.utilities.Rout;

import java.util.List;

public interface RoutsCallback {
    void onRoutsReceived(List<Rout> routs);
}

