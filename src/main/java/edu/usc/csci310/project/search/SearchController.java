package edu.usc.csci310.project.search;

import edu.usc.csci310.project.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        FavoriteResponse response = parkService.addFavorite(request.getUserName(), request.getParkCode());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-user-favorites")
    public List<String> getUserFavorites(@RequestParam String username) {
        List<String> favoriteParkCodes = parkService.getFavoriteParkCodes(username);
        return favoriteParkCodes;
    }
}
