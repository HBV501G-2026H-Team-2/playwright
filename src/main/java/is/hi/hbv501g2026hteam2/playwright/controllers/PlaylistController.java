package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistResponse;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;
import is.hi.hbv501g2026hteam2.playwright.services.PlaylistService;
import jakarta.validation.Valid;

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
            @Valid @RequestBody PlaylistRequest request) {
        if (user == null || user.getRole() != UserRole.User)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Playlist playlist = playlistService.create(user, request);

        URI location = URI.create("/api/playlists/" + playlist.getId().toString());
        return ResponseEntity.created(location).body(new PlaylistResponse(playlist));
    }

    @PatchMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> updatePlaylist(
            @AuthenticationPrincipal User user,
            @PathVariable UUID playlistId,
            @Valid @RequestBody PlaylistRequest request) {
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Playlist> playlistOpt = playlistService.get(playlistId);
        if (playlistOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Playlist playlist = playlistOpt.get();
        if (!user.getId().equals(playlist.getOwner().getId()))
            return ResponseEntity.notFound().build();

        playlist = playlistService.update(playlist, request);

        return ResponseEntity.ok(new PlaylistResponse(playlist));
    }

}
