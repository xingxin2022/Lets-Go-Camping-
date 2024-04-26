package edu.usc.csci310.project;

import java.util.List;

public class ParkCodesRequest {
    private List<String> parkCodes;

    public ParkCodesRequest() {}

    public List<String> getParkCodes() {
        return parkCodes;
    }

    public void setParkCodes(List<String> parkCodes) {
        this.parkCodes = parkCodes;
    }
}
