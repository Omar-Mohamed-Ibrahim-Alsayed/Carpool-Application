package com.example.carpool7813.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.carpool7813.utilities.DateTimeConverter;

import java.time.LocalDateTime;


@Entity(tableName = "rout_table")
@TypeConverters(DateTimeConverter.class)
public class RoutEntity {

    @PrimaryKey
    @NonNull
    private String rideId;

    private String startLocation;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime reservationDeadline;
    private int seatsAvailable;
    private String driverID;
    private String status;

    public RoutEntity(String rideId, String startLocation, String destination, LocalDateTime departureTime,
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

    @NonNull
    public String getRideId() {
        return rideId;
    }

    public void setRideId(@NonNull String rideId) {
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
}
