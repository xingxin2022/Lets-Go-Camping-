package edu.usc.csci310.project.search;

public class ParkSearchRequest {
    private String query;
    private String searchType;
    private int startPosition;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int start) {
        this.startPosition = start;
    }
}
