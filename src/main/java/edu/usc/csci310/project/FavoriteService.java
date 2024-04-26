package edu.usc.csci310.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.csci310.project.search.AmenityPerParkResponse;
import edu.usc.csci310.project.search.Park;
import edu.usc.csci310.project.search.Park.Amenity;
import edu.usc.csci310.project.search.ParkSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {

    private final DataSource dataSource;
    private final String API_KEY = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
    private final String BASE_URL = "https://developer.nps.gov/api/v1";
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public FavoriteService(DataSource dataSource, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.dataSource = dataSource;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public List<Park> fetchParkDetailsBatch(int start, List<String> parkCodes ){
        String url = "";
        List<Park> parks = new ArrayList<>();
        for (String parkcode : parkCodes) {
            url = BASE_URL + "/parks?parkCode=" + parkcode + "&limit=1&sort=-relevanceScore&api_key=" + API_KEY;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(url, requestEntity, String.class);


            List<Park> temp_parks = new ArrayList<>();

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseJson = responseEntity.getBody();
                try {
                    ParkSearchResponse parkSearchResponse = objectMapper.readValue(responseJson, ParkSearchResponse.class);
                    if (parkSearchResponse != null && parkSearchResponse.getData() != null) {
                        temp_parks = parkSearchResponse.getData();
                        Park new_park = temp_parks.get(0);
                        //fetch amenity
                        List<String> temp_parkCode = new ArrayList<>();
                        temp_parkCode.add(parkcode);
                        Map<String, List<Park.Amenity>> amenities = fetchAmenitiesForParks(temp_parkCode);
                        List<Park.Amenity> parkAmenities = amenities.get(parkcode);
                        new_park.setAmenities(parkAmenities);
                        parks.add(new_park);
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return parks;
    }

    public Map<String, List<Park.Amenity>> fetchAmenitiesForParks(List<String> parkCodes){
        Map<String, List<Park.Amenity>> parkAmenities = new HashMap<>();
        for (String parkCode : parkCodes){
            String url = BASE_URL + "/amenities/?q=" + parkCode + "&api_key=" + API_KEY;
            System.out.println(url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(url, requestEntity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseJson = responseEntity.getBody();
                try {
                    AmenityPerParkResponse amenityPerParkResponse = objectMapper.readValue(responseJson, AmenityPerParkResponse.class);
                    if (amenityPerParkResponse != null && amenityPerParkResponse.getData() != null) {
                        List<Park.Amenity> amenities = parkAmenities.computeIfAbsent(parkCode, k -> new ArrayList<>());
                        amenities.addAll(amenityPerParkResponse.getData());
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException("Error processing JSON", e);
                }
            }
        }
        return parkAmenities;

    }

    // Method to update the privacy setting of a user's favorite list
    public void updateFavoriteListPrivacy(String username, boolean isPublic) throws SQLException {
        String sql = "UPDATE favorites SET isPublic = ? WHERE username = ?";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBoolean(1, isPublic);
            stmt.setString(2, Hash.hash(username));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to update privacy setting: " + e.getMessage(), e);
        }
    }

    // Method to move a park up or down by one position
    public void movePark(String username, String parkCode, boolean moveUp) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Find the number of parks in the user's list
            String countSql = "SELECT COUNT(*) FROM favorites WHERE username = ?";
            stmt = connection.prepareStatement(countSql);
            stmt.setString(1, Hash.hash(username));
            rs = stmt.executeQuery();
            int count = rs.getInt(1);
            rs.close();
            stmt.close();

            // Find the park's order
            String findParkOrderSql = "SELECT parkOrder FROM favorites WHERE username = ? AND parkCode = ?";
            stmt = connection.prepareStatement(findParkOrderSql);
            stmt.setString(1, Hash.hash(username));
            stmt.setString(2, parkCode);
            rs = stmt.executeQuery();
            int parkOrder = rs.getInt("parkOrder");
            if (parkOrder == 1 && moveUp || parkOrder == count && !moveUp) {
                connection.rollback();
                return;
            }
            rs.close();
            stmt.close();

            // Find the park to swap with
            int swapOrder = moveUp ? parkOrder - 1 : parkOrder + 1;
            String swapOrderSql = "SELECT parkCode FROM favorites WHERE username = ? AND parkOrder = ?";
            stmt = connection.prepareStatement(swapOrderSql);
            stmt.setString(1, Hash.hash(username));
            stmt.setInt(2, swapOrder);
            rs = stmt.executeQuery();
            String swapParkCode = rs.getString("parkCode");
            rs.close();
            stmt.close();

            // Swap the park orders
            String updateOrderSql = "UPDATE favorites SET parkOrder = ? WHERE username = ? AND parkCode = ?";
            stmt = connection.prepareStatement(updateOrderSql);
            // Update the moving park
            stmt.setInt(1, swapOrder);
            stmt.setString(2, Hash.hash(username));
            stmt.setString(3, parkCode);
            stmt.executeUpdate();
            stmt.close();
            // Update the swap park
            stmt = connection.prepareStatement(updateOrderSql);
            stmt.setInt(1, parkOrder);
            stmt.setString(2, Hash.hash(username));
            stmt.setString(3, swapParkCode);
            stmt.executeUpdate();

            // Commit transaction
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Rollback on errors
            }
            throw new SQLException("Failed to move park: " + e.getMessage(), e);
        }
    }


//    public void updateParkOrder(String username, String parkCode, int newOrder) throws SQLException {
//        String sql = "UPDATE favorites SET parkOrder = ? WHERE username = ? AND parkCode = ?";
//        try {
//            Connection connection = dataSource.getConnection();
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            stmt.setInt(1, newOrder);
//            stmt.setString(2, username);
//            stmt.setString(3, parkCode);
//            stmt.executeUpdate();
//        }
//        catch (SQLException e) {
//            throw new SQLException("Failed to update park order: " + e.getMessage(), e);
//        }
//    }

    public FavoriteResponse getFavorites(String username) throws RuntimeException {
        List<Park> parks = new ArrayList<>();
        String sql = "SELECT parkCode, parkName, parkOrder FROM favorites WHERE username = ? ORDER BY parkOrder ASC";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, Hash.hash(username));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Park park = new Park();
                park.setParkCode(rs.getString("parkCode"));
                park.setFullName(rs.getString("parkName"));
                parks.add(park);
            }
            if (parks.isEmpty()) {
                return new FavoriteResponse(parks, "No favorite parks found for user: " + username, false);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get favorite parks: " + e.getMessage(), e);
        }
        return new FavoriteResponse(parks, "Successfully retrieved favorite parks.", true);
    }

    public void removePark(String username, String parkCode) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Determine the parkOrder of the park to be deleted
            String getOrderSql = "SELECT parkOrder FROM favorites WHERE username = ? AND parkCode = ?";
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(getOrderSql);
            connection.setAutoCommit(false);
            stmt.setString(1, Hash.hash(username));
            stmt.setString(2, parkCode);
            int parkOrder = 0;
            rs = stmt.executeQuery();
            parkOrder = rs.getInt("parkOrder");
            stmt.close();

            // Delete the specified park
            String deleteSql = "DELETE FROM favorites WHERE username = ? AND parkCode = ?";
            stmt = connection.prepareStatement(deleteSql);
            stmt.setString(1, Hash.hash(username));
            stmt.setString(2, parkCode);
            stmt.executeUpdate();
            stmt.close();

            // Decrement the parkOrder for all parks that have a higher order
            String updateOrderSql = "UPDATE favorites SET parkOrder = parkOrder - 1 WHERE username = ? AND parkOrder > ?";
            stmt = connection.prepareStatement(updateOrderSql);
            stmt.setString(1, Hash.hash(username));
            stmt.setInt(2, parkOrder);
            stmt.executeUpdate();
            stmt.close();

            // Commit transaction
            connection.commit();

        } catch (SQLException e) {
            if (connection != null ) {
                connection.rollback();
            }
            throw new SQLException("Failed to remove park: " + e.getMessage(), e);
        }
    }

    // Method to delete all parks from a user's favorite list
    public void deleteAll(String username) throws SQLException {
        String sql = "DELETE FROM favorites WHERE username = ?";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, Hash.hash(username));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to delete all parks: " + e.getMessage(), e);
        }
    }

    public Boolean getPublic(String username) throws SQLException{
        String sql = "SELECT isPublic FROM favorites WHERE username = ? LIMIT 1";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isPublic");
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to get public status: ", e);
        }finally {
            // It's important to close resources in the finally block to avoid resource leaks.
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignored */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignored */ }
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { /* ignored */ }
            }
        }
    }


}
