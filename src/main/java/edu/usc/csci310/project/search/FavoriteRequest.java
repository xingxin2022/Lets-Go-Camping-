package edu.usc.csci310.project.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoriteRequest {
    private String userName;
    private String parkCode;
    private String parkName;
    private boolean isPrivate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public boolean isPublic() {
        return isPrivate;
    }

    public void setPublic(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
