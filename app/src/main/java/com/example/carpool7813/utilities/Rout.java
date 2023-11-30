package com.example.carpool7813.utilities;

import java.time.LocalDateTime;
import java.util.List;

public class Rout {
    private String driver;
    private List<String> riders;
    private List<String> stops;
    private String start, end;
    private LocalDateTime time;

    public Rout(String driver, List<String> riders, List<String> stops, String start, String end, LocalDateTime time) {
        this.driver = driver;
        this.riders = riders;
        this.stops = stops;
        this.start = start;
        this.end = end;
        this.time = time;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public List<String> getRiders() {
        return riders;
    }

    public void setRiders(List<String> riders) {
        this.riders = riders;
    }

    public List<String> getStops() {
        return stops;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
