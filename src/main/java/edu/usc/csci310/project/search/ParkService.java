package edu.usc.csci310.project.search;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.csci310.project.RegisterResponse;
import edu.usc.csci310.project.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ParkService {
    private final String API_KEY = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
    private final String BASE_URL = "https://developer.nps.gov/api/v1";

    @Autowired
    private final DataSource dataSource;
    private final RestTemplate restTemplate;

    @Autowired
    public ParkService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.restTemplate = new RestTemplate();
    }


    private static final Map<String, String> STATE_CODE_MAP = new HashMap<>();

    static {
        STATE_CODE_MAP.put("alabama", "AL");
        STATE_CODE_MAP.put("alaska", "AK");
        STATE_CODE_MAP.put("arizona", "AZ");
        STATE_CODE_MAP.put("arkansas", "AR");
        STATE_CODE_MAP.put("california", "CA");
        STATE_CODE_MAP.put("colorado", "CO");
        STATE_CODE_MAP.put("connecticut", "CT");
        STATE_CODE_MAP.put("delaware", "DE");
        STATE_CODE_MAP.put("florida", "FL");
        STATE_CODE_MAP.put("georgia", "GA");
        STATE_CODE_MAP.put("hawaii", "HI");
        STATE_CODE_MAP.put("idaho", "ID");
        STATE_CODE_MAP.put("illinois", "IL");
        STATE_CODE_MAP.put("indiana", "IN");
        STATE_CODE_MAP.put("iowa", "IA");
        STATE_CODE_MAP.put("kansas", "KS");
        STATE_CODE_MAP.put("kentucky", "KY");
        STATE_CODE_MAP.put("louisiana", "LA");
        STATE_CODE_MAP.put("maine", "ME");
        STATE_CODE_MAP.put("maryland", "MD");
        STATE_CODE_MAP.put("massachusetts", "MA");
        STATE_CODE_MAP.put("michigan", "MI");
        STATE_CODE_MAP.put("minnesota", "MN");
        STATE_CODE_MAP.put("mississippi", "MS");
        STATE_CODE_MAP.put("missouri", "MO");
        STATE_CODE_MAP.put("montana", "MT");
        STATE_CODE_MAP.put("nebraska", "NE");
        STATE_CODE_MAP.put("nevada", "NV");
        STATE_CODE_MAP.put("new hampshire", "NH");
        STATE_CODE_MAP.put("new jersey", "NJ");
        STATE_CODE_MAP.put("new mexico", "NM");
        STATE_CODE_MAP.put("new york", "NY");
        STATE_CODE_MAP.put("north carolina", "NC");
        STATE_CODE_MAP.put("north dakota", "ND");
        STATE_CODE_MAP.put("ohio", "OH");
        STATE_CODE_MAP.put("oklahoma", "OK");
        STATE_CODE_MAP.put("oregon", "OR");
        STATE_CODE_MAP.put("pennsylvania", "PA");
        STATE_CODE_MAP.put("rhode island", "RI");
        STATE_CODE_MAP.put("south carolina", "SC");
        STATE_CODE_MAP.put("south dakota", "SD");
        STATE_CODE_MAP.put("tennessee", "TN");
        STATE_CODE_MAP.put("texas", "TX");
        STATE_CODE_MAP.put("utah", "UT");
        STATE_CODE_MAP.put("vermont", "VT");
        STATE_CODE_MAP.put("virginia", "VA");
        STATE_CODE_MAP.put("washington", "WA");
        STATE_CODE_MAP.put("west virginia", "WV");
        STATE_CODE_MAP.put("wisconsin", "WI");
        STATE_CODE_MAP.put("wyoming", "WY");
    }


    private List<String> parkCodes = new ArrayList<>();

    public List<Park> fetchParkDetailsBatch(int start, List<String> parkCodes ){
        String url = "";
        List<Park> parks = new ArrayList<>();
        for (String parkcode : parkCodes) {
            url = BASE_URL + "/parks?parkCode=" + parkcode + "&limit=1&sort=-relevanceScore&api_key=" + API_KEY;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

//            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(url, requestEntity, String.class);


            List<Park> temp_parks = new ArrayList<>();

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseJson = responseEntity.getBody();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ParkSearchResponse parkSearchResponse = objectMapper.readValue(responseJson, ParkSearchResponse.class);
                    if (parkSearchResponse != null && parkSearchResponse.getData() != null) {
                        temp_parks = parkSearchResponse.getData();
                        parks.add(temp_parks.get(0));
                    }

                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return parks;
    }
    public List<Park> searchParks(String query, String searchType, int startPosition) {
        String url = "";
        if (searchType.equals("parkname")) {
            url = BASE_URL + "/parks?q=" + query + "&limit=10&start=" + startPosition + "&api_key=" + API_KEY;
        } else if (searchType.equals("activities")) {
            url = BASE_URL + "/activities/parks?limit=50&q=" + query + "&api_key=" + API_KEY;
        } else if (searchType.equals("states")) {
            String stateCode =  STATE_CODE_MAP.get(query.trim().toLowerCase());;
            url = BASE_URL + "/parks?limit=10&start=" + startPosition + "&stateCode=" + stateCode + "&api_key=" + API_KEY;
        } else {
            url = BASE_URL + "/amenities/parksplaces?q=" + query + "&limit=50&api_key=" + API_KEY;
        }
        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(url, requestEntity, String.class);

        List<Park> parks = new ArrayList<>();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseJson = responseEntity.getBody();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                if (searchType.equals("activities") ) {
                    if (startPosition == 0){
                        List<String> newParkCodes = new ArrayList<>();
                        ParkActivityResponse parkActivitiesResponse = objectMapper.readValue(responseJson, ParkActivityResponse.class);
                        if (parkActivitiesResponse != null ) {
                              List<ActivityPark> activityPark = parkActivitiesResponse.getData();
                              for (ActivityPark park: activityPark){
                                  for (ActivityPark.DetailedActivityPark detailPark :  park.getActivityParks()){
                                      newParkCodes.add(detailPark.getParkCode());
                                  }
                              }
                              parkCodes = newParkCodes;
                              List<String> initialParkCodes = parkCodes.subList(0, Math.min(10, parkCodes.size()));
                              parks = fetchParkDetailsBatch(startPosition, initialParkCodes);
                        }
                    } else{
                        List<String> initialParkCodes = parkCodes.subList(startPosition,  Math.min(startPosition + 10, parkCodes.size()));
                        parks = fetchParkDetailsBatch(startPosition, initialParkCodes);
                    }

                } else if (searchType.equals("amenities")) {
                    if (startPosition == 0){
                        List<String> newParkCodes = new ArrayList<>();
                        ParkAmenityResponse parkAmenityResponse = objectMapper.readValue(responseJson, ParkAmenityResponse.class);
                        if (parkAmenityResponse != null ) {
                            for (List<AmenityPark> amenityParkList: parkAmenityResponse.getData()){
                                for (AmenityPark park: amenityParkList){
                                    for (AmenityPark.DetailedAmenityPark detailPark :  park.getAmenityParks()){
                                        newParkCodes.add(detailPark.getParkCode());
                                    }
                                }
                            }
                            parkCodes = newParkCodes;
                            List<String> initialParkCodes = parkCodes.subList(0, Math.min(10, parkCodes.size()));
                            parks = fetchParkDetailsBatch(startPosition, initialParkCodes);
                        }
                    } else{
                        List<String> initialParkCodes = parkCodes.subList(startPosition,  Math.min(startPosition + 10, parkCodes.size()));
                        parks = fetchParkDetailsBatch(startPosition, initialParkCodes);
                    }
                } else {
                    ParkSearchResponse parkSearchResponse = objectMapper.readValue(responseJson, ParkSearchResponse.class);
                    if (parkSearchResponse != null && parkSearchResponse.getData() != null) {
                        parks = parkSearchResponse.getData();
                    }
                }
            }  catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return parks;

    }

    public FavoriteResponse addFavorite(String userName, String parkCode) {
        String checkFavoriteSql = "SELECT COUNT(*) FROM favorites WHERE username = ? AND parkcode = ?";
        String getMaxOrderSql = "SELECT MAX(parkOrder) FROM favorites WHERE username = ?";
        String insertFavoriteSql = "INSERT INTO favorites (username, parkCode, parkOrder) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement checkFavoriteStmt = connection.prepareStatement(checkFavoriteSql);
             PreparedStatement getMaxOrderStmt = connection.prepareStatement(getMaxOrderSql)) {

            // Check if park is already favorited
            checkFavoriteStmt.setString(1, userName);
            checkFavoriteStmt.setString(2, parkCode);
            ResultSet rs = checkFavoriteStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new FavoriteResponse("Park already in the favorite list");
            }

            // Get the maximum order
            getMaxOrderStmt.setString(1, userName);
            rs = getMaxOrderStmt.executeQuery();
            int maxOrder = 0;
            if (rs.next()) {
                maxOrder = rs.getInt(1);
            }

            // Insert new favorite with next highest order
            try (PreparedStatement insertFavoriteStmt = connection.prepareStatement(insertFavoriteSql)) {
                insertFavoriteStmt.setString(1, userName);
                insertFavoriteStmt.setString(2, parkCode);
                insertFavoriteStmt.setInt(3, maxOrder + 1);
                insertFavoriteStmt.executeUpdate();
            }
            return new FavoriteResponse("Park successfully added to favorite list");
        } catch (SQLTimeoutException sqlte) {
            sqlte.printStackTrace();
            return new FavoriteResponse("Error when adding favorite park: " + sqlte.getMessage());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new FavoriteResponse("Error when adding favorite park: " + sqle.getMessage());
        }
    }

    public List<String> getFavoriteParkCodes(String userName) {
        String getUserFavoriteSql = "SELECT parkCode FROM favorites WHERE username = ? ";
        List<String> favoriteParkCodes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement getUserFavoriteStmt = connection.prepareStatement(getUserFavoriteSql);
             ) {

            // Check if user does not have favorite park
            getUserFavoriteStmt.setString(1, userName);
            ResultSet rs = getUserFavoriteStmt.executeQuery();
            while (rs.next()) {
                String parkCode = rs.getString("parkCode");
                favoriteParkCodes.add(parkCode);
            }

        } catch (SQLTimeoutException sqlte) {
            sqlte.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return favoriteParkCodes;
    }

    @PostConstruct
    public void initializeParkDatabase() {
        String createFavoritesTableSql = "CREATE TABLE IF NOT EXISTS favorites (" +
                "username TEXT NOT NULL, " +
                "parkCode TEXT NOT NULL, " +
                "parkOrder INTEGER, " +
                "PRIMARY KEY (username, parkCode))";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createFavoritesTableSql);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

}
