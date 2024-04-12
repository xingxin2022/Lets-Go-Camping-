package edu.usc.csci310.project.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkActivityResponse {

    private List<ActivityPark> data;

    public List<ActivityPark> getData(){
        return data;
    }

    public void setData(List<ActivityPark> data){
        this.data = data;
    }
}
