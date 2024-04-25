package edu.usc.csci310.project;

import java.util.ArrayList;
import java.util.List;

public class ParkCount {
    private String parkName;
    private String parkCode;

    private int count;
    private float ratio;
    private List<String> usernames;

    public ParkCount(String parkName, String parkCode) {
        this.parkName = parkName;
        this.parkCode = parkCode;
        this.count = 0;
        this.ratio = 0;
        this.usernames = new ArrayList<>();
    }

    public void incrementCount(String username) {
        this.count++;
        this.usernames.add(username);
    }

    public void calculateRatio(int totalUsers) {
        this.ratio =  (float) this.count / totalUsers * 100;
    }


    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public List<String> getUsernames() {
        return usernames;
    }

}

