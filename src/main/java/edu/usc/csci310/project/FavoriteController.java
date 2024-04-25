package edu.usc.csci310.project;

import edu.usc.csci310.project.search.Park;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // Endpoint to move a park up or down in the favorite list database
    @PostMapping("/movePark")
    public ResponseEntity<String> movePark(@RequestParam String username, @RequestParam String parkCode, @RequestParam boolean moveUp) {
        try {
            favoriteService.movePark(username, parkCode, moveUp);
            return ResponseEntity.ok("Park moved successfully.");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to remove a park from the favorite list database
    @PostMapping("/removePark")
    public ResponseEntity<String> removePark(@RequestParam String username, @RequestParam String parkCode) {
        try {
            favoriteService.removePark(username, parkCode);
            return ResponseEntity.ok("Park removed successfully.");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to update the privacy setting of a user's favorite list
    @PostMapping("/updatePrivacy")
    public ResponseEntity<String> updatePrivacy(@RequestParam String username, @RequestParam boolean isPublic) {
        try {
            favoriteService.updateFavoriteListPrivacy(username, isPublic);
            return ResponseEntity.ok("Privacy setting updated successfully.");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to delete all parks from a user's favorite list
    @PostMapping("/deleteAll")
    public ResponseEntity<String> deleteAll(@RequestParam String username) {
        try {
            favoriteService.deleteAll(username);
            return ResponseEntity.ok("All parks removed successfully.");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to get the full list of a user's favorite parks
    @PostMapping("/getFavorites")
    public ResponseEntity<FavoriteResponse> getFavorites(@RequestParam String username) {
        try {
            FavoriteResponse response = favoriteService.getFavorites(username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new FavoriteResponse(null, e.getMessage(), false),
                                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to fetch details of a list of parks
    @PostMapping("/fetchDetails")
    public ResponseEntity<FavoriteResponse> fetchDetails(@RequestBody ParkCodesRequest request) {
        try {
            List<Park> parks = favoriteService.fetchParkDetailsBatch(0, request.getParkCodes());
            FavoriteResponse response = new FavoriteResponse(parks, null, true);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new FavoriteResponse(null, e.getMessage(), false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/fetchPublicStatus")
    public ResponseEntity<?> fetchPublicStatus(@RequestParam String username) {
        try {
            Boolean isPublic = favoriteService.getPublic(username);
            System.out.println(isPublic);
            return ResponseEntity.ok(isPublic);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get public status: " + e.getMessage());
        }
    }
}
