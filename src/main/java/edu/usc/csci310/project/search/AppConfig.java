package edu.usc.csci310.project.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Map<String, String> stateCodeMap() {
        Map<String, String> stateCodeMap = new HashMap<>();
        stateCodeMap.put("alabama", "AL");
        stateCodeMap.put("alaska", "AK");
        stateCodeMap.put("arizona", "AZ");
        stateCodeMap.put("arkansas", "AR");
        stateCodeMap.put("california", "CA");
        stateCodeMap.put("colorado", "CO");
        stateCodeMap.put("connecticut", "CT");
        stateCodeMap.put("delaware", "DE");
        stateCodeMap.put("florida", "FL");
        stateCodeMap.put("georgia", "GA");
        stateCodeMap.put("hawaii", "HI");
        stateCodeMap.put("idaho", "ID");
        stateCodeMap.put("illinois", "IL");
        stateCodeMap.put("indiana", "IN");
        stateCodeMap.put("iowa", "IA");
        stateCodeMap.put("kansas", "KS");
        stateCodeMap.put("kentucky", "KY");
        stateCodeMap.put("louisiana", "LA");
        stateCodeMap.put("maine", "ME");
        stateCodeMap.put("maryland", "MD");
        stateCodeMap.put("massachusetts", "MA");
        stateCodeMap.put("michigan", "MI");
        stateCodeMap.put("minnesota", "MN");
        stateCodeMap.put("mississippi", "MS");
        stateCodeMap.put("missouri", "MO");
        stateCodeMap.put("montana", "MT");
        stateCodeMap.put("nebraska", "NE");
        stateCodeMap.put("nevada", "NV");
        stateCodeMap.put("new hampshire", "NH");
        stateCodeMap.put("new jersey", "NJ");
        stateCodeMap.put("new mexico", "NM");
        stateCodeMap.put("new york", "NY");
        stateCodeMap.put("north carolina", "NC");
        stateCodeMap.put("north dakota", "ND");
        stateCodeMap.put("ohio", "OH");
        stateCodeMap.put("oklahoma", "OK");
        stateCodeMap.put("oregon", "OR");
        stateCodeMap.put("pennsylvania", "PA");
        stateCodeMap.put("rhode island", "RI");
        stateCodeMap.put("south carolina", "SC");
        stateCodeMap.put("south dakota", "SD");
        stateCodeMap.put("tennessee", "TN");
        stateCodeMap.put("texas", "TX");
        stateCodeMap.put("utah", "UT");
        stateCodeMap.put("vermont", "VT");
        stateCodeMap.put("virginia", "VA");
        stateCodeMap.put("washington", "WA");
        stateCodeMap.put("west virginia", "WV");
        stateCodeMap.put("wisconsin", "WI");
        stateCodeMap.put("wyoming", "WY");
        return stateCodeMap;
    }
}