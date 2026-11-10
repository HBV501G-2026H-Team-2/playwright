package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistResponse;
import is.hi.hbv501g2026hteam2.playwright.dto.TrackResponse;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
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
        Playlist playlist = playlistService.create(user, request);
        URI location = URI.create("/api/playlists/" + playlist.getId().toString());
        return ResponseEntity.created(location).body(new PlaylistResponse(playlist));
    }

    @GetMapping
    public ResponseEntity<Page<PlaylistResponse>> getPlaylists(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 50, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Playlist> playlists = playlistService.getAll(user, name, pageable);
        return ResponseEntity.ok(playlists.map(playlist -> new PlaylistResponse(playlist)));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> getPlaylist(@AuthenticationPrincipal User user, @PathVariable UUID playlistId) {
        Optional<Playlist> playlist = playlistService.get(user, playlistId);
        if (playlist.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new PlaylistResponse(playlist.get()));
    }

    @GetMapping("/{playlistId}/tracks")
    public ResponseEntity<List<TrackResponse>> getPlaylistTracks(@AuthenticationPrincipal User user, @PathVariable UUID playlistId) {
        Optional<Playlist> playlist = playlistService.get(user, playlistId);
        if (playlist.isEmpty())
            return ResponseEntity.notFound().build();

        List<TrackResponse> response = playlist.get().getTracks()
                .stream().map(track -> new TrackResponse(track)).toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> updatePlaylist(
            @AuthenticationPrincipal User user,
            @PathVariable UUID playlistId,
            @Valid @RequestBody PlaylistRequest request) {
        Optional<Playlist> playlist = playlistService.update(user, playlistId, request);
        if (playlist.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new PlaylistResponse(playlist.get()));
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@AuthenticationPrincipal User user, @PathVariable UUID playlistId) {
        if (!playlistService.delete(user, playlistId))
            return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }

}
