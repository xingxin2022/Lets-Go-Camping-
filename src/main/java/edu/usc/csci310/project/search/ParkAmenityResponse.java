package edu.usc.csci310.project.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkAmenityResponse {

    private List<List<AmenityPark>> data;

    public List<List<AmenityPark>> getData(){
        return data;
    }

    public void setData(List<List<AmenityPark>> data){
        this.data = data;
    }
}
