package com.example.carpool7813.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Rout {

    private String rideId;
    private String startLocation;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime reservationDeadline;
    private int seatsAvailable;
    private String driverID;
    private String status;

    public Rout(String rideId, String startLocation, String destination, LocalDateTime departureTime,
                LocalDateTime reservationDeadline, int seatsAvailable, String driverID, String status) {
        this.rideId = rideId;
        this.startLocation = startLocation;
        this.destination = destination;
        this.departureTime = departureTime;
        this.reservationDeadline = reservationDeadline;
        this.seatsAvailable = seatsAvailable;
        this.driverID = driverID;
        this.status = status;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(LocalDateTime reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormattedDepartureTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd   HH:mm");
        return this.departureTime.format(formatter);
    }
}
