package edu.usc.csci310.project.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;



@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkSearchResponse {

    private List<Park> data;

    public List<Park> getData(){
        return data;
    }

    public void setData(List<Park> data){
        this.data = data;
    }
}
