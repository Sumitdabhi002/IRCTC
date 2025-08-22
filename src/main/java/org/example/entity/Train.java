package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Train {
    private String trainId;
    private String trainNo;
    private List<List<Integer>> seats; // 0 = free, 1 = booked
    private List<String> stations;
    private Map<String, String> stationTimes; // station -> time

    public Train(Map<String, String> stationTimes, List<String> stations,
                 List<List<Integer>> seats, String trainNo, String trainId) {
        this.stationTimes = stationTimes;
        this.stations = stations;
        this.seats = seats;
        this.trainNo = trainNo;
        this.trainId = trainId;
    }


    public Train() {
        this.trainId = "";
        this.trainNo = "";
        this.seats = new ArrayList<>();
        this.stations = new ArrayList<>();
        this.stationTimes = new HashMap<>();
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public Map<String, String> getStationTimes() {
        return stationTimes;
    }

    public void setStationTimes(Map<String, String> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public String getTicketInfo() {
        return String.format("Train ID: %s, Train No: %s", trainId, trainNo);
    }
}
