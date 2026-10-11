package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.services.TrackService;

@RestController
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    // TODO: Get userId from authenticated user once auth implementation is decided.
    @PostMapping("/tracks")
    public Track uploadTrack(
            @RequestParam("userId") UUID userId,
            @RequestParam("file") MultipartFile file) {

        return trackService.store(userId, file);
    }

    @RequestMapping(value="/tracks/{trackId}", method=RequestMethod.GET)
    public Track getTrack(@PathVariable UUID trackId, Model model) {
        Track track = trackService.get(trackId); 
        return track; 
    }

    @RequestMapping(value="/tracks/{trackId}/download", method=RequestMethod.GET)
    public Resource downloadTrack(@PathVariable UUID trackId, Model model) {
        Track track = trackService.get(trackId);
        return new InputStreamResource(trackService.getFileContents(track));
    } 

    @DeleteMapping(value="/tracks/{trackId}")
    public ResponseEntity<Void> deleteTrack(@PathVariable UUID trackId){
        Track track = trackService.get(trackId); 
        trackService.delete(track);
        return ResponseEntity.noContent().build(); 
    }
}