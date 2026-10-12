package is.hi.hbv501g2026hteam2.playwright.controllers;

import org.springframework.web.bind.annotation.RestController;

import is.hi.hbv501g2026hteam2.playwright.services.PlaylistService;

@RestController
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

}
