package com.example.carpool7813.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private String orderID;
    private String paymentStatus;
    private String rideID;
    private String status;
    private LocalDateTime timeOfBooking;
    private String userID;
    private float price; // New field to hold the price

    public Order(String orderID, String paymentStatus, String rideID, String status, LocalDateTime timeOfBooking, String userID, float price) {
        this.orderID = orderID;
        this.paymentStatus = paymentStatus;
        this.rideID = rideID;
        this.status = status;
        this.timeOfBooking = timeOfBooking;
        this.userID = userID;
        this.price = price; // Initialize price
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Order(String orderID, String paymentStatus, String rideID, String status, String timeOfBooking, String userID) {
        this.orderID = orderID;
        this.paymentStatus = paymentStatus;
        this.rideID = rideID;
        this.status = status;
        this.timeOfBooking = LocalDateTime.parse(timeOfBooking, DateTimeFormatter.ofPattern("dd/MMMM/yyyy-HH:mm"));
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimeOfBooking() {
        return timeOfBooking;
    }

    public void setTimeOfBooking(LocalDateTime timeOfBooking) {
        this.timeOfBooking = timeOfBooking;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
