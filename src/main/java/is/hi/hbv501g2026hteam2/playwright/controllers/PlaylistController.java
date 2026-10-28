package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.hi.hbv501g2026hteam2.playwright.dto.CreatePlaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistResponse;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;
import is.hi.hbv501g2026hteam2.playwright.services.PlaylistService;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity<PlaylistResponse> createPlaylist(
            @AuthenticationPrincipal User user,
            @RequestBody CreatePlaylistRequest request) {
        if (user == null || user.getRole() != UserRole.User)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Playlist playlist = playlistService.create(user, request);

        URI location = URI.create("/api/playlists/" + playlist.getId().toString());
        return ResponseEntity.created(location).body(new PlaylistResponse(playlist));
    }

}
