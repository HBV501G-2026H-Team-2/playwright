package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.services.TrackService;

@Controller
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    // TODO: Get userId from authenticated user once auth implementation is decided.
    @PostMapping("/tracks")
    @ResponseBody
    public Track uploadTrack(
            @RequestParam("userId") UUID userId,
            @RequestParam("file") MultipartFile file) {

        return trackService.store(userId, file);
    }
}
