package edu.usc.csci310.project;

import edu.usc.csci310.project.search.Park;

public class ParkPopularity {
    private String fullName;
    private Double popularity;

    public ParkPopularity(Park park, Double popularity) {
        this.fullName = park.getFullName();  // Ensure this matches exactly what's needed
        this.popularity = popularity;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public Double getPopularity() {
        return popularity;
    }
}