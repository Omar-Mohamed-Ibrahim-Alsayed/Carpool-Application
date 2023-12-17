package com.example.carpool7813.interfaces;

import com.example.carpool7813.utilities.Order;

import java.util.List;

public interface OrdersCallback {
    void onOrdersReceived(List<Order> orders);
}

