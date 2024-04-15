package edu.usc.csci310.project.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmenityPark {
    private String id;
    private String name;
    @JsonProperty("parks")
    private List<DetailedAmenityPark> amenityParks;

//    @Override
//    public String toString() {
//        StringBuilder str = new StringBuilder(id + name + ": ");
//        for (DetailedActivityPark park : activityParks) {
//            str.append(park);
//        }
//        return str.toString();
//    }

    public String getName() {
        return name;
    }

    public void setName(String fullName) {
        this.name = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DetailedAmenityPark> getAmenityParks() {
        return amenityParks;
    }

    public void setAmenityParks(List<DetailedAmenityPark> amenityParks) {
        this.amenityParks = amenityParks;
    }


    static class DetailedAmenityPark{
        private String states;
        private String parkCode;
        private String designation;
        private String fullName;
        private String url;
        private String name;

        private List<Place> places;

        public String getStates() {
            return states;
        }

        public void setStates(String states) {
            this.states = states;
        }

        public String getParkCode() {
            return parkCode;
        }

        public void setParkCode(String parkCode) {
            this.parkCode = parkCode;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Place> getPlaces() {
            return places;
        }

        public void setPlaces(List<Place> places) {
            this.places = places;
        }

        static class Place{
            private String title;
            private String id;
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }



}
