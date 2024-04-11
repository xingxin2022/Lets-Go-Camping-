package edu.usc.csci310.project.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AmenityPerParkResponse {

    private List<Park.Amenity> data;

    public List<Park.Amenity> getData(){
        return data;
    }

    public void setData(List<Park.Amenity> data){
        this.data = data;
    }
}
