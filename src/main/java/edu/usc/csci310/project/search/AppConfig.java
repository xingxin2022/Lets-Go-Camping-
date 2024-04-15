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
        stateCodeMap.put("al", "AL");
        stateCodeMap.put("alaska", "AK");
        stateCodeMap.put("ak", "AK");
        stateCodeMap.put("arizona", "AZ");
        stateCodeMap.put("az", "AZ");
        stateCodeMap.put("arkansas", "AR");
        stateCodeMap.put("ar", "AR");
        stateCodeMap.put("california", "CA");
        stateCodeMap.put("ca", "CA");
        stateCodeMap.put("colorado", "CO");
        stateCodeMap.put("co", "CO");
        stateCodeMap.put("connecticut", "CT");
        stateCodeMap.put("ct", "CT");
        stateCodeMap.put("delaware", "DE");
        stateCodeMap.put("de", "DE");
        stateCodeMap.put("florida", "FL");
        stateCodeMap.put("fl", "FL");
        stateCodeMap.put("georgia", "GA");
        stateCodeMap.put("ga", "GA");
        stateCodeMap.put("hawaii", "HI");
        stateCodeMap.put("hi", "HI");
        stateCodeMap.put("idaho", "ID");
        stateCodeMap.put("id", "ID");
        stateCodeMap.put("illinois", "IL");
        stateCodeMap.put("il", "IL");
        stateCodeMap.put("indiana", "IN");
        stateCodeMap.put("in", "IN");
        stateCodeMap.put("iowa", "IA");
        stateCodeMap.put("ia", "IA");
        stateCodeMap.put("kansas", "KS");
        stateCodeMap.put("ks", "KS");
        stateCodeMap.put("kentucky", "KY");
        stateCodeMap.put("ky", "KY");
        stateCodeMap.put("louisiana", "LA");
        stateCodeMap.put("la", "LA");
        stateCodeMap.put("maine", "ME");
        stateCodeMap.put("me", "ME");
        stateCodeMap.put("maryland", "MD");
        stateCodeMap.put("md", "MD");
        stateCodeMap.put("massachusetts", "MA");
        stateCodeMap.put("ma", "MA");
        stateCodeMap.put("michigan", "MI");
        stateCodeMap.put("mi", "MI");
        stateCodeMap.put("minnesota", "MN");
        stateCodeMap.put("mn", "MN");
        stateCodeMap.put("mississippi", "MS");
        stateCodeMap.put("ms", "MS");
        stateCodeMap.put("missouri", "MO");
        stateCodeMap.put("mo", "MO");
        stateCodeMap.put("montana", "MT");
        stateCodeMap.put("mt", "MT");
        stateCodeMap.put("nebraska", "NE");
        stateCodeMap.put("ne", "NE");
        stateCodeMap.put("nevada", "NV");
        stateCodeMap.put("nv", "NV");
        stateCodeMap.put("new hampshire", "NH");
        stateCodeMap.put("nh", "NH");
        stateCodeMap.put("new jersey", "NJ");
        stateCodeMap.put("nj", "NJ");
        stateCodeMap.put("new mexico", "NM");
        stateCodeMap.put("nm", "NM");
        stateCodeMap.put("new york", "NY");
        stateCodeMap.put("ny", "NY");
        stateCodeMap.put("north carolina", "NC");
        stateCodeMap.put("nc", "NC");
        stateCodeMap.put("north dakota", "ND");
        stateCodeMap.put("nd", "ND");
        stateCodeMap.put("ohio", "OH");
        stateCodeMap.put("oh", "OH");
        stateCodeMap.put("oklahoma", "OK");
        stateCodeMap.put("ok", "OK");
        stateCodeMap.put("oregon", "OR");
        stateCodeMap.put("or", "OR");
        stateCodeMap.put("pennsylvania", "PA");
        stateCodeMap.put("pa", "PA");
        stateCodeMap.put("rhode island", "RI");
        stateCodeMap.put("ri", "RI");
        stateCodeMap.put("south carolina", "SC");
        stateCodeMap.put("sc", "SC");
        stateCodeMap.put("south dakota", "SD");
        stateCodeMap.put("sd", "SD");
        stateCodeMap.put("tennessee", "TN");
        stateCodeMap.put("tn", "TN");
        stateCodeMap.put("texas", "TX");
        stateCodeMap.put("tx", "TX");
        stateCodeMap.put("utah", "UT");
        stateCodeMap.put("ut", "UT");
        stateCodeMap.put("vermont", "VT");
        stateCodeMap.put("vt", "VT");
        stateCodeMap.put("virginia", "VA");
        stateCodeMap.put("va", "VA");
        stateCodeMap.put("washington", "WA");
        stateCodeMap.put("wa", "WA");
        stateCodeMap.put("west virginia", "WV");
        stateCodeMap.put("wv", "WV");
        stateCodeMap.put("wisconsin", "WI");
        stateCodeMap.put("wi", "WI");
        stateCodeMap.put("wyoming", "WY");
        stateCodeMap.put("wy", "WY");
        return stateCodeMap;
    }
}