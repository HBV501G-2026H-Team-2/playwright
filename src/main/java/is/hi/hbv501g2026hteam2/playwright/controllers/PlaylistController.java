package is.hi.hbv501g2026hteam2.playwright.controllers;

import org.springframework.stereotype.Controller; 

import is.hi.hbv501g2026hteam2.playwright.services.PlaylistService; 

import org.springframework.web.bind.annotation.PostMapping; 
import java.util.List; 

@Controller
public class PlaylistController {
    private final PlaylistService playlistService; 

    public PlaylistController(PlaylistService playlistService){
        this.playlistService = playlistService; 
    }

}