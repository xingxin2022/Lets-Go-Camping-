package edu.usc.csci310.project.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Park {
    private String id;
    private String url;
    private String fullName;
    private String parkCode;
    private String description;
    private List<Activity> activities;
    private List<Amenity> amenities;
    private String states;
    private List<EntranceFee> entranceFees;
    private List<Address> addresses;
    private List<Image> images;

    private List<DetailedActivityPark> activityParks;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<EntranceFee> getEntranceFees() {
        return entranceFees;
    }

    public void setEntranceFees(List<EntranceFee> entranceFees) {
        this.entranceFees = entranceFees;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<DetailedActivityPark> getActivityParks() {
        return activityParks;
    }

    public void setActivityParks(List<DetailedActivityPark> activityParks) {
        this.activityParks = activityParks;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    static class Activity {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class Amenity {
        private String id;
        private String name;

        private List<String> categories;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getCategories() {
            return categories;
        }

        public void setCategories(List<String> categories) {
            this.categories = categories;
        }
    }

    static class DetailedActivityPark{
        private String states;
        private String parkCode;
        private String designation;
        private String fullName;
        private String url;
        private String name;

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
    }

    static class Image {
        private String credit;
        private String title;
        private String altText;
        private String caption;
        private String url;

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAltText() {
            return altText;
        }

        public void setAltText(String altText) {
            this.altText = altText;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    static class EntranceFee {
        private String cost;
        private String description;
        private String title;

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    static class Address {
        private String postalCode;
        private String city;
        private String stateCode;
        private String countryCode;
        private String provinceTerritoryCode;
        private String line1;
        private String type;
        private String line3;
        private String line2;

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getProvinceTerritoryCode() {
            return provinceTerritoryCode;
        }

        public void setProvinceTerritoryCode(String provinceTerritoryCode) {
            this.provinceTerritoryCode = provinceTerritoryCode;
        }

        public String getLine1() {
            return line1;
        }

        public void setLine1(String line1) {
            this.line1 = line1;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLine3() {
            return line3;
        }

        public void setLine3(String line3) {
            this.line3 = line3;
        }

        public String getLine2() {
            return line2;
        }

        public void setLine2(String line2) {
            this.line2 = line2;
        }
    }

}
