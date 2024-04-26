package edu.usc.csci310.project.search;

import edu.usc.csci310.project.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private ParkService parkService;

    @Autowired
    public void ParkController(ParkService parkService) {
        this.parkService = parkService;
    }

    @PostMapping("/search-parks")
    public ResponseEntity<ParkSearchResponse> getParks(@RequestBody ParkSearchRequest request) {
        System.out.println("backend start: " + request.getStartPosition());
        System.out.println(request);
        List<Park> parks = parkService.searchParks(request.getQuery(), request.getSearchType(), request.getStartPosition());

        ParkSearchResponse response = new ParkSearchResponse();
        response.setData(parks);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/add-favorite")
    public ResponseEntity<FavoriteResponse> addFavorite(@RequestBody FavoriteRequest request ) {
        FavoriteResponse response = parkService.addFavorite(request.getUserName(), request.getParkCode(), request.getParkName(), request.isPublic());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-user-favorites")
    public List<String> getUserFavorites(@RequestParam String username) {
        List<String> favoriteParkCodes = parkService.getFavoriteParkCodes(username);
        return favoriteParkCodes;
    }

    @PostMapping("/search-park-by-id")
    public ResponseEntity<ParkSearchResponse> searchParkById(@RequestBody SingleParkSearchRequest request) {
        List<String> favoriteParkCodes = Collections.singletonList(request.getParkCode());
        List<Park> parks = parkService.fetchParkDetailsBatch(0, favoriteParkCodes);
        ParkSearchResponse response = new ParkSearchResponse();
        response.setData(parks);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


}
