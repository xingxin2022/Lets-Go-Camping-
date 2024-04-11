package edu.usc.csci310.project.search;

import java.util.List;

public class AmenityPerPark {
    private String id;
    private String name;

    private List<String> categories;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
