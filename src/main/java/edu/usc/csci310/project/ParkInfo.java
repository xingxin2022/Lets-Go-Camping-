package edu.usc.csci310.project;


public class ParkInfo {
    private String parkName;
    private String parkCode;
    private Boolean isPrivate;

    public ParkInfo(String parkName, String parkCode, Boolean isPrivate) {
        this.parkName = parkName;
        this.parkCode = parkCode;
        this.isPrivate = isPrivate;
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

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        this.isPrivate = aPrivate;
    }
}